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
package org.eclipse.emf.edapt.common.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

/**
 * A dialog that can be resized.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class ResizeableDialogBase extends TitleMessageDialogBase {

	/**
	 * Initial size
	 */
	private final Point initialSize;

	/**
	 * Constructor
	 */
	public ResizeableDialogBase(Point initialSize, String title, String message) {
		super(title, message);

		this.initialSize = initialSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		if (initialSize != null) {
			getShell().setSize(initialSize);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}
}
