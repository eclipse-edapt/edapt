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
package org.eclipse.emf.edapt.history.provider.util;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * Helper methods for the history metamodels
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class HistoryUIUtils {

	/**
	 * Adapter factory
	 */
	private static AdapterFactory adapterFactory = new EcoreItemProviderAdapterFactory();

	/**
	 * Get the label surrounded by braces
	 *
	 * @param element
	 * @return Brace label
	 */
	public static String getBracedLabel(Object element) {
		if (element == null) {
			return "null"; //$NON-NLS-1$
		}
		return "{" + getLabel(element) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Get the label for an element
	 *
	 * @param element
	 * @return Label
	 */
	public static String getLabel(Object element) {
		if (element == null) {
			return "null"; //$NON-NLS-1$
		}
		else if (element instanceof ENamedElement) {
			return ((ENamedElement) element).getName();
		}
		else if (element instanceof String) {
			return "\"" + element + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}
		else {
			final IItemLabelProvider labelProvider = (IItemLabelProvider) adapterFactory.adapt(element,
				IItemLabelProvider.class);
			if (labelProvider == null) {
				return element.toString();
			}
			return labelProvider.getText(element);
		}
	}
}
