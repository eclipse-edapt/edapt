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
package org.eclipse.emf.edapt.history.presentation.action;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edapt.common.ui.EditingDomainHandlerBase;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * Action to replace a composite changes by its children primitive changes.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class FlattenMigrationHandler extends EditingDomainHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomain domain, ExecutionEvent event) {

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		final MigrationChange change = SelectionUtils
				.getSelectedElement(selection);

		final Release release = (Release) change.eContainer();
		Command command = new ChangeCommand(release) {

			@Override
			protected void doExecute() {
				int index = release.getChanges().indexOf(change);
				release.getChanges().remove(index);
				release.getChanges().addAll(index, change.getChanges());
			}
			
		};
		domain.getCommandStack().execute(command);
		return null;
	}
}