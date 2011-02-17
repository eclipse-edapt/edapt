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
package org.eclipse.emf.edapt.tests.history.lifecycle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

import junit.framework.Assert;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.instantiation.MigratorImporter;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.migration.test.MigrationTestBase;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;


/**
 * Base class for example tests
 * 
 * @author herrmama
 *
 */
public abstract class TestBase extends MigrationTestBase {
	
	static {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	}	

	/**
	 * Initialize the recorder
	 * 
	 * @param metamodelURI
	 * @return Pair of edit domain and recorder
	 * @throws IOException
	 */
	protected EditingDomainListener initRecorder(URI metamodelURI) throws IOException {
		
		AdapterFactory adapterFactory = new ReflectiveItemProviderAdapterFactory();
		CommandStack commandStack = new BasicCommandStack();
		
		EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
		
		// load metamodel
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.createResource(metamodelURI);
		resource.load(null);
		Assert.assertEquals(1, resourceSet.getResources().size());
		
		// init history
		EditingDomainListener listener = new EditingDomainListener(editingDomain);
		listener.createHistory(Collections.singletonList(resource));
		listener.beginListening();
		
		return listener;
	}

	/**
	 * Reconstruct a certain release of a metamodel
	 * 
	 * @param release
	 * @return Root package
	 */
	protected EPackage reconstruct(Release release) {
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(URI.createFileURI("temp/metamodel.ecore"));
		reconstructor.reconstruct(release, false);		
		
		EPackage reconstructed = (EPackage) reconstructor.getResourceSet()
				.getResources().get(0).getContents().get(0);
		return reconstructed;
	}
	
	/**
	 * Import a migrator into the metamodel history
	 * 
	 * @param migratorURI
	 * @param targetMetamodelURI
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	protected void importMigrator(URI migratorURI, URI targetMetamodelURI) throws IOException, MalformedURLException {
		
		URI sourceMetamodelURI = migratorURI.appendSegment("release1").appendSegment("release0.ecore");
		URI historyURI = targetMetamodelURI.trimFileExtension().appendFileExtension("history");
		
		FileUtils.delete(historyURI);
		FileUtils.delete(targetMetamodelURI);		
		FileUtils.copy(sourceMetamodelURI, targetMetamodelURI);
		
		EditingDomainListener listener = initRecorder(targetMetamodelURI);
		EditingDomain domain = listener.getEditingDomain();
		listener.release();
		
		MigratorImporter importer = new MigratorImporter(listener);
		importer.importMigrator(migratorURI);
		
		ResourceUtils.saveResourceSet(domain.getResourceSet());
	}
}
