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

import java.util.List;

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
 * Action to compare metamodel versions.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CompareHandler extends AbstractHandler {

	/** {@inheritDoc} */
	@Override
	public Object execute(ExecutionEvent event) {
		final List<EObject> selectedElements = HandlerUtils
			.getSelectedElements(event, EObject.class);
		if (!selectedElements.isEmpty()) {
			final EObject from = selectedElements.get(0);
			final EObject to = selectedElements.get(selectedElements.size() - 1);
			try {
				final ComparisonView view = HandlerUtils.showView(event,
					ComparisonView.ID);
				if (from instanceof Change) {
					view.setChange((Change) from, (Change) to);
				} else {
					view.setRelease((Release) from, (Release) to);
				}
			} catch (final PartInitException e) {
				LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
			}
		}
		return null;
	}
}
