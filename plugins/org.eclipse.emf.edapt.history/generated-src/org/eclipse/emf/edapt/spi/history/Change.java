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
package org.eclipse.emf.edapt.spi.history;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A base class for all metamodel changes
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.spi.history.Change#isBreaking <em>Breaking</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.Change#getDescription <em>Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getChange()
 * @model abstract="true"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore constraints='Breaking'"
 * @generated
 */
public interface Change extends EObject {
	/**
	 * Returns the value of the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the change is breaking, i.e. a migration has to be specified (this attribute is only used during
	 * validation and is therefore not persisted)
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Breaking</em>' attribute.
	 * @see #setBreaking(boolean)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getChange_Breaking()
	 * @model transient="true"
	 * @generated
	 */
	boolean isBreaking();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.Change#isBreaking <em>Breaking</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Breaking</em>' attribute.
	 * @see #isBreaking()
	 * @generated
	 */
	void setBreaking(boolean value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Description to comment the change
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getChange_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.Change#getDescription <em>Description</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the release to which this change belongs
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation"
	 * @generated
	 */
	Release getRelease();

} // Change
