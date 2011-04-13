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
package org.eclipse.emf.edapt.migration.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.reconstruction.MigratorCodeGenerator;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.MigrationException;
import org.eclipse.emf.edapt.migration.execution.OldMigrator;
import org.eclipse.emf.edapt.migration.execution.OldMigratorRegistry;
import org.eclipse.emf.edapt.migration.execution.Persistency;
import org.eclipse.emf.edapt.migration.execution.ReleaseUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Base class to deal with migrators.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class MigratorHandlerBase extends AbstractHandler {

	/** File with model. */
	private List<IFile> selectedFiles;

	/** Folder with migrator. */
	private IFolder migratorFolder;

	/** File with history. */
	private IFile historyFile;

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		updateSelection(HandlerUtil.getCurrentSelection(event));
		run();
		return null;
	}

	/** Run the action. */
	protected void run() {
		final List<URI> modelURIs = getURIs();
		final OldMigrator migrator = getMigrator(modelURIs);
		if (migrator == null) {
			return;
		}

		int release = getRelease(modelURIs, migrator);
		if (release < 0) {
			return;
		}

		run(modelURIs, migrator, release);
	}

	/** Run the action. */
	protected abstract void run(List<URI> modelURIs, OldMigrator migrator,
			int release);

	/** Get the migrator for a model. */
	protected OldMigrator getMigrator(final List<URI> modelURIs) {

		OldMigratorRegistry.getInstance().setOracle(new InteractiveOracle());
		OldMigratorRegistry.getInstance().setDebugger(new InteractiveDebugger());

		String nsURI = ReleaseUtil.getNamespaceURI(modelURIs.get(0));

		if (nsURI == null) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Namespace", "Not a valid model");
			return null;
		}

		OldMigrator migrator = null;
		if (migratorFolder != null) {
			migrator = getFolderMigrator(nsURI);
		}
		if (migrator == null && historyFile != null) {
			return getHistoryMigrator(nsURI);
		}
		if (migrator == null) {
			return getRegistryMigrator(nsURI);
		}
		return migrator;
	}

	/** Load the migrator from a folder. */
	private OldMigrator getFolderMigrator(String nsURI) {
		try {
			OldMigrator migrator = new OldMigrator(URIUtils.getURI(migratorFolder));
			if (migrator.getNsURIs().contains(nsURI)) {
				return migrator;
			}
		} catch (MigrationException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(),
					"Not a valid migrator", e);
		}
		return null;
	}

	/** Load the migrator from a history file. */
	private OldMigrator getHistoryMigrator(String nsURI) {
		try {
			OldMigrator migrator = generateMigrator();
			if (migrator.getNsURIs().contains(nsURI)) {
				return migrator;
			}
		} catch (IOException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(),
					"Not a valid history file", e);
		} catch (MigrationException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(),
					"Not a valid migrator", e);
		}
		return null;
	}

	/** Search for a migrator in the registry. */
	private OldMigrator getRegistryMigrator(String nsURI) {
		final OldMigrator migrator = OldMigratorRegistry.getInstance().getMigrator(
				nsURI);
		if (migrator == null) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Migrator",
					"No migrator registered for model with that namespace");
			return null;
		}
		return migrator;
	}

	/** Generate the migrator. */
	private OldMigrator generateMigrator() throws MigrationException, IOException {
		History history = loadHistory();
		URI historyURI = history.eResource().getURI();

		URI migratorURI = URI.createFileURI(".migrator").resolve(historyURI);
		IFolder migratorFolder = URIUtils.getFolder(migratorURI);
		if (migratorFolder.exists()) {
			try {
				migratorFolder.delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				LoggingUtils.logError(MigrationUIActivator.getDefault(),
						"Migrator folder could not be deleted", e);
			}
		}

		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
				migratorURI);
		reconstructor.addReconstructor(new MigratorCodeGenerator(migratorURI));
		reconstructor.reconstruct(history.getLastRelease(), false);

		return new OldMigrator(migratorURI);
	}

	/** Load the history. */
	private History loadHistory() throws IOException {
		URI historyURI = URIUtils.getURI(historyFile);
		History history = ResourceUtils.loadElement(historyURI);
		return history;
	}

	/** Infer the release of a model. */
	protected int getRelease(final List<URI> modelURIs, final OldMigrator migrator) {
		Set<Integer> releases = new HashSet<Integer>(migrator
				.getRelease(modelURIs.get(0)));
		int release = -1;
		if (releases.size() > 1) {
			for (Iterator<Integer> i = releases.iterator(); i.hasNext();) {
				Integer r = i.next();
				Metamodel metamodel = migrator.getMetamodel(r);
				try {
					Model model = Persistency.loadModel(modelURIs, metamodel);
					model.checkConformance();
				} catch (Exception e) {
					i.remove();
				}
			}
		}

		if (releases.size() > 1) {
			ReleaseDialog dialog = new ReleaseDialog(releases);
			if (dialog.open() != IDialogConstants.OK_ID) {
				return -1;
			}
			release = dialog.getRelease();
		} else {
			release = releases.iterator().next();
		}
		return release;
	}

	/** Returns the URIs based on files. */
	protected List<URI> getURIs() {
		List<URI> uris = new ArrayList<URI>();
		for (IFile file : selectedFiles) {
			uris.add(URIUtils.getURI(file));
		}
		return uris;
	}

	/** Update the selection. */
	private void updateSelection(ISelection selection) {
		List<IResource> resources = SelectionUtils
				.getSelectedElements(selection);
		migratorFolder = null;
		historyFile = null;
		selectedFiles = new ArrayList<IFile>();
		for (IResource resource : resources) {
			if (resource instanceof IFolder) {
				migratorFolder = (IFolder) resource;
			} else if (resource instanceof IFile) {
				File file = resource.getProjectRelativePath().toFile();
				String extension = FileUtils.getExtension(file);
				if (extension != null && "history".equals(extension)) {
					historyFile = (IFile) resource;
				} else {
					selectedFiles.add((IFile) resource);
				}
			}
		}
	}

	/** Get the selected files. */
	protected List<IFile> getSelectedFiles() {
		return selectedFiles;
	}
}
