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
package org.eclipse.emf.edapt.history.reconstruction.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.internal.common.URIUtils;
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
	@Override
	public Object execute(ExecutionEvent event) {
		final EObject target = HandlerUtils.getSelectedElement(event, EObject.class);

		final EditorPart editor = (EditorPart) HandlerUtil.getActiveEditor(event);
		final FileEditorInput editorInput = (FileEditorInput) editor.getEditorInput();
		final ContainerSelectionDialog dialog = new ContainerSelectionDialog(Display
			.getCurrent().getActiveShell(),
			editorInput.getFile()
				.getParent(),
			false, ""); //$NON-NLS-1$

		if (dialog.open() == IDialogConstants.OK_ID) {
			final IPath path = (IPath) dialog.getResult()[0];

			// reconstruct metamodel and history
			final EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
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
	protected void customizeReconstructor(EcoreForwardReconstructor reconstructor) {
		// to be implemented by sub classes
	}
}
