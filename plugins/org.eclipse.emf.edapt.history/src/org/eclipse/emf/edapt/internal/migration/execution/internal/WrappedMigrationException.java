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
package org.eclipse.emf.edapt.internal.migration.execution.internal;

import org.eclipse.emf.edapt.migration.MigrationException;

/**
 * Wrap a {@link MigrationException} into a {@link RuntimeException}.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class WrappedMigrationException extends RuntimeException {

	private static final long serialVersionUID = 1033180466211898623L;

	/** Constructor. */
	public WrappedMigrationException(MigrationException e) {
		super(e);
	}

	/** {@inheritDoc} */
	@Override
	public synchronized MigrationException getCause() {
		return (MigrationException) super.getCause();
	}
}
