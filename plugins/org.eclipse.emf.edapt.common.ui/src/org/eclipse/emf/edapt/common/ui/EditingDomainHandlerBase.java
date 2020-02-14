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
package org.eclipse.emf.edapt.common.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handler that provides the {@link EditingDomain} of the editor on which the
 * command is performed.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class EditingDomainHandlerBase extends AbstractHandler {

	/** {@inheritDoc} */
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		final EditingDomain domain = ((IEditingDomainProvider) HandlerUtil
			.getActiveEditor(event)).getEditingDomain();
		return execute(domain, event);
	}

	/** Execute the command. */
	protected abstract Object execute(EditingDomain domain, ExecutionEvent event)
		throws ExecutionException;
}
