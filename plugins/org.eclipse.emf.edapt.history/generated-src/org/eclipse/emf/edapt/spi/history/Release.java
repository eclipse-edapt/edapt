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

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Release</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A container to collect a sequence of changes until a release
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.spi.history.Release#getDate <em>Date</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.Release#getChanges <em>Changes</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.Release#getHistory <em>History</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.spi.history.Release#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getRelease()
 * @model
 * @generated
 */
public interface Release extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to get the number of the release
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getNumber();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean isLatestRelease();

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The date when the version is released
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getRelease_Date()
	 * @model
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.Release#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

	/**
	 * Returns the value of the '<em><b>Changes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.spi.history.Change}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The sequence of changes which lead to a release
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Changes</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getRelease_Changes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Change> getChanges();

	/**
	 * Returns the value of the '<em><b>History</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.spi.history.History#getReleases
	 * <em>Releases</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>History</em>' container reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * History to which the release belongs
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>History</em>' container reference.
	 * @see #setHistory(History)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getRelease_History()
	 * @see org.eclipse.emf.edapt.spi.history.History#getReleases
	 * @model opposite="releases" required="true" transient="false"
	 * @generated
	 */
	History getHistory();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.Release#getHistory <em>History</em>}' container
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>History</em>' container reference.
	 * @see #getHistory()
	 * @generated
	 */
	void setHistory(History value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Label to tag the release
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getRelease_Label()
	 * @model
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.Release#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Release</em>' reference isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the release which succeeds this release
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation"
	 * @generated
	 */
	Release getNextRelease();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Previous Release</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine the release that preceeds this release
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation"
	 * @generated
	 */
	Release getPreviousRelease();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>First Release</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine whether the release is the first one in the history
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean isFirstRelease();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Release</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Helper method to determine whether the release is the last one in the history
	 * <!-- end-model-doc -->
	 *
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean isLastRelease();

} // Release
