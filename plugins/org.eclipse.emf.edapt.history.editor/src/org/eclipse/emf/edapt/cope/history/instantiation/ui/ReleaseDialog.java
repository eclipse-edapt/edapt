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

import org.eclipse.emf.edapt.cope.common.ui.TitleMessageDialogBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * A dialog to support the update of package namespaces when releasing
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReleaseDialog extends TitleMessageDialogBase {

	/**
	 * Widget for target label
	 */
	private Text targetText;

	/**
	 * Widget for update of namespace URIs
	 */
	private Button updateButton;
	
	/**
	 * Label to be replaced
	 */
	private String source;

	/**
	 * Label by which it is replaced
	 */
	private String target;

	/**
	 * Whether the namespace URIs should be updated
	 */
	private boolean update;

	/**
	 * Constructor
	 * 
	 * @param source
	 */
	public ReleaseDialog(String source) {
		super("Update namespace URI of packages",
				"In the namespace URI of each package, a label is replaced by another one.\n" +
				   "The new label will denote the release. If you want to change the namespace\n" +
				   "URIs in a different, modify them manually before pressing Release.");
		this.source = source;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(parent, SWT.None);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		
		Label sourceLabel = new Label(composite, SWT.None);
		sourceLabel.setText("Label to match:");
		
		Text sourceText = new Text(composite, SWT.BORDER);
		sourceText.setText(source);
		sourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label targetLabel = new Label(composite, SWT.None);
		targetLabel.setText("Label to replace with:");
		
		targetText = new Text(composite, SWT.BORDER);
		targetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label updateLabel = new Label(composite, SWT.None);
		updateLabel.setText("Update namespace URIs:");
		
		updateButton = new Button(composite, SWT.CHECK);
		updateButton.setSelection(true);

		return parent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		target = targetText.getText();
		update = updateButton.getSelection();
		super.okPressed();
	}
	
	/**
	 * Returns target label
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Returns update flag 
	 */
	public boolean isUpdate() {
		return update;
	}
}
