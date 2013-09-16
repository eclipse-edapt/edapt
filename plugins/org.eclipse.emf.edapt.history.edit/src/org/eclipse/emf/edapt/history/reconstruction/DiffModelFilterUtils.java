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
package org.eclipse.emf.edapt.history.reconstruction;

import java.util.ArrayList;


// CB Migrate
//import org.eclipse.emf.compare.diff.metamodel.DiffElement;
//import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
//import org.eclipse.emf.compare.diff.metamodel.DiffModel;
//import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Helper methods for filtering difference models.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class DiffModelFilterUtils {

	/** Constructor. */
	private DiffModelFilterUtils() {
		// hidden, since this class is not expected to be instantiated.
	}

	/** Filter a diff model. */
	// CB Migrate
//	public static DiffModel filter(DiffModel model, IDiffModelFilter filter) {
//		doFilter(model, filter);
//		return model;
//	}

	/** Filter a diff model. */
	
	// CB Migrate
//	public static DiffResourceSet filter(DiffResourceSet model,
//			IDiffModelFilter filter) {
//		doFilter(model, filter);
//		return model;
//	}

	/** Perform filtering on an element in a diff model. */
	
	// CB Migrate
//	private static void doFilter(EObject element, IDiffModelFilter filter) {
//		for (EObject child : new ArrayList<EObject>(element.eContents())) {
//			if (child instanceof DiffElement
//					&& !filter.select((DiffElement) child)) {
//				EcoreUtil.delete(child);
//				continue;
//			}
//			doFilter(child, filter);
//			if (child instanceof DiffGroup) {
//				if (child.eContents().isEmpty()) {
//					EcoreUtil.delete(child);
//				}
//			}
//		}
//	}

	/**
	 * Create a filter from a number of filters that only selects an element if
	 * it is selected by all filters.
	 */
	public static <E> IDiffModelFilter and(final IDiffModelFilter... filters) {
		return new IDiffModelFilter() {
			
			// CB Migrate
//			public boolean select(DiffElement element) {
//				for (IDiffModelFilter filter : filters) {
//					if (!filter.select(element)) {
//						return false;
//					}
//				}
//				return true;
//			}
		};
	}
}
