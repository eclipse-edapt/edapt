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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * A tree viewer to display a metamodel using the Ecore default item providers
 *
 * @author markus.herrmannsdoer
 *
 */
public class StructureTreeViewer extends TreeViewer {

	/**
	 * Constructor
	 *
	 * @param parent Parent composite
	 * @param style Viewer style
	 * @param adapterFactory
	 */
	public StructureTreeViewer(Composite parent, int style, AdapterFactory adapterFactory) {
		super(parent, style | SWT.BORDER | SWT.MULTI);

		setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		final AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
		setLabelProvider(labelProvider);

		setSorter(new ViewerSorter() {
			@Override
			public int category(Object element) {
				if (element instanceof EObject) {
					final EObject eObject = (EObject) element;
					if (eObject.eContainer() != null) {
						final EReference containmentFeature = eObject.eContainmentFeature();
						final EList<EStructuralFeature> features = eObject.eContainer().eClass()
							.getEAllStructuralFeatures();
						final int indexOf = features.indexOf(containmentFeature);
						return indexOf;
					}
				}
				return 0;
			}
		});

		addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
				Object element) {
				if (element instanceof EGenericType) {
					return false;
				}
				return true;
			}
		});
	}
}
