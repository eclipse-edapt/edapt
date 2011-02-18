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
import java.util.Collections;

import junit.framework.Assert;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.reconstruction.IntegrityChecker;
import org.eclipse.emf.edapt.history.reconstruction.Mapping;
import org.eclipse.emf.edapt.history.reconstruction.MigratorCodeGenerator;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.migration.execution.MigratorRegistry;
import org.eclipse.emf.edapt.migration.test.MigrationTestBase;
import org.eclipse.emf.edapt.tests.util.TestUtils;
import org.eclipse.emf.edit.domain.EditingDomain;


/**
 * Test the full life cycle of coupled evolution:
 * recording, migrator generation, migration
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class LifecycleTestBase extends MigrationTestBase {
	
	/**
	 * Context URI
	 */
	private URI contextURI;

	/**
	 * URI for temporary content
	 */
	private URI tempURI;
	
	/**
	 * URI with migrator
	 */
	private URI migratorURI;

	/**
	 * Identifier of the example
	 */
	private String id;

	/** Test the full lifecycle of COPE. */
	protected void testLifecycle(String id) throws Exception {
		testLifecycle(id, 0);
	}

	/** Test the full lifecyle of COPE. */
	protected void testLifecycle(String id, int expectedDifferences)
			throws Exception {
		this.id = id;
		prepare();
		checkRecording();
		checkMigratorGeneration();
		checkMigration(expectedDifferences);
	}
	
	/**
	 * Delete old stuff
	 */
	private void prepare() {		

		contextURI = URIUtils.getURI("data/" + id);
		tempURI = contextURI.appendSegment("temp");
		migratorURI = tempURI.appendSegment("migrator");

		FileUtils.deleteContents(URIUtils.getJavaFile(tempURI));
		URIUtils.getJavaFile(migratorURI).mkdir();
	}
	
	/**
	 * Test recording of changes to the history
	 */
	private void checkRecording() throws IOException {
		
		History history = loadHistory();
		EcoreForwardReconstructor reconstructor = reconstructSourceRelease(history);
		EditingDomain editingDomain = TestUtils
				.createEditingDomain(reconstructor.getResourceSet());
		EditingDomainListener listener = initRecorder(editingDomain);
		interpretHistory(history, reconstructor.getMapping(), listener);
	}

	/**
	 * load history
	 */
	private History loadHistory() throws IOException {
		HistoryPackage.eINSTANCE.getAdd();

		URI historyURI = contextURI.appendSegment(id).appendFileExtension(HistoryUtils.HISTORY_FILE_EXTENSION);
		History history = (History) ResourceUtils.loadResourceSet(historyURI).getResources().get(0).getContents().get(0);
		return history;
	}

	/**
	 * reconstruct source release
	 */
	private EcoreForwardReconstructor reconstructSourceRelease(History history) {
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(tempURI);
		reconstructor.reconstruct(history.getFirstRelease(), false);
		
		ResourceSet resourceSet = history.eResource().getResourceSet();
		for(Resource resource : resourceSet.getResources()) {
			if(resource == HistoryUtils.getRootResource(resourceSet) || resource == HistoryUtils.getHistoryResource(resourceSet)) {
				continue;
			}
			reconstructor.getResourceSet().getResources().add(resource);
		}
		return reconstructor;
	}

	/**
	 * init recorder
	 */
	private EditingDomainListener initRecorder(EditingDomain editingDomain) {
		EditingDomainListener listener = new EditingDomainListener(editingDomain);
		listener.createHistory(Collections.singletonList(editingDomain
				.getResourceSet().getResources().get(0)));
		IntegrityChecker checker = new IntegrityChecker(listener.getHistory());
		Assert.assertTrue(checker.check());
		listener.beginListening();
		return listener;
	}

	/**
	 * interpret history until target release
	 */
	private void interpretHistory(History history, Mapping mapping, EditingDomainListener listener) throws IOException {
		EditingDomain domain = listener.getEditingDomain();
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(migratorURI);
		reconstructor.addReconstructor(new HistoryInterpreter(listener, mapping));
		reconstructor.reconstruct(history.getLastRelease(), false);

		ResourceUtils.saveResourceSet(domain.getResourceSet());
		
		Assert.assertTrue(new IntegrityChecker(listener.getHistory()).check());
	}

	/**
	 * Test the generation of the migrator
	 */
	private void checkMigratorGeneration() throws IOException {
		
		ResourceSet resourceSet = ResourceUtils.loadResourceSet(tempURI.appendSegment(id).appendFileExtension(HistoryUtils.HISTORY_FILE_EXTENSION));
		Resource resource = resourceSet.getResources().get(0);
		History history = (History) resource.getContents().get(0);
		
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(migratorURI);
		reconstructor.addReconstructor(new MigratorCodeGenerator(migratorURI));
		reconstructor.reconstruct(history.getLastRelease(), false);
	}

	/**
	 * Test the migration of a model
	 */
	private void checkMigration(int expectedDifferences) throws Exception {
				
		URI sourceModelURI = contextURI.appendSegment(id + "_r0").appendFileExtension("xmi");
		URI expectedTargetModelURI = contextURI.appendSegment(id + "_r1").appendFileExtension("xmi");
		URI expectedTargetMetamodelURI = contextURI.appendSegment(id).appendFileExtension(ResourceUtils.ECORE_FILE_EXTENSION);
		
		MigratorRegistry.getInstance().registerMigrator(migratorURI);
		
		testMigration(sourceModelURI, expectedTargetModelURI,
				expectedTargetMetamodelURI, expectedDifferences);
	}
}