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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.internal.MigratorOptions;
import org.eclipse.emf.edapt.internal.migration.internal.Persistency;
import org.eclipse.emf.edapt.migration.ReleaseUtils;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.MigratorRegistry;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;
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

	/** {@inheritDoc} */
	@Override
	public Object execute(ExecutionEvent event) {
		updateSelection(HandlerUtil.getCurrentSelection(event));
		run();
		return null;
	}

	/** Run the action. */
	protected void run() {
		final List<URI> modelURIs = getURIs();
		final Migrator migrator = getMigrator(modelURIs);
		if (migrator == null) {
			return;
		}

		final Release release = getRelease(modelURIs, migrator);
		if (release == null) {
			return;
		}

		run(modelURIs, migrator, release);
	}

	/** Run the action. */
	protected abstract void run(List<URI> modelURIs, Migrator migrator,
		Release release);

	/** Get the migrator for a model. */
	protected Migrator getMigrator(final List<URI> modelURIs) {

		if (modelURIs.isEmpty()) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
				"Namespace", "Not a valid model"); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		MigratorOptions.getInstance().setOracle(new InteractiveOracle());
		MigratorOptions.getInstance().setDebugger(new InteractiveDebugger());

		final String nsURI = ReleaseUtils.getNamespaceURI(modelURIs.get(0));

		if (nsURI == null) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
				"Namespace", "Not a valid model"); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		final Migrator migrator = getRegistryMigrator(nsURI);
		return migrator;
	}

	/** Search for a migrator in the registry. */
	private Migrator getRegistryMigrator(String nsURI) {
		final Migrator migrator = MigratorRegistry.getInstance().getMigrator(
			nsURI);
		if (migrator == null) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
				"Migrator", //$NON-NLS-1$
				"No migrator registered for model with that namespace"); //$NON-NLS-1$
			return null;
		}
		return migrator;
	}

	/** Infer the release of a model. */
	protected Release getRelease(final List<URI> modelURIs,
		final Migrator migrator) {
		final Set<Release> releases = new HashSet<Release>(
			migrator.getRelease(modelURIs.get(0)));
		Release release = null;
		if (releases.size() > 1) {
			for (final Iterator<Release> i = releases.iterator(); i.hasNext();) {
				final Release r = i.next();
				final Metamodel metamodel = migrator.getMetamodel(r);
				try {
					final Model model = Persistency.loadModel(modelURIs, metamodel,
						migrator.getResourceSetFactory());
					model.checkConformance();
				} catch (final Exception e) {
					i.remove();
				}
			}
		}

		if (releases.size() > 1) {
			final ReleaseDialog dialog = new ReleaseDialog(releases);
			if (dialog.open() != IDialogConstants.OK_ID) {
				return null;
			}
			release = dialog.getRelease();
		} else {
			release = releases.iterator().next();
		}
		return release;
	}

	/** Returns the URIs based on files. */
	protected List<URI> getURIs() {
		final List<URI> uris = new ArrayList<URI>();
		for (final IFile file : selectedFiles) {
			uris.add(URIUtils.getURI(file));
		}
		return uris;
	}

	/** Update the selection. */
	private void updateSelection(ISelection selection) {
		selectedFiles = SelectionUtils.getSelectedElements(selection, IFile.class);
	}

	/** Get the selected files. */
	protected List<IFile> getSelectedFiles() {
		return selectedFiles;
	}
}
