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


import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.edapt.migration.test.TestCaseDefinition;
import org.eclipse.emf.edapt.migration.test.TestPackage;
import org.eclipse.emf.edapt.migration.test.TestSuiteDefinition;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Case Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.test.impl.TestCaseDefinitionImpl#getModel <em>Model</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.test.impl.TestCaseDefinitionImpl#getExpectedModel <em>Expected Model</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.test.impl.TestCaseDefinitionImpl#getSuite <em>Suite</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.test.impl.TestCaseDefinitionImpl#getExpectedDifferences <em>Expected Differences</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TestCaseDefinitionImpl extends NamedElementImpl implements TestCaseDefinition {
	/**
	 * The default value of the '{@link #getModel() <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected static final String MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected String model = MODEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getExpectedModel() <em>Expected Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpectedModel()
	 * @generated
	 * @ordered
	 */
	protected static final String EXPECTED_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExpectedModel() <em>Expected Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpectedModel()
	 * @generated
	 * @ordered
	 */
	protected String expectedModel = EXPECTED_MODEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getExpectedDifferences() <em>Expected Differences</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpectedDifferences()
	 * @generated
	 * @ordered
	 */
	protected static final int EXPECTED_DIFFERENCES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getExpectedDifferences() <em>Expected Differences</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpectedDifferences()
	 * @generated
	 * @ordered
	 */
	protected int expectedDifferences = EXPECTED_DIFFERENCES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TestCaseDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.TEST_CASE_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(String newModel) {
		String oldModel = model;
		model = newModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.TEST_CASE_DEFINITION__MODEL, oldModel, model));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExpectedModel() {
		return expectedModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpectedModel(String newExpectedModel) {
		String oldExpectedModel = expectedModel;
		expectedModel = newExpectedModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.TEST_CASE_DEFINITION__EXPECTED_MODEL, oldExpectedModel, expectedModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestSuiteDefinition getSuite() {
		if (eContainerFeatureID() != TestPackage.TEST_CASE_DEFINITION__SUITE) return null;
		return (TestSuiteDefinition)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSuite(TestSuiteDefinition newSuite, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSuite, TestPackage.TEST_CASE_DEFINITION__SUITE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSuite(TestSuiteDefinition newSuite) {
		if (newSuite != eInternalContainer() || (eContainerFeatureID() != TestPackage.TEST_CASE_DEFINITION__SUITE && newSuite != null)) {
			if (EcoreUtil.isAncestor(this, newSuite))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSuite != null)
				msgs = ((InternalEObject)newSuite).eInverseAdd(this, TestPackage.TEST_SUITE_DEFINITION__CASES, TestSuiteDefinition.class, msgs);
			msgs = basicSetSuite(newSuite, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.TEST_CASE_DEFINITION__SUITE, newSuite, newSuite));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getExpectedDifferences() {
		return expectedDifferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpectedDifferences(int newExpectedDifferences) {
		int oldExpectedDifferences = expectedDifferences;
		expectedDifferences = newExpectedDifferences;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.TEST_CASE_DEFINITION__EXPECTED_DIFFERENCES, oldExpectedDifferences, expectedDifferences));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.TEST_CASE_DEFINITION__SUITE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSuite((TestSuiteDefinition)otherEnd, msgs);
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
			case TestPackage.TEST_CASE_DEFINITION__SUITE:
				return basicSetSuite(null, msgs);
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
			case TestPackage.TEST_CASE_DEFINITION__SUITE:
				return eInternalContainer().eInverseRemove(this, TestPackage.TEST_SUITE_DEFINITION__CASES, TestSuiteDefinition.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestPackage.TEST_CASE_DEFINITION__MODEL:
				return getModel();
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_MODEL:
				return getExpectedModel();
			case TestPackage.TEST_CASE_DEFINITION__SUITE:
				return getSuite();
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_DIFFERENCES:
				return getExpectedDifferences();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestPackage.TEST_CASE_DEFINITION__MODEL:
				setModel((String)newValue);
				return;
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_MODEL:
				setExpectedModel((String)newValue);
				return;
			case TestPackage.TEST_CASE_DEFINITION__SUITE:
				setSuite((TestSuiteDefinition)newValue);
				return;
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_DIFFERENCES:
				setExpectedDifferences((Integer)newValue);
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
			case TestPackage.TEST_CASE_DEFINITION__MODEL:
				setModel(MODEL_EDEFAULT);
				return;
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_MODEL:
				setExpectedModel(EXPECTED_MODEL_EDEFAULT);
				return;
			case TestPackage.TEST_CASE_DEFINITION__SUITE:
				setSuite((TestSuiteDefinition)null);
				return;
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_DIFFERENCES:
				setExpectedDifferences(EXPECTED_DIFFERENCES_EDEFAULT);
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
			case TestPackage.TEST_CASE_DEFINITION__MODEL:
				return MODEL_EDEFAULT == null ? model != null : !MODEL_EDEFAULT.equals(model);
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_MODEL:
				return EXPECTED_MODEL_EDEFAULT == null ? expectedModel != null : !EXPECTED_MODEL_EDEFAULT.equals(expectedModel);
			case TestPackage.TEST_CASE_DEFINITION__SUITE:
				return getSuite() != null;
			case TestPackage.TEST_CASE_DEFINITION__EXPECTED_DIFFERENCES:
				return expectedDifferences != EXPECTED_DIFFERENCES_EDEFAULT;
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
		result.append(" (model: ");
		result.append(model);
		result.append(", expectedModel: ");
		result.append(expectedModel);
		result.append(", expectedDifferences: ");
		result.append(expectedDifferences);
		result.append(')');
		return result.toString();
	}

} //TestCaseDefinitionImpl
