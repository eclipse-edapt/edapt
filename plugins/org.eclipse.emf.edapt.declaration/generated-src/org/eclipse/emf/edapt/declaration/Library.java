/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.declaration;

import org.eclipse.emf.common.util.EList;

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
 * <li>{@link org.eclipse.emf.edapt.declaration.Library#getOperations <em>Operations</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Library#getImplementation <em>Implementation</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Library#getLabel <em>Label</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.Library#getLibraries <em>Libraries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getLibrary()
 * @model
 * @generated
 */
public interface Library extends IdentifiedElement {
	/**
	 * Returns the value of the '<em><b>Operations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.declaration.Operation}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.declaration.Operation#getLibrary
	 * <em>Library</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Set of coupled evolution operations
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Operations</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getLibrary_Operations()
	 * @see org.eclipse.emf.edapt.declaration.Operation#getLibrary
	 * @model opposite="library" containment="true"
	 * @generated
	 */
	EList<Operation> getOperations();

	/**
	 * Returns the value of the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementation</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Relative path to the code implementing the operations of the library
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Implementation</em>' attribute.
	 * @see #setImplementation(Class)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getLibrary_Implementation()
	 * @model required="true"
	 * @generated
	 */
	Class getImplementation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Library#getImplementation
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
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getLibrary_Label()
	 * @model required="true"
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.Library#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Libraries</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.declaration.Library}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Libraries</em>' containment reference list isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Libraries</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getLibrary_Libraries()
	 * @model containment="true"
	 * @generated
	 */
	EList<Library> getLibraries();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the operation in the library with a certain name
	 * <!-- end-model-doc -->
	 *
	 * @model
	 * @generated
	 */
	Operation getOperation(String name);

} // Library
