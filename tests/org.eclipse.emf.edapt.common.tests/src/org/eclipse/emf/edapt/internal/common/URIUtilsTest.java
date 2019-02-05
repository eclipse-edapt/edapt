/*******************************************************************************
 * Copyright (c) 2007-2019 BMW Car IT, TUM, EclipseSource Muenchen GmbH, Christian W. Damus, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 * Christian W. Damus - bug 544147
 ******************************************************************************/
package org.eclipse.emf.edapt.internal.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Enclosed.class)
public class URIUtilsTest {

	/**
	 * Core tests.
	 */
	public static class Core {

		@Test
		public void testGetJavaFileWithPlatformPluginURI() throws FileNotFoundException {
			Scanner scanner = null;
			try {
				final URI uri = URI.createPlatformPluginURI("org.eclipse.emf.edapt.common.tests/resources/resource", //$NON-NLS-1$
					false);
				assertTrue(new ExtensibleURIConverterImpl().exists(uri, null));
				final File javaFile = URIUtils.getJavaFile(uri);
				assertNotNull(javaFile);
				scanner = new Scanner(javaFile);
				scanner.useDelimiter("\\Z"); //$NON-NLS-1$
				final String content = scanner.next();
				assertEquals("foo", content); //$NON-NLS-1$
			} finally {
				if (scanner != null) {
					scanner.close();
				}
			}
		}

	}

	/**
	 * Test cases for the {@link URIUtils#incrementVersionSegment(String)} API.
	 */
	@RunWith(Parameterized.class)
	public static class IncrementVersionSegment {
		private final String input;
		private final String expected;

		/**
		 * Initializes me with the {@code input} segment and the {@code expected} result.
		 *
		 * @param input the segment to increment
		 * @param expected the expected result
		 */
		public IncrementVersionSegment(String input, String expected) {
			super();

			this.input = input;
			this.expected = expected;
		}

		@Test
		public void increment() {
			assertThat(URIUtils.incrementVersionSegment(input), is(expected));
		}

		@Parameters(name = "{0}")
		@SuppressWarnings("nls")
		public static Iterable<Object[]> parameters() {
			return Arrays.asList(new Object[][] {
				{ "1", "2" },
				{ "v1", "v2" },
				{ "1.0.0", "2.0.0" },
				{ "v1.0.0", "v2.0.0" },
				{ "beta_1", "beta_2" },
				{ "beta-1.0.0", "beta-2.0.0" },
				{ "dev1", "dev1" }, // Not like "v1"
				{ "dev1_1.0.0", "dev1_2.0.0" },
				{ "jar.1.0.0", "jar.2.0.0" },
				{ "v123456789012345678901234567890", "v123456789012345678901234567891" },
				{ "SNAPSHOT", "SNAPSHOT" },
			});
		}

	}

}
