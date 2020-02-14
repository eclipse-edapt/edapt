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
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

/**
 * A sash to display both the structure and the properties of a metamodel
 *
 * @author markus.herrmannsdoer
 *
 */
public class ModelSash extends SashForm {

	/**
	 * Viewer to display the structure of the metamodel
	 */
	private StructureTreeViewer structureViewer;

	/**
	 * Viewer to display the properties of a metamodel element seletected in the structure viewer
	 */
	private PropertiesTableViewer propertiesViewer;

	/**
	 * Adapter factory
	 */
	private final AdapterFactory adapterFactory;

	/**
	 * Constructor
	 *
	 * @param parent Parent composite
	 * @param style Sash style
	 */
	public ModelSash(Composite parent, int style) {
		super(parent, style | SWT.VERTICAL);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		this.adapterFactory = adapterFactory;

		init();
	}

	/**
	 * Constructor
	 *
	 * @param parent Parent composite
	 * @param style Sash style
	 */
	public ModelSash(Composite parent, int style, AdapterFactory adapterFactory) {
		super(parent, style | SWT.VERTICAL);

		this.adapterFactory = adapterFactory;

		init();
	}

	/**
	 * Initialize the contents of the sash
	 *
	 */
	private void init() {

		structureViewer = new StructureTreeViewer(this, SWT.None, adapterFactory);
		propertiesViewer = new PropertiesTableViewer(this, SWT.None, adapterFactory);

		structureViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
				if (!structuredSelection.isEmpty() && structuredSelection.getFirstElement() instanceof EObject) {
					final EObject before = (EObject) structuredSelection.getFirstElement();
					propertiesViewer.setElement(before);
				} else {
					propertiesViewer.setInput(null);
				}
			}

		});

		propertiesViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				final TableItem item = (TableItem) e.item;
				final PropertyDescriptor descriptor = (PropertyDescriptor) item.getData();
				final PropertySource propertySource = (PropertySource) propertiesViewer.getInput();
				Object value = propertySource.getPropertyValue(descriptor.getId());
				if (value instanceof ItemPropertyDescriptor.PropertyValueWrapper) {
					final ItemPropertyDescriptor.PropertyValueWrapper wrapper = (ItemPropertyDescriptor.PropertyValueWrapper) value;
					value = wrapper.getEditableValue(value);
					propertyValuesSelected(value);
				}
			}

		});

		setWeights(new int[] { 2, 1 });
	}

	/**
	 * Code to perform when property values are selected
	 * (can be overwritten by sub classes)
	 *
	 * @param value
	 */
	protected void propertyValuesSelected(Object value) {
		if (value instanceof EObject) {
			structureViewer.setSelection(new StructuredSelection(value), true);
		} else if (value instanceof List) {
			structureViewer.setSelection(new StructuredSelection((List) value), true);
		}
	}

	/**
	 * Getter for structure viewer
	 *
	 * @return Structure viewer
	 */
	public StructureTreeViewer getStructureViewer() {
		return structureViewer;
	}

	/**
	 * Getter for properties viewer
	 *
	 * @return Properties viewer
	 */
	public PropertiesTableViewer getPropertiesViewer() {
		return propertiesViewer;
	}
}
