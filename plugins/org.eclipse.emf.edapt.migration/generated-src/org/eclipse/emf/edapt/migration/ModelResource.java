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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Resource for model parts
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.ModelResource#getRootInstances <em>Root Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.migration.MigrationPackage#getModelResource()
 * @model
 * @generated
 */
public interface ModelResource extends AbstractResource {
	/**
	 * Returns the value of the '<em><b>Root Instances</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.migration.Instance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root Instances</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The instances which are the root elements of the resource
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Root Instances</em>' reference list.
	 * @see org.eclipse.emf.edapt.migration.MigrationPackage#getModelResource_RootInstances()
	 * @model
	 * @generated
	 */
	EList<Instance> getRootInstances();

} // ModelResource
