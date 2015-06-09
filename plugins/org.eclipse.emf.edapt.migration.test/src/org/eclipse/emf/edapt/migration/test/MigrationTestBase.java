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
package org.eclipse.emf.edapt.migration.test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.history.reconstruction.ModelAssert;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.internal.migration.internal.BackupUtils;
import org.eclipse.emf.edapt.internal.migration.internal.Persistency;
import org.eclipse.emf.edapt.internal.migration.internal.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.ReleaseUtils;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.MigratorRegistry;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.migration.Metamodel;

/**
 * A class for test cases to validate a model migration.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 028BD5369E87644723FF537FB5E55E4D
 */
public abstract class MigrationTestBase extends TestCase {

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

		final Migrator migrator = new Migrator(migratorURI, loader);
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

		final Set<Release> releases = migrator.getRelease(modelURI);
		assertTrue(releases.size() >= 1);
		final Release release = HistoryUtils.getMinimumRelease(releases);
		final URI targetModelURI = rename(migrator, modelURI, release);

		migrator.migrateAndSave(Collections.singletonList(targetModelURI), release,
			null, new PrintStreamProgressMonitor(System.out));

		final Metamodel expectedMetamodel = Persistency
			.loadMetamodel(expectedTargetMetamodelURI);

		final EObject actualModel = ResourceUtils.loadResourceSet(targetModelURI,
			expectedMetamodel.getEPackages()).getResources().get(0)
			.getContents().get(0);
		final EObject expectedModel = ResourceUtils.loadResourceSet(
			expectedTargetModelURI, expectedMetamodel.getEPackages())
			.getResources().get(0).getContents().get(0);

		ModelAssert
			.assertDifference(expectedModel, actualModel, expectedNumber);
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
	private URI rename(Migrator migrator, URI modelURI, Release release)
		throws IOException {

		final Metamodel metamodel = migrator.getMetamodel(release);
		final List<URI> modelURIs = Collections.singletonList(modelURI);
		final List<URI> backupURIs = rename(modelURIs, metamodel);
		return backupURIs.get(0);
	}

	/**
	 * Rename a model.
	 */
	protected List<URI> rename(List<URI> modelURIs, Metamodel metamodel)
		throws IOException {
		final List<URI> backupURIs = BackupUtils.copy(modelURIs, metamodel,
			new BackupUtils.URIMapper() {

				@Override
				public URI map(URI uri) {
					final String name = uri.lastSegment().replace(".", //$NON-NLS-1$
						"_migrated."); //$NON-NLS-1$
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

		final String nsURI = ReleaseUtils.getNamespaceURI(modelURI);

		final Migrator migrator = MigratorRegistry.getInstance().getMigrator(nsURI);
		assertNotNull(migrator);
		testMigration(migrator, modelURI, expectedTargetModelURI,
			expectedTargetMetamodelURI, expectedDifferences);
	}
}
