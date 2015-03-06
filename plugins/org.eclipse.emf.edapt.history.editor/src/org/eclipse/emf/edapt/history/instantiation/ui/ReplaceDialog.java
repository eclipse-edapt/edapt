/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.ui.ModelSash;
import org.eclipse.emf.edapt.common.ui.ResizeableDialogBase;
import org.eclipse.emf.edapt.history.instantiation.ReplaceCommand;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.internal.common.IExtentProvider;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Dialog to support replacement of a sequence of changes by the execution of an
 * operation.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReplaceDialog extends ResizeableDialogBase {

	/** Sequence of primitive changes. */
	private final List<PrimitiveChange> changes;

	/** Operation browser. */
	private OperationSash operationSash;

	/** Reconstructor for the metamodel before the changes. */
	private EcoreForwardReconstructor reconstructor;

	/** Viewer for the metamodel before the changes. */
	private ModelSash metamodelSash;

	/** Command to replace the primitive changes by the operation execution. */
	private ReplaceCommand command;

	/** Constructor. */
	public ReplaceDialog(List<PrimitiveChange> changes) {
		super(
			new Point(1024, 768), "Replace primitive changes with operation", //$NON-NLS-1$
			"A sequence of primitive changes can be replaced by the instantiation of an operation. " //$NON-NLS-1$
				+ "On the left-hand side, the state of the metamodel before the primitive changes is shown. " //$NON-NLS-1$
				+ "In the middle, the sequence of primitive changes is shown. " //$NON-NLS-1$
				+ "An element can be located in the metamodel by double-clicking on a property of a primitive change. " //$NON-NLS-1$
				+ "On the right-hand side, the operations viewer allows to instantiate the operation."); //$NON-NLS-1$

		this.changes = changes;
	}

	/** {@inheritDoc} */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));

		initMetamodelSash(sash);
		initChangeSash(sash);
		initOperationSash(sash);

		sash.setWeights(new int[] { 1, 1, 1 });

		return parent;
	}

	/** Initialize the operation browser. */
	private void initOperationSash(SashForm sash) {

		final MetamodelExtent extent = new MetamodelExtent(MetamodelUtils
			.getAllRootPackages(reconstructor.getResourceSet()));
		final IExtentProvider provider = new IExtentProvider() {

			@Override
			public MetamodelExtent getExtent() {
				return extent;
			}

		};

		operationSash = new OperationSash(sash, provider) {
			@Override
			protected boolean updateConstraints(
				OperationInstance operationInstance) {
				final boolean valid = super.updateConstraints(operationInstance);
				if (valid) {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}
				return valid;
			}
		};

		metamodelSash.getStructureViewer().addSelectionChangedListener(
			new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					operationSash
						.updateOperations((IStructuredSelection) event
							.getSelection());
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}

			});
	}

	/** Initialize viewer for the changes. */
	private void initChangeSash(SashForm sash) {
		final ModelSash changeSash = new ModelSash(sash, SWT.None) {
			@Override
			protected void propertyValuesSelected(Object value) {
				if (value instanceof EObject) {
					final EObject target = reconstructor.getMapping().getTarget(
						(EObject) value);
					if (target != null) {
						final StructuredSelection selection = new StructuredSelection(
							target);
						metamodelSash.getStructureViewer().setSelection(
							selection, true);
					}
				}
			}
		};
		changeSash.getStructureViewer().setInput(changes.get(0).eContainer());
		changeSash.getStructureViewer().addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
				Object element) {
				final EObject eObject = (EObject) element;
				return changes.contains(element)
					|| !(eObject.eContainer() instanceof Release);
			}

		});
		changeSash.getStructureViewer().addDoubleClickListener(
			new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					final IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
					final Object value = selection.getFirstElement();
					if (value instanceof EObject) {
						final EObject target = reconstructor.getMapping()
							.getTarget((EObject) value);
						if (target != null) {
							final StructuredSelection newSelection = new StructuredSelection(
								target);
							metamodelSash.getStructureViewer()
								.setSelection(newSelection, true);
						}
					}
				}

			});
		changeSash.getStructureViewer().setSorter(null);
	}

	/** Initialize the viewer for the metamodel before the changes. */
	private void initMetamodelSash(SashForm sash) {
		reconstructor = new EcoreForwardReconstructor(URI
			.createFileURI("recons")); //$NON-NLS-1$
		reconstructor.reconstruct(changes.get(0), true);

		metamodelSash = new ModelSash(sash, SWT.None);
		metamodelSash.getStructureViewer().setInput(
			reconstructor.getResourceSet());
	}

	/** {@inheritDoc} */
	@Override
	protected void okPressed() {
		final OperationInstance operation = (OperationInstance) reconstructor
			.getMapping().copyResolveSource(
				operationSash.getSelectedOperation());

		command = new ReplaceCommand(changes, operation);
		if (command.canExecute()) {
			super.okPressed();
		} else {
			@SuppressWarnings("unused")
			final String title = "Primitive changes cannot be replaced by operation"; //$NON-NLS-1$
			@SuppressWarnings("unused")
			final String message = "The primitive changes cannot be replaced by the instantiation of the operation. " //$NON-NLS-1$
				+ "The differences between the state of the metamodel after the primitive changes " //$NON-NLS-1$
				+ "and the state of the metamodel after the instantiation of the operation is shown below."; //$NON-NLS-1$

			// ModelDialog diffDialog = new ModelDialog(command.getDiff(), title,
			// message);
			// diffDialog.open();
		}
	}

	/** Get the command that replaces the primitive changes with an operation. */
	public ReplaceCommand getReplaceCommand() {
		return command;
	}
}
