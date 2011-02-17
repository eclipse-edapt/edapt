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
package org.eclipse.emf.edapt.history.instantiation;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.provider.HistoryEditPlugin;
import org.eclipse.emf.edapt.history.reconstruction.DiffModelFilterUtils;
import org.eclipse.emf.edapt.history.reconstruction.DiffModelOrderFilter;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.reconstruction.HistoryComparer;
import org.eclipse.emf.edapt.history.reconstruction.IDiffModelFilter;
import org.eclipse.emf.edapt.history.reconstruction.ModelAssert;
import org.eclipse.emf.edit.command.ChangeCommand;


/**
 * Command to replace a sequence of changes by the execution of an operation.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReplaceCommand extends ChangeCommand {

	/** Sequence of primitive changes. */
	private final List<PrimitiveChange> changes;

	/** Execution of the operation. */
	private final OperationInstance operation;

	/** Difference model. */
	private DiffResourceSet diff;

	/** Constructor. */
	public ReplaceCommand(List<PrimitiveChange> changes,
			OperationInstance operation) {
		super(changes.get(0).eContainer());

		this.changes = changes;
		this.operation = operation;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean prepare() {
		diff = calculateDifference(operation);
		IDiffModelFilter filter = DiffModelFilterUtils
				.and(DiffModelOrderFilter.INSTANCE);
		DiffModelFilterUtils.filter(diff, filter);
		boolean validReplacement = ModelAssert.numberOfChanges(diff) == 0;
		return validReplacement && super.prepare();
	}

	/**
	 * Compare the metamodel after the sequence of changes with the metamodel
	 * before the sequence of changes on which the operation was executed.
	 */
	private DiffResourceSet calculateDifference(OperationInstance operation) {

		EcoreForwardReconstructor current = new EcoreForwardReconstructor(URI
				.createFileURI("current"));
		current.reconstruct(changes.get(changes.size() - 1), false);

		EcoreForwardReconstructor replaced = new EcoreForwardReconstructor(URI
				.createFileURI("replaced"));
		replaced.reconstruct(changes.get(0), true);

		tryOperation(operation, current, replaced);

		HistoryComparer differ = new HistoryComparer(current, replaced);
		DiffResourceSet diff = differ.compare().getDiffResourceSet();

		return diff;
	}

	/** Try to execute the operation. */
	private void tryOperation(OperationInstance operation,
			EcoreForwardReconstructor current,
			EcoreForwardReconstructor replaced) {

		OperationInstance replacedOperation = (OperationInstance) replaced
				.getMapping().copyResolveTarget(operation);

		MetamodelExtent extent = new MetamodelExtent(MetamodelUtils
				.getAllRootPackages(replaced.getResourceSet()));
		ChangeRecorder recorder = new ChangeRecorder(extent.getRootPackages());
		Command command = new ExecuteCommand(replacedOperation, extent);
		command.execute();
		ChangeDescription changeDescription = recorder.endRecording();

		adaptMapping(changeDescription, current, replaced);
	}

	/** Adapt the mapping of the reconstructor for the replaced metamodel. */
	@SuppressWarnings("unchecked")
	private void adaptMapping(ChangeDescription changeDescription,
			EcoreForwardReconstructor current,
			EcoreForwardReconstructor replaced) {
		for (EObject replacedElement : changeDescription.getObjectsToDetach()) {
			if (replacedElement instanceof EGenericType) {
				continue;
			}
			try {
				EObject container = replacedElement.eContainer();
				EObject originalContainer = replaced.getMapping().getSource(
						container);
				EObject currentContainer = current.getMapping().getTarget(
						originalContainer);

				EReference containment = replacedElement.eContainmentFeature();
				if (containment.isMany()) {
					int index = ((List<EObject>) container.eGet(containment))
							.indexOf(replacedElement);
					EObject currentElement = ((List<EObject>) currentContainer
							.eGet(containment)).get(index);
					EObject originalElement = current.getMapping().getSource(
							currentElement);
					replaced.getMapping().map(originalElement, replacedElement);
				} else {
					EObject originalElement = (EObject) originalContainer
							.eGet(containment);
					replaced.getMapping().map(originalElement, replacedElement);
				}
			} catch (RuntimeException e) {
				LoggingUtils.logError(HistoryEditPlugin.getPlugin(), e);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void doExecute() {
		OperationChange change = HistoryFactory.eINSTANCE
				.createOperationChange();
		change.setOperation(operation);

		PrimitiveChange firstChange = changes.get(0);
		Release release = (Release) firstChange.eContainer();
		release.getChanges().add(release.getChanges().indexOf(firstChange),
				change);

		change.getChanges().addAll(changes);
	}

	/** Returns difference model. */
	public DiffResourceSet getDiff() {
		return diff;
	}
}