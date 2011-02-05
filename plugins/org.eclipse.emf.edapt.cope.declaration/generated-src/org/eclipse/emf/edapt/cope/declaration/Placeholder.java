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
 * A representation of the model object '<em><b>Placeholder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Base class for parameters and variables
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Placeholder#getInitExpression <em>Init Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getPlaceholder()
 * @model abstract="true"
 * @generated
 */
public interface Placeholder extends TypedElement, IdentifiedElement {
	/**
	 * Returns the value of the '<em><b>Init Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Init Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The init expression evaluates to the initial value for this placeholder
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Init Expression</em>' attribute.
	 * @see #setInitExpression(String)
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getPlaceholder_InitExpression()
	 * @model
	 * @generated
	 */
	String getInitExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Placeholder#getInitExpression <em>Init Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init Expression</em>' attribute.
	 * @see #getInitExpression()
	 * @generated
	 */
	void setInitExpression(String value);

} // Placeholder
