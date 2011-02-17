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
package org.eclipse.emf.edapt.cope.declaration;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A constraint which determines whether an operation can be executed with a setting of parameters
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Constraint#getOperation <em>Operation</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Constraint#getBooleanExpression <em>Boolean Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getConstraint()
 * @model
 * @generated
 */
public interface Constraint extends LabeledElement {
	/**
	 * Returns the value of the '<em><b>Operation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.cope.declaration.Operation#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Operation to which the parameter belongs
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Operation</em>' container reference.
	 * @see #setOperation(Operation)
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getConstraint_Operation()
	 * @see org.eclipse.emf.edapt.cope.declaration.Operation#getConstraints
	 * @model opposite="constraints" required="true" transient="false"
	 * @generated
	 */
	Operation getOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Constraint#getOperation <em>Operation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' container reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(Operation value);

	/**
	 * Returns the value of the '<em><b>Boolean Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Boolean Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The boolean expression to evaluate whether the constraint is fulfilled
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Boolean Expression</em>' attribute.
	 * @see #setBooleanExpression(String)
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getConstraint_BooleanExpression()
	 * @model required="true"
	 * @generated
	 */
	String getBooleanExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Constraint#getBooleanExpression <em>Boolean Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Boolean Expression</em>' attribute.
	 * @see #getBooleanExpression()
	 * @generated
	 */
	void setBooleanExpression(String value);

} // Constraint
