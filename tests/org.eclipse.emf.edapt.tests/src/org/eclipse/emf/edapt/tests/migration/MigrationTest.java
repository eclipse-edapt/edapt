/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.tests.migration;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edapt.common.IResourceSetFactory;
import org.eclipse.emf.edapt.internal.common.FileUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.internal.migration.execution.internal.ClassLoaderFacade;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.test.MigrationTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test of model migrations defined by test models.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
@SuppressWarnings("restriction")
public class MigrationTest extends TestSuite {

	/** Assembling the test suite. */
	public static Test suite() {
		final TestSuite suite = new TestSuite("Migration Test"); //$NON-NLS-1$

		addMigrationTestSuites(suite, new File("data")); //$NON-NLS-1$

		return suite;
	}

	/** Search for test models and add them to the test suite. */
	private static void addMigrationTestSuites(TestSuite suite, File dir) {
		for (final File file : dir.listFiles()) {
			if (file.isDirectory()) {
				addMigrationTestSuites(suite, file);
			} else {
				final String extension = FileUtils.getExtension(file);
				if (extension != null && "test".equals(extension)) { //$NON-NLS-1$
					final URI uri = URIUtils.getURI(file);
					try {
						final MigrationTestSuite migrationTestSuite = new UUIDMigrationTestSuite(
							uri, new ClassLoaderFacade(MigrationTest.class.getClassLoader()));
						suite.addTest(migrationTestSuite);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static final class UUIDMigrationTestSuite extends MigrationTestSuite {
		private UUIDMigrationTestSuite(URI definitionURI, IClassLoader loader) throws IOException {
			super(definitionURI, loader);
		}

		@Override
		protected org.eclipse.emf.edapt.migration.execution.Migrator loadMigrator()
			throws IOException {
			final Migrator loadedMigrator = super.loadMigrator();
			loadedMigrator.setResourceSetFactory(new IResourceSetFactory() {
				@Override
				public ResourceSet createResourceSet() {
					final ResourceSetImpl resourceSet = new ResourceSetImpl();
					resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
						Resource.Factory.Registry.DEFAULT_EXTENSION, new UuidResourceFactory());
					return resourceSet;
				}
			});
			return loadedMigrator;
		}

		private static class UuidResourceFactory extends XMIResourceFactoryImpl {
			@Override
			public Resource createResource(URI uri) {
				return new XMIResourceImpl(uri) {
					@Override
					protected boolean useUUIDs() {
						return true;
					}
				};
			}
		}
	}

}
