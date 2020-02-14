/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation;

import java.util.List;

import org.eclipse.emf.common.command.Command;
// CB Migrate
// import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.history.provider.HistoryEditPlugin;
import org.eclipse.emf.edit.command.ChangeCommand;
// CB Migrate
// import org.eclipse.emf.edapt.history.reconstruction.HistoryComparer;

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
	// CB Migrate
	// private DiffResourceSet diff;

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

		return false;

		// CB Migrate
		// diff = calculateDifference(operation);
		// IDiffModelFilter filter = DiffModelFilterUtils
		// .and(DiffModelOrderFilter.INSTANCE);
		// DiffModelFilterUtils.filter(diff, filter);
		// boolean validReplacement = ModelAssert.numberOfChanges(diff) == 0;
		// return validReplacement && super.prepare();
	}

	/**
	 * Compare the metamodel after the sequence of changes with the metamodel
	 * before the sequence of changes on which the operation was executed.
	 */
	// CB Migrate
	// private DiffResourceSet calculateDifference(OperationInstance operation) {
	//
	// EcoreForwardReconstructor current = new EcoreForwardReconstructor(URI
	// .createFileURI("current"));
	// current.reconstruct(changes.get(changes.size() - 1), false);
	//
	// EcoreForwardReconstructor replaced = new EcoreForwardReconstructor(URI
	// .createFileURI("replaced"));
	// replaced.reconstruct(changes.get(0), true);
	//
	// tryOperation(operation, current, replaced);
	//
	// HistoryComparer differ = new HistoryComparer(current, replaced);
	// DiffResourceSet diff = differ.compare().getDiffResourceSet();
	//
	// return diff;
	// }

	/** Try to execute the operation. */
	@SuppressWarnings("unused")
	private void tryOperation(OperationInstance operation,
		EcoreForwardReconstructor current,
		EcoreForwardReconstructor replaced) {

		final OperationInstance replacedOperation = (OperationInstance) replaced
			.getMapping().copyResolveTarget(operation);

		final MetamodelExtent extent = new MetamodelExtent(MetamodelUtils
			.getAllRootPackages(replaced.getResourceSet()));
		final ChangeRecorder recorder = new ChangeRecorder(extent.getRootPackages());
		final Command command = new ExecuteCommand(replacedOperation, extent);
		command.execute();
		final ChangeDescription changeDescription = recorder.endRecording();

		adaptMapping(changeDescription, current, replaced);
	}

	/** Adapt the mapping of the reconstructor for the replaced metamodel. */
	@SuppressWarnings("unchecked")
	private void adaptMapping(ChangeDescription changeDescription,
		EcoreForwardReconstructor current,
		EcoreForwardReconstructor replaced) {
		for (final EObject replacedElement : changeDescription.getObjectsToDetach()) {
			if (replacedElement instanceof EGenericType) {
				continue;
			}
			try {
				final EObject container = replacedElement.eContainer();
				final EObject originalContainer = replaced.getMapping().getSource(
					container);
				final EObject currentContainer = current.getMapping().getTarget(
					originalContainer);

				final EReference containment = replacedElement.eContainmentFeature();
				if (containment.isMany()) {
					final int index = ((List<EObject>) container.eGet(containment))
						.indexOf(replacedElement);
					final EObject currentElement = ((List<EObject>) currentContainer
						.eGet(containment)).get(index);
					final EObject originalElement = current.getMapping().getSource(
						currentElement);
					replaced.getMapping().map(originalElement, replacedElement);
				} else {
					final EObject originalElement = (EObject) originalContainer
						.eGet(containment);
					replaced.getMapping().map(originalElement, replacedElement);
				}
			} catch (final RuntimeException e) {
				LoggingUtils.logError(HistoryEditPlugin.getPlugin(), e);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void doExecute() {
		final OperationChange change = HistoryFactory.eINSTANCE
			.createOperationChange();
		change.setOperation(operation);

		final PrimitiveChange firstChange = changes.get(0);
		final Release release = (Release) firstChange.eContainer();
		release.getChanges().add(release.getChanges().indexOf(firstChange),
			change);

		change.getChanges().addAll(changes);
	}

	/** Returns difference model. */
	// CB Migrate
	// public DiffResourceSet getDiff() {
	// return diff;
	// }
}