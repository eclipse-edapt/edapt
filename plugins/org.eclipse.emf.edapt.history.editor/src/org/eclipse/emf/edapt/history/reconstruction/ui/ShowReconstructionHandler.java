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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.Release;
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
	@Override
	public Object execute(ExecutionEvent event) {
		final EObject element = HandlerUtils.getSelectedElement(event);
		try {
			final ReconstructionView view = (ReconstructionView) HandlerUtils
				.showView(event, ReconstructionView.ID);
			if (element instanceof Change) {
				view.setChange((Change) element);
			} else {
				view.setRelease((Release) element);
			}
		} catch (final PartInitException e) {
			LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
		}
		return null;
	}
}
