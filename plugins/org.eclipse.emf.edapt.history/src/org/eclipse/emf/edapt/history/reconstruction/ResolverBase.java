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
package org.eclipse.emf.edapt.history.reconstruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * A resolver is able to perform a deep copy of history elements, but it
 * resolves references to metamodel elements
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class ResolverBase {

	/**
	 * Do the resolution (to be implemented by subclasses)
	 */
	protected abstract EObject doResolve(EObject source);

	/**
	 * Perform a copy of a model element, but resolve all references to
	 * metamodel elements
	 */
	@SuppressWarnings("unchecked")
	public EObject copyResolve(EObject element, boolean deepCopy) {
		if (element == null) {
			return null;
		}
		final EClass c = element.eClass();
		if (c.getEPackage() != EcorePackage.eINSTANCE) {
			final EObject copy = c.getEPackage().getEFactoryInstance().create(c);
			for (final Iterator features = c.getEAllStructuralFeatures().iterator(); features
				.hasNext();) {
				final EStructuralFeature feature = (EStructuralFeature) features
					.next();

				if (!feature.isChangeable() || feature.isTransient()) {
					continue;
				}

				if (feature instanceof EAttribute) {
					final EAttribute attribute = (EAttribute) feature;
					if (attribute.isMany()) {
						((List) copy.eGet(attribute)).addAll((List) element
							.eGet(attribute));
					} else {
						copy.eSet(attribute, element.eGet(attribute));
					}
				} else {
					final EReference reference = (EReference) feature;
					if (reference.isContainment()) {
						if (deepCopy) {
							if (reference.isMany()) {
								for (final Iterator i = ((List) element
									.eGet(reference)).iterator(); i
										.hasNext();) {
									final EObject child = (EObject) i.next();
									((List) copy.eGet(reference))
										.add(copyResolve(child, deepCopy));
								}
							} else {
								copy.eSet(reference, copyResolve(
									(EObject) element.eGet(reference),
									deepCopy));
							}
						}
					} else {
						if (reference.getEOpposite() != null
							&& reference.getEOpposite().isContainment()) {
							continue;
						}
						if (reference.isMany()) {
							((List) copy.eGet(reference))
								.addAll(resolve((List) element
									.eGet(reference)));
						} else {
							copy.eSet(reference, resolve(element
								.eGet(reference)));
						}
					}
				}
			}
			return copy;
		}
		return resolve(element);
	}

	/**
	 * Resolve a metamodel reference (elements of primitive type will be mapped
	 * to themselves)
	 */
	@SuppressWarnings("unchecked")
	public <V> V resolve(V source) {
		if (source instanceof EObject) {
			return (V) resolve((EObject) source);
		} else if (source instanceof List) {
			return (V) resolve((List) source);
		} else {
			return source;
		}
	}

	/**
	 * Resolve a list of metamodel references
	 */
	@SuppressWarnings("unchecked")
	private List resolve(List sourceList) {
		final List targetList = new ArrayList();
		for (final Iterator i = sourceList.iterator(); i.hasNext();) {
			final Object source = i.next();
			final Object target = resolve(source);
			targetList.add(target);
		}
		return targetList;
	}

	/**
	 * Resolve a metamodel reference (non-resolvable elements will be mapped to
	 * themselves)
	 */
	private EObject resolve(EObject source) {
		final EObject target = doResolve(source);
		if (target != null) {
			return target;
		}
		return source;
	}
}
