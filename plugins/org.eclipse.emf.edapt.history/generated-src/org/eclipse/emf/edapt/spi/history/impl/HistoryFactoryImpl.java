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
package org.eclipse.emf.edapt.spi.history.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.edapt.spi.history.Add;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edapt.spi.history.ModelReference;
import org.eclipse.emf.edapt.spi.history.Move;
import org.eclipse.emf.edapt.spi.history.NoChange;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.ParameterInstance;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.history.Remove;
import org.eclipse.emf.edapt.spi.history.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class HistoryFactoryImpl extends EFactoryImpl implements HistoryFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static HistoryFactory init() {
		try {
			final HistoryFactory theHistoryFactory = (HistoryFactory) EPackage.Registry.INSTANCE
				.getEFactory("http://www.eclipse.org/emf/edapt/history/0.3"); //$NON-NLS-1$
			if (theHistoryFactory != null) {
				return theHistoryFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new HistoryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public HistoryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case HistoryPackage.HISTORY:
			return createHistory();
		case HistoryPackage.RELEASE:
			return createRelease();
		case HistoryPackage.NO_CHANGE:
			return createNoChange();
		case HistoryPackage.CREATE:
			return createCreate();
		case HistoryPackage.MOVE:
			return createMove();
		case HistoryPackage.DELETE:
			return createDelete();
		case HistoryPackage.SET:
			return createSet();
		case HistoryPackage.ADD:
			return createAdd();
		case HistoryPackage.REMOVE:
			return createRemove();
		case HistoryPackage.COMPOSITE_CHANGE:
			return createCompositeChange();
		case HistoryPackage.OPERATION_CHANGE:
			return createOperationChange();
		case HistoryPackage.OPERATION_INSTANCE:
			return createOperationInstance();
		case HistoryPackage.PARAMETER_INSTANCE:
			return createParameterInstance();
		case HistoryPackage.MODEL_REFERENCE:
			return createModelReference();
		case HistoryPackage.MIGRATION_CHANGE:
			return createMigrationChange();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public History createHistory() {
		final HistoryImpl history = new HistoryImpl();
		return history;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Release createRelease() {
		final ReleaseImpl release = new ReleaseImpl();
		return release;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NoChange createNoChange() {
		final NoChangeImpl noChange = new NoChangeImpl();
		return noChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Create createCreate() {
		final CreateImpl create = new CreateImpl();
		return create;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Move createMove() {
		final MoveImpl move = new MoveImpl();
		return move;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Delete createDelete() {
		final DeleteImpl delete = new DeleteImpl();
		return delete;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Set createSet() {
		final SetImpl set = new SetImpl();
		return set;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Add createAdd() {
		final AddImpl add = new AddImpl();
		return add;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Remove createRemove() {
		final RemoveImpl remove = new RemoveImpl();
		return remove;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public CompositeChange createCompositeChange() {
		final CompositeChangeImpl compositeChange = new CompositeChangeImpl();
		return compositeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationChange createOperationChange() {
		final OperationChangeImpl operationChange = new OperationChangeImpl();
		return operationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationInstance createOperationInstance() {
		final OperationInstanceImpl operationInstance = new OperationInstanceImpl();
		return operationInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ParameterInstance createParameterInstance() {
		final ParameterInstanceImpl parameterInstance = new ParameterInstanceImpl();
		return parameterInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ModelReference createModelReference() {
		final ModelReferenceImpl modelReference = new ModelReferenceImpl();
		return modelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public MigrationChange createMigrationChange() {
		final MigrationChangeImpl migrationChange = new MigrationChangeImpl();
		return migrationChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public HistoryPackage getHistoryPackage() {
		return (HistoryPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static HistoryPackage getPackage() {
		return HistoryPackage.eINSTANCE;
	}

} // HistoryFactoryImpl
