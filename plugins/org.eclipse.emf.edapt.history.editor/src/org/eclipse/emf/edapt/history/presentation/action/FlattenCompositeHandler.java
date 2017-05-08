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
package org.eclipse.emf.edapt.history.presentation.action;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edapt.common.ui.EditingDomainHandlerBase;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Action to flatten a {@link CompositeChange}.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class FlattenCompositeHandler extends EditingDomainHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomain domain, ExecutionEvent event) {
		final CompositeChange change = HandlerUtils.getSelectedElement(event, CompositeChange.class);
		final Release release = (Release) change.eContainer();
		final Command command = new ChangeCommand(release) {

			@Override
			protected void doExecute() {
				final int index = release.getChanges().indexOf(change);
				release.getChanges().remove(index);
				release.getChanges().addAll(index, change.getChanges());
			}

		};
		domain.getCommandStack().execute(command);
		return null;
	}
}
