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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.edapt.history.HistoryPackage
 * @generated
 */
public interface HistoryFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	HistoryFactory eINSTANCE = org.eclipse.emf.edapt.history.impl.HistoryFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>History</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>History</em>'.
	 * @generated
	 */
	History createHistory();

	/**
	 * Returns a new object of class '<em>Release</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Release</em>'.
	 * @generated
	 */
	Release createRelease();

	/**
	 * Returns a new object of class '<em>No Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>No Change</em>'.
	 * @generated
	 */
	NoChange createNoChange();

	/**
	 * Returns a new object of class '<em>Create</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Create</em>'.
	 * @generated
	 */
	Create createCreate();

	/**
	 * Returns a new object of class '<em>Move</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Move</em>'.
	 * @generated
	 */
	Move createMove();

	/**
	 * Returns a new object of class '<em>Delete</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Delete</em>'.
	 * @generated
	 */
	Delete createDelete();

	/**
	 * Returns a new object of class '<em>Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Set</em>'.
	 * @generated
	 */
	Set createSet();

	/**
	 * Returns a new object of class '<em>Add</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add</em>'.
	 * @generated
	 */
	Add createAdd();

	/**
	 * Returns a new object of class '<em>Remove</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remove</em>'.
	 * @generated
	 */
	Remove createRemove();

	/**
	 * Returns a new object of class '<em>Composite Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Composite Change</em>'.
	 * @generated
	 */
	CompositeChange createCompositeChange();

	/**
	 * Returns a new object of class '<em>Operation Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Change</em>'.
	 * @generated
	 */
	OperationChange createOperationChange();

	/**
	 * Returns a new object of class '<em>Operation Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Instance</em>'.
	 * @generated
	 */
	OperationInstance createOperationInstance();

	/**
	 * Returns a new object of class '<em>Parameter Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter Instance</em>'.
	 * @generated
	 */
	ParameterInstance createParameterInstance();

	/**
	 * Returns a new object of class '<em>Model Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Reference</em>'.
	 * @generated
	 */
	ModelReference createModelReference();

	/**
	 * Returns a new object of class '<em>Variable Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variable Instance</em>'.
	 * @generated
	 */
	VariableInstance createVariableInstance();

	/**
	 * Returns a new object of class '<em>Migration Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Migration Change</em>'.
	 * @generated
	 */
	MigrationChange createMigrationChange();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	HistoryPackage getHistoryPackage();

} //HistoryFactory
