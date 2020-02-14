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

import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;

/**
 * Helper class to wrap a Java class loader into the {@link IClassLoader} interface.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ClassLoaderFacade implements IClassLoader {

	/** The Java classloader. */
	private final ClassLoader loader;

	/** Constructor. */
	public ClassLoaderFacade(ClassLoader loader) {
		this.loader = loader;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T> load(String name) throws ClassNotFoundException {
		return (Class<T>) loader.loadClass(name);
	}
}
