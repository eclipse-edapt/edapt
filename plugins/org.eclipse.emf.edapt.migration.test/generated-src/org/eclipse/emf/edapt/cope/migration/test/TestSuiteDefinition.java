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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Suite Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getCases <em>Cases</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getHistory <em>History</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestSuiteDefinition()
 * @model
 * @generated
 */
public interface TestSuiteDefinition extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Cases</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getSuite <em>Suite</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cases</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cases</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestSuiteDefinition_Cases()
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestCaseDefinition#getSuite
	 * @model opposite="suite" containment="true"
	 * @generated
	 */
	EList<TestCaseDefinition> getCases();

	/**
	 * Returns the value of the '<em><b>History</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>History</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>History</em>' attribute.
	 * @see #setHistory(String)
	 * @see org.eclipse.emf.edapt.cope.migration.test.TestPackage#getTestSuiteDefinition_History()
	 * @model required="true"
	 * @generated
	 */
	String getHistory();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.migration.test.TestSuiteDefinition#getHistory <em>History</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>History</em>' attribute.
	 * @see #getHistory()
	 * @generated
	 */
	void setHistory(String value);

} // TestSuiteDefinition
