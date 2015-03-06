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
package org.eclipse.emf.edapt.common.ui;

import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

/**
 * Base class for a view that is linked to the default Ecore Editor
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class MetamodelEditorViewBase extends ViewPart implements
	ISelectionChangedListener {

	/** Ecore editor to which the browser is currently linked. */
	private EcoreEditor editor;

	/** Part listener to react to an editor being activated or closed. */
	private final IPartListener partListener = new PartAdapter() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void partActivated(IWorkbenchPart part) {
			setEditorPart(part);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void partClosed(IWorkbenchPart part) {
			if (part == editor) {
				setEditor(null);
			}
		}
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void createPartControl(Composite parent) {
		createActions();
		createContents(parent);

		setEditorPart(getSite().getPage().getActiveEditor());
		getSite().getPage().addPartListener(partListener);
	}

	/**
	 * Create the actions of that view
	 */
	protected void createActions() {
		// to be implemented by subclasses
	}

	/**
	 * Create the contents of that view
	 *
	 * @param parent
	 */
	protected abstract void createContents(Composite parent);

	/**
	 * Helper method to set editor part (checks whether editor is of the needed type)
	 *
	 * @param part Part
	 */
	private void setEditorPart(IWorkbenchPart part) {
		if (part instanceof EcoreEditor) {
			final EcoreEditor ecoreEditor = (EcoreEditor) part;
			// not used as reflective editor
			if (EcoreUIUtils.isMetamodelEditor(ecoreEditor)) {
				setEditor(ecoreEditor);
			}
		}
	}

	/**
	 * Helper method to set editor
	 */
	private void setEditor(EcoreEditor newEditor) {

		if (editor == newEditor) {
			return;
		}

		uninstallSelectionChangedListener();

		final EcoreEditor oldEditor = editor;
		editor = newEditor;
		editorChanged(oldEditor);

		installSelectionChangedListener();
	}

	/**
	 * Start to listen to the selection of the current editor
	 */
	protected void installSelectionChangedListener() {
		if (editor != null) {
			editor.addSelectionChangedListener(this);
		}
	}

	/**
	 * Stop to listen to the selection of the current editor
	 */
	protected void uninstallSelectionChangedListener() {
		if (editor != null) {
			editor.removeSelectionChangedListener(this);
		}
	}

	/**
	 * Notify when the Ecore editor has changed
	 *
	 * @param oldEditor
	 */
	protected void editorChanged(EcoreEditor oldEditor) {
		// to be implemented by sub classes
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		selectionChanged((IStructuredSelection) event.getSelection());
	}

	/**
	 * The selection in the editor has changed
	 */
	protected abstract void selectionChanged(IStructuredSelection selection);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		getSite().getPage().removePartListener(partListener);
		uninstallSelectionChangedListener();
		super.dispose();
	}

	/**
	 * Getter for editor
	 *
	 * @return Ecore editor
	 */
	public EcoreEditor getEditor() {
		return editor;
	}
}
