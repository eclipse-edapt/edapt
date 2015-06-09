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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Action to load a model based on the selected or registered migrators.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class LoadModelHandler extends MigratorHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected void run() {
		final List<URI> modelURIs = getURIs();
		final List<URI> metamodelURIs = getMetamodelURIs(modelURIs);
		if (metamodelURIs.size() > 0) {
			loadEcoreModel(modelURIs, metamodelURIs);
			return;
		}
		super.run();
	}

	/** Filter the metamodel URIs from a list of model URIs. */
	private List<URI> getMetamodelURIs(List<URI> modelURIs) {
		final List<URI> metamodelURIs = new ArrayList<URI>();
		for (final Iterator<URI> i = modelURIs.iterator(); i.hasNext();) {
			final URI uri = i.next();
			if (MetamodelUtils.isMetamodelURI(uri)) {
				i.remove();
				metamodelURIs.add(uri);
			}
		}
		return metamodelURIs;
	}

	/** Load the model using a metamodel. */
	private void loadEcoreModel(List<URI> modelURIs, List<URI> metamodelURIs) {
		try {
			final ResourceSet resourceSet = ResourceUtils.loadResourceSet(
				metamodelURIs, new ArrayList<EPackage>());
			final List<EPackage> ePackages = ResourceUtils.getRootElements(
				resourceSet, EPackage.class);
			loadModel(modelURIs, ePackages);
		} catch (final IOException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(), e);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void run(List<URI> modelURIs, Migrator migrator, Release release) {
		final Metamodel metamodel = migrator.getMetamodel(release);
		final List<EPackage> ePackages = metamodel.getEPackages();
		loadModel(modelURIs, ePackages);
	}

	/** Load a model based on a number of packages. */
	private void loadModel(List<URI> modelURIs, List<EPackage> ePackages) {
		final Map<String, EPackage> backup = ResourceUtils.register(ePackages,
			EPackage.Registry.INSTANCE);
		try {
			final IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
			final EcoreEditor editor = (EcoreEditor) page.openEditor(
				new URIEditorInput(modelURIs.get(0)),
				"org.eclipse.emf.ecore.presentation.ReflectiveEditorID"); //$NON-NLS-1$
			final ResourceSet resourceSet = editor.getEditingDomain()
				.getResourceSet();
			ResourceUtils.resolveAll(resourceSet);
			for (final URI modelURI : modelURIs) {
				resourceSet.getResource(modelURI, true);
				for (final EPackage ePackage : ePackages) {
					final Resource resource = ePackage.eResource();
					if (!resourceSet.getResources().contains(resource)) {
						resourceSet.getResources().add(resource);
					}
				}
			}
		} catch (final PartInitException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(), e);
		} finally {
			for (final Entry<String, EPackage> e : backup.entrySet()) {
				EPackage.Registry.INSTANCE.put(e.getKey(), e.getValue());
			}
		}
	}
}
