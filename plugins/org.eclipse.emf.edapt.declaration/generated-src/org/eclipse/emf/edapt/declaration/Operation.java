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
package org.eclipse.emf.edapt.declaration;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Coupled evolution operation
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#getLibrary <em>Library</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#getVariables <em>Variables</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#isDeprecated <em>Deprecated</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#isDeleting <em>Deleting</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#getBefore <em>Before</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.Operation#getAfter <em>After</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation()
 * @model
 * @generated
 */
public interface Operation extends IdentifiedElement, DescribedElement, LabeledElement {
	/**
	 * Returns the value of the '<em><b>Library</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Library#getOperations <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Library</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Library to which the operation belongs
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Library</em>' container reference.
	 * @see #setLibrary(Library)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Library()
	 * @see org.eclipse.emf.edapt.declaration.Library#getOperations
	 * @model opposite="operations" required="true" transient="false"
	 * @generated
	 */
	Library getLibrary();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#getLibrary <em>Library</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Library</em>' container reference.
	 * @see #getLibrary()
	 * @generated
	 */
	void setLibrary(Library value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.declaration.Parameter}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Parameter#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of parameters of the coupled evolution operations
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Parameters()
	 * @see org.eclipse.emf.edapt.declaration.Parameter#getOperation
	 * @model opposite="operation" containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.declaration.Constraint}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Constraint#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of constraints restricting the executability of the coupled evolution operation
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Constraints()
	 * @see org.eclipse.emf.edapt.declaration.Constraint#getOperation
	 * @model opposite="operation" containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.declaration.Variable}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Variable#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of helper variables for use in initial expressions of parameters and constraint expressions. Helper variables are initialized in the order in which they are specified so that a variable can access all previously declared variables.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Variables</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Variables()
	 * @see org.eclipse.emf.edapt.declaration.Variable#getOperation
	 * @model opposite="operation" containment="true"
	 * @generated
	 */
	EList<Variable> getVariables();

	/**
	 * Returns the value of the '<em><b>Deprecated</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deprecated</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this operation should no longer be used
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Deprecated</em>' attribute.
	 * @see #setDeprecated(boolean)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Deprecated()
	 * @model default="false"
	 * @generated
	 */
	boolean isDeprecated();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#isDeprecated <em>Deprecated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deprecated</em>' attribute.
	 * @see #isDeprecated()
	 * @generated
	 */
	void setDeprecated(boolean value);

	/**
	 * Returns the value of the '<em><b>Deleting</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deleting</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the operation deletes elements of values of elements
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Deleting</em>' attribute.
	 * @see #setDeleting(boolean)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Deleting()
	 * @model default="false"
	 * @generated
	 */
	boolean isDeleting();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#isDeleting <em>Deleting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deleting</em>' attribute.
	 * @see #isDeleting()
	 * @generated
	 */
	void setDeleting(boolean value);

	/**
	 * Returns the value of the '<em><b>Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Before</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Before</em>' attribute.
	 * @see #setBefore(String)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Before()
	 * @model
	 * @generated
	 */
	String getBefore();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#getBefore <em>Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Before</em>' attribute.
	 * @see #getBefore()
	 * @generated
	 */
	void setBefore(String value);

	/**
	 * Returns the value of the '<em><b>After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>After</em>' attribute.
	 * @see #setAfter(String)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_After()
	 * @model
	 * @generated
	 */
	String getAfter();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#getAfter <em>After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>After</em>' attribute.
	 * @see #getAfter()
	 * @generated
	 */
	void setAfter(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the parameter of the operation with a certain name
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	Parameter getParameter(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the variable of the operation with a certain name
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	Variable getVariable(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the main parameter of the operation, i.e. the parameter with main set to true
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Parameter getMainParameter();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean refines();

} // Operation
