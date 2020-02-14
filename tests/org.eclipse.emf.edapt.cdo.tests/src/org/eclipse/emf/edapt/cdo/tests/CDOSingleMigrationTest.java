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
package org.eclipse.emf.edapt.cdo.tests;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.internal.common.FileUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.execution.internal.ClassLoaderFacade;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test of model migrations defined by test models.
 *
 * @author Christophe Bouhier
 * @version 0.4.0
 */
@SuppressWarnings("restriction")
public class CDOSingleMigrationTest extends TestSuite {

	// Add test models from the Edapt test plugin.

	/** Assembling the test suite. */
	public static Test suite() {
		final TestSuite suite = new TestSuite("Migration Test");
		addMigrationTestSuites(suite, new File("data/component"));

		return suite;
	}

	/** Search for test models and add them to the test suite. */
	private static void addMigrationTestSuites(TestSuite suite, File dir) {
		for (final File file : dir.listFiles()) {
			if (file.isDirectory()) {
				addMigrationTestSuites(suite, file);
			} else {
				final String extension = FileUtils.getExtension(file);
				if (extension != null && "test".equals(extension)) {
					final URI uri = URIUtils.getURI(file);
					try {
						suite.addTest(new CDOMigrationTestSuite(uri,
							new ClassLoaderFacade(CDOSingleMigrationTest.class
								.getClassLoader())));

					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
