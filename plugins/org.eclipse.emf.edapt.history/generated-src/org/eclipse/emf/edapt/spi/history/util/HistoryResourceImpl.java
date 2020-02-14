/*******************************************************************************
 * Copyright (c) 2015 CohesionForce, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * CohesionForce, Inc - Initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.edapt.spi.history.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 *
 * @see org.eclipse.emf.edapt.spi.history.util.HistoryResourceFactoryImpl
 * @generated
 * @since 1.2
 */
public class HistoryResourceImpl extends XMIResourceImpl {
	/**
	 * Creates an instance of the resource.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param uri the URI of the new resource.
	 * @generated
	 */
	public HistoryResourceImpl(URI uri) {
		super(uri);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#useUUIDs()
	 */
	@Override
	protected boolean useUUIDs() {
		return true;
	}

} // HistoryResourceImpl
