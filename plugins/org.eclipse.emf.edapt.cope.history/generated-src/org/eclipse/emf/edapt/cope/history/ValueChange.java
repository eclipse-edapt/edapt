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
package org.eclipse.emf.edapt.cope.history;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A base class for metamodel changes which affect attributes and cross references of elements
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.ValueChange#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.ValueChange#getFeature <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.ValueChange#getFeatureName <em>Feature Name</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.ValueChange#getDataValue <em>Data Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.ValueChange#getReferenceValue <em>Reference Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.ValueChange#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getValueChange()
 * @model abstract="true"
 * @generated
 */
public interface ValueChange extends PrimitiveChange {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The element whose attribute or cross reference will be changed
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(EObject)
	 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getValueChange_Element()
	 * @model required="true"
	 * @generated
	 */
	EObject getElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(EObject value);

	/**
	 * Returns the value of the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A sequence of metamodel versions
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Feature</em>' reference.
	 * @see #setFeature(EStructuralFeature)
	 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getValueChange_Feature()
	 * @model required="true" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	EStructuralFeature getFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getFeature <em>Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature</em>' reference.
	 * @see #getFeature()
	 * @generated
	 */
	void setFeature(EStructuralFeature value);

	/**
	 * Returns the value of the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A reference to the meta-metamodel feature which is derived from the featureName attribute
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Feature Name</em>' attribute.
	 * @see #setFeatureName(String)
	 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getValueChange_FeatureName()
	 * @model required="true"
	 * @generated
	 */
	String getFeatureName();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getFeatureName <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Name</em>' attribute.
	 * @see #getFeatureName()
	 * @generated
	 */
	void setFeatureName(String value);

	/**
	 * Returns the value of the '<em><b>Data Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Serialized value of a primitive type to be set in, added to or removed from an attribute
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Value</em>' attribute.
	 * @see #setDataValue(String)
	 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getValueChange_DataValue()
	 * @model
	 * @generated
	 */
	String getDataValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getDataValue <em>Data Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Value</em>' attribute.
	 * @see #getDataValue()
	 * @generated
	 */
	void setDataValue(String value);

	/**
	 * Returns the value of the '<em><b>Reference Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Value of an object type to be set in, added to or removed from an reference
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Reference Value</em>' reference.
	 * @see #setReferenceValue(EObject)
	 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getValueChange_ReferenceValue()
	 * @model
	 * @generated
	 */
	EObject getReferenceValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getReferenceValue <em>Reference Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Value</em>' reference.
	 * @see #getReferenceValue()
	 * @generated
	 */
	void setReferenceValue(EObject value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Derived attribute that provides either dataValue or referenceValue depending on the type of the feature (attribute or reference respectively)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see org.eclipse.emf.edapt.cope.history.HistoryPackage#getValueChange_Value()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

} // ValueChange
