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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.edapt.migration.MetamodelResource;
import org.eclipse.emf.edapt.migration.MigrationPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metamodel Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.impl.MetamodelResourceImpl#getRootPackages <em>Root Packages</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetamodelResourceImpl extends AbstractResourceImpl implements MetamodelResource {
	/**
	 * The cached value of the '{@link #getRootPackages() <em>Root Packages</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<EPackage> rootPackages;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetamodelResourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MigrationPackage.Literals.METAMODEL_RESOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EPackage> getRootPackages() {
		if (rootPackages == null) {
			rootPackages = new EObjectResolvingEList<EPackage>(EPackage.class, this, MigrationPackage.METAMODEL_RESOURCE__ROOT_PACKAGES);
		}
		return rootPackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MigrationPackage.METAMODEL_RESOURCE__ROOT_PACKAGES:
				return getRootPackages();
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
			case MigrationPackage.METAMODEL_RESOURCE__ROOT_PACKAGES:
				getRootPackages().clear();
				getRootPackages().addAll((Collection<? extends EPackage>)newValue);
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
			case MigrationPackage.METAMODEL_RESOURCE__ROOT_PACKAGES:
				getRootPackages().clear();
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
			case MigrationPackage.METAMODEL_RESOURCE__ROOT_PACKAGES:
				return rootPackages != null && !rootPackages.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MetamodelResourceImpl
