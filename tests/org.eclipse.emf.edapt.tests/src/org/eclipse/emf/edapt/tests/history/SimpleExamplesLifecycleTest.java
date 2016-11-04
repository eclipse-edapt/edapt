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
package org.eclipse.emf.edapt.tests.history;

/**
 * Lifecycle test for simple metamodels
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class SimpleExamplesLifecycleTest extends LifecycleTestBase {

	/**
	 * Test full lifecycle of the filesystem example
	 */
	public void testFilesystemLifecycle() throws Exception {
		testLifecycle("filesystem"); //$NON-NLS-1$
	}

	/**
	 * Test full lifecycle of the component example
	 */
	public void testComponentLifecycle() throws Exception {
		testLifecycle("component"); //$NON-NLS-1$
	}

	/**
	 * Test full lifecycle of sprinkle's example
	 */
	public void testSprinkleLifecycle() throws Exception {
		testLifecycle("sprinkle"); //$NON-NLS-1$
	}

	/**
	 * Test full lifecycle of the statemachine example
	 */
	public void testStatemachineLifecycle() throws Exception {
		testLifecycle("statemachine"); //$NON-NLS-1$
	}

	/**
	 * Test full lifecycle of the deleteClass example
	 */
	public void testDeleteClassLifecycle() throws Exception {
		testLifecycle("deleteClass"); //$NON-NLS-1$
	}

}
