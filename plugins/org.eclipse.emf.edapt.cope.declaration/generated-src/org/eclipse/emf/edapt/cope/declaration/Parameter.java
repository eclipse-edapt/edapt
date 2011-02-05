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
 * A representation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A parameter of an operation
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Parameter#getOperation <em>Operation</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Parameter#isRequired <em>Required</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Parameter#getChoiceExpression <em>Choice Expression</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Parameter#isMain <em>Main</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getParameter()
 * @model
 * @generated
 */
public interface Parameter extends Placeholder, DescribedElement {
	/**
	 * Returns the value of the '<em><b>Operation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.cope.declaration.Operation#getParameters <em>Parameters</em>}'.
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
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getParameter_Operation()
	 * @see org.eclipse.emf.edapt.cope.declaration.Operation#getParameters
	 * @model opposite="parameters" required="true" transient="false"
	 * @generated
	 */
	Operation getOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Parameter#getOperation <em>Operation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' container reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(Operation value);

	/**
	 * Returns the value of the '<em><b>Required</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the parameter is required to be set for allowing an operation to be executed
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Required</em>' attribute.
	 * @see #setRequired(boolean)
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getParameter_Required()
	 * @model default="true"
	 * @generated
	 */
	boolean isRequired();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Parameter#isRequired <em>Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Required</em>' attribute.
	 * @see #isRequired()
	 * @generated
	 */
	void setRequired(boolean value);

	/**
	 * Returns the value of the '<em><b>Choice Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Choice Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The choice expression determines the possible values for the parameter
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Choice Expression</em>' attribute.
	 * @see #setChoiceExpression(String)
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getParameter_ChoiceExpression()
	 * @model
	 * @generated
	 */
	String getChoiceExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Parameter#getChoiceExpression <em>Choice Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Choice Expression</em>' attribute.
	 * @see #getChoiceExpression()
	 * @generated
	 */
	void setChoiceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Main</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the parameter is determined by selection (at most one parameter for unambiguousness)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Main</em>' attribute.
	 * @see #setMain(boolean)
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getParameter_Main()
	 * @model
	 * @generated
	 */
	boolean isMain();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Parameter#isMain <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Main</em>' attribute.
	 * @see #isMain()
	 * @generated
	 */
	void setMain(boolean value);

} // Parameter
