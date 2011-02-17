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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Case Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getModel <em>Model</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedModel <em>Expected Model</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getSuite <em>Suite</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedDifferences <em>Expected Differences</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestCaseDefinition()
 * @model
 * @generated
 */
public interface TestCaseDefinition extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' attribute.
	 * @see #setModel(String)
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestCaseDefinition_Model()
	 * @model required="true"
	 * @generated
	 */
	String getModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getModel <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' attribute.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(String value);

	/**
	 * Returns the value of the '<em><b>Expected Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expected Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expected Model</em>' attribute.
	 * @see #setExpectedModel(String)
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestCaseDefinition_ExpectedModel()
	 * @model required="true"
	 * @generated
	 */
	String getExpectedModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedModel <em>Expected Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expected Model</em>' attribute.
	 * @see #getExpectedModel()
	 * @generated
	 */
	void setExpectedModel(String value);

	/**
	 * Returns the value of the '<em><b>Suite</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getCases <em>Cases</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Suite</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Suite</em>' container reference.
	 * @see #setSuite(TestSuiteDefinition)
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestCaseDefinition_Suite()
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getCases
	 * @model opposite="cases" transient="false"
	 * @generated
	 */
	TestSuiteDefinition getSuite();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getSuite <em>Suite</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Suite</em>' container reference.
	 * @see #getSuite()
	 * @generated
	 */
	void setSuite(TestSuiteDefinition value);

	/**
	 * Returns the value of the '<em><b>Expected Differences</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expected Differences</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expected Differences</em>' attribute.
	 * @see #setExpectedDifferences(int)
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestCaseDefinition_ExpectedDifferences()
	 * @model default="0"
	 * @generated
	 */
	int getExpectedDifferences();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getExpectedDifferences <em>Expected Differences</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expected Differences</em>' attribute.
	 * @see #getExpectedDifferences()
	 * @generated
	 */
	void setExpectedDifferences(int value);

} // TestCaseDefinition
