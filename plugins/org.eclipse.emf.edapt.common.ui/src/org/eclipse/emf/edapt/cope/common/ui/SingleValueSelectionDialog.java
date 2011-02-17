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

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

/**
 * Dialog to select a single value.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: B5190DE11ADF34F7F6D183B7F9E33109
 */
public class SingleValueSelectionDialog extends SelectionStatusDialog {

	/**
	 * The label provider
	 */
	protected final AdapterFactoryLabelProvider labelProvider;

	/**
	 * Root elements of area in which values can be found
	 */
	@SuppressWarnings("unchecked")
	private final Collection valueArea;

	/**
	 * Current value that is selected
	 */
	private final Object value;

	/**
	 * Composite for value selection
	 */
	private ValueSelectionComposite composite;

	/**
	 * Validator to determine possible values.
	 */
	private final IValueValidator validator;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public SingleValueSelectionDialog(Shell parent, Image image, String title,
			Object value, Collection valueArea,
			AdapterFactoryLabelProvider labelProvider, IValueValidator validator) {
		super(parent);

		setImage(image);
		setTitle(title);

		this.value = value;
		this.valueArea = valueArea;
		this.labelProvider = labelProvider;
		this.validator = validator;
	}

	/** {@inheritDoc} */
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		getShell().setSize(400, 500);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite contents = (Composite) super.createDialogArea(parent);

		composite = new ValueSelectionComposite(contents, labelProvider, value,
				false, valueArea, validator);

		composite.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				validateSelection();
			}

		});

		composite.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				if (composite.validSelection()) {
					okPressed();
				}
			}

		});

		return contents;
	}

	/**
	 * Validate the selection
	 */
	private void validateSelection() {
		if (composite.validSelection()) {
			getOkButton().setEnabled(true);
		} else {
			getOkButton().setEnabled(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create() {
		super.create();

		validateSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void computeResult() {
		List selectedElements = composite.getSelectedElements();
		setResult(selectedElements);
	}
}
