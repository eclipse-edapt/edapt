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
package org.eclipse.emf.edapt.migration.test.impl;


import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edapt.migration.test.TestCaseDefinition;
import org.eclipse.emf.edapt.migration.test.TestPackage;
import org.eclipse.emf.edapt.migration.test.TestSuiteDefinition;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Suite Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.test.impl.TestSuiteDefinitionImpl#getCases <em>Cases</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.test.impl.TestSuiteDefinitionImpl#getHistory <em>History</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TestSuiteDefinitionImpl extends NamedElementImpl implements TestSuiteDefinition {
	/**
	 * The cached value of the '{@link #getCases() <em>Cases</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCases()
	 * @generated
	 * @ordered
	 */
	protected EList<TestCaseDefinition> cases;

	/**
	 * The default value of the '{@link #getHistory() <em>History</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHistory()
	 * @generated
	 * @ordered
	 */
	protected static final String HISTORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHistory() <em>History</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHistory()
	 * @generated
	 * @ordered
	 */
	protected String history = HISTORY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TestSuiteDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.TEST_SUITE_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TestCaseDefinition> getCases() {
		if (cases == null) {
			cases = new EObjectContainmentWithInverseEList<TestCaseDefinition>(TestCaseDefinition.class, this, TestPackage.TEST_SUITE_DEFINITION__CASES, TestPackage.TEST_CASE_DEFINITION__SUITE);
		}
		return cases;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHistory() {
		return history;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHistory(String newHistory) {
		String oldHistory = history;
		history = newHistory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.TEST_SUITE_DEFINITION__HISTORY, oldHistory, history));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.TEST_SUITE_DEFINITION__CASES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getCases()).basicAdd(otherEnd, msgs);
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
			case TestPackage.TEST_SUITE_DEFINITION__CASES:
				return ((InternalEList<?>)getCases()).basicRemove(otherEnd, msgs);
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
			case TestPackage.TEST_SUITE_DEFINITION__CASES:
				return getCases();
			case TestPackage.TEST_SUITE_DEFINITION__HISTORY:
				return getHistory();
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
			case TestPackage.TEST_SUITE_DEFINITION__CASES:
				getCases().clear();
				getCases().addAll((Collection<? extends TestCaseDefinition>)newValue);
				return;
			case TestPackage.TEST_SUITE_DEFINITION__HISTORY:
				setHistory((String)newValue);
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
			case TestPackage.TEST_SUITE_DEFINITION__CASES:
				getCases().clear();
				return;
			case TestPackage.TEST_SUITE_DEFINITION__HISTORY:
				setHistory(HISTORY_EDEFAULT);
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
			case TestPackage.TEST_SUITE_DEFINITION__CASES:
				return cases != null && !cases.isEmpty();
			case TestPackage.TEST_SUITE_DEFINITION__HISTORY:
				return HISTORY_EDEFAULT == null ? history != null : !HISTORY_EDEFAULT.equals(history);
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
		result.append(" (history: ");
		result.append(history);
		result.append(')');
		return result.toString();
	}

} //TestSuiteDefinitionImpl
