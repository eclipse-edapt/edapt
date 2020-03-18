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

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * A table viewer to display the properties of a metamodel element using the Ecore default item property source
 *
 * @author markus.herrmannsdoer
 *
 */
public class PropertiesTableViewer extends AutoColumnSizeTableViewer {

	/**
	 * Property label for value column
	 */
	private static final String VALUE_COLUMN_PROPERTY = "Value"; //$NON-NLS-1$

	/**
	 * Property label for name column
	 */
	private static final String NAME_COLUMN_PROPERTY = "Property"; //$NON-NLS-1$

	/**
	 * Input metamodel element
	 */
	private EObject input;

	/**
	 * Adapter factory to access item property source
	 */
	private final AdapterFactory adapterFactory;

	/**
	 * Property source to access the properties
	 */
	private PropertySource propertySource;

	/**
	 * Property descriptors
	 */
	private IPropertyDescriptor[] propertyDescriptors;

	/**
	 * Constructor
	 *
	 * @param parent Parent composite
	 * @param style Viewer style
	 * @param adapterFactory
	 */
	public PropertiesTableViewer(Composite parent, int style, AdapterFactory adapterFactory) {
		super(parent, style | SWT.FULL_SELECTION | SWT.BORDER);

		this.adapterFactory = adapterFactory;

		init();
	}

	/**
	 * Initialize the features of the viewer
	 *
	 */
	private void init() {
		final Table propertiesTable = getTable();

		// table columns
		final TableColumn nameColumn = new TableColumn(propertiesTable, SWT.None);
		nameColumn.setText(NAME_COLUMN_PROPERTY);
		final TableColumn valueColumn = new TableColumn(propertiesTable, SWT.None);
		valueColumn.setText(VALUE_COLUMN_PROPERTY);

		propertiesTable.setHeaderVisible(true);
		propertiesTable.setLinesVisible(true);

		// content provider
		setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				final PropertySource propertySource = (PropertySource) inputElement;
				propertyDescriptors = propertySource.getPropertyDescriptors();
				return propertyDescriptors;
			}

			@Override
			public void dispose() {
				// not required
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// not required
			}

		});

		// label provider
		setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				final IPropertyDescriptor descriptor = (IPropertyDescriptor) element;

				switch (columnIndex) {
				case 0:
					return null;
				case 1: {
					Object value = propertySource.getPropertyValue(descriptor.getId());
					if (value instanceof ItemPropertyDescriptor.PropertyValueWrapper) {
						final ItemPropertyDescriptor.PropertyValueWrapper wrapper = (ItemPropertyDescriptor.PropertyValueWrapper) value;
						value = wrapper.getEditableValue(input);
						if (value == null || value instanceof List && ((List) value).isEmpty()) {
							return null;
						}
					}
					return descriptor.getLabelProvider().getImage(value);
				}
				default:
					return null;
				}
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				final IPropertyDescriptor descriptor = (IPropertyDescriptor) element;

				switch (columnIndex) {
				case 0: {
					return descriptor.getDisplayName();
				}
				case 1: {
					final Object value = propertySource.getPropertyValue(descriptor.getId());
					return descriptor.getLabelProvider().getText(value);
				}
				default:
					return ""; //$NON-NLS-1$
				}
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// not required
			}

			@Override
			public void dispose() {
				// not required
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// not required
			}

		});

		// sorter
		setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				final IPropertyDescriptor descriptor1 = (IPropertyDescriptor) e1;
				final IPropertyDescriptor descriptor2 = (IPropertyDescriptor) e2;

				return descriptor1.getDisplayName().compareTo(descriptor2.getDisplayName());
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Object firstElement = structuredSelection.getFirstElement();
			if (structuredSelection.size() == 1 && firstElement instanceof EStructuralFeature) {
				final EStructuralFeature feature = (EStructuralFeature) firstElement;
				for (final IPropertyDescriptor propertyDescriptor : propertyDescriptors) {
					if (propertyDescriptor.getId().equals(feature.getName())) {
						super.setSelection(new StructuredSelection(propertyDescriptor), true);
						return;
					}
				}
			}
		}
		super.setSelection(selection, reveal);
	}

	/**
	 * Set the metamodel model element as input
	 * (This method should be used instead of setInput)
	 *
	 * @param input Input metamodel element
	 */
	public void setElement(EObject input) {
		this.input = input;
		if (input == null) {
			setInput(null);
		} else {
			final IItemPropertySource itemPropertySource = (IItemPropertySource) adapterFactory.adapt(input,
				IItemPropertySource.class);
			propertySource = new PropertySource(input, itemPropertySource);
			setInput(propertySource);
		}
	}
}
