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
package org.eclipse.emf.edapt.cope.history;

import org.eclipse.emf.edapt.cope.declaration.Variable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An instance of a helper variable, i.e. the variable is assigned a value
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getVariableInstance()
 * @model
 * @generated
 */
public interface VariableInstance extends PlaceholderInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the instantiated variable
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Variable getVariable();

} // VariableInstance
