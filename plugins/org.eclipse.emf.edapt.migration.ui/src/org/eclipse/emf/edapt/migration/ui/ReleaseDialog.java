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
package org.eclipse.emf.edapt.migration.ui;

import java.util.Set;

import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.common.ui.TitleMessageDialogBase;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Dialog to choose a release from a number of releases
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReleaseDialog extends TitleMessageDialogBase {

	/**
	 * Set of releases
	 */
	private final Set<Release> releases;

	/**
	 * Combo viewer to display releases
	 */
	private ComboViewer releaseCombo;

	/**
	 * Chosen release
	 */
	private Release release;

	/**
	 * Constructor
	 */
	public ReleaseDialog(Set<Release> releases) {
		super("Choose metamodel release for model", //$NON-NLS-1$
			"The release of the metamodel to which the model conforms cannot be uniquely determined.\n" + //$NON-NLS-1$
				"This dialog allows to choose the release from the possibilities."); //$NON-NLS-1$

		this.releases = releases;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		final Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label releaseLabel = new Label(composite, SWT.None);
		releaseLabel.setText("Release"); //$NON-NLS-1$

		releaseCombo = new ComboViewer(composite);
		releaseCombo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		releaseCombo.setContentProvider(new ArrayContentProvider());
		releaseCombo.setLabelProvider(new LabelProvider());
		releaseCombo.setComparator(new ViewerComparator());

		releaseCombo.setInput(releases);
		releaseCombo.getCombo().select(0);

		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		release = SelectionUtils.getSelectedElement(releaseCombo.getSelection(), Release.class);
		super.okPressed();
	}

	/**
	 * Gets the chosen release.
	 */
	public Release getRelease() {
		return release;
	}

	/**
	 * Label provider
	 */
	private class LabelProvider implements ILabelProvider {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getImage(Object element) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getText(Object element) {
			final Release release = (Release) element;
			return release.getLabel() + " (" + release.getNumber() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addListener(ILabelProviderListener listener) {
			// not required
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			// not required
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeListener(ILabelProviderListener listener) {
			// not required
		}

	}
}
