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
package org.eclipse.emf.edapt.cope.migration.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edapt.cope.migration.DiagnosticException;
import org.eclipse.emf.edapt.cope.migration.Metamodel;
import org.eclipse.emf.edapt.cope.migration.MetamodelResource;
import org.eclipse.emf.edapt.cope.migration.MigrationPackage;
import org.eclipse.emf.edapt.cope.migration.Repository;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metamodel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.impl.MetamodelImpl#getResources <em>Resources</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.impl.MetamodelImpl#getRepository <em>Repository</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetamodelImpl extends EObjectImpl implements Metamodel {
	/**
	 * The cached value of the '{@link #getResources() <em>Resources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResources()
	 * @generated
	 * @ordered
	 */
	protected EList<MetamodelResource> resources;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetamodelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MigrationPackage.Literals.METAMODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MetamodelResource> getResources() {
		if (resources == null) {
			resources = new EObjectContainmentEList<MetamodelResource>(MetamodelResource.class, this, MigrationPackage.METAMODEL__RESOURCES);
		}
		return resources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Repository getRepository() {
		if (eContainerFeatureID() != MigrationPackage.METAMODEL__REPOSITORY) return null;
		return (Repository)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRepository(Repository newRepository, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRepository, MigrationPackage.METAMODEL__REPOSITORY, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepository(Repository newRepository) {
		if (newRepository != eInternalContainer() || (eContainerFeatureID() != MigrationPackage.METAMODEL__REPOSITORY && newRepository != null)) {
			if (EcoreUtil.isAncestor(this, newRepository))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRepository != null)
				msgs = ((InternalEObject)newRepository).eInverseAdd(this, MigrationPackage.REPOSITORY__METAMODEL, Repository.class, msgs);
			msgs = basicSetRepository(newRepository, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MigrationPackage.METAMODEL__REPOSITORY, newRepository, newRepository));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<EPackage> getEPackages() {
		EList<EPackage> ePackages = new UniqueEList<EPackage>();
		for (MetamodelResource resource : this.getResources()) {
			ePackages.addAll(resource.getRootPackages());
		}
		return ePackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MigrationPackage.METAMODEL__REPOSITORY:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRepository((Repository)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MigrationPackage.METAMODEL__RESOURCES:
				return ((InternalEList<?>)getResources()).basicRemove(otherEnd, msgs);
			case MigrationPackage.METAMODEL__REPOSITORY:
				return basicSetRepository(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case MigrationPackage.METAMODEL__REPOSITORY:
				return eInternalContainer().eInverseRemove(this, MigrationPackage.REPOSITORY__METAMODEL, Repository.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EPackage getEPackage(String name) {
		try {
			String[] path = name.split("\\.");
			int len = path.length;
			EPackage p = findPackage(this.getEPackages(), path[0]);
			for (int i = 1; i < len; i++) {
				p = findPackage(p.getESubpackages(), path[i]);
			}
			return p;
		} catch (NullPointerException e) {
			return null;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Find a package by name within a list of packages
	 * 
	 * @param name
	 * @return Package
	 */
	private EPackage findPackage(List<EPackage> packages, String name) {
		for (EPackage p : packages) {
			if (name.equals(p.getName()) || name.equals(p.getNsURI())) {
				return p;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EClassifier getEClassifier(String name) {
		try {
			int pos = name.lastIndexOf('.');
			String packageName = name.substring(0, pos);
			EPackage p = this.getEPackage(packageName);
			String classifierName = name.substring(pos + 1);
			return p.getEClassifier(classifierName);
		} catch (NullPointerException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EStructuralFeature getEFeature(String name) {
		try {
			int pos = name.lastIndexOf('.');
			String classifierName = name.substring(0, pos);
			EClass c = this.getEClass(classifierName);
			String featureName = name.substring(pos + 1);
			EStructuralFeature feature = c.getEStructuralFeature(featureName);
			return feature;
		} catch (NullPointerException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EClass getEClass(String name) {
		try {
			return (EClass) this.getEClassifier(name);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EReference getEReference(String name) {
		try {
			return (EReference) this.getEFeature(name);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EAttribute getEAttribute(String name) {
		try {
			return (EAttribute) this.getEFeature(name);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EDataType getEDataType(String name) {
		try {
			return (EDataType) this.getEClassifier(name);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EModelElement getElement(String name) {
		EPackage p = this.getEPackage(name);
		if (p != null) {
			return p;
		}
		EClassifier c = this.getEClassifier(name);
		if (c != null) {
			return c;
		}
		EStructuralFeature f = this.getEFeature(name);
		if (f != null) {
			return f;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void validate() throws DiagnosticException {
		BasicDiagnostic diagnostic = new BasicDiagnostic();
		Diagnostician diagnostician = new Diagnostician();
		for (EPackage p : this.getEPackages()) {
			diagnostician.validate(p, diagnostic);
		}
		if (diagnostic.getSeverity() != Diagnostic.OK) {
			throw new DiagnosticException("Metamodel not valid", diagnostic);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MigrationPackage.METAMODEL__RESOURCES:
				return getResources();
			case MigrationPackage.METAMODEL__REPOSITORY:
				return getRepository();
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
			case MigrationPackage.METAMODEL__RESOURCES:
				getResources().clear();
				getResources().addAll((Collection<? extends MetamodelResource>)newValue);
				return;
			case MigrationPackage.METAMODEL__REPOSITORY:
				setRepository((Repository)newValue);
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
			case MigrationPackage.METAMODEL__RESOURCES:
				getResources().clear();
				return;
			case MigrationPackage.METAMODEL__REPOSITORY:
				setRepository((Repository)null);
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
			case MigrationPackage.METAMODEL__RESOURCES:
				return resources != null && !resources.isEmpty();
			case MigrationPackage.METAMODEL__REPOSITORY:
				return getRepository() != null;
		}
		return super.eIsSet(featureID);
	}

} //MetamodelImpl
