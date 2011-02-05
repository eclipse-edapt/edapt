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
package org.eclipse.emf.edapt.cope.migration.test;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.edapt.cope.migration.test.TestFactory
 * @model kind="package"
 * @generated
 */
public interface TestPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "test";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.cs.tum.edu/cope/migration/test";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "test";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TestPackage eINSTANCE = org.eclipse.emf.edapt.cope.migration.test.impl.TestPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.migration.test.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.migration.test.impl.NamedElementImpl
	 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.migration.test.impl.TestSuiteDefinitionImpl <em>Suite Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestSuiteDefinitionImpl
	 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestPackageImpl#getTestSuiteDefinition()
	 * @generated
	 */
	int TEST_SUITE_DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_SUITE_DEFINITION__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Cases</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_SUITE_DEFINITION__CASES = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>History</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_SUITE_DEFINITION__HISTORY = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Suite Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_SUITE_DEFINITION_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.migration.test.impl.TestCaseDefinitionImpl <em>Case Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestCaseDefinitionImpl
	 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestPackageImpl#getTestCaseDefinition()
	 * @generated
	 */
	int TEST_CASE_DEFINITION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CASE_DEFINITION__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CASE_DEFINITION__MODEL = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expected Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CASE_DEFINITION__EXPECTED_MODEL = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Suite</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CASE_DEFINITION__SUITE = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Expected Differences</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CASE_DEFINITION__EXPECTED_DIFFERENCES = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Case Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CASE_DEFINITION_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 4;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition <em>Suite Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Suite Definition</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition
	 * @generated
	 */
	EClass getTestSuiteDefinition();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getCases <em>Cases</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cases</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getCases()
	 * @see #getTestSuiteDefinition()
	 * @generated
	 */
	EReference getTestSuiteDefinition_Cases();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getHistory <em>History</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>History</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getHistory()
	 * @see #getTestSuiteDefinition()
	 * @generated
	 */
	EAttribute getTestSuiteDefinition_History();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition <em>Case Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Case Definition</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition
	 * @generated
	 */
	EClass getTestCaseDefinition();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getModel()
	 * @see #getTestCaseDefinition()
	 * @generated
	 */
	EAttribute getTestCaseDefinition_Model();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedModel <em>Expected Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expected Model</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedModel()
	 * @see #getTestCaseDefinition()
	 * @generated
	 */
	EAttribute getTestCaseDefinition_ExpectedModel();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getSuite <em>Suite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Suite</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getSuite()
	 * @see #getTestCaseDefinition()
	 * @generated
	 */
	EReference getTestCaseDefinition_Suite();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedDifferences <em>Expected Differences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expected Differences</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedDifferences()
	 * @see #getTestCaseDefinition()
	 * @generated
	 */
	EAttribute getTestCaseDefinition_ExpectedDifferences();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.migration.test.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.migration.test.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.edapt.cope.migration.test.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TestFactory getTestFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.migration.test.impl.TestSuiteDefinitionImpl <em>Suite Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestSuiteDefinitionImpl
		 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestPackageImpl#getTestSuiteDefinition()
		 * @generated
		 */
		EClass TEST_SUITE_DEFINITION = eINSTANCE.getTestSuiteDefinition();

		/**
		 * The meta object literal for the '<em><b>Cases</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_SUITE_DEFINITION__CASES = eINSTANCE.getTestSuiteDefinition_Cases();

		/**
		 * The meta object literal for the '<em><b>History</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_SUITE_DEFINITION__HISTORY = eINSTANCE.getTestSuiteDefinition_History();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.migration.test.impl.TestCaseDefinitionImpl <em>Case Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestCaseDefinitionImpl
		 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestPackageImpl#getTestCaseDefinition()
		 * @generated
		 */
		EClass TEST_CASE_DEFINITION = eINSTANCE.getTestCaseDefinition();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_CASE_DEFINITION__MODEL = eINSTANCE.getTestCaseDefinition_Model();

		/**
		 * The meta object literal for the '<em><b>Expected Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_CASE_DEFINITION__EXPECTED_MODEL = eINSTANCE.getTestCaseDefinition_ExpectedModel();

		/**
		 * The meta object literal for the '<em><b>Suite</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_CASE_DEFINITION__SUITE = eINSTANCE.getTestCaseDefinition_Suite();

		/**
		 * The meta object literal for the '<em><b>Expected Differences</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_CASE_DEFINITION__EXPECTED_DIFFERENCES = eINSTANCE.getTestCaseDefinition_ExpectedDifferences();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.migration.test.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.migration.test.impl.NamedElementImpl
		 * @see org.eclipse.emf.edapt.cope.migration.test.impl.TestPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

	}

} //TestPackage
