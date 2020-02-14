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

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.edapt.history.instantiation.ReplaceCommand;
import org.eclipse.emf.edapt.history.presentation.action.SubsequentChangesHandler;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.IDialogConstants;

/**
 * Action to replace a sequence of changes by the execution of an operation
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReplaceHandler extends SubsequentChangesHandler<PrimitiveChange> {

	/** {@inheritDoc} */
	@Override
	protected Object execute(Release release, List<PrimitiveChange> changes,
		EditingDomain domain, ExecutionEvent event) {
		final ReplaceDialog dialog = new ReplaceDialog(changes);
		if (dialog.open() == IDialogConstants.OK_ID) {
			final ReplaceCommand command = dialog.getReplaceCommand();
			domain.getCommandStack().execute(command);
		}
		return null;
	}
}
