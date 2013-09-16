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
package org.eclipse.emf.edapt.history.reconstruction;


// CB Migrate
//import org.eclipse.emf.compare.diff.metamodel.DiffElement;
//import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;

/**
 * A filter for diff models that filters out changes which span resources
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class DiffModelResourceFilter implements IDiffModelFilter {
	
	/** Shared instance. */
	public static final DiffModelResourceFilter INSTANCE = new DiffModelResourceFilter();

	/** {@inheritDoc} */
	// CB Migrate
//	public boolean select(DiffElement element) {
//		if (element instanceof ResourceDependencyChange) {
//			return false;
//		}
//		return true;
//	}
}
