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
package org.eclipse.emf.edapt.cope.migration.execution;

import org.eclipse.emf.edapt.cope.migration.Instance;

/**
 * Debugger for model migrations
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public interface IDebugger {

	/**
	 * Debug
	 * 
	 * @param context
	 * @param message
	 */
	public void debug(Instance context, String message);
}
