/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BMW Car IT - Initial API and implementation
 *     Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.tests.history.lifecycle;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.StrictCompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.history.Add;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.MigrateableChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.NoChange;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.Set;
import org.eclipse.emf.edapt.history.ValueChange;
import org.eclipse.emf.edapt.history.instantiation.ExecuteCommand;
import org.eclipse.emf.edapt.history.instantiation.ReleaseCommand;
import org.eclipse.emf.edapt.history.presentation.AttachMigrationCommand;
import org.eclipse.emf.edapt.history.presentation.action.CombineChangesCommand;
import org.eclipse.emf.edapt.history.reconstruction.Mapping;
import org.eclipse.emf.edapt.history.reconstruction.ReconstructorBase;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.history.util.HistorySwitch;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;


/**
 * Facility to perform operations based on a history
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class HistoryInterpreter extends ReconstructorBase {

	/**
	 * Recording facility
	 */
	private final EditingDomainListener listener;

	/**
	 * Switch for interpretation of changes
	 */
	private final InterpreterSwitch interpreterSwitch;

	/**
	 * Current release
	 */
	private Release release;

	/**
	 * Facility to detect migrateable changes
	 */
	private ChangeCombiner<MigrateableChange> migrationCombiner;

	/**
	 * Facility to detect primitive changes
	 */
	private ChangeCombiner<PrimitiveChange> compositeCombiner;

	/**
	 * Current operation change
	 */
	private OperationChange operationChange;

	/**
	 * Mapping for the modified metamodel
	 */
	private final Mapping externalMapping;

	/**
	 * Mapping for the reconstructed metamodel
	 */
	private Mapping internalMapping;

	/**
	 * Metamodel extent
	 */
	private final MetamodelExtent extent;

	/**
	 * Constructor
	 */
	public HistoryInterpreter(EditingDomainListener listener,
			Mapping externalMapping) {
		this.listener = listener;
		interpreterSwitch = new InterpreterSwitch();
		ResourceSet resourceSet = listener.getHistory().eResource()
				.getResourceSet();
		Collection<EPackage> allRootPackages = MetamodelUtils
				.getAllRootPackages(resourceSet);
		this.extent = new MetamodelExtent(allRootPackages);
		this.externalMapping = externalMapping;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		internalMapping = mapping;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startRelease(Release originalRelease) {
		release = originalRelease;
	}

	/**
	 * Get the current release
	 */
	private Release getCurrentRelease() {
		Release lastRelease = listener.getHistory().getLastRelease();
		return lastRelease;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endRelease(Release originalRelease) {
		if (!originalRelease.isLastRelease()) {
			execute(new ReleaseCommand(listener, originalRelease.getLabel()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startChange(Change originalChange) {
		if (release.isFirstRelease()) {
			return;
		}

		if (originalChange instanceof MigrationChange) {
			migrationCombiner = new ChangeCombiner<MigrateableChange>(
					getCurrentRelease());
		} else if (originalChange instanceof OperationChange) {
			operationChange = (OperationChange) originalChange;
			execute(interpreterSwitch.doSwitch(originalChange));
		} else if (originalChange instanceof CompositeChange) {
			compositeCombiner = new ChangeCombiner<PrimitiveChange>(
					getCurrentRelease());
		} else if (operationChange == null) {
			if (!(originalChange.eContainer() instanceof Create)) {
				execute(interpreterSwitch.doSwitch(originalChange));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endChange(Change originalChange) {
		if (release.isFirstRelease()) {
			return;
		}

		if (originalChange instanceof MigrationChange) {
			final MigrationChange migrationChange = (MigrationChange) originalChange;
			List<MigrateableChange> changes = migrationCombiner.stop();
			Command command = null;
			if (changes.isEmpty()) {
				command = new ChangeCommand(getCurrentRelease()) {
					@Override
					protected void doExecute() {
						MigrationChange mc = HistoryFactory.eINSTANCE
								.createMigrationChange();
						mc.setMigration(migrationChange.getMigration());
						getCurrentRelease().getChanges().add(mc);
					}
				};
			} else {
				command = new AttachMigrationCommand(changes, migrationChange
						.getMigration());
			}
			execute(command);
			migrationCombiner = null;
		} else if (originalChange instanceof OperationChange) {
			fixMapping();
			operationChange = null;
		} else if (originalChange instanceof CompositeChange) {
			List<PrimitiveChange> changes = compositeCombiner.stop();
			CombineChangesCommand command = new CombineChangesCommand(
					getCurrentRelease(), changes);
			execute(command);
			compositeCombiner = null;
		}
	}

	/**
	 * Fix the external mapping after an operation was executed
	 */
	@SuppressWarnings("unchecked")
	private void fixMapping() {
		for (PrimitiveChange change : operationChange.getChanges()) {
			if (change instanceof Create) {
				Create create = (Create) change;
				EObject target = externalMapping.getTarget(create.getTarget());
				EReference reference = create.getReference();
				if (reference.isMany()) {
					int index = ((List<EObject>) internalMapping.getTarget(
							create.getTarget()).eGet(reference))
							.indexOf(internalMapping.getTarget(create
									.getElement()));
					externalMapping
							.map(create.getElement(), ((List<EObject>) target
									.eGet(reference)).get(index));
				} else {
					externalMapping.map(create.getElement(), (EObject) target
							.eGet(reference));
				}
			}
		}
	}

	/**
	 * Execute a command
	 */
	private void execute(Command command) {
		listener.getEditingDomain().getCommandStack().execute(command);
	}

	/**
	 * A switch to interpret different changes
	 */
	private class InterpreterSwitch extends HistorySwitch<Command> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseChange(Change change) {
			throw new UnsupportedOperationException(change.eClass().getName()
					+ " not supported");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseSet(Set set) {
			EObject element = externalMapping.getTarget(set.getElement());
			Object value = externalMapping.resolveTarget(set.getValue());

			SetCommand command = new SetCommand(listener.getEditingDomain(),
					element, set.getFeature(), value);
			return command;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseAdd(Add add) {
			EObject element = externalMapping.getTarget(add.getElement());
			Object value = externalMapping.resolveTarget(add.getValue());

			AddCommand command = new AddCommand(listener.getEditingDomain(),
					element, add.getFeature(), value);
			return command;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseRemove(Remove remove) {
			EObject element = externalMapping.getTarget(remove.getElement());
			Object value = externalMapping.resolveTarget(remove.getValue());

			RemoveCommand command = new RemoveCommand(listener
					.getEditingDomain(), element, remove.getFeature(), value);
			return command;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseOperationChange(OperationChange operationChange) {
			OperationInstance originalOperationInstance = operationChange
					.getOperation();
			OperationInstance reproducedOperationInstance = (OperationInstance) externalMapping
					.copyResolveTarget(originalOperationInstance);

			ExecuteCommand command = new ExecuteCommand(
					reproducedOperationInstance, extent);
			return command;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseMove(Move move) {
			EObject target = externalMapping.getTarget(move.getTarget());
			EObject element = externalMapping.getTarget(move.getElement());

			AddCommand command = new AddCommand(listener.getEditingDomain(),
					target, move.getReference(), element);
			return command;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseCreate(Create create) {
			EObject target = externalMapping.getTarget(create.getTarget());
			EClass type = create.getElement().eClass();
			EObject element = type.getEPackage().getEFactoryInstance().create(
					type);
			externalMapping.map(create.getElement(), element);
			extent.addToExtent(element);

			StrictCompoundCommand command = new StrictCompoundCommand();
			command.append(new CreateChildCommand(listener.getEditingDomain(),
					target, create.getReference(), element, null));
			for (ValueChange valueChange : create.getChanges()) {
				command.append(doSwitch(valueChange));
			}
			return command;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseDelete(Delete delete) {
			EObject element = externalMapping.getTarget(delete.getElement());

			DeleteCommand command = new DeleteCommand(listener
					.getEditingDomain(), Arrays.asList(element));
			return command;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Command caseNoChange(final NoChange noChange) {
			Command command = new ChangeCommand(getCurrentRelease()) {
				@Override
				protected void doExecute() {
					NoChange nc = HistoryFactory.eINSTANCE.createNoChange();
					nc.setDescription(noChange.getDescription());
					getCurrentRelease().getChanges().add(nc);
				}
			};
			return command;
		}
	}
}
