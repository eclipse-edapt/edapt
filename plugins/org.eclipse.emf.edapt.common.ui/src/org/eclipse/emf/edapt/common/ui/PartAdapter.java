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
package org.eclipse.emf.edapt.common.ui;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This class provides default implementations of the interface methods.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class PartAdapter implements IPartListener {

	/**
	 * {@inheritDoc}
	 */
	public void partActivated(IWorkbenchPart part) {
		// avoid empty implementations in sub classes
	}

	/**
	 * {@inheritDoc}
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
		// avoid empty implementations in sub classes
	}

	/**
	 * {@inheritDoc}
	 */
	public void partClosed(IWorkbenchPart part) {
		// avoid empty implementations in sub classes
	}

	/**
	 * {@inheritDoc}
	 */
	public void partDeactivated(IWorkbenchPart part) {
		// avoid empty implementations in sub classes
	}

	/**
	 * {@inheritDoc}
	 */
	public void partOpened(IWorkbenchPart part) {
		// avoid empty implementations in sub classes
	}

}
