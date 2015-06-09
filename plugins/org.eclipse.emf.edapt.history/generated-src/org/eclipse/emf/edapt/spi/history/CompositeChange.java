/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.spi.history;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A change consisting of a sequence of primitive changes
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.spi.history.CompositeChange#getChanges <em>Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getCompositeChange()
 * @model
 * @generated
 */
public interface CompositeChange extends MigrateableChange {
	/**
	 * Returns the value of the '<em><b>Changes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.spi.history.PrimitiveChange}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A sequence of primitive changes
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Changes</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getCompositeChange_Changes()
	 * @model containment="true"
	 * @generated
	 */
	EList<PrimitiveChange> getChanges();

} // CompositeChange
