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
package org.eclipse.emf.edapt.cdo.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOURIData;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.cdo.migration.execution.CDOMigrator;
import org.eclipse.emf.edapt.common.ResourceSetFactoryImpl;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.internal.migration.BackupUtils;
import org.eclipse.emf.edapt.internal.migration.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * A class for test cases to validate a model migration.
 * 
 * @author herrmama
 * @author Christophe Bouhier
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 028BD5369E87644723FF537FB5E55E4D
 */
public abstract class CDOMigrationTestBase extends TestCase {

	private CDONet4jSession testSession;

	/**
	 * Test a model migration.
	 * 
	 * @param migratorURI
	 *            URI of the migrator
	 * @param modelURI
	 *            URI of the model to be migrated
	 * @param expectedTargetModelURI
	 *            URI of the expected result of the migration
	 * @param expectedTargetMetamodelURI
	 *            URI of the target metamodel of the migration
	 */
	public void testMigration(URI migratorURI, URI modelURI,
			URI expectedTargetModelURI, URI expectedTargetMetamodelURI,
			IClassLoader loader) throws MigrationException, IOException {

		CDOMigrator migrator = new CDOMigrator(migratorURI, loader);
		testMigration(migrator, modelURI, expectedTargetModelURI,
				expectedTargetMetamodelURI);
	}

	/**
	 * Test a model migration.
	 * 
	 * @param migrator
	 *            Migrator
	 * @param modelURI
	 *            URI of the model to be migrated
	 * @param expectedTargetModelURI
	 *            URI of the expected result of the migration
	 * @param expectedTargetMetamodelURI
	 *            URI of the target metamodel of the migration
	 */
	public void testMigration(CDOMigrator migrator, URI modelURI,
			URI expectedTargetModelURI, URI expectedTargetMetamodelURI)
			throws MigrationException, IOException {
		testMigration(migrator, modelURI, expectedTargetModelURI,
				expectedTargetMetamodelURI, 0);
	}

	/**
	 * Test a model migration by loading the model into a source repository in
	 * CDO first, let the Migrator produces a migrated {@link Model} instance
	 * and push the result to a target repository. The result resource URI is
	 * specified by the argument <code>expectedTargetModelURI</code> in CDO URI
	 * format as specified by {@link CDOURIData}
	 * 
	 * @param migrator
	 *            Migrator
	 * @param modelURI
	 *            URI of the model to be migrated
	 * @param expectedTargetModelURI
	 *            URI of the expected result of the migration
	 * @param expectedTargetMetamodelURI
	 *            URI of the target metamodel of the migration
	 * @param expectedNumber
	 *            Expected number of differences
	 */
	public void testMigration(CDOMigrator migrator, URI modelURI,
			URI expectedTargetModelURI, URI expectedTargetMetamodelURI,
			int expectedNumber) throws MigrationException, IOException {

		// Get the release for source model URI.
		// FIXME, get release is not CDO Compatible (yet).
		Set<Release> releases = migrator.getRelease(modelURI);
		assertTrue(releases.size() >= 1);

		Release release = HistoryUtils.getMinimumRelease(releases);

		// Get a metamodel to construct to load the resource with the
		// matching EPackage.
		Metamodel metamodel = migrator.getMetamodel(release);

		// Rename in case we want a URI for a regular resource...
		// URI targetModelURI = rename(metamodel, modelURI, release);
		// List<URI> cdoTargetURIs = Collections.singletonList(targetModelURI);

		List<URI> modelURIs = Collections.singletonList(modelURI);

		// Get the source and target CDO URIs
		List<URI> cdoSourceURIs = cdoSourceURIs(modelURIs);
		List<URI> cdoTargetURIs = cdoTargetURIs(modelURIs);

		// Load the source set for the model URI(s).
		ResourceSet sourceSet = ResourceUtils.loadResourceSet(modelURIs,
				metamodel.getEPackages());

		migrator.clearCDORepositories(CDOTestUtil.HOST,
				CDOTestUtil.SOURCE_PORT, CDOTestUtil.REPO_SOURCE);

		migrator.clearCDORepositories(CDOTestUtil.HOST,
				CDOTestUtil.TARGET_PORT, CDOTestUtil.REPO_TARGET);

		// Copy the source set to the CDO source repo, let our CDOView provider
		// do all the CDO stuff
		// like creating a container, connector, session and transaction. See
		// EDaptCDOViewProvider
		CDOMigrator.copy(metamodel, sourceSet, cdoSourceURIs,
				new ResourceSetFactoryImpl());

		migrator.migrateAndCopy(cdoSourceURIs, release, null,
				new PrintStreamProgressMonitor(System.out), cdoTargetURIs);

		// Test Comparision is metamodel based.
		// TODO, FIX THE Result ASSERT.
		// Metamodel expectedMetamodel = Persistency
		// .loadMetamodel(expectedTargetMetamodelURI);
		// EObject actualModel = ResourceUtils
		// .loadResourceSet(targetModelURI,
		// expectedMetamodel.getEPackages()).getResources().get(0)
		// .getContents().get(0);
		//
		// EObject expectedModel = ResourceUtils
		// .loadResourceSet(expectedTargetModelURI,
		// expectedMetamodel.getEPackages()).getResources().get(0)
		// .getContents().get(0);
		//
		// ModelAssert
		// .assertDifference(expectedModel, actualModel, expectedNumber);

	}

	public void testMigration(CDOURIData sourceURIData,
			CDOURIData targetURIData, CDOMigrator migrator, int expectedNumber)
			throws MigrationException, IOException {

		migrator.migrateAndCopy(sourceURIData, targetURIData,
				new PrintStreamProgressMonitor(System.out));

		// Test Comparision is metamodel based.
		// TODO, FIX THE Result ASSERT.
		// Metamodel expectedMetamodel = Persistency
		// .loadMetamodel(expectedTargetMetamodelURI);
		// EObject actualModel = ResourceUtils
		// .loadResourceSet(targetModelURI,
		// expectedMetamodel.getEPackages()).getResources().get(0)
		// .getContents().get(0);
		//
		// EObject expectedModel = ResourceUtils
		// .loadResourceSet(expectedTargetModelURI,
		// expectedMetamodel.getEPackages()).getResources().get(0)
		// .getContents().get(0);
		//
		// ModelAssert
		// .assertDifference(expectedModel, actualModel, expectedNumber);

	}

	/**
	 * Copies a the resources
	 * 
	 * 
	 * @param modelURI
	 * @param t
	 * @param metamodel
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	private URI copyToCDORepo(URI modelURI, CDOTransaction t,
			Metamodel metamodel, ResourceSet model) {
		// Register the packages with this CDO Session.
		for (EPackage ePack : metamodel.getEPackages()) {
			EPackage ePackage = testSession.getPackageRegistry().getEPackage(
					ePack.getNsURI());
			if (ePackage == null) {
				testSession.getPackageRegistry().putEPackage(ePack);
			}
		}

		// Do we have a resource with this name.
		CDOResource cdoResource = null;

		for (Resource resource : model.getResources()) {

			if (resource.getURI() == null
					|| resource.getURI().isPlatformPlugin()) {
				continue;
			}
			String fileName = modelURI.lastSegment();
			String resourceName = fileName.substring(0,
					fileName.lastIndexOf("."));

			if (t.hasResource(resourceName)) {
				cdoResource = t.getResource(resourceName);
			} else {

				EObject loadElement = resource.getContents().get(0);

				// The resource without the extension.
				cdoResource = t.createResource(resourceName);

				// Copy will not work, as the EPackage resolved from the
				// referenced,
				// .ecore in the serialization, which is already upgraded.

				EObject copy = EcoreUtil.copy(loadElement);
				cdoResource.getContents().add(copy);
			}
		}

		if (CDOMigrator.commitTransaction(t)) {
			// Commit failed.
			// Nothing going on in CDO, remove the package and return.
			// clearEPackage(ePack);
			throw new IllegalStateException("Commit failed");
		}

		// We need a CDO resource to proceed.
		if (cdoResource == null) {
			throw new IllegalStateException("CDO Resource not created");
		}
		// Change the modelURI to deal with CDO.
		URI cdoModelURI = cdoResource.getURI();
		return cdoModelURI;
	}

	/**
	 * Obtain CDO {@link CDOURIData Connection Aware URI} from a collection of
	 * 'file' based URI's. The connection details are taken from the target
	 * repository settings.
	 * 
	 * @param modelURIs
	 * @return
	 */
	public List<URI> cdoTargetURIs(List<URI> modelURIs) {

		List<URI> arrayList = new ArrayList<URI>();
		for (URI uri : modelURIs) {
			URI cdoTargetConnectionAwareURI = cdoTargetConnectionAwareURI(uri);
			arrayList.add(cdoTargetConnectionAwareURI);
		}

		return arrayList;

	}

	public List<URI> cdoSourceURIs(List<URI> modelURIs) {

		List<URI> arrayList = new ArrayList<URI>();
		for (URI uri : modelURIs) {
			URI cdoTargetConnectionAwareURI = cdoSourceConnectionAwareURI(uri);
			arrayList.add(cdoTargetConnectionAwareURI);
		}

		return arrayList;

	}

	private URI cdoSourceConnectionAwareURI(URI sourceURI) {
		return CDOMigrator.cdoConnectionAwareURI(sourceURI, CDOTestUtil.HOST,
				CDOTestUtil.SOURCE_PORT, CDOTestUtil.REPO_SOURCE);
	}

	private URI cdoTargetConnectionAwareURI(URI sourceURI) {
		return CDOMigrator.cdoConnectionAwareURI(sourceURI, CDOTestUtil.HOST,
				CDOTestUtil.TARGET_PORT, CDOTestUtil.REPO_TARGET);
	}

	/**
	 * Rename a model.
	 * 
	 * @param metamodel
	 *            Migrator (required to be able to open the model)
	 * @param modelURI
	 *            URI of the model to be renamed
	 * @return URI of the renamed model
	 */
	@SuppressWarnings("unused")
	private URI rename(Metamodel metamodel, URI modelURI, Release release)
			throws IOException {

		List<URI> modelURIs = Collections.singletonList(modelURI);
		List<URI> backupURIs = rename(modelURIs, metamodel);
		return backupURIs.get(0);
	}

	/**
	 * Rename a model.
	 */
	protected List<URI> rename(List<URI> modelURIs, Metamodel metamodel)
			throws IOException {
		List<URI> backupURIs = BackupUtils.copy(modelURIs, metamodel,
				new BackupUtils.URIMapper() {

					public URI map(URI uri) {
						String name = uri.lastSegment().replace(".",
								"_migrated.");
						return uri.trimSegments(1).appendSegment(name);
					}

				});
		return backupURIs;
	}

	/**
	 * Test a model migration.
	 * 
	 * @param modelURI
	 *            URI of the model to be migrated
	 * @param expectedTargetModelURI
	 *            URI of the expected result of the migration
	 * @param expectedTargetMetamodelURI
	 *            URI of the target metamodel of the migration
	 */
	public void testMigration(URI modelURI, URI expectedTargetModelURI,
			URI expectedTargetMetamodelURI, int expectedDifferences)
			throws MigrationException, IOException {

		// CB TODO, Fix the Migrator Registry, define IMigrator to support
		// various implementations....

		// String nsURI = ReleaseUtils.getNamespaceURI(modelURI);
		//
		// Migrator migrator =
		// MigratorRegistry.getInstance().getMigrator(nsURI);
		// assertNotNull(migrator);
		// testMigration(migrator, modelURI, expectedTargetModelURI,
		// expectedTargetMetamodelURI, expectedDifferences);

	}
}
