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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edapt.declaration.Parameter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An instance of a parameter, i.e. the parameter is assigned a value
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.spi.history.ParameterInstance#getValue <em>Value</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.ParameterInstance#getDataValue <em>Data Value</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.ParameterInstance#getReferenceValue <em>Reference Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getParameterInstance()
 * @model
 * @generated
 */
public interface ParameterInstance extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The value conforming to the type derived from dataValue and referenceValue depending on its characteristics
	 * (primitive or object type, single-valued or multi-valued type)
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getParameterInstance_Value()
	 * @model transient="true" volatile="true"
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.ParameterInstance#getValue <em>Value</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

	/**
	 * Returns the value of the '<em><b>Data Value</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of serialized instances of a primitive type
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Data Value</em>' attribute list.
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getParameterInstance_DataValue()
	 * @model
	 * @generated
	 */
	EList<String> getDataValue();

	/**
	 * Returns the value of the '<em><b>Reference Value</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.spi.history.ModelReference}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of serialized instances of an object type
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Reference Value</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getParameterInstance_ReferenceValue()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModelReference> getReferenceValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the instantiated parameter
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation"
	 * @generated
	 */
	Parameter getParameter();

} // ParameterInstance
