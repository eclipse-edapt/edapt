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
package org.eclipse.emf.edapt.cope.history.instantiation.ui;

import org.eclipse.emf.edapt.cope.common.ui.ResizeableDialogBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;


/**
 * Dialog to enter a Groovy script
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ScriptDialog extends ResizeableDialogBase {
	
	/**
	 * Last script
	 */
	private static String lastScript = "";

	/**
	 * Text widget
	 */
	private Text scriptText;
	
	/**
	 * Script
	 */
	private String script;

	/**
	 * Constructor
	 */
	public ScriptDialog() {
		super(new Point(640, 480), "Run script",
				"This dialog allows to enter a script to be executed.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(parent, SWT.None);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		
		scriptText = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		scriptText.setLayoutData(new GridData(GridData.FILL_BOTH));
		scriptText.setText(lastScript);

		return parent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		script = scriptText.getText();
		lastScript = script;
		super.okPressed();
	}
	
	/**
	 * Get the script
	 * 
	 * @return Groovy script
	 */
	public String getScript() {
		return script;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setShellStyle(int newShellStyle) {
	    super.setShellStyle(newShellStyle | SWT.RESIZE);
	}
}
