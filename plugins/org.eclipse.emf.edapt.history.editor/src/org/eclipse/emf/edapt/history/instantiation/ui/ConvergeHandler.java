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

import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;


/**
 * Action to support the convergence of a source metamodel to a target metamodel
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ConvergeHandler extends EditingDomainListenerHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomainListener listener, EcoreEditor editor) {
		ResourceDialog dialog = new ResourceDialog(Display.getDefault()
				.getActiveShell(), "Target metamodel", SWT.None);
		if (dialog.open() != IDialogConstants.OK_ID) {
			return null;
		}

		URI targetURI = dialog.getURIs().get(0);
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			ConvergenceView view = (ConvergenceView) page
					.showView(ConvergenceView.ID);
			view.init(targetURI, editor);
		} catch (PartInitException e) {
			LoggingUtils
					.logError(HistoryEditorPlugin.getPlugin(), e);
		}
		return null;
	}
}
