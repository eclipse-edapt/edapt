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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edapt.declaration.Operation;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An instance of an operation, i.e. parameters and variables are assigned values
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.history.OperationInstance#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.history.HistoryPackage#getOperationInstance()
 * @model
 * @generated
 */
public interface OperationInstance extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.history.ParameterInstance}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * List of instances of parameters
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.history.HistoryPackage#getOperationInstance_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<ParameterInstance> getParameters();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the instantiated operation
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Operation getOperation();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	void setParameterValue(String name, Object value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the instantiated parameter with a certain name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return OperationInstance_static.getParameter(this, name);'"
	 * @generated
	 */
	ParameterInstance getParameter(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the value of a parameter with a certain name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	Object getParameterValue(String name);

} // OperationInstance
