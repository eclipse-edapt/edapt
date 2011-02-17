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
package org.eclipse.emf.edapt.history;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Initializer Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Base class for content changes that perform a number of value changes to initialize a created element or detach a deleted element
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.history.InitializerChange#getChanges <em>Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.history.HistoryPackage#getInitializerChange()
 * @model abstract="true"
 * @generated
 */
public interface InitializerChange extends ContentChange {
	/**
	 * Returns the value of the '<em><b>Changes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.history.ValueChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The value changes that are performed as part of the content change
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Changes</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.history.HistoryPackage#getInitializerChange_Changes()
	 * @model containment="true"
	 * @generated
	 */
	EList<ValueChange> getChanges();

} // InitializerChange
