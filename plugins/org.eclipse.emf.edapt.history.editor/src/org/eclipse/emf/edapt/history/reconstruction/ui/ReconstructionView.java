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
package org.eclipse.emf.edapt.history.reconstruction.ui;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.common.ui.ModelSash;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.common.ui.StructureTreeViewer;
import org.eclipse.emf.edapt.common.ui.SyncedMetamodelEditorViewBase;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.history.reconstruction.CompositeReconstructorBase;
import org.eclipse.emf.edapt.history.reconstruction.EcoreBackwardReconstructor;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.ContentChange;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.NonDelete;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.history.ValueChange;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * View showing a reconstructed metamodel.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReconstructionView extends SyncedMetamodelEditorViewBase {

	/** Identifier as in the plugin.xml */
	public static final String ID = ReconstructionView.class.getName();

	/** Sash to show the model */
	private ModelSash sash;

	/** Reconstructor */
	private CompositeReconstructorBase reconstructor;

	/** {@inheritDoc} */
	@Override
	protected void createContents(Composite parent) {
		sash = new ModelSash(parent, SWT.NULL);
		sash.getStructureViewer().addDoubleClickListener(
			new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					final Object element = SelectionUtils
						.getSelectedElement(event.getSelection(), Object.class);
					if (element != null) {
						final Object source = reconstructor.getMapping()
							.resolveSource(element);
						getEditor().setSelectionToViewer(
							Collections.singletonList(source));
					}
				}

			});
	}

	/** {@inheritDoc} */
	@Override
	public void selectionChanged(IStructuredSelection structuredSelection) {
		if (structuredSelection.size() == 1) {
			final Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof Change) {
				setChange((Change) firstElement);
			} else if (firstElement instanceof Release) {
				setRelease((Release) firstElement);
			}
		}
	}

	/** Set release to be reconstructed. */
	public void setRelease(Release release) {
		reconstructor = createReconstructor(release);
		reconstructor.reconstruct(release, false);
		final StructureTreeViewer structureViewer = sash.getStructureViewer();
		structureViewer.setInput(reconstructor.getResourceSet());
		structureViewer.expandToLevel(3);
	}

	/** Set change to be reconstructed. */
	public void setChange(Change change) {
		reconstructor = createReconstructor(change.getRelease());
		reconstructor.reconstruct(change, false);
		final StructureTreeViewer structureViewer = sash.getStructureViewer();
		structureViewer.setInput(reconstructor.getResourceSet());
		structureViewer.expandToLevel(3);

		showAffectedElements(change);
	}

	/** Show the elements affected by a change. */
	private void showAffectedElements(Change change) {
		final StructureTreeViewer structureViewer = sash.getStructureViewer();
		if (change instanceof PrimitiveChange) {
			if (change instanceof ContentChange) {
				if (change instanceof Delete) {
					final Delete delete = (Delete) change;
					final EObject target = reconstructor.getMapping().resolveTarget(
						delete.getTarget());
					structureViewer.setSelection(
						new StructuredSelection(target), true);
				} else if (change instanceof NonDelete) {
					final NonDelete nonDelete = (NonDelete) change;
					final EObject element = reconstructor.getMapping().resolveTarget(
						nonDelete.getElement());
					structureViewer.setSelection(new StructuredSelection(
						element), true);
				}
			} else if (change instanceof ValueChange) {
				final ValueChange valueChange = (ValueChange) change;
				final EObject element = reconstructor.getMapping().resolveTarget(
					valueChange.getElement());
				if (element != null) {
					/*
					 * don't try to select an element in case we couldn't resolve the target.
					 * Otherwise we will get a NPE.
					 */
					structureViewer.setSelection(new StructuredSelection(element), true);
				}
			}
		} else if (change instanceof OperationChange) {
			final OperationChange operationChange = (OperationChange) change;
			final OperationInstance operationInstance = operationChange
				.getOperation();
			final Parameter mainParameter = operationInstance.getOperation()
				.getMainParameter();
			final Object value = operationInstance.getParameterValue(mainParameter
				.getName());
			final Object resolved = reconstructor.getMapping().resolveTarget(value);
			if (mainParameter.isMany()) {
				structureViewer.setSelection(new StructuredSelection(
					(List) resolved), true);
			} else {
				structureViewer.setSelection(new StructuredSelection(resolved),
					true);
			}
		}
	}

	/**
	 * Create either a backward or forward reconstructor depending on the
	 * proximity to the end or begin of history
	 */
	private CompositeReconstructorBase createReconstructor(Release release) {
		final URI uri = URI.createURI("recons"); //$NON-NLS-1$
		final List<Release> releases = release.getHistory().getReleases();
		final int index = releases.indexOf(release);
		if (index >= releases.size() - 2) {
			return new EcoreBackwardReconstructor(uri);
		}
		return new EcoreForwardReconstructor(uri);
	}

	/** {@inheritDoc} */
	@Override
	protected void editorChanged(EcoreEditor oldEditor) {
		if (getEditor() == null) {
			sash.getStructureViewer().setInput(null);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void setFocus() {
		if (!sash.isDisposed()) {
			sash.setFocus();
		}
	}
}
