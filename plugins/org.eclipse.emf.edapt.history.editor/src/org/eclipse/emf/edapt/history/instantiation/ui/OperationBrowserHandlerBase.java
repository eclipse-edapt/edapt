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
package org.eclipse.emf.edapt.history.instantiation.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Base class for command handlers that are attached to a view.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class OperationBrowserHandlerBase extends AbstractHandler {

	/** {@inheritDoc} */
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		final OperationBrowser browser = getOperationBrowser(event);
		Assert.isNotNull(browser);
		if (browser.getEditor() == null) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
				"Metamodel not found", //$NON-NLS-1$
				"No metamodel found. Please open one."); //$NON-NLS-1$
			return null;
		}
		return execute(browser, event);
	}

	/** Get the operation browser. */
	private OperationBrowser getOperationBrowser(ExecutionEvent event) {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		for (final IViewReference reference : window.getActivePage()
			.getViewReferences()) {
			if (OperationBrowser.ID.equals(reference.getId())) {
				final IViewPart view = reference.getView(true);
				/*
				 * check the instance since due to problems with the workspace, etc. the view might be an ErrorViewPart
				 */
				if (view instanceof OperationBrowser) {
					return (OperationBrowser) view;
				}
			}
		}
		return null;
	}

	/** Execute the command using the operation browser. */
	protected abstract Object execute(OperationBrowser browser,
		ExecutionEvent event) throws ExecutionException;

}
