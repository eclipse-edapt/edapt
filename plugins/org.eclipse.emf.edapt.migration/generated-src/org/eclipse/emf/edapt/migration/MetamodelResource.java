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
package org.eclipse.emf.edapt.migration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metamodel Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Resource for metamodel parts
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.MetamodelResource#getRootPackages <em>Root Packages</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.migration.MigrationPackage#getMetamodelResource()
 * @model
 * @generated
 */
public interface MetamodelResource extends AbstractResource {
	/**
	 * Returns the value of the '<em><b>Root Packages</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EPackage}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root Packages</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Packages that constitute the root of a metamodel resource
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Root Packages</em>' reference list.
	 * @see org.eclipse.emf.edapt.migration.MigrationPackage#getMetamodelResource_RootPackages()
	 * @model
	 * @generated
	 */
	EList<EPackage> getRootPackages();

} // MetamodelResource
