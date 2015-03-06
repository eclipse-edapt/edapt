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
package org.eclipse.emf.edapt.common.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Dialog to show a model
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ModelDialog extends ResizeableDialogBase {

	/**
	 * Root element of the model
	 */
	private final Object element;

	/**
	 * Model sash
	 */
	protected ModelSash sash;

	/**
	 * Constructor
	 *
	 * @param element
	 */
	public ModelDialog(Object element, String title, String message) {
		super(new Point(800, 600), title, message);

		this.element = element;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		final Composite composite = new Composite(parent, SWT.None);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);

		sash = new ModelSash(composite, SWT.None);
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));

		sash.getStructureViewer().setInput(element);

		return parent;
	}
}
