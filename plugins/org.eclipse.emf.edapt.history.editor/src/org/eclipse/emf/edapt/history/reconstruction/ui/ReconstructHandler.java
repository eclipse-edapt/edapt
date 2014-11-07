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
package org.eclipse.emf.edapt.history.reconstruction.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;


/**
 * Action to reconstruct a metamodel until a release or right before a change.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReconstructHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		EObject target = HandlerUtils.getSelectedElement(event);

		EditorPart editor = (EditorPart) HandlerUtil.getActiveEditor(event);
		FileEditorInput editorInput = (FileEditorInput) editor.getEditorInput();
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(Display
				.getCurrent().getActiveShell(), editorInput.getFile()
				.getParent(), false, "");

		if (dialog.open() == IDialogConstants.OK_ID) {
			IPath path = (IPath) dialog.getResult()[0];

			// reconstruct metamodel and history
			EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
					URIUtils.getURI(path));
			customizeReconstructor(reconstructor);
			if (target instanceof Change) {
				reconstructor.reconstruct((Change) target, false);
			} else {
				reconstructor.reconstruct((Release) target, false);
			}
			reconstructor.save();
		}
		return null;
	}

	/** Customize the reconstructor (to be overwritten by sub classes). */
	protected void customizeReconstructor(
			@SuppressWarnings("unused") EcoreForwardReconstructor reconstructor) {
		// to be implemented by sub classes
	}
}
