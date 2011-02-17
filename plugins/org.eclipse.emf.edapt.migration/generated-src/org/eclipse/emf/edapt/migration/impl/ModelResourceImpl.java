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
package org.eclipse.emf.edapt.migration.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.MigrationPackage;
import org.eclipse.emf.edapt.migration.ModelResource;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.impl.ModelResourceImpl#getRootInstances <em>Root Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelResourceImpl extends AbstractResourceImpl implements ModelResource {
	/**
	 * The cached value of the '{@link #getRootInstances() <em>Root Instances</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootInstances()
	 * @generated
	 * @ordered
	 */
	protected EList<Instance> rootInstances;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelResourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MigrationPackage.Literals.MODEL_RESOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Instance> getRootInstances() {
		if (rootInstances == null) {
			rootInstances = new EObjectResolvingEList<Instance>(Instance.class, this, MigrationPackage.MODEL_RESOURCE__ROOT_INSTANCES);
		}
		return rootInstances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MigrationPackage.MODEL_RESOURCE__ROOT_INSTANCES:
				return getRootInstances();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MigrationPackage.MODEL_RESOURCE__ROOT_INSTANCES:
				getRootInstances().clear();
				getRootInstances().addAll((Collection<? extends Instance>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MigrationPackage.MODEL_RESOURCE__ROOT_INSTANCES:
				getRootInstances().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MigrationPackage.MODEL_RESOURCE__ROOT_INSTANCES:
				return rootInstances != null && !rootInstances.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ModelResourceImpl
