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
package org.eclipse.emf.edapt.declaration;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Typed Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Proxy for a type
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.declaration.TypedElement#isMany <em>Many</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.TypedElement#getClassifier <em>Classifier</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.TypedElement#getClassifierName <em>Classifier Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getTypedElement()
 * @model abstract="true"
 * @generated
 */
public interface TypedElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the type is multi-valued
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Many</em>' attribute.
	 * @see #setMany(boolean)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getTypedElement_Many()
	 * @model
	 * @generated
	 */
	boolean isMany();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.TypedElement#isMany <em>Many</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Many</em>' attribute.
	 * @see #isMany()
	 * @generated
	 */
	void setMany(boolean value);

	/**
	 * Returns the value of the '<em><b>Classifier</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Classifier for which the type stands (the value of this reference is derived from the type's name)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Classifier</em>' reference.
	 * @see #setClassifier(EClassifier)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getTypedElement_Classifier()
	 * @model required="true" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	EClassifier getClassifier();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.TypedElement#getClassifier <em>Classifier</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Classifier</em>' reference.
	 * @see #getClassifier()
	 * @generated
	 */
	void setClassifier(EClassifier value);

	/**
	 * Returns the value of the '<em><b>Classifier Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Classifier Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the classifier for which the type stands
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Classifier Name</em>' attribute.
	 * @see #setClassifierName(String)
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#getTypedElement_ClassifierName()
	 * @model required="true"
	 * @generated
	 */
	String getClassifierName();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.declaration.TypedElement#getClassifierName <em>Classifier Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Classifier Name</em>' attribute.
	 * @see #getClassifierName()
	 * @generated
	 */
	void setClassifierName(String value);

} // TypedElement
