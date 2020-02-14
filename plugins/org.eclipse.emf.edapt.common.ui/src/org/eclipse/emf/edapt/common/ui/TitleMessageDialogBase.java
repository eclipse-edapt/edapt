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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog that shows a title and a message.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class TitleMessageDialogBase extends TitleAreaDialog {

	/**
	 * Title
	 */
	private final String title;

	/**
	 * Message
	 */
	private final String message;

	/**
	 * Constructor
	 */
	public TitleMessageDialogBase(String title, String message) {
		super(Display.getDefault().getActiveShell());

		this.title = title;
		this.message = message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createContents(Composite parent) {
		final Control contents = super.createContents(parent);
		setTitle(title);
		setMessage(message);
		return contents;
	}
}
