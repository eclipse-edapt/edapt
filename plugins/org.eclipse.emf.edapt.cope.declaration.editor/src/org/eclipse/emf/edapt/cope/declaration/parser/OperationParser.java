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
package org.eclipse.emf.edapt.cope.declaration.parser;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.treewalker.PreOrderTraversal;
import org.eclipse.emf.edapt.cope.declaration.Library;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * Parser to extract operation declarations from a groovy file
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationParser {

	/**
	 * Parse a groovy file
	 * 
	 * @param in
	 * @return Operation declarations
	 * @throws RecognitionException
	 * @throws TokenStreamException
	 */
	public Library parse(InputStream in) throws RecognitionException, TokenStreamException {
		
		SourceBuffer sourceBuffer = new SourceBuffer();
		UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(new InputStreamReader(in), sourceBuffer);
        GroovyLexer lexer = new GroovyLexer(unicodeReader);
        unicodeReader.setLexer(lexer);
		
		GroovyRecognizer parser = GroovyRecognizer.make(lexer);
        parser.setSourceBuffer(sourceBuffer);
		
		parser.compilationUnit();
		
		GroovySourceAST ast = (GroovySourceAST) parser.getAST();
		DeclarationVisitor visitor = new DeclarationVisitor(sourceBuffer);
		PreOrderTraversal traversal = new PreOrderTraversal(visitor);
		traversal.process(ast);
		
		Library library = visitor.getLibrary();
		return library;
	}
}
