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
package org.eclipse.emf.edapt.tests.declaration.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edapt.common.ExtensionFileFilter;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.parser.OperationParser;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * Tests for the {@link OperationParser}.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationParserTest extends TestCase {

	/** Basic test. */
	public void testSimpleOperationParser() throws Exception {

		File file = new File("data/operation.groovy");
		Library library = parseLibrary(file);

		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(library);
		Assert.assertEquals(diagnostic.getSeverity(), Diagnostic.OK);

		Assert.assertEquals(1, library.getOperations().size());

		Operation operation = library.getOperation("extractPartClass");
		Assert.assertNotNull(operation);

		Assert.assertNotNull(operation.getLabel());
		Assert.assertEquals(4, operation.getParameters().size());
		Assert.assertEquals(1, operation.getVariables().size());
		Assert.assertEquals(3, operation.getConstraints().size());
	}

	/** Advanced test. */
	public void testOperationParser() throws Exception {

		File dir = new File("../org.eclipse.emf.edapt.cope.declaration/operations/");
		ExtensionFileFilter filter = new ExtensionFileFilter("declaration");
		for (File declarationFile : dir.listFiles(filter)) {
			URI declarationURI = URIUtils.getURI(declarationFile);
			Library expectedLibrary = ResourceUtils.loadElement(declarationURI);

			File groovyFile = FileUtils.replaceFileExtension(declarationFile,
					"groovy");
			Library library = parseLibrary(groovyFile);
			Diagnostic diagnostic = Diagnostician.INSTANCE.validate(library);
			Assert.assertEquals(Diagnostic.OK, diagnostic.getSeverity());

			for (Operation operation : expectedLibrary.getOperations()) {
				Assert.assertNotNull(library.getOperation(operation.getName()));
			}
		}
	}

	/** Parse the library from a file. */
	private Library parseLibrary(File file) throws FileNotFoundException,
			RecognitionException, TokenStreamException {

		OperationParser parser = new OperationParser();
		FileInputStream in = new FileInputStream(file);

		Library library = parser.parse(in);
		library.setImplementation(file.getName());
		return library;
	}
}
