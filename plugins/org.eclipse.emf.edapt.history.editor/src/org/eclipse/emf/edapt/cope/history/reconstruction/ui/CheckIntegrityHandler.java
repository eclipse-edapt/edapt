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
package org.eclipse.emf.edapt.cope.history.reconstruction.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.edapt.cope.common.ui.ModelDialog;
import org.eclipse.emf.edapt.cope.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.cope.history.History;
import org.eclipse.emf.edapt.cope.history.reconstruction.IntegrityChecker;
import org.eclipse.emf.edapt.cope.history.reconstruction.RepairHistoryIntegrityCommand;
import org.eclipse.emf.edapt.cope.history.reconstruction.RepairMetamodelIntegrityCommand;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
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
	public Object execute(ExecutionEvent event) {
		History history = SelectionUtils.getSelectedElement(HandlerUtil
				.getCurrentSelection(event));
		IEditingDomainProvider editor = (IEditingDomainProvider) HandlerUtil
				.getActiveEditor(event);
		IntegrityChecker checker = new IntegrityChecker(history);

		if (checker.check()) {
			MessageDialog.openInformation(
					Display.getDefault().getActiveShell(),
					"Integrity check of history succeeded",
					"Integrity check of history succeeded");
		} else {
			String title = "Integrity check of history failed";
			String message = "This dialog shows the differences between the current metamodel version"
					+ "and the one reconstructed from the history";

			ModelDialog diffDialog = new ModelDialog(checker.getDiffModel(),
					title, message);
			if (diffDialog.open() == IDialogConstants.OK_ID) {
				CommandStack commandStack = editor.getEditingDomain()
						.getCommandStack();

				RepairMetamodelIntegrityCommand metamodelCommand = new RepairMetamodelIntegrityCommand(
						history.getRootPackages(), checker.getDiffModel());
				commandStack.execute(metamodelCommand);

				RepairHistoryIntegrityCommand integrityCommand = new RepairHistoryIntegrityCommand(
						metamodelCommand);
				commandStack.execute(integrityCommand);
			}
		}

		return null;
	}

}
