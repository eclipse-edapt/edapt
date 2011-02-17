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

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.ui.PartInitException;


/**
 * Action to compare metamodel versions.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CompareHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		List<EObject> selectedElements = HandlerUtils
				.getSelectedElements(event);
		if (!selectedElements.isEmpty()) {
			EObject from = selectedElements.get(0);
			EObject to = selectedElements.get(selectedElements.size() - 1);
			try {
				ComparisonView view = HandlerUtils.showView(event,
						ComparisonView.ID);
				if (from instanceof Change) {
					view.setChange((Change) from, (Change) to);
				} else {
					view.setRelease((Release) from, (Release) to);
				}
			} catch (PartInitException e) {
				LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
			}
		}
		return null;
	}
}
