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
package org.eclipse.emf.edapt.cope.history.reconstruction.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.cope.common.LoggingUtils;
import org.eclipse.emf.edapt.cope.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.cope.history.Change;
import org.eclipse.emf.edapt.cope.history.Release;
import org.eclipse.emf.edapt.cope.history.presentation.HistoryEditorPlugin;
import org.eclipse.ui.PartInitException;


/**
 * Action to show the reconstructed version of the metamodel
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ShowReconstructionHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		EObject element = HandlerUtils.getSelectedElement(event);
		try {
			ReconstructionView view = (ReconstructionView) HandlerUtils
					.showView(event, ReconstructionView.ID);
			if (element instanceof Change) {
				view.setChange((Change) element);
			} else {
				view.setRelease((Release) element);
			}
		} catch (PartInitException e) {
			LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
		}
		return null;
	}
}
