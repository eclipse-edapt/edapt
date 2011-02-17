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

import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.ANNOTATION;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.ANNOTATION_MEMBER_VALUE_PAIR;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.ASSIGN;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.CLOSABLE_BLOCK;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.EXPR;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.IDENT;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.LITERAL_assert;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.MODIFIERS;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.PARAMETERS;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.STRING_LITERAL;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.TYPE;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.TYPE_ARGUMENT;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.TYPE_ARGUMENTS;
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.VARIABLE_DEF;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.LineColumn;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.edapt.cope.common.LoggingUtils;
import org.eclipse.emf.edapt.cope.declaration.Constraint;
import org.eclipse.emf.edapt.cope.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.cope.declaration.DeclarationPlugin;
import org.eclipse.emf.edapt.cope.declaration.Library;
import org.eclipse.emf.edapt.cope.declaration.Operation;
import org.eclipse.emf.edapt.cope.declaration.Parameter;
import org.eclipse.emf.edapt.cope.declaration.Placeholder;
import org.eclipse.emf.edapt.cope.declaration.TypedElement;
import org.eclipse.emf.edapt.cope.declaration.Variable;

import antlr.collections.AST;

/**
 * A visitor to extract the signature of an operation from a groovy file
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class DeclarationVisitor extends VisitorAdapter {

	/**
	 * Tag for operation label
	 */
	private static final String LABEL = "label";

	/**
	 * Tag for operation and parameter description
	 */
	private static final String DESCRIPTION = "description";

	/**
	 * Tag for deprecated operations
	 */
	private static final String DEPRECATED = "deprecated";

	/**
	 * Tag for deleting operations
	 */
	private static final String DELETING = "deleting";

	/**
	 * Tag for denoting that an operation has to be performed after another
	 * operation
	 */
	private static final String AFTER = "after";

	/**
	 * Tag for denoting that an operation has to be performed before another
	 * operation
	 */
	private static final String BEFORE = "before";

	/**
	 * Container for operations
	 */
	private final Library library;

	/**
	 * Source buffer of the parser
	 */
	private final SourceBuffer sourceBuffer;

	/**
	 * Constructor
	 * 
	 * @param sourceBuffer
	 */
	public DeclarationVisitor(SourceBuffer sourceBuffer) {
		library = DeclarationFactory.eINSTANCE.createLibrary();
		this.sourceBuffer = sourceBuffer;
	}

	/**
	 * Gets the container for operations
	 * 
	 * @return Operations container
	 */
	public Library getLibrary() {
		return library;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitVariableDef(GroovySourceAST ast, int visit) {
		if (visit == OPENING_VISIT) {
			GroovySourceAST id = ast.childOfType(IDENT);
			GroovySourceAST assign = ast.childOfType(ASSIGN);

			if (id != null && assign != null) {
				GroovySourceAST block = assign.childOfType(CLOSABLE_BLOCK);
				if (block != null) {
					Operation operation = addOperation(block, id.getText());
					GroovySourceAST modifierAST = ast.childOfType(MODIFIERS);

					// description and label
					for (GroovySourceAST child : getChildren(modifierAST)) {
						if (child.getType() == ANNOTATION) {
							GroovySourceAST identAST = child.childOfType(IDENT);
							GroovySourceAST annotationAST = child
									.childOfType(ANNOTATION_MEMBER_VALUE_PAIR);
							if (DESCRIPTION.equals(identAST.getText())) {
								GroovySourceAST stringAST = annotationAST
										.childAt(1);
								String description = getStringText(stringAST);
								operation.setDescription(description);
							} else if (LABEL.equals(identAST.getText())) {
								GroovySourceAST stringAST = annotationAST
										.childAt(1);
								String label = getStringText(stringAST);
								operation.setLabel(label);
							} else if (DEPRECATED.equals(identAST.getText())) {
								operation.setDeprecated(true);
							} else if (DELETING.equals(identAST.getText())) {
								operation.setDeleting(true);
							} else if (AFTER.equals(identAST.getText())) {
								GroovySourceAST stringAST = annotationAST
										.childAt(1);
								String label = getStringText(stringAST);
								operation.setAfter(label);
							} else if (BEFORE.equals(identAST.getText())) {
								GroovySourceAST stringAST = annotationAST
										.childAt(1);
								String label = getStringText(stringAST);
								operation.setBefore(label);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Get the value of a string expression
	 */
	public String getStringText(GroovySourceAST stringAST) {
		switch (stringAST.getType()) {
		case STRING_LITERAL:
			return stringAST.getText();
		case GroovyTokenTypes.PLUS:
			int n = stringAST.getNumberOfChildren();
			String result = "";
			for (int i = 0; i < n; i++) {
				GroovySourceAST ast = stringAST.childAt(i);
				result += getStringText(ast);
			}
			return result;
		default:
			return "";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitAssign(GroovySourceAST ast, int visit) {
		if (visit == OPENING_VISIT) {
			List<GroovySourceAST> children = getChildren(ast);
			if (children.size() == 2) {
				// redirect operation
				GroovySourceAST first = children.get(0);
				GroovySourceAST second = children.get(1);
				if (first.getType() == IDENT && second.getType() == IDENT) {
					Operation operation = library
							.getOperation(second.getText());
					if (operation == null) {
						String message = "operation not found: \""
								+ second.getText() + "\"";
						if (Platform.isRunning()) {
							LoggingUtils.logError(DeclarationPlugin.getPlugin(), message);
						} else {
							System.err.println(message);
						}
					} else {
						operation.setName(first.getText());
					}
				}
			}
		}
	}

	/**
	 * Add an operation to the container
	 * 
	 * @param block
	 *            Abstract syntax tree of the operation's implementation
	 * @param id
	 *            Operation identifier
	 * @return Operation
	 */
	private Operation addOperation(GroovySourceAST block, String id) {
		Operation operation = DeclarationFactory.eINSTANCE.createOperation();
		operation.setName(id);
		library.getOperations().add(operation);

		// parameters
		GroovySourceAST parametersAST = block.childOfType(PARAMETERS);
		for (GroovySourceAST child : getChildren(parametersAST)) {
			addParameter(operation, child);
			if (operation.getParameters().size() == 1) {
				operation.getParameters().get(0).setMain(true);
			}
		}

		// constraints and variables
		List<GroovySourceAST> children = getChildren(block);
		int lastAssert = findLastAssert(children);
		for (int i = 0; i < lastAssert + 1; i++) {
			GroovySourceAST child = children.get(i);
			if (child.getType() == LITERAL_assert) {
				addConstraint(operation, child);
			} else if (child.getType() == VARIABLE_DEF) {
				addVariable(operation, child);
			}
		}

		return operation;
	}

	/**
	 * Find the index of the last assertion within a sequence of abstract syntax
	 * tree
	 * 
	 * @param children
	 *            Sequence of abstract syntax trees
	 * @return Index
	 */
	private int findLastAssert(List<GroovySourceAST> children) {
		int n = -1;
		for (int i = 0; i < children.size(); i++) {
			GroovySourceAST child = children.get(i);
			if (child.getType() == LITERAL_assert) {
				n = i;
			}
		}
		return n;
	}

	/**
	 * Add a variable to an operation
	 * 
	 * @param operation
	 *            Operation
	 * @param ast
	 *            Abstract syntax tree corresponding to the variable definition
	 */
	private void addVariable(Operation operation, GroovySourceAST ast) {
		Variable variable = DeclarationFactory.eINSTANCE.createVariable();
		variable.setName(ast.childOfType(IDENT).getText());

		setType(variable, ast);
		setInitExpression(variable, ast);

		operation.getVariables().add(variable);
	}

	/**
	 * Add a constraint to an operation
	 * 
	 * @param operation
	 *            Operation
	 * @param ast
	 *            Abstract syntax tree corresponding to the constraint
	 *            definition
	 */
	private void addConstraint(Operation operation, GroovySourceAST ast) {
		Constraint constraint = DeclarationFactory.eINSTANCE.createConstraint();
		constraint.setLabel(ast.childOfType(STRING_LITERAL).getText());
		GroovySourceAST expr = ast.childOfType(EXPR);

		constraint.setBooleanExpression(getSnippet(expr));

		operation.getConstraints().add(constraint);
		checkChoiceExpression(constraint, expr);
	}

	/**
	 * Check whether a constraint provides a choice expression for a parameter
	 * 
	 * @param constraint
	 *            Constraint
	 * @param ast
	 *            Abstract syntax tree corresponding to the constraint
	 *            expression
	 */
	private void checkChoiceExpression(Constraint constraint,
			GroovySourceAST ast) {
		GroovySourceAST methodCall = ast
				.childOfType(GroovyTokenTypes.METHOD_CALL);
		if (methodCall == null)
			return;

		GroovySourceAST dot = methodCall.childOfType(GroovyTokenTypes.DOT);
		if (dot == null)
			return;

		GroovySourceAST ident = dot.childOfType(GroovyTokenTypes.IDENT);
		if (!("contains".equals(ident.getText()) || "containsAll".equals(ident
				.getText())))
			return;

		String parameterName = getSnippet(methodCall.childAt(1));
		Parameter parameter = constraint.getOperation().getParameter(
				parameterName);
		if (parameter == null || parameter.isMain())
			return;

		String choiceExpression = getSnippet(dot.childAt(0));
		parameter.setChoiceExpression(choiceExpression);
	}

	/**
	 * Add a parameter to an operation
	 * 
	 * @param operation
	 *            Operation
	 * @param ast
	 *            Abstract syntax tree corresponding to the parameter definition
	 */
	private void addParameter(Operation operation, GroovySourceAST ast) {
		Parameter parameter = DeclarationFactory.eINSTANCE.createParameter();
		parameter.setName(ast.childOfType(IDENT).getText());

		setType(parameter, ast);
		setInitExpression(parameter, ast);
		if (parameter.getInitExpression() != null
				&& parameter.getInitExpression().startsWith("null")) {
			parameter.setRequired(false);
		}

		// description
		GroovySourceAST modifierAST = ast.childOfType(MODIFIERS);
		for (GroovySourceAST child : getChildren(modifierAST)) {
			if (child.getType() == ANNOTATION) {
				GroovySourceAST identAST = child.childOfType(IDENT);
				if (identAST == null)
					continue;
				if (DESCRIPTION.equals(identAST.getText())) {
					String description = child.childOfType(
							ANNOTATION_MEMBER_VALUE_PAIR).childOfType(
							STRING_LITERAL).getText();
					parameter.setDescription(description);
				}
			}
		}

		operation.getParameters().add(parameter);
	}

	/**
	 * Set the initial expression of a placeholder (either parameter or
	 * variable)
	 * 
	 * @param placeholder
	 *            Placeholder
	 * @param ast
	 *            Abstract syntax tree corresponding to the placeholder
	 */
	private void setInitExpression(Placeholder placeholder, GroovySourceAST ast) {
		try {
			GroovySourceAST initAST = ast.childOfType(ASSIGN);
			if (initAST != null) {
				String snippet = getSnippet(ast);
				snippet = snippet.substring(snippet.indexOf('=') + 1);
				while (snippet.charAt(0) == ' ') {
					snippet = snippet.substring(1);
				}
				placeholder.setInitExpression(snippet);
			}
		} catch (RuntimeException e) {
			// ignore
		}
	}

	/**
	 * Set the type of a typed element
	 * 
	 * @param typedElement
	 *            Typed element
	 * @param ast
	 *            Abstract syntax tree corresponding to the typed element
	 */
	private void setType(TypedElement typedElement, GroovySourceAST ast) {
		try {
			GroovySourceAST typeAST = ast.childOfType(TYPE).childAt(0);
			String type = typeAST.getText();
			if ("List".equals(type) || "Collection".equals(type)) {
				typedElement.setMany(true);
				typeAST = typeAST.childOfType(TYPE_ARGUMENTS).childOfType(
						TYPE_ARGUMENT).childOfType(TYPE).childOfType(IDENT);
				type = typeAST.getText();
			}
			typedElement.setClassifierName(type);
			if (typedElement.getClassifier() == null) {
				type = "E" + type.substring(0, 1).toUpperCase()
						+ type.substring(1);
				typedElement.setClassifierName(type);
			}
		} catch (RuntimeException e) {
			// ignore
		}
	}

	/**
	 * Get the children of a node in the abstract syntax tree
	 * 
	 * @param ast
	 *            Parent node
	 * @return Children nodes
	 */
	private List<GroovySourceAST> getChildren(GroovySourceAST ast) {
		List<GroovySourceAST> children = new ArrayList<GroovySourceAST>();
		AST child = ast.getFirstChild();
		while (child != null) {
			children.add((GroovySourceAST) child);
			child = child.getNextSibling();
		}
		return children;
	}

	/**
	 * Get the code corresponding to an abstract syntax tree
	 * 
	 * @param ast
	 *            Abstract syntax tree
	 * @return Code
	 */
	private String getSnippet(GroovySourceAST ast) {
		int startLine = ast.getLine();
		int startColumn = ast.getColumn();
		int endLine = ast.getLineLast();
		int endColumn = ast.getColumnLast();
		LineColumn start = new LineColumn(startLine, startColumn);
		LineColumn end = new LineColumn(endLine, endColumn);
		String snippet = sourceBuffer.getSnippet(start, end);
		return snippet;
	}
}
