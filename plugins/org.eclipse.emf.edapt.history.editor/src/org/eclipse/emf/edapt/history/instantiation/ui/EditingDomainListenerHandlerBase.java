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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.history.recorder.ui.EcoreEditorDetector;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Base class for command handlers that are attached to a metamodel whose
 * history is recorded.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class EditingDomainListenerHandlerBase extends
	OperationBrowserHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(OperationBrowser browser, ExecutionEvent event) {
		final EditingDomainListener listener = EcoreEditorDetector.getInstance()
			.getListener(browser.getEditor());
		if (listener == null) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
				"History not found", "No history found. Create one first."); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		return execute(listener, browser.getEditor());
	}

	/** Execute the command using the metamodel listener. */
	protected abstract Object execute(EditingDomainListener listener,
		EcoreEditor editor);
}
