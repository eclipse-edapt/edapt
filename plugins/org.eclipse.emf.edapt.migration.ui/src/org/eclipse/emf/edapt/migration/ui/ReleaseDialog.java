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
package org.eclipse.emf.edapt.migration.ui;

import java.util.Set;

import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.common.ui.TitleMessageDialogBase;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
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
	private final Set<Integer> releases;
	
	/**
	 * Combo viewer to display releases
	 */
	private ComboViewer releaseCombo;

	/**
	 * Chosen release
	 */
	private Integer release;

	/**
	 * Constructor
	 */
	public ReleaseDialog(Set<Integer> releases) {
		super("Choose metamodel release for model",
				"The release of the metamodel to which the model conforms cannot be uniquely determined.\n" +
				"This dialog allows to choose the release from the possibilities.");
		
		this.releases = releases;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label releaseLabel = new Label(composite, SWT.None);
		releaseLabel.setText("Release");
		
		releaseCombo = new ComboViewer(composite);
		releaseCombo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		releaseCombo.setContentProvider(new ContentProvider());
		releaseCombo.setLabelProvider(new LabelProvider());
		releaseCombo.setComparator(new ViewerComparator());
		
		releaseCombo.setInput(new Object());
		releaseCombo.getCombo().select(0);
		
		return parent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		release = SelectionUtils.getSelectedElement(releaseCombo.getSelection());
		super.okPressed();
	}
	
	/**
	 * Gets the chosen release.
	 */
	public int getRelease() {
		return release;
	}
	
	/**
	 * Content provider
	 */
	private class ContentProvider implements IStructuredContentProvider {
		
		/**
		 * {@inheritDoc}
		 */
		public Object[] getElements(Object inputElement) {
			return releases.toArray();
		}

		/**
		 * {@inheritDoc}
		 */
		public void dispose() {
			// not required
		}

		/**
		 * {@inheritDoc}
		 */
		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {
			// not required
		}

	}
	
	/**
	 * Label provider
	 */
	private class LabelProvider implements ILabelProvider {

		/**
		 * {@inheritDoc}
		 */
		public Image getImage(Object element) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getText(Object element) {
			Integer release = (Integer) element;
			return release.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		public void addListener(ILabelProviderListener listener) {
			// not required
		}

		/**
		 * {@inheritDoc}
		 */
		public void dispose() {
			// not required
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public void removeListener(ILabelProviderListener listener) {
			// not required
		}
		
	}
}