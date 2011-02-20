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
package org.eclipse.emf.edapt.history;

import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A change that sets the value (may be null) of an single-valued attribute or reference
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.history.Set#getOldDataValue <em>Old Data Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.Set#getOldReferenceValue <em>Old Reference Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.Set#getOldValue <em>Old Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.history.HistoryPackage#getSet()
 * @model
 * @generated
 */
public interface Set extends ValueChange {

	/**
	 * Returns the value of the '<em><b>Old Data Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Data Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Serialized value of a primitive type that is the old value of the attribute
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Old Data Value</em>' attribute.
	 * @see #setOldDataValue(String)
	 * @see org.eclipse.emf.edapt.history.HistoryPackage#getSet_OldDataValue()
	 * @model
	 * @generated
	 */
	String getOldDataValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.history.Set#getOldDataValue <em>Old Data Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Data Value</em>' attribute.
	 * @see #getOldDataValue()
	 * @generated
	 */
	void setOldDataValue(String value);

	/**
	 * Returns the value of the '<em><b>Old Reference Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Reference Value</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Value of an object type that is the old value of the reference
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Old Reference Value</em>' reference.
	 * @see #setOldReferenceValue(EObject)
	 * @see org.eclipse.emf.edapt.history.HistoryPackage#getSet_OldReferenceValue()
	 * @model
	 * @generated
	 */
	EObject getOldReferenceValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.history.Set#getOldReferenceValue <em>Old Reference Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Reference Value</em>' reference.
	 * @see #getOldReferenceValue()
	 * @generated
	 */
	void setOldReferenceValue(EObject value);

	/**
	 * Returns the value of the '<em><b>Old Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Derived attribute that provides either oldDataValue or oldFeferenceValue depending on the type of the feature (attribute or reference respectively)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Old Value</em>' attribute.
	 * @see #setOldValue(Object)
	 * @see org.eclipse.emf.edapt.history.HistoryPackage#getSet_OldValue()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	Object getOldValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.history.Set#getOldValue <em>Old Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Value</em>' attribute.
	 * @see #getOldValue()
	 * @generated
	 */
	void setOldValue(Object value);
} // Set
