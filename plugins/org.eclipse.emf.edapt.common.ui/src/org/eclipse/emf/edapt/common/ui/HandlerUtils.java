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
package org.eclipse.emf.edapt.common.ui;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Helper methods to support command handlers.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class HandlerUtils {

	/** Hidden constructor. */
	private HandlerUtils() {
		// This class should not be instantiated.
	}

	/** Get the selected element of type V. */
	public static <V> V getSelectedElement(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		return SelectionUtils.getSelectedElement(selection);
	}

	/** Get a list of selected elements of type V. */
	public static <V> List<V> getSelectedElements(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		return SelectionUtils.getSelectedElements(selection);
	}

	/** Get the active page from within a handler. */
	public static IWorkbenchPage getActivePage(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		return window.getActivePage();
	}

	/** Open an editor from within a handler. */
	@SuppressWarnings("unchecked")
	public static <E extends IEditorPart> E openEditor(ExecutionEvent event,
			String editorId, IEditorInput editorInput) throws PartInitException {
		try {
			IWorkbenchPage page = getActivePage(event);
			return (E) page.openEditor(editorInput, editorId);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/** Show a view from within a handler. */
	@SuppressWarnings("unchecked")
	public static <V extends IViewPart> V showView(ExecutionEvent event,
			String viewId) throws PartInitException {
		try {
			IWorkbenchPage page = getActivePage(event);
			return (V) page.showView(viewId);
		} catch (ClassCastException e) {
			return null;
		}
	}
}
