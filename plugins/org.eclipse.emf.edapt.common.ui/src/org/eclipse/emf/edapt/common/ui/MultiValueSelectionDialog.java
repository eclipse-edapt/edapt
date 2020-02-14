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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

/**
 * Dialog to select multiple values.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MultiValueSelectionDialog extends SelectionStatusDialog {

	/**
	 * The label provider
	 */
	protected final AdapterFactoryLabelProvider labelProvider;

	/**
	 * Root elements of area in which values can be found
	 */
	private final Collection valueArea;

	/**
	 * Current value that is selected
	 */
	private final List values;

	/**
	 * Composite for value selection
	 */
	private ValueSelectionComposite composite;

	/**
	 * Validator to determine possible values.
	 */
	private final IValueValidator validator;

	/**
	 * The viewer displaying the current values
	 */
	private TableViewer valuesViewer;

	/**
	 * Constructor.
	 */
	@SuppressWarnings("unchecked")
	public MultiValueSelectionDialog(Shell parent, Image image, String title,
		List values, Collection valueArea,
		AdapterFactoryLabelProvider labelProvider, IValueValidator validator) {
		super(parent);

		setImage(image);
		setTitle(title);

		this.values = new ArrayList(values);
		this.valueArea = valueArea;
		this.labelProvider = labelProvider;
		this.validator = validator;
	}

	/** {@inheritDoc} */
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		getShell().setSize(650, 500);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite contents = (Composite) super.createDialogArea(parent);
		final GridLayout layout = (GridLayout) contents.getLayout();
		layout.numColumns = 3;

		createValueSelectionComposite(contents);
		createButtons(contents);
		createValueList(contents);

		return contents;
	}

	/**
	 * Create the composite to select values.
	 */
	private void createValueSelectionComposite(Composite contents) {
		composite = new ValueSelectionComposite(
			contents, labelProvider, values, true, valueArea, validator);

		composite.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (composite.validSelection()) {
					addSelectedValues();
				}
			}
		});
	}

	/**
	 * Create the table to display the values.
	 */
	private void createValueList(Composite contents) {
		valuesViewer = new TableViewer(contents);
		final GridData data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = 200;
		valuesViewer.getTable().setLayoutData(
			data);

		valuesViewer.setLabelProvider(labelProvider);
		valuesViewer.setContentProvider(ArrayContentProvider.getInstance());
		valuesViewer.setInput(values);

		valuesViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				removeSelectedValues();
			}
		});
	}

	/**
	 * Create the buttons to add and remove values.
	 */
	private void createButtons(Composite contents) {
		final Composite buttonComposite = new Composite(contents, SWT.None);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		buttonComposite.setLayout(layout);
		final GridData data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = 50;
		buttonComposite.setLayoutData(data);

		final Button addButton = new Button(buttonComposite, SWT.None);
		addButton.setText("add"); //$NON-NLS-1$
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addSelectedValues();
			}
		});

		final Button removeButton = new Button(buttonComposite, SWT.None);
		removeButton.setText("remove"); //$NON-NLS-1$
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelectedValues();
			}
		});

		new Label(buttonComposite, SWT.None);

		final Button upButton = new Button(buttonComposite, SWT.None);
		upButton.setText("up"); //$NON-NLS-1$
		upButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				upSelectedValues();
			}
		});

		final Button downButton = new Button(buttonComposite, SWT.None);
		downButton.setText("down"); //$NON-NLS-1$
		downButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				downSelectedValues();
			}
		});

		new Label(buttonComposite, SWT.None);

		final Button clearButton = new Button(buttonComposite, SWT.None);
		clearButton.setText("clear"); //$NON-NLS-1$
		clearButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		clearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSelectedValues();
			}
		});
	}

	/**
	 * Clear the currently selected values.
	 */
	protected void clearSelectedValues() {
		values.clear();
		valuesViewer.refresh();
	}

	/**
	 * Move the currently selected values down.
	 */
	@SuppressWarnings("unchecked")
	protected void downSelectedValues() {
		final List<Object> selectedValues = SelectionUtils
			.getSelectedElements(valuesViewer.getSelection(), Object.class);
		sortSelectedValues(selectedValues);
		Collections.reverse(selectedValues);
		for (final Object element : selectedValues) {
			final int index = values.indexOf(element);
			if (index < values.size() - 1
				&& !selectedValues.contains(values.get(index + 1))) {
				values.remove(element);
				values.add(index + 1, element);
			}
		}
		valuesViewer.refresh();
	}

	/**
	 * Move the currently selected values up.
	 */
	@SuppressWarnings("unchecked")
	protected void upSelectedValues() {
		final List<Object> selectedValues = SelectionUtils
			.getSelectedElements(valuesViewer.getSelection(), Object.class);
		sortSelectedValues(selectedValues);
		for (final Object element : selectedValues) {
			final int index = values.indexOf(element);
			if (index > 0 && !selectedValues.contains(values.get(index - 1))) {
				values.remove(element);
				values.add(index - 1, element);
			}
		}
		valuesViewer.refresh();
	}

	/**
	 * Sort the currently selected values according to their order in the list
	 * of values.
	 */
	private void sortSelectedValues(List<Object> selectedValues) {
		Collections.sort(selectedValues, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				return values.indexOf(o1) - values.indexOf(o2);
			}
		});
	}

	/**
	 * Remove the currently selected values.
	 */
	@SuppressWarnings("unchecked")
	protected void removeSelectedValues() {
		final List<Object> selectedValues = SelectionUtils
			.getSelectedElements(valuesViewer.getSelection(), Object.class);
		values.removeAll(selectedValues);
		valuesViewer.refresh();
	}

	/**
	 * Add the currently selected values.
	 */
	@SuppressWarnings("unchecked")
	protected void addSelectedValues() {
		final List<Object> selectedValues = composite.getSelectedElements();
		for (final Object element : selectedValues) {
			if (!values.contains(element)) {
				values.add(element);
			}
		}
		valuesViewer.refresh();
	}

	/** {@inheritDoc} */
	@Override
	protected void computeResult() {
		setResult(values);
	}

}
