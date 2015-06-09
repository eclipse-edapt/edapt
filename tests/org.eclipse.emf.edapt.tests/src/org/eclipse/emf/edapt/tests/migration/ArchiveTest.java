/*******************************************************************************
 * Copyright (c) 2007-2015 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.tests.migration;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.internal.common.FileUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.execution.internal.ClassLoaderFacade;
import org.eclipse.emf.edapt.tests.util.ArchiveMigrationTS;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * {@link TestSuite} performing all archive test cases.
 *
 * @author jfaltermeier
 *
 */
@SuppressWarnings("restriction")
public class ArchiveTest extends TestSuite {

	/** Assembling the test suite. */
	public static Test suite() {
		final TestSuite suite = new TestSuite("Archive Test"); //$NON-NLS-1$

		addMigrationTestSuites(suite, new File("archive")); //$NON-NLS-1$

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
						suite.addTest(new ArchiveMigrationTS(uri,
							new ClassLoaderFacade(ArchiveTest.class
								.getClassLoader())));
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}