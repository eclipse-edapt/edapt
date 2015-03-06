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
package org.eclipse.emf.edapt.migration.ui;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.internal.BackupUtils;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.migration.Metamodel;

/**
 * Action to restore the backup of the model file.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class RestoreBackupHandler extends MigratorHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected void run(List<URI> backupURIs, Migrator migrator, Release release) {

		try {
			final Metamodel metamodel = migrator.getMetamodel(release);
			BackupUtils.restore(backupURIs, metamodel);
			for (final URI backupURI : backupURIs) {
				final IFile file = URIUtils.getFile(backupURI);
				if (file.exists()) {
					file.delete(true, new NullProgressMonitor());
				}
			}

			for (final IFile backupFile : getSelectedFiles()) {
				backupFile.getParent().refreshLocal(1,
					new NullProgressMonitor());
			}
		} catch (final CoreException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(), e);
		} catch (final IOException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(), e);
		}
	}
}