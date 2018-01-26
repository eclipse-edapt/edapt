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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
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

	private final Map<EPackage, String> packageToInferedSource = new LinkedHashMap<EPackage, String>();

	private final Map<EPackage, Text> packageToSourceText = new LinkedHashMap<EPackage, Text>();
	private final Map<EPackage, Text> packageToTargetText = new LinkedHashMap<EPackage, Text>();
	private final Map<EPackage, Button> packageToUpdateButton = new LinkedHashMap<EPackage, Button>();

	private final List<EPackage> packages;
	private final Set<EPackage> changedPackages;

	/**
	 * Constructs a new {@link ReleaseWizardPage}.
	 *
	 * @param pageName
	 * @param description
	 * @param titleImage
	 * @param packages the packages
	 * @param changedPackages the changed packages
	 */
	protected ReleaseWizardPage(String pageName, String description, ImageDescriptor titleImage,
		List<EPackage> packages, Set<EPackage> changedPackages) {
		super(pageName, pageName, titleImage);
		setDescription(description);
		this.packages = packages;
		for (final EPackage ePackage : packages) {
			packageToInferedSource.put(ePackage, inferSource(ePackage));
		}
		this.changedPackages = changedPackages;
	}

	/** Infer the label to be replaced from the package. */
	private String inferSource(EPackage ePackage) {
		try {
			final String nsURI = ePackage.getNsURI();
			final int index = nsURI.lastIndexOf('/');
			return nsURI.substring(index + 1);
		} catch (final RuntimeException e) {
			return ""; //$NON-NLS-1$
		}
	}

	@Override
	public void createControl(Composite parent) {
		final Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(mainComposite);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(mainComposite, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setShowFocusedControl(true);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(scrolledComposite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(scrolledComposite);

		final Composite composite = new Composite(scrolledComposite, SWT.None);
		GridLayoutFactory.fillDefaults().numColumns(5).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.BEGINNING, SWT.BEGINNING).applyTo(composite);
		composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite.setBackgroundMode(SWT.INHERIT_FORCE);

		createLabel(composite, "#", SWT.COLOR_DARK_BLUE); //$NON-NLS-1$
		createLabel(composite, "Package", SWT.COLOR_DARK_BLUE); //$NON-NLS-1$
		GridDataFactory.fillDefaults().hint(250, SWT.DEFAULT)
			.applyTo(createLabel(composite, "Label to match", SWT.COLOR_DARK_BLUE)); //$NON-NLS-1$
		GridDataFactory.fillDefaults().hint(250, SWT.DEFAULT)
			.applyTo(createLabel(composite, "Label to replace with", SWT.COLOR_DARK_BLUE)); //$NON-NLS-1$
		createLabel(composite, "Update?", SWT.COLOR_DARK_BLUE) //$NON-NLS-1$
			.setToolTipText("Whether to update the NS-URI at all."); //$NON-NLS-1$

		for (int i = 0; i < packages.size(); i++) {
			final EPackage ePackage = packages.get(i);

			createLabel(composite, i + 1 + "."); //$NON-NLS-1$

			createLabel(composite, ePackage.getNsURI());

			final Text sourceText = new Text(composite, SWT.BORDER);
			packageToSourceText.put(ePackage, sourceText);
			sourceText.setText(packageToInferedSource.get(ePackage));
			sourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			initSourceText(sourceText);

			final Text targetText = new Text(composite, SWT.BORDER);
			packageToTargetText.put(ePackage, targetText);
			targetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			initTargetText(targetText);

			final Button updateButton = new Button(composite, SWT.CHECK);
			updateButton.setToolTipText("Whether to update the NS-URI at all."); //$NON-NLS-1$
			packageToUpdateButton.put(ePackage, updateButton);
			updateButton.setSelection(changedPackages.contains(ePackage));
			initUpdateButton(updateButton, ePackage);
			setTextEnablement(updateButton, ePackage);

		}

		scrolledComposite.setContent(composite);
		composite.layout();
		final Point point = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrolledComposite.setMinSize(point);

		setControl(mainComposite);

		checkIfPageComplete();
	}

	private Label createLabel(Composite composite, String string) {
		final Label label = new Label(composite, SWT.NONE);
		label.setText(string);
		return label;
	}

	private Label createLabel(Composite composite, String string, int color) {
		final Label label = createLabel(composite, string);
		label.setForeground(label.getDisplay().getSystemColor(color));
		return label;
	}

	private void initSourceText(Text sourceText) {
		sourceText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				checkIfPageComplete();
			}
		});
	}

	private void initTargetText(Text targetText) {
		targetText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				checkIfPageComplete();
			}
		});
	}

	private void initUpdateButton(final Button updateButton, final EPackage ePackage) {
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setTextEnablement(updateButton, ePackage);
				checkIfPageComplete();
			}
		});
	}

	private void setTextEnablement(final Button updateButton, final EPackage ePackage) {
		packageToSourceText.get(ePackage).setEnabled(updateButton.getSelection());
		packageToTargetText.get(ePackage).setEnabled(updateButton.getSelection());
	}

	private void checkIfPageComplete() {
		for (int i = 0; i < packages.size(); i++) {
			final EPackage ePackage = packages.get(i);

			final Button updateButton = packageToUpdateButton.get(ePackage);
			final Text sourceText = packageToSourceText.get(ePackage);
			final Text targetText = packageToTargetText.get(ePackage);

			if (!updateButton.getSelection()) {
				continue;
			}
			if (sourceText.getText().isEmpty()) {
				setErrorMessage("Package at index " + (i + 1) + ":\n Label to match may not be empty"); //$NON-NLS-1$ //$NON-NLS-2$
				setPageComplete(false);
				return;
			}
			if (targetText.getText().isEmpty()) {
				setErrorMessage("Package at index " + (i + 1) + ":\n Label to replace may not be empty"); //$NON-NLS-1$ //$NON-NLS-2$
				setPageComplete(false);
				return;
			}
			if (sourceText.getText().equals(targetText.getText())) {
				setErrorMessage("Package at index " + (i + 1) + ":\n Source and target label may not be equal"); //$NON-NLS-1$ //$NON-NLS-2$
				setPageComplete(false);
				return;
			}
		}

		setErrorMessage(null);
		setPageComplete(true);
	}

	/**
	 * Returns source label.
	 *
	 * @param ePackage
	 */
	public String getSource(EPackage ePackage) {
		return packageToSourceText.get(ePackage).getText();
	}

	/**
	 * Returns target label.
	 *
	 * @param ePackage
	 */
	public String getTarget(EPackage ePackage) {
		return packageToTargetText.get(ePackage).getText();
	}

	/** Returns update flag. */
	public boolean isUpdate(EPackage ePackage) {
		return packageToUpdateButton.get(ePackage).getSelection();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			packageToTargetText.get(packages.get(0)).setFocus();
		}
	}

	/**
	 * Allows to set the target text that is visible.
	 */
	public void setTarget(EPackage ePackage, String target) {
		packageToTargetText.get(ePackage).setText(target);
		checkIfPageComplete();
	}

}
