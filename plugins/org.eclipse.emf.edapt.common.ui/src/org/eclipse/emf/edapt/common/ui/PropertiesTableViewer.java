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
	private static final String VALUE_COLUMN_PROPERTY = "Value";
	
	/**
	 * Property label for name column
	 */
	private static final String NAME_COLUMN_PROPERTY = "Property";
	
	/**
	 * Input metamodel element
	 */
	private EObject input;
	
	/**
	 * Adapter factory to access item property source
	 */
	private AdapterFactory adapterFactory;
	
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
		Table propertiesTable = getTable();
		
		// table columns
		TableColumn nameColumn = new TableColumn(propertiesTable, SWT.None);
		nameColumn.setText(NAME_COLUMN_PROPERTY);
		TableColumn valueColumn = new TableColumn(propertiesTable, SWT.None);
		valueColumn.setText(VALUE_COLUMN_PROPERTY);
		
		propertiesTable.setHeaderVisible(true);
		propertiesTable.setLinesVisible(true);
		
		// content provider
		setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				PropertySource propertySource = (PropertySource) inputElement;
				propertyDescriptors = propertySource.getPropertyDescriptors();
				return propertyDescriptors;
			}

			public void dispose() {
				// not required
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// not required
			}
			
		});
		
		// label provider
		setLabelProvider(new ITableLabelProvider() {
			
			@SuppressWarnings("unchecked")
			public Image getColumnImage(Object element, int columnIndex) {
				IPropertyDescriptor descriptor = (IPropertyDescriptor) element;

				switch(columnIndex) {
				case 0: return null;
				case 1: {
					Object value = propertySource.getPropertyValue(descriptor.getId());
					if(value instanceof ItemPropertyDescriptor.PropertyValueWrapper) {
						ItemPropertyDescriptor.PropertyValueWrapper wrapper = (ItemPropertyDescriptor.PropertyValueWrapper) value;
						value = wrapper.getEditableValue(input);
						if(value == null || (value instanceof List && ((List) value).isEmpty())) {
							return null;
						}
					}
					return descriptor.getLabelProvider().getImage(value);
				}
				default: return null;
				}
			}

			public String getColumnText(Object element, int columnIndex) {
				IPropertyDescriptor descriptor = (IPropertyDescriptor) element;
				
				switch(columnIndex) {
				case 0: {
					return descriptor.getDisplayName();
				}
				case 1: {
					Object value = propertySource.getPropertyValue(descriptor.getId());
					return descriptor.getLabelProvider().getText(value);
				}
				default: return "";
				}
			}

			public void addListener(ILabelProviderListener listener) {
				// not required
			}

			public void dispose() {
				// not required
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
				// not required
			}
			
		});
		
		// sorter
		setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				IPropertyDescriptor descriptor1 = (IPropertyDescriptor) e1;
				IPropertyDescriptor descriptor2 = (IPropertyDescriptor) e2;
				
				return descriptor1.getDisplayName().compareTo(descriptor2.getDisplayName());
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			if(structuredSelection.size() == 1 && firstElement instanceof EStructuralFeature) {
				EStructuralFeature feature = (EStructuralFeature) firstElement;
				for(IPropertyDescriptor propertyDescriptor : propertyDescriptors) {
					if(propertyDescriptor.getId().equals(feature.getName())) {
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
		if(input == null) {
			setInput(null);
		}
		else {
			IItemPropertySource itemPropertySource = (IItemPropertySource) adapterFactory.adapt(input, IItemPropertySource.class);
			propertySource = new PropertySource(input, itemPropertySource);
			setInput(propertySource);
		}
	}
}
