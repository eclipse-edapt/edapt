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
package org.eclipse.emf.edapt.tests.history;

/**
 * Lifecycle test for GMF metamodels
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class GMFLifecycleTest extends LifecycleTestBase {

	/**
	 * Test full lifecycle of the gmfgen example
	 */
	public void testGMFGenLifecycle() throws Exception {
		testLifecycle("gmfgen", 5);
	}

	/**
	 * Test full lifecycle of the gmfgraph example
	 */
	public void testGMFGraphLifecycle() throws Exception {
		testLifecycle("gmfgraph");
	}

	/**
	 * Test full lifecycle of the gmfgraph example
	 */
	public void testGMFMapLifecycle() throws Exception {
		testLifecycle("mappings");
	}
}