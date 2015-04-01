/*******************************************************************************
 * Copyright (c) 2007-2015 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * {@link WizardPage} to replace the ns uri label of a single EPackage.
 *
 * @author jfaltermeier
 *
 */
public class ReleaseWizardPage extends WizardPage {

	private final String source;

	private Text sourceText;
	private Text targetText;
	private Button updateButton;

	/**
	 * Constructs a new {@link ReleaseWizardPage}.
	 *
	 * @param pageName
	 * @param description
	 * @param titleImage
	 * @param source the releases label to replace
	 */
	protected ReleaseWizardPage(String pageName, String description, ImageDescriptor titleImage, String source) {
		super(pageName, pageName, titleImage);
		setDescription(description);
		this.source = source;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.None);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		final Label sourceLabel = new Label(composite, SWT.None);
		sourceLabel.setText("Label to match:"); //$NON-NLS-1$

		sourceText = new Text(composite, SWT.BORDER);
		sourceText.setText(source);
		sourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		initSourceText(sourceText);

		final Label targetLabel = new Label(composite, SWT.None);
		targetLabel.setText("Label to replace with:"); //$NON-NLS-1$

		targetText = new Text(composite, SWT.BORDER);
		targetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		initTargetText(targetText);

		final Label updateLabel = new Label(composite, SWT.None);
		updateLabel.setText("Update namespace URI:"); //$NON-NLS-1$

		updateButton = new Button(composite, SWT.CHECK);
		updateButton.setSelection(true);
		initUpdateButton(updateButton);

		setControl(composite);

		checkIfPageComplete();
	}

	private void initSourceText(Text sourceText) {
		sourceText.addKeyListener(new KeyAdapter() {
			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				checkIfPageComplete();
			}
		});
	}

	private void initTargetText(Text targetText) {
		targetText.addKeyListener(new KeyAdapter() {
			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				checkIfPageComplete();
			}
		});
	}

	private void initUpdateButton(Button updateButton) {
		updateButton.addSelectionListener(new SelectionAdapter() {
			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkIfPageComplete();
			}
		});
	}

	private void checkIfPageComplete() {
		if (!updateButton.getSelection()) {
			setErrorMessage(null);
			setPageComplete(true);
			return;
		}
		if (sourceText.getText().isEmpty()) {
			setErrorMessage("Label to match may not be empty"); //$NON-NLS-1$
			setPageComplete(false);
			return;
		}
		if (targetText.getText().isEmpty()) {
			setErrorMessage("Label to replace may not be empty"); //$NON-NLS-1$
			setPageComplete(false);
			return;
		}
		if (sourceText.getText().equals(targetText.getText())) {
			setErrorMessage("Source and target label may not be equal"); //$NON-NLS-1$
			setPageComplete(false);
			return;
		}
		setErrorMessage(null);
		setPageComplete(true);
	}

	/** Returns source label. */
	public String getSource() {
		return sourceText.getText();
	}

	/** Returns target label. */
	public String getTarget() {
		return targetText.getText();
	}

	/** Returns update flag. */
	public boolean isUpdate() {
		return updateButton.getSelection();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			targetText.setFocus();
		}
	}

}
