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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Action to create an initial history.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CreateHistoryHandler extends OperationBrowserHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(OperationBrowser browser, ExecutionEvent event) {
		final EcoreEditor editor = browser.getEditor();
		if (editor == null) {
			return null;
		}
		final CreateHistoryWizard wizard = new CreateHistoryWizard(editor);
		final WizardDialog dialog = new WizardDialog(Display.getDefault()
			.getActiveShell(), wizard);
		dialog.open();
		return null;
	}
}
