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
package org.eclipse.emf.edapt.internal.migration.execution.internal;

import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.osgi.framework.Bundle;

/**
 * Helper class to wrap an Eclipse bundle into the {@link IClassLoader} interface.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class BundleClassLoader implements IClassLoader {

	/** An Eclipse bundle. */
	private final Bundle bundle;

	/** Constructor. */
	public BundleClassLoader(Bundle bundle) {
		this.bundle = bundle;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T> load(String name) throws ClassNotFoundException {
		return (Class<T>) bundle.loadClass(name);
	}
}
