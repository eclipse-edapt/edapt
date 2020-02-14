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
package org.eclipse.emf.edapt.spi.history.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.spi.history.Add;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.ContentChange;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.InitializerChange;
import org.eclipse.emf.edapt.spi.history.MigrateableChange;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edapt.spi.history.ModelReference;
import org.eclipse.emf.edapt.spi.history.Move;
import org.eclipse.emf.edapt.spi.history.NamedElement;
import org.eclipse.emf.edapt.spi.history.NoChange;
import org.eclipse.emf.edapt.spi.history.NonDelete;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.ParameterInstance;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.history.Remove;
import org.eclipse.emf.edapt.spi.history.Set;
import org.eclipse.emf.edapt.spi.history.ValueChange;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage
 * @generated
 */
public class HistoryAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static HistoryPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public HistoryAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = HistoryPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance
	 * object of the model.
	 * <!-- end-user-doc -->
	 *
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected HistorySwitch<Adapter> modelSwitch =
		new HistorySwitch<Adapter>() {
			@Override
			public Adapter caseHistory(History object) {
				return createHistoryAdapter();
			}

			@Override
			public Adapter caseRelease(Release object) {
				return createReleaseAdapter();
			}

			@Override
			public Adapter caseChange(Change object) {
				return createChangeAdapter();
			}

			@Override
			public Adapter casePrimitiveChange(PrimitiveChange object) {
				return createPrimitiveChangeAdapter();
			}

			@Override
			public Adapter caseNoChange(NoChange object) {
				return createNoChangeAdapter();
			}

			@Override
			public Adapter caseContentChange(ContentChange object) {
				return createContentChangeAdapter();
			}

			@Override
			public Adapter caseNonDelete(NonDelete object) {
				return createNonDeleteAdapter();
			}

			@Override
			public Adapter caseCreate(Create object) {
				return createCreateAdapter();
			}

			@Override
			public Adapter caseMove(Move object) {
				return createMoveAdapter();
			}

			@Override
			public Adapter caseDelete(Delete object) {
				return createDeleteAdapter();
			}

			@Override
			public Adapter caseValueChange(ValueChange object) {
				return createValueChangeAdapter();
			}

			@Override
			public Adapter caseSet(Set object) {
				return createSetAdapter();
			}

			@Override
			public Adapter caseAdd(Add object) {
				return createAddAdapter();
			}

			@Override
			public Adapter caseRemove(Remove object) {
				return createRemoveAdapter();
			}

			@Override
			public Adapter caseCompositeChange(CompositeChange object) {
				return createCompositeChangeAdapter();
			}

			@Override
			public Adapter caseOperationChange(OperationChange object) {
				return createOperationChangeAdapter();
			}

			@Override
			public Adapter caseOperationInstance(OperationInstance object) {
				return createOperationInstanceAdapter();
			}

			@Override
			public Adapter caseParameterInstance(ParameterInstance object) {
				return createParameterInstanceAdapter();
			}

			@Override
			public Adapter caseModelReference(ModelReference object) {
				return createModelReferenceAdapter();
			}

			@Override
			public Adapter caseNamedElement(NamedElement object) {
				return createNamedElementAdapter();
			}

			@Override
			public Adapter caseMigrateableChange(MigrateableChange object) {
				return createMigrateableChangeAdapter();
			}

			@Override
			public Adapter caseMigrationChange(MigrationChange object) {
				return createMigrationChangeAdapter();
			}

			@Override
			public Adapter caseInitializerChange(InitializerChange object) {
				return createInitializerChangeAdapter();
			}

			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.History <em>History</em>}
	 * '.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.History
	 * @generated
	 */
	public Adapter createHistoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Release <em>Release</em>}
	 * '.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Release
	 * @generated
	 */
	public Adapter createReleaseAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Change <em>Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Change
	 * @generated
	 */
	public Adapter createChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.PrimitiveChange
	 * <em>Primitive Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.PrimitiveChange
	 * @generated
	 */
	public Adapter createPrimitiveChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.NoChange
	 * <em>No Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.NoChange
	 * @generated
	 */
	public Adapter createNoChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.ContentChange
	 * <em>Content Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.ContentChange
	 * @generated
	 */
	public Adapter createContentChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.NonDelete
	 * <em>Non Delete</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.NonDelete
	 * @generated
	 */
	public Adapter createNonDeleteAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Create <em>Create</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Create
	 * @generated
	 */
	public Adapter createCreateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Move <em>Move</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Move
	 * @generated
	 */
	public Adapter createMoveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Delete <em>Delete</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Delete
	 * @generated
	 */
	public Adapter createDeleteAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.ValueChange
	 * <em>Value Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.ValueChange
	 * @generated
	 */
	public Adapter createValueChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Set <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Set
	 * @generated
	 */
	public Adapter createSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Add <em>Add</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Add
	 * @generated
	 */
	public Adapter createAddAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.Remove <em>Remove</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.Remove
	 * @generated
	 */
	public Adapter createRemoveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.CompositeChange
	 * <em>Composite Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.CompositeChange
	 * @generated
	 */
	public Adapter createCompositeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.OperationChange
	 * <em>Operation Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.OperationChange
	 * @generated
	 */
	public Adapter createOperationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.OperationInstance
	 * <em>Operation Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.OperationInstance
	 * @generated
	 */
	public Adapter createOperationInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.ParameterInstance
	 * <em>Parameter Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.ParameterInstance
	 * @generated
	 */
	public Adapter createParameterInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.ModelReference
	 * <em>Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.ModelReference
	 * @generated
	 */
	public Adapter createModelReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.NamedElement
	 * <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.MigrateableChange
	 * <em>Migrateable Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.MigrateableChange
	 * @generated
	 */
	public Adapter createMigrateableChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.MigrationChange
	 * <em>Migration Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.MigrationChange
	 * @generated
	 */
	public Adapter createMigrationChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.edapt.spi.history.InitializerChange
	 * <em>Initializer Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.emf.edapt.spi.history.InitializerChange
	 * @generated
	 */
	public Adapter createInitializerChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // HistoryAdapterFactory
