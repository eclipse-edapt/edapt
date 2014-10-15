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
package org.eclipse.emf.edapt.spi.history;



/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A change to witness the execution of a coupled evolution operation
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.spi.history.OperationChange#getOperation <em>Operation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getOperationChange()
 * @model
 * @generated
 */
public interface OperationChange extends CompositeChange {
	/**
	 * Returns the value of the '<em><b>Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * An instance of the operation
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Operation</em>' containment reference.
	 * @see #setOperation(OperationInstance)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getOperationChange_Operation()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OperationInstance getOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.OperationChange#getOperation <em>Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' containment reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(OperationInstance value);

} // OperationChange
