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
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.IExtentProvider;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.common.ui.ModelDialog;
import org.eclipse.emf.edapt.common.ui.ModelSash;
import org.eclipse.emf.edapt.common.ui.ResizeableDialogBase;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.instantiation.ReplaceCommand;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
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
				new Point(1024, 768),
				"Replace primitive changes with operation",
				"A sequence of primitive changes can be replaced by the instantiation of an operation. "
						+ "On the left-hand side, the state of the metamodel before the primitive changes is shown. "
						+ "In the middle, the sequence of primitive changes is shown. "
						+ "An element can be located in the metamodel by double-clicking on a property of a primitive change. "
						+ "On the right-hand side, the operations viewer allows to instantiate the operation.");

		this.changes = changes;
	}

	/** {@inheritDoc} */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
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
		IExtentProvider provider = new IExtentProvider() {

			public MetamodelExtent getExtent() {
				return extent;
			}

		};

		operationSash = new OperationSash(sash, provider) {
			@Override
			protected boolean updateConstraints(
					OperationInstance operationInstance) {
				boolean valid = super.updateConstraints(operationInstance);
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
		ModelSash changeSash = new ModelSash(sash, SWT.None) {
			@Override
			protected void propertyValuesSelected(Object value) {
				if (value instanceof EObject) {
					EObject target = reconstructor.getMapping().getTarget(
							(EObject) value);
					if (target != null) {
						StructuredSelection selection = new StructuredSelection(
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
				EObject eObject = (EObject) element;
				return changes.contains(element)
						|| !(eObject.eContainer() instanceof Release);
			}

		});
		changeSash.getStructureViewer().addDoubleClickListener(
				new IDoubleClickListener() {

					public void doubleClick(DoubleClickEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						Object value = selection.getFirstElement();
						if (value instanceof EObject) {
							EObject target = reconstructor.getMapping()
									.getTarget((EObject) value);
							if (target != null) {
								StructuredSelection newSelection = new StructuredSelection(
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
				.createFileURI("recons"));
		reconstructor.reconstruct(changes.get(0), true);

		metamodelSash = new ModelSash(sash, SWT.None);
		metamodelSash.getStructureViewer().setInput(
				reconstructor.getResourceSet());
	}

	/** {@inheritDoc} */
	@Override
	protected void okPressed() {
		OperationInstance operation = (OperationInstance) reconstructor
				.getMapping().copyResolveSource(
						operationSash.getSelectedOperation());

		command = new ReplaceCommand(changes, operation);
		if (command.canExecute()) {
			super.okPressed();
		} else {
			String title = "Primitive changes cannot be replaced by operation";
			String message = "The primitive changes cannot be replaced by the instantiation of the operation. "
					+ "The differences between the state of the metamodel after the primitive changes "
					+ "and the state of the metamodel after the instantiation of the operation is shown below.";

			ModelDialog diffDialog = new ModelDialog(command.getDiff(), title,
					message);
			diffDialog.open();
		}
	}

	/** Get the command that replaces the primitive changes with an operation. */
	public ReplaceCommand getReplaceCommand() {
		return command;
	}
}
