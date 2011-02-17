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
package org.eclipse.emf.edapt.cope.common.ui;

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
	private Point initialSize;

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
		if(initialSize != null) {
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
