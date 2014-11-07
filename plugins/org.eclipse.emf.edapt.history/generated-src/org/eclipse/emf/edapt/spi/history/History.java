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
package org.eclipse.emf.edapt.spi.history;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>History</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A container for a whole history of a metamodel
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.spi.history.History#getReleases <em>Releases</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getHistory()
 * @model
 * @generated
 */
public interface History extends EObject {
	/**
	 * Returns the value of the '<em><b>Releases</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.spi.history.Release}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.spi.history.Release#getHistory <em>History</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A sequence of metamodel versions
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Releases</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getHistory_Releases()
	 * @see org.eclipse.emf.edapt.spi.history.Release#getHistory
	 * @model opposite="history" containment="true"
	 * @generated
	 */
	EList<Release> getReleases();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>First Release</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the first release of the history
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Release getFirstRelease();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Release</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the last release of the history
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Release getLastRelease();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the root packages created by the history
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<EPackage> getRootPackages();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Release getLatestRelease();

} // History
