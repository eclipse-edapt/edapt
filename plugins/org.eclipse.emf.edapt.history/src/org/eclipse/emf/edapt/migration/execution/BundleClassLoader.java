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
package org.eclipse.emf.edapt.migration.execution;

import org.osgi.framework.Bundle;

public class BundleClassLoader implements IClassLoader {
	
	private final Bundle bundle;

	public BundleClassLoader(Bundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public <T> Class<T> load(String name) throws ClassNotFoundException {
		return bundle.loadClass(name);
	}

}
