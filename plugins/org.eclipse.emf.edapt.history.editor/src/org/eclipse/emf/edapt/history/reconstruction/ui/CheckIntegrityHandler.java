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
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.history.reconstruction.IntegrityChecker;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Action to check the integrity of a history. This is performed by
 * reconstructing the metamodel from the history and comparing it with the
 * current version of the metamodel.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CheckIntegrityHandler extends AbstractHandler {

	/** {@inheritDoc} */
	@Override
	public Object execute(ExecutionEvent event) {
		final History history = SelectionUtils.getSelectedElement(HandlerUtil
			.getCurrentSelection(event), History.class);
		@SuppressWarnings("unused")
		final IEditingDomainProvider editor = (IEditingDomainProvider) HandlerUtil
			.getActiveEditor(event);
		final IntegrityChecker checker = new IntegrityChecker(history);

		if (checker.check()) {
			MessageDialog.openInformation(
				Display.getDefault().getActiveShell(), "Integrity check of history succeeded", //$NON-NLS-1$
				"Integrity check of history succeeded"); //$NON-NLS-1$
		} else {
			@SuppressWarnings("unused")
			final String title = "Integrity check of history failed"; //$NON-NLS-1$
			@SuppressWarnings("unused")
			final String message = "This dialog shows the differences between the current metamodel version" //$NON-NLS-1$
				+ "and the one reconstructed from the history"; //$NON-NLS-1$

			// CB Migrate

			System.out.println("CB Migrate model dialog to repair integrity of the model"); //$NON-NLS-1$
			// ModelDialog diffDialog = new ModelDialog(checker.getDiffModel(),
			// title, message);
			//
			// if (diffDialog.open() == IDialogConstants.OK_ID) {
			// CommandStack commandStack = editor.getEditingDomain()
			// .getCommandStack();
			//
			// RepairMetamodelIntegrityCommand metamodelCommand = new RepairMetamodelIntegrityCommand(
			// history.getRootPackages(), checker.getDiffModel());
			// commandStack.execute(metamodelCommand);
			//
			// RepairHistoryIntegrityCommand integrityCommand = new RepairHistoryIntegrityCommand(
			// metamodelCommand);
			// commandStack.execute(integrityCommand);
			// }
		}

		return null;
	}

}
