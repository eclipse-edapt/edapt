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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edapt.cope.history.HistoryPackage;
import org.eclipse.emf.edapt.cope.history.MigrateableChange;
import org.eclipse.emf.edapt.cope.history.MigrationChange;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Migration Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.impl.MigrationChangeImpl#getMigration <em>Migration</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.impl.MigrationChangeImpl#getChanges <em>Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MigrationChangeImpl extends ChangeImpl implements MigrationChange {
	/**
	 * The default value of the '{@link #getMigration() <em>Migration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMigration()
	 * @generated
	 * @ordered
	 */
	protected static final String MIGRATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMigration() <em>Migration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMigration()
	 * @generated
	 * @ordered
	 */
	protected String migration = MIGRATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChanges() <em>Changes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChanges()
	 * @generated
	 * @ordered
	 */
	protected EList<MigrateableChange> changes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MigrationChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HistoryPackage.Literals.MIGRATION_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMigration() {
		return migration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMigration(String newMigration) {
		String oldMigration = migration;
		migration = newMigration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HistoryPackage.MIGRATION_CHANGE__MIGRATION, oldMigration, migration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MigrateableChange> getChanges() {
		if (changes == null) {
			changes = new EObjectContainmentEList<MigrateableChange>(MigrateableChange.class, this, HistoryPackage.MIGRATION_CHANGE__CHANGES);
		}
		return changes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case HistoryPackage.MIGRATION_CHANGE__CHANGES:
				return ((InternalEList<?>)getChanges()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case HistoryPackage.MIGRATION_CHANGE__MIGRATION:
				return getMigration();
			case HistoryPackage.MIGRATION_CHANGE__CHANGES:
				return getChanges();
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
			case HistoryPackage.MIGRATION_CHANGE__MIGRATION:
				setMigration((String)newValue);
				return;
			case HistoryPackage.MIGRATION_CHANGE__CHANGES:
				getChanges().clear();
				getChanges().addAll((Collection<? extends MigrateableChange>)newValue);
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
			case HistoryPackage.MIGRATION_CHANGE__MIGRATION:
				setMigration(MIGRATION_EDEFAULT);
				return;
			case HistoryPackage.MIGRATION_CHANGE__CHANGES:
				getChanges().clear();
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
			case HistoryPackage.MIGRATION_CHANGE__MIGRATION:
				return MIGRATION_EDEFAULT == null ? migration != null : !MIGRATION_EDEFAULT.equals(migration);
			case HistoryPackage.MIGRATION_CHANGE__CHANGES:
				return changes != null && !changes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (migration: ");
		result.append(migration);
		result.append(')');
		return result.toString();
	}

} //MigrationChangeImpl
