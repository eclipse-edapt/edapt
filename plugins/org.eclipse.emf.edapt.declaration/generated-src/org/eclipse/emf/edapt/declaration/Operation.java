/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.declaration;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation</b></em>'.
 * @noimplement This interface is not intended to be implemented by clients.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Coupled evolution operation
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#getLibrary <em>Library</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#getParameters <em>Parameters</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#getConstraints <em>Constraints</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#isDeprecated <em>Deprecated</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#getBefore <em>Before</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#getAfter <em>After</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#getImplementation <em>Implementation</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#getLabel <em>Label</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Operation#isBreaking <em>Breaking</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation()
 * @model
 * @generated
 */
public interface Operation extends IdentifiedElement {
	/**
	 * Returns the value of the '<em><b>Library</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Library#getOperations
	 * <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Library</em>' container reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Library to which the operation belongs
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Library</em>' container reference.
	 * @see #setLibrary(Library)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Library()
	 * @see org.eclipse.emf.edapt.declaration.Library#getOperations
	 * @model opposite="operations" required="true" transient="false"
	 * @generated
	 */
	Library getLibrary();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#getLibrary <em>Library</em>}' container
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Library</em>' container reference.
	 * @see #getLibrary()
	 * @generated
	 */
	void setLibrary(Library value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.declaration.Parameter}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Parameter#getOperation
	 * <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of parameters of the coupled evolution operations
	 * <!-- end-model-doc -->
	 *
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
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Constraint#getOperation
	 * <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of constraints restricting the executability of the coupled evolution operation
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Constraints()
	 * @see org.eclipse.emf.edapt.declaration.Constraint#getOperation
	 * @model opposite="operation" containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Deprecated</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deprecated</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this operation should no longer be used
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Deprecated</em>' attribute.
	 * @see #setDeprecated(boolean)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Deprecated()
	 * @model default="false"
	 * @generated
	 */
	boolean isDeprecated();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#isDeprecated <em>Deprecated</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Deprecated</em>' attribute.
	 * @see #isDeprecated()
	 * @generated
	 */
	void setDeprecated(boolean value);

	/**
	 * Returns the value of the '<em><b>Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Before</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
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
	 *
	 * @param value the new value of the '<em>Before</em>' attribute.
	 * @see #getBefore()
	 * @generated
	 */
	void setBefore(String value);

	/**
	 * Returns the value of the '<em><b>After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>After</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
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
	 *
	 * @param value the new value of the '<em>After</em>' attribute.
	 * @see #getAfter()
	 * @generated
	 */
	void setAfter(String value);

	/**
	 * Returns the value of the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementation</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Implementation</em>' attribute.
	 * @see #setImplementation(Class)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Implementation()
	 * @model required="true" transient="true"
	 * @generated
	 */
	Class getImplementation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#getImplementation
	 * <em>Implementation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Implementation</em>' attribute.
	 * @see #getImplementation()
	 * @generated
	 */
	void setImplementation(Class value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Label for an element
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Label()
	 * @model required="true"
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 *
	 * @since 1.3
	 *        <!-- end-user-doc -->
	 *        <!-- begin-model-doc -->
	 *        Whether this operation may cause existing models to need migration.
	 *        <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Breaking</em>' attribute.
	 * @see #setBreaking(boolean)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getOperation_Breaking()
	 * @model required="true"
	 * @generated
	 */
	boolean isBreaking();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Operation#isBreaking <em>Breaking</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * 
	 * @since 1.3
	 *        <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Breaking</em>' attribute.
	 * @see #isBreaking()
	 * @generated
	 */
	void setBreaking(boolean value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the parameter of the operation with a certain name
	 * <!-- end-model-doc -->
	 *
	 * @model
	 * @generated
	 */
	Parameter getParameter(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the main parameter of the operation, i.e. the parameter with main set to true
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation"
	 * @generated
	 */
	Parameter getMainParameter();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @model
	 * @generated
	 */
	boolean refines();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @model
	 * @generated
	 */
	Constraint getConstraint(String name);

} // Operation
