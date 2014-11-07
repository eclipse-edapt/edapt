package org.eclipse.emf.edapt.tests.migration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edapt.common.IResourceSetFactory;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.spi.history.Release;

/**
 * Test for ensuring that the configuration of the custom serialization has
 * effect on the migration.
 * 
 * @author herrmi
 * @author $Author: hummelb $
 * @version $Rev: 18709 $
 * @levd.rating RED Rev:
 */
public class CustomSerializationTest extends TestCase {

	/** Test that the custom serialization works as configured. */
	public void testCustomSerialization() throws MigrationException {

		Migrator migrator = new Migrator(
				URIUtils.getURI("data/node/node2.history"), null);

		List<URI> modelURIs = Arrays.asList(URIUtils
				.getURI("data/node/Graph1.xmi"));
		Release sourceRelease = migrator.getRelease(0);
		Release targetRelease = migrator.getRelease(1);
		PrintStreamProgressMonitor monitor = new PrintStreamProgressMonitor(
				System.out);

		EPackage metamodel = migrator.getMetamodel(targetRelease)
				.getEPackages().get(0);

		Registry.INSTANCE.put(metamodel.getNsURI(), metamodel);

		// without custom serialization
		ResourceSet resourceSet = migrator.migrateAndLoad(modelURIs,
				sourceRelease, targetRelease, monitor);

		Assert.assertEquals(1, resourceSet.getResources().size());

		// with custom serialization
		migrator.setResourceSetFactory(new IResourceSetFactory() {

			public ResourceSet createResourceSet() {
				ResourceSet resourceSet = new ResourceSetImpl();
				Map<URI, URI> uriMap = resourceSet.getURIConverter()
						.getURIMap();
				uriMap.put(
						URI.createURI("urn:app:com.emc.xcp.application:Graph2"),
						URIUtils.getURI("data/node/Graph2.xmi"));
				return resourceSet;
			}
		});

		resourceSet = migrator.migrateAndLoad(modelURIs, sourceRelease,
				targetRelease, monitor);

		Assert.assertEquals(2, resourceSet.getResources().size());
	}
}
