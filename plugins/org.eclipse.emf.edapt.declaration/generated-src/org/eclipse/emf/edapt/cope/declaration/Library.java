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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Library</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Container for a set of coupled evolution operations
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Library#getOperations <em>Operations</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.Library#getImplementation <em>Implementation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getLibrary()
 * @model
 * @generated
 */
public interface Library extends EObject {
	/**
	 * Returns the value of the '<em><b>Operations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.cope.declaration.Operation}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.cope.declaration.Operation#getLibrary <em>Library</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Set of coupled evolution operations
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Operations</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getLibrary_Operations()
	 * @see org.eclipse.emf.edapt.cope.declaration.Operation#getLibrary
	 * @model opposite="library" containment="true"
	 * @generated
	 */
	EList<Operation> getOperations();

	/**
	 * Returns the value of the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Relative path to the code implementing the operations of the library
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Implementation</em>' attribute.
	 * @see #setImplementation(String)
	 * @see org.eclipse.emf.edapt.cope.declaration.DeclarationPackage#getLibrary_Implementation()
	 * @model required="true"
	 * @generated
	 */
	String getImplementation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.declaration.Library#getImplementation <em>Implementation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Implementation</em>' attribute.
	 * @see #getImplementation()
	 * @generated
	 */
	void setImplementation(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the operation in the library with a certain name
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	Operation getOperation(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to access the code implementing the operations declared in the library
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getCode();

} // Library
