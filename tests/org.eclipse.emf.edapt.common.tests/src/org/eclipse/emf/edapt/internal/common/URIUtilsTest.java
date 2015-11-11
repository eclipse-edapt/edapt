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
package org.eclipse.emf.edapt.internal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.junit.Test;

public class URIUtilsTest {

	@Test
	public void testGetJavaFileWithPlatformPluginURI() throws FileNotFoundException {
		Scanner scanner = null;
		try {
			final URI uri = URI.createPlatformPluginURI("org.eclipse.emf.edapt.common.tests/resources/resource", false); //$NON-NLS-1$
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
