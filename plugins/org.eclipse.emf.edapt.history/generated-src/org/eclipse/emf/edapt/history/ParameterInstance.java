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
 *
 * @see org.eclipse.emf.edapt.history.HistoryPackage#getParameterInstance()
 * @model
 * @generated
 */
public interface ParameterInstance extends PlaceholderInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the instantiated parameter
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Parameter getParameter();

} // ParameterInstance
