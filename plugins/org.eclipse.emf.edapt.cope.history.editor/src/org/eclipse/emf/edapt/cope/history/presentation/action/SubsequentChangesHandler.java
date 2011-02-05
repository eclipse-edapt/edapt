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
package org.eclipse.emf.edapt.cope.history.presentation.action;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.edapt.cope.common.ui.EditingDomainHandlerBase;
import org.eclipse.emf.edapt.cope.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.cope.history.Change;
import org.eclipse.emf.edapt.cope.history.Release;
import org.eclipse.emf.edit.domain.EditingDomain;


/**
 * Action for a number of subsequent changes within a release.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class SubsequentChangesHandler<C extends Change> extends
		EditingDomainHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected final Object execute(EditingDomain domain, ExecutionEvent event)
			throws ExecutionException {
		List<C> changes = HandlerUtils.getSelectedElements(event);
		Release release = SubsequentChangesPropertyTester.sort(changes);
		return execute(release, changes, domain, event);
	}

	/** Convenience method to execute this command. */
	protected abstract Object execute(Release release, List<C> changes,
			EditingDomain domain, ExecutionEvent event)
			throws ExecutionException;
}
