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
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.ui.EditingDomainHandlerBase;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.presentation.util.SpecialEditorInput;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;


/**
 * Action to edit the migration of a {@link MigrationChange}.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OldEditMigrationHandler extends EditingDomainHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomain domain, ExecutionEvent event) {
		MigrationChange change = HandlerUtils.getSelectedElement(event);
		IStorageEditorInput editorInput = new SpecialEditorInput(change, domain);
		try {
			String editorId = "org.codehaus.groovy.eclipse.editor.GroovyEditor";
			HandlerUtils.openEditor(event, editorId, editorInput);
		} catch (PartInitException e) {
			LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
		}
		return null;
	}
}
