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
package org.eclipse.emf.edapt.cope.history.instantiation.ui;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.cope.common.LoggingUtils;
import org.eclipse.emf.edapt.cope.common.URIUtils;
import org.eclipse.emf.edapt.cope.history.instantiation.MigratorImporter;
import org.eclipse.emf.edapt.cope.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.cope.history.recorder.EditingDomainListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;


/**
 * Action to import a migrator into a history
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ImportMigratorHandler extends EditingDomainListenerHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomainListener listener, EcoreEditor editor) {
		try {
			DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
			String fileName = dialog.open();
			if(fileName != null) {
				URI migratorURI = URIUtils.getURI(fileName);
				MigratorImporter importer = new MigratorImporter(listener);
				importer.importMigrator(migratorURI);
			}
		
		} catch(Exception e) {
			LoggingUtils
					.logError(HistoryEditorPlugin.getPlugin(), e);
		}
		return null;
	}
}
