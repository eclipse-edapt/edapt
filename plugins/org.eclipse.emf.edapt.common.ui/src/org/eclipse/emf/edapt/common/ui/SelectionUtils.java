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
 * Johannes Faltermeier - Add explicit type parameters
 *******************************************************************************/
package org.eclipse.emf.edapt.common.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Helper class for selection.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: CC8B5BB77A65A46F0423226613B4F8D3
 */
public final class SelectionUtils {

	/**
	 * Private constructor
	 */
	private SelectionUtils() {
		// nothing to do
	}

	/**
	 * Get the selected element of type V.
	 */
	public static <V> V getSelectedElement(ISelection selection, Class<V> type) {
		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (!structuredSelection.isEmpty()) {
				final Object firstElement = structuredSelection.getFirstElement();
				if (!type.isInstance(firstElement)) {
					return null;
				}
				return type.cast(firstElement);
			}
		}
		return null;
	}

	/**
	 * Get a list of selected elements of type V.
	 */
	public static <V> List<V> getSelectedElements(ISelection selection, Class<V> type) {
		final List<V> elements = new ArrayList<V>();
		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			for (final Iterator i = structuredSelection.iterator(); i.hasNext();) {
				final Object next = i.next();
				if (!type.isInstance(next)) {
					continue;
				}
				elements.add(type.cast(next));
			}
		}
		return elements;
	}

}
