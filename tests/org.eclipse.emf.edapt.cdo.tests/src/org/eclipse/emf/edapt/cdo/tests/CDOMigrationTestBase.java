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
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.CDOResourceFactory;
import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOResourceLeaf;
import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOURIData;
import org.eclipse.emf.cdo.util.CDOURIUtil;
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
import org.eclipse.emf.edapt.cdo.migration.execution.CDOMigrator;
import org.eclipse.emf.edapt.common.IResourceSetFactory;
import org.eclipse.emf.edapt.common.ResourceSetFactoryImpl;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.migration.BackupUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.Persistency;
import org.eclipse.emf.edapt.migration.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.migration.execution.Migrator;

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

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Make sure we register various protocols with the CDO Factory.
		registerCDOResourceFactory();

		// Initialize the PluginViewProvider (Which needs a Session factory), it
		// can deal with
		// both cdo and cdo.net4j.tcp
		CDOViewProviderRegistry vpRegistry = CDOViewProviderRegistry.INSTANCE;
		vpRegistry.addViewProvider(new EdaptCDOViewProvider(
				"cdo(\\.net4j\\.tcp)?://.*", CDOViewProvider.DEFAULT_PRIORITY));

		// clearCDORepositories();

	}

	private void clearCDORepositories() {
		ResourceSetImpl set = new ResourceSetImpl();

		URI uri = cdoConnectionAwareURI("", CDOTestUtil.SOURCE_PORT,
				CDOTestUtil.REPO_SOURCE);
		Resource resource = set.getResource(uri, true);
		clearCDO((CDOResourceNode) resource);

	}

	private void clearCDO(CDOResourceNode resource) {
		// Walk the resource hierarchy and clean all.
		if (resource instanceof CDOResourceFolder) {
			CDOResourceFolder folder = (CDOResourceFolder) resource;
			for (CDOResourceNode node : folder.getNodes()) {
				clearCDO(node);
			}
		} else if (resource instanceof CDOResourceLeaf) {
			// clear the leaf object.
			EObject eContainer = resource.eContainer();

		}
	}

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

		ResourceSetFactoryImpl resourceSetFactoryImpl = new ResourceSetFactoryImpl();

		clear(cdoSourceURIs, resourceSetFactoryImpl);
		clear(cdoTargetURIs, resourceSetFactoryImpl);

		// Copy the source set to the CDO source repo, let our CDOView provider
		// do all the CDO stuff
		// like creating a container, connector, session and transaction. See
		// EDaptCDOViewProvider
		copy(metamodel, sourceSet, cdoSourceURIs, new ResourceSetFactoryImpl());

		// CB FIX the CDOMigrationTestCase so it can work with connection aware
		// CDO URI's! For now we produce the target URI ourself.
		migrator.migrateAndSave(cdoSourceURIs, release, null,
				new PrintStreamProgressMonitor(System.out), cdoTargetURIs);

		// Test Comparision is metamodel based.

		Metamodel expectedMetamodel = Persistency
				.loadMetamodel(expectedTargetMetamodelURI);

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

		// Remove the CDO Resource.
		// t.getResourceSet().getResources().remove(cdoResource);

		// commitTransaction(t);
		// clearEPackage(ePack);
		// }
		// closeSession();
	}

	private void clear(List<URI> cdoSourceURIs,
			ResourceSetFactoryImpl resourceSetFactoryImpl) {
		ResourceSet set = resourceSetFactoryImpl.createResourceSet();

		CDOTransaction transaction;
		for (URI uri : cdoSourceURIs) {

			try {

				Resource resource = set.getResource(uri, true);

				if (resource instanceof CDOResource) {
					CDOResource cdoRes = (CDOResource) resource;
					transaction = (CDOTransaction) cdoRes.cdoView();
					try {
						cdoRes.delete(null);
						transaction.commit();
					} catch (IOException e) {
					} catch (ConcurrentAccessException e) {
					} catch (CommitException e) {
					}
				}
			} catch (RuntimeException re) {
				// Guard for Exception, as the resource might not exist,
				// which will be an invalid URI when demand loading duh....
			}

		}
	}

	private void registerCDOResourceFactory() {
		// Initialize our factoy.
		Map<String, Object> map = Resource.Factory.Registry.INSTANCE
				.getProtocolToFactoryMap();

		// cdo.net4j.tcp
		if (!map.containsKey(CDONet4jUtil.PROTOCOL_TCP)) {
			map.put(CDONet4jUtil.PROTOCOL_TCP, CDOResourceFactory.INSTANCE);
		}
		// cdo
		if (!map.containsKey(CDOURIUtil.PROTOCOL_NAME)) {
			map.put(CDOURIUtil.PROTOCOL_NAME, CDOResourceFactory.INSTANCE);
		}
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

		if (commitTransaction(t)) {
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
	 * Construct CDO {@link URI} from a given {@link ResourceSet source models},
	 * create {@link ResourceSet target models } and copy the source content to
	 * the target content. Note: this will only work with a configured
	 * {@link CDOViewProvider}.
	 * 
	 * @param metamodel
	 * @param sourceModels
	 * @param resourceSetFactory
	 * @return
	 */
	private ResourceSet copyToSource(Metamodel metamodel,
			ResourceSet sourceModels, IResourceSetFactory resourceSetFactory) {

		// Our CDO target Resourceset.
		ResourceSet set = resourceSetFactory.createResourceSet();

		for (Resource resource : sourceModels.getResources()) {

			// Construct a URI for each resource in the Model ResourceSet, let
			// the CDO View Provider take care of the session, transaction,
			// etc....
			if (resource.getURI() == null
					|| resource.getURI().isPlatformPlugin()) {
				continue;
			}

			URI cdoResourceURI = cdoSourceConnectionAwareURI(resource.getURI());
			CDOResource cdoLoadResource = cdoCreateResource(set,
					cdoResourceURI, metamodel);
			EObject loadElement = resource.getContents().get(0);

			EObject copy = EcoreUtil.copy(loadElement);
			cdoLoadResource.getContents().add(copy);

			try {
				cdoLoadResource.save(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return set;
	}

	/**
	 * Construct CDO {@link URI} from a given {@link ResourceSet source models},
	 * create {@link ResourceSet target models } and copy the source content to
	 * the target content. Note: this will only work with a configured
	 * {@link CDOViewProvider}.
	 * 
	 * @param metamodel
	 * @param sourceModels
	 * @param resourceSetFactory
	 * @return
	 */
	public void copy(Metamodel metamodel, ResourceSet sourceModels,
			List<URI> cdoURIs, IResourceSetFactory resourceSetFactory) {

		// Our CDO target Resourceset.
		ResourceSet set = resourceSetFactory.createResourceSet();

		for (Resource resource : sourceModels.getResources()) {

			int index = sourceModels.getResources().indexOf(resource);
			URI cdoResourceURI = cdoURIs.get(index);

			CDOResource cdoLoadResource = cdoCreateResource(set,
					cdoResourceURI, metamodel);

			// Copy over the stuff to CDO.
			EObject loadElement = resource.getContents().get(0);

			EObject copy = EcoreUtil.copy(loadElement);
			cdoLoadResource.getContents().add(copy);

			try {
				cdoLoadResource.save(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// TODO, how to get rid of the transaction?

	}

	/**
	 * Obtain CDO {@link CDOURIData Connection Aware URI} from a collection of
	 * 'file' based URI's. The connection details are taken from the target
	 * repository settings.
	 * 
	 * FIXME Implement an abstract ConnectURIProvider which can be specialized
	 * for source and target.
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
		return cdoConnectionAwareURI(sourceURI, CDOTestUtil.SOURCE_PORT,
				CDOTestUtil.REPO_SOURCE);
	}

	private URI cdoTargetConnectionAwareURI(URI sourceURI) {
		return cdoConnectionAwareURI(sourceURI, CDOTestUtil.TARGET_PORT,
				CDOTestUtil.REPO_TARGET);
	}

	/**
	 * Converts a 'regular' resource URI to a CDO Resource URI.
	 * 
	 * @param sourceURI
	 * @return
	 */
	private URI cdoConnectionAwareURI(URI sourceURI, String port, String repo) {

		try {
			URI createFileURI = URI.createFileURI(sourceURI.toString());
		} catch (Exception e) {
			// bail when we are not a file URI.
			e.printStackTrace();
		}

		String fileName = sourceURI.lastSegment();

		// Strip the extension of the file name.
		String resourceName = fileName.substring(0, fileName.lastIndexOf("."));
		return cdoConnectionAwareURI(resourceName, port, repo);
	}

	private URI cdoConnectionAwareURI(String resourceName, String port,
			String repo) {

		CDOURIData cdouriData = new CDOURIData();
		cdouriData.setScheme("cdo.net4j.tcp");
		cdouriData.setAuthority(CDOTestUtil.HOST + ":" + port);
		cdouriData.setRepositoryName(repo);
		cdouriData.setResourcePath(new Path(resourceName));

		return cdouriData.toURI();
	}

	/**
	 * Converts a 'regular' resource URI to a CDO Resource URI.
	 * 
	 * @param sourceURI
	 * @return
	 */
	@SuppressWarnings("unused")
	private URI cdoCanonicalURI(URI sourceURI) {

		// FIXME, check the URI is file based....
		String fileName = sourceURI.lastSegment();

		// Strip the extension of the file name.
		String resourceName = fileName.substring(0, fileName.lastIndexOf("."));

		CDOURIData cdouriData = new CDOURIData();

		cdouriData.setScheme("cdo");
		cdouriData.setRepositoryName(CDOTestUtil.REPO_SOURCE);
		cdouriData.setResourcePath(new Path(resourceName));

		return cdouriData.toURI();
	}

	/**
	 * Create a {@link CDOResource} from a {@link URI} the {@link Metamodel
	 * MMMeta} will be used to register the corresponding {@link EPackage} by
	 * the {@link CDOViewProvider}.
	 * 
	 * @param set
	 * @param cdoResourceURI
	 * @param mmmeta
	 * @return
	 */
	public CDOResource cdoCreateResource(ResourceSet set, URI cdoResourceURI,
			Metamodel mmmeta) {

		ResourceUtils.register(mmmeta.getEPackages(), set.getPackageRegistry());

		Resource resource = set.createResource(cdoResourceURI);

		if (resource instanceof CDOResource) {
			return (CDOResource) resource;
		}
		return null;
	}

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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
