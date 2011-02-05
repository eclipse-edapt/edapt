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
package org.eclipse.emf.edapt.cope.common.ui;

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
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		EditingDomain domain = ((IEditingDomainProvider) HandlerUtil
				.getActiveEditor(event)).getEditingDomain();
		return execute(domain, event);
	}

	/** Execute the command. */
	protected abstract Object execute(EditingDomain domain, ExecutionEvent event)
			throws ExecutionException;
}
