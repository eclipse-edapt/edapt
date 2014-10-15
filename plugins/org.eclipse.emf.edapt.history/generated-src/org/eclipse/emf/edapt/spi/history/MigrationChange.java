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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Migration Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A change to attach a migration to a sequence of changes
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.spi.history.MigrationChange#getMigration <em>Migration</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.spi.history.MigrationChange#getChanges <em>Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getMigrationChange()
 * @model
 * @generated
 */
public interface MigrationChange extends Change {
	/**
	 * Returns the value of the '<em><b>Migration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The code snippet overwriting the identity transformation
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Migration</em>' attribute.
	 * @see #setMigration(String)
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getMigrationChange_Migration()
	 * @model required="true"
	 * @generated
	 */
	String getMigration();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.spi.history.MigrationChange#getMigration <em>Migration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Migration</em>' attribute.
	 * @see #getMigration()
	 * @generated
	 */
	void setMigration(String value);

	/**
	 * Returns the value of the '<em><b>Changes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.spi.history.MigrateableChange}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A sequence of primitive changes
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Changes</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#getMigrationChange_Changes()
	 * @model containment="true"
	 * @generated
	 */
	EList<MigrateableChange> getChanges();

} // MigrationChange
