/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.spi.history;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Content Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A change to the containment structure of the metamodel
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.spi.history.ContentChange#getTarget <em>Target</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.ContentChange#getReference <em>Reference</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.ContentChange#getReferenceName <em>Reference Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getContentChange()
 * @model abstract="true"
 * @generated
 */
public interface ContentChange extends PrimitiveChange {

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The target element of the create, delete or move
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(EObject)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getContentChange_Target()
	 * @model
	 * @generated
	 */
	EObject getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.ContentChange#getTarget <em>Target</em>}'
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(EObject value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A reference to the meta-metamodel feature which is derived from the featureName attribute
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Reference</em>' reference.
	 * @see #setReference(EReference)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getContentChange_Reference()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	EReference getReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.ContentChange#getReference <em>Reference</em>}'
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Reference</em>' reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(EReference value);

	/**
	 * Returns the value of the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the feature within which the child is created in the element
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Reference Name</em>' attribute.
	 * @see #setReferenceName(String)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getContentChange_ReferenceName()
	 * @model
	 * @generated
	 */
	String getReferenceName();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.ContentChange#getReferenceName
	 * <em>Reference Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Reference Name</em>' attribute.
	 * @see #getReferenceName()
	 * @generated
	 */
	void setReferenceName(String value);
} // ContentChange
