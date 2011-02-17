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
package org.eclipse.emf.edapt.cope.history.recorder.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.cope.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.cope.history.recorder.AddResourceCommand;
import org.eclipse.emf.edapt.cope.history.recorder.EditingDomainListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * Command to add a metamodel to the history.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class AddResourceHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		EcoreEditor editor = (EcoreEditor) HandlerUtil.getActiveEditor(event);
		EditingDomainListener listener = EcoreEditorDetector.getInstance()
				.getListener(editor);

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Resource resource = SelectionUtils.getSelectedElement(selection);

		AddResourceCommand command = new AddResourceCommand(listener, resource);
		if (listener.isRecorded(resource)) {
			MessageDialog.openInformation(
					Display.getDefault().getActiveShell(),
					"Metamodel already recorded",
					"The metamodel is already recorded.");
			return null;
		}
		editor.getEditingDomain().getCommandStack().execute(command);
		return null;
	}
}
