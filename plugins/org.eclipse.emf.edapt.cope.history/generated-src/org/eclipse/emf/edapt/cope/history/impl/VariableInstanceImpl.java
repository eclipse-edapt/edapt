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
package org.eclipse.emf.edapt.cope.history.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.cope.declaration.Operation;
import org.eclipse.emf.edapt.cope.declaration.Variable;
import org.eclipse.emf.edapt.cope.history.HistoryPackage;
import org.eclipse.emf.edapt.cope.history.OperationInstance;
import org.eclipse.emf.edapt.cope.history.VariableInstance;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class VariableInstanceImpl extends PlaceholderInstanceImpl implements VariableInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariableInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HistoryPackage.Literals.VARIABLE_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Variable getVariable() {
		OperationInstance operationInstance = (OperationInstance) this.eContainer();
		Operation operation = operationInstance.getOperation();
		return operation.getVariable(this.getName());
	}

} //VariableInstanceImpl
