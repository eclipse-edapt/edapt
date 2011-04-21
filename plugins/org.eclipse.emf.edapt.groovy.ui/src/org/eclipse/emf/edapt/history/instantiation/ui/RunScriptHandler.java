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
package org.eclipse.emf.edapt.history.instantiation.ui;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.history.instantiation.ScriptCommand;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.jface.dialogs.IDialogConstants;


/**
 * Action to execute a Groovy script
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class RunScriptHandler extends EditingDomainListenerHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomainListener listener, EcoreEditor editor) {
		ScriptDialog dialog = new ScriptDialog();
		if (dialog.open() == IDialogConstants.OK_ID) {
			String script = dialog.getScript();

			Command command = new ScriptCommand(script, listener.getExtent());
			listener.getEditingDomain().getCommandStack().execute(
					command);
		}
		return null;
	}
}
