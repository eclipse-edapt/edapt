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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.cdo.view.CDOViewProvider;
import org.eclipse.emf.cdo.view.CDOViewProviderRegistry;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.reconstruction.ModelAssert;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.migration.BackupUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Persistency;
import org.eclipse.emf.edapt.migration.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.migration.ReleaseUtils;
import org.eclipse.emf.edapt.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.MigratorRegistry;
import org.eclipse.emf.internal.cdo.view.CDOViewProviderRegistryImpl;
import org.eclipse.net4j.util.lifecycle.Lifecycle;

/**
 * A class for test cases to validate a model migration.
 * 
 * @author herrmama
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

		Migrator migrator = new Migrator(migratorURI, loader);
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
	public void testMigration(Migrator migrator, URI modelURI,
			URI expectedTargetModelURI, URI expectedTargetMetamodelURI)
			throws MigrationException, IOException {
		testMigration(migrator, modelURI, expectedTargetModelURI,
				expectedTargetMetamodelURI, 0);
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
	 * @param expectedNumber
	 *            Expected number of differences
	 */
	public void testMigration(Migrator migrator, URI modelURI,
			URI expectedTargetModelURI, URI expectedTargetMetamodelURI,
			int expectedNumber) throws MigrationException, IOException {

		prepareSession();

//		EPackage ePack = loadEPackageFromEcore(expectedTargetMetamodelURI);

//		if (ePack != null) {
		
			CDOTransaction t = testSession.openTransaction();

			Set<Release> releases = migrator.getRelease(modelURI);

			assertTrue(releases.size() >= 1);

			Release release = HistoryUtils.getMinimumRelease(releases);

			// Get a metamodel to construct to load the resource with the 
			// matching EPackage. 
			
			Metamodel metamodel = migrator.getMetamodel(release);
			
			List<URI> modelURIs = Collections.singletonList(modelURI);

			ResourceSet model = ResourceUtils.loadResourceSet(modelURIs,
					metamodel.getEPackages());
			
			// Register the packages with this CDO Session. 
			for(EPackage ePack : metamodel.getEPackages()){
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
			
			if (commitTransaction(t)) {
				// Nothing going on in CDO, remove the package and return.
//				clearEPackage(ePack);
				return;
			}
			
			// We need a CDO resource to proceed. 
			if(cdoResource == null){
				return;
			}
			// Change the modelURI to deal with CDO.
			URI cdoModelURI = cdoResource.getURI();
			System.out.println(cdoModelURI);

			URI targetModelURI = cdoModelURI;

			
			// Initialize the PluginViewProvider (Which needs a Session factory). 
			CDOViewProviderRegistry vpRegistry = CDOViewProviderRegistry.INSTANCE;
			vpRegistry.addViewProvider(new EdaptCDOViewProvider("cdo:.*",CDOViewProvider.DEFAULT_PRIORITY));
			
			
			// Do not rename/backup , as we use the CDO concept to commit the transaction. 
			
			// URI targetModelURI = rename(migrator, modelURI, release);

			migrator.migrateAndSave(Collections.singletonList(targetModelURI),
					release, null, new PrintStreamProgressMonitor(System.out));

			Metamodel expectedMetamodel = Persistency
					.loadMetamodel(expectedTargetMetamodelURI);

			EObject actualModel = ResourceUtils
					.loadResourceSet(targetModelURI,
							expectedMetamodel.getEPackages()).getResources()
					.get(0).getContents().get(0);
			EObject expectedModel = ResourceUtils
					.loadResourceSet(expectedTargetModelURI,
							expectedMetamodel.getEPackages()).getResources()
					.get(0).getContents().get(0);

			ModelAssert.assertDifference(expectedModel, actualModel,
					expectedNumber);

			// Remove the CDO Resource.
//			t.getResourceSet().getResources().remove(cdoResource);

			commitTransaction(t);
//			clearEPackage(ePack);
//		}
		closeSession();
	}

	private void clearEPackage(EPackage ePack) {
		if (testSession.getPackageRegistry().containsKey(ePack.getNsURI())) {
			testSession.getPackageRegistry().remove(ePack.getNsURI());
		}
	}

	private boolean commitTransaction(CDOTransaction t) {
		boolean commitFailed = false;
		try {
			t.commit();
		} catch (ConcurrentAccessException e) {
			e.printStackTrace();
			commitFailed = true;
		} catch (CommitException e) {
			e.printStackTrace();
			commitFailed = true;
		}
		return commitFailed;
	}

	private void prepareSession() {
		testSession = CDOTestUtil.self.openSession();
	}

	private void closeSession() {
		if (testSession != null && !testSession.isClosed()) {
			testSession.close();
		}
	}

	private EPackage loadEPackageFromEcore(URI expectedTargetMetamodelURI) {
		// register globally the Ecore Resource Factory to the ".ecore"
		// extension
		// weird that we need to do this, but well...
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"ecore", new EcoreResourceFactoryImpl());

		ResourceSet rs = new ResourceSetImpl();
		// enable extended metadata
		final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(
				rs.getPackageRegistry());
		rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA,
				extendedMetaData);

		Resource r = rs.getResource(expectedTargetMetamodelURI, true);
		EObject eObject = r.getContents().get(0);
		if (eObject instanceof EPackage) {
			EPackage p = (EPackage) eObject;
			rs.getPackageRegistry().put(p.getNsURI(), p);
			return p;
		}
		return null;
	}

	/**
	 * Rename a model.
	 * 
	 * @param migrator
	 *            Migrator (required to be able to open the model)
	 * @param modelURI
	 *            URI of the model to be renamed
	 * @return URI of the renamed model
	 */
	@SuppressWarnings("unused")
	private URI rename(Migrator migrator, URI modelURI, Release release)
			throws IOException {

		Metamodel metamodel = migrator.getMetamodel(release);
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

		String nsURI = ReleaseUtils.getNamespaceURI(modelURI);

		Migrator migrator = MigratorRegistry.getInstance().getMigrator(nsURI);
		assertNotNull(migrator);
		testMigration(migrator, modelURI, expectedTargetModelURI,
				expectedTargetMetamodelURI, expectedDifferences);
	}
}
