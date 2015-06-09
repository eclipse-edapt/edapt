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
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.internal.PrintStreamProgressMonitor;
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
@SuppressWarnings({ "deprecation", "restriction" })
public class CustomSerializationTest extends TestCase {

	/** Test that the custom serialization works as configured. */
	public void testCustomSerialization() throws MigrationException {

		final Migrator migrator = new Migrator(
			URIUtils.getURI("data/node/node2.history"), null); //$NON-NLS-1$

		final List<URI> modelURIs = Arrays.asList(URIUtils
			.getURI("data/node/Graph1.xmi")); //$NON-NLS-1$
		final Release sourceRelease = migrator.getRelease(0);
		final Release targetRelease = migrator.getRelease(1);
		final PrintStreamProgressMonitor monitor = new PrintStreamProgressMonitor(
			System.out);

		final EPackage metamodel = migrator.getMetamodel(targetRelease)
			.getEPackages().get(0);

		Registry.INSTANCE.put(metamodel.getNsURI(), metamodel);

		// without custom serialization
		ResourceSet resourceSet = migrator.migrateAndLoad(modelURIs,
			sourceRelease, targetRelease, monitor);

		Assert.assertEquals(1, resourceSet.getResources().size());

		// with custom serialization
		migrator.setResourceSetFactory(new IResourceSetFactory() {

			@Override
			public ResourceSet createResourceSet() {
				final ResourceSet resourceSet = new ResourceSetImpl();
				final Map<URI, URI> uriMap = resourceSet.getURIConverter()
					.getURIMap();
				uriMap.put(
					URI.createURI("urn:app:com.emc.xcp.application:Graph2"), //$NON-NLS-1$
					URIUtils.getURI("data/node/Graph2.xmi")); //$NON-NLS-1$
				return resourceSet;
			}
		});

		resourceSet = migrator.migrateAndLoad(modelURIs, sourceRelease,
			targetRelease, monitor);

		Assert.assertEquals(2, resourceSet.getResources().size());
	}
}
