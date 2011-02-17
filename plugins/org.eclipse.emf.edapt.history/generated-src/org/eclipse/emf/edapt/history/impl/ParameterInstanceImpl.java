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
package org.eclipse.emf.edapt.history.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class ParameterInstanceImpl extends PlaceholderInstanceImpl implements ParameterInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HistoryPackage.Literals.PARAMETER_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Parameter getParameter() {
		OperationInstance operationInstance = (OperationInstance) this.eContainer();
		Operation operation = operationInstance.getOperation();
		return operation.getParameter(this.getName());
	}

} //ParameterInstanceImpl
