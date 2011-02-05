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
package org.eclipse.emf.edapt.cope.migration.test;

import java.io.PrintStream;

import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * A progress monitor to report to a {@link PrintStream}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class PrintStreamProgressMonitor extends NullProgressMonitor {
	
	/**
	 * Print stream
	 */
	private PrintStream out;
	
	/**
	 * Current step
	 */
	int step = 1;

	/**
	 * Constructor
	 * 
	 * @param out
	 */
	public PrintStreamProgressMonitor(PrintStream out) {
		this.out = out;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTask(String name, int totalWork) {
		out.println(name + "...");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		out.println("...done");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subTask(String name) {
		out.println(name);
		step = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void worked(int work) {
		out.println("Step " + step);
		step++;
	}

}
