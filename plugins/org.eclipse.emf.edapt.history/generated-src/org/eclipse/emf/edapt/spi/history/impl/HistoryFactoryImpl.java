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
package org.eclipse.emf.edapt.spi.history.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.edapt.spi.history.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class HistoryFactoryImpl extends EFactoryImpl implements HistoryFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static HistoryFactory init() {
		try {
			HistoryFactory theHistoryFactory = (HistoryFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/edapt/history/0.3"); 
			if (theHistoryFactory != null) {
				return theHistoryFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new HistoryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HistoryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case HistoryPackage.HISTORY: return createHistory();
			case HistoryPackage.RELEASE: return createRelease();
			case HistoryPackage.NO_CHANGE: return createNoChange();
			case HistoryPackage.CREATE: return createCreate();
			case HistoryPackage.MOVE: return createMove();
			case HistoryPackage.DELETE: return createDelete();
			case HistoryPackage.SET: return createSet();
			case HistoryPackage.ADD: return createAdd();
			case HistoryPackage.REMOVE: return createRemove();
			case HistoryPackage.COMPOSITE_CHANGE: return createCompositeChange();
			case HistoryPackage.OPERATION_CHANGE: return createOperationChange();
			case HistoryPackage.OPERATION_INSTANCE: return createOperationInstance();
			case HistoryPackage.PARAMETER_INSTANCE: return createParameterInstance();
			case HistoryPackage.MODEL_REFERENCE: return createModelReference();
			case HistoryPackage.MIGRATION_CHANGE: return createMigrationChange();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public History createHistory() {
		HistoryImpl history = new HistoryImpl();
		return history;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Release createRelease() {
		ReleaseImpl release = new ReleaseImpl();
		return release;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NoChange createNoChange() {
		NoChangeImpl noChange = new NoChangeImpl();
		return noChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Create createCreate() {
		CreateImpl create = new CreateImpl();
		return create;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Move createMove() {
		MoveImpl move = new MoveImpl();
		return move;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Delete createDelete() {
		DeleteImpl delete = new DeleteImpl();
		return delete;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set createSet() {
		SetImpl set = new SetImpl();
		return set;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Add createAdd() {
		AddImpl add = new AddImpl();
		return add;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Remove createRemove() {
		RemoveImpl remove = new RemoveImpl();
		return remove;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CompositeChange createCompositeChange() {
		CompositeChangeImpl compositeChange = new CompositeChangeImpl();
		return compositeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationChange createOperationChange() {
		OperationChangeImpl operationChange = new OperationChangeImpl();
		return operationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationInstance createOperationInstance() {
		OperationInstanceImpl operationInstance = new OperationInstanceImpl();
		return operationInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterInstance createParameterInstance() {
		ParameterInstanceImpl parameterInstance = new ParameterInstanceImpl();
		return parameterInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference createModelReference() {
		ModelReferenceImpl modelReference = new ModelReferenceImpl();
		return modelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MigrationChange createMigrationChange() {
		MigrationChangeImpl migrationChange = new MigrationChangeImpl();
		return migrationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HistoryPackage getHistoryPackage() {
		return (HistoryPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static HistoryPackage getPackage() {
		return HistoryPackage.eINSTANCE;
	}

} //HistoryFactoryImpl
