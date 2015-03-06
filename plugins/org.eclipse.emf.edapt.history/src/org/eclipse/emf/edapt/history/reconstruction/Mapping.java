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
package org.eclipse.emf.edapt.history.reconstruction;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.internal.common.ReversableMap;
import org.eclipse.emf.edapt.internal.common.TwoWayIdentityHashMap;

/**
 * Mapping between elements of two metamodel versions
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class Mapping extends MappingBase {

	/**
	 * Bidirectional mapping
	 */
	private final ReversableMap<EObject, EObject> mapping;

	/**
	 * Constructor
	 *
	 */
	public Mapping() {
		mapping = new TwoWayIdentityHashMap<EObject, EObject>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject getTarget(EObject source) {
		return mapping.get(source);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject getSource(EObject target) {
		return mapping.reverseGet(target);
	}

	/**
	 * Add bidirectional relationship between source and target element
	 */
	public void map(EObject source, EObject target) {
		if (getTarget(source) != null && getTarget(source) != target) {
			throw new IllegalArgumentException("Source element already mapped to different target element"); //$NON-NLS-1$
		}
		if (getSource(target) != null && getSource(target) != source) {
			throw new IllegalArgumentException("Target element already mapped to different source element"); //$NON-NLS-1$
		}
		mapping.put(source, target);
	}

	/**
	 * Remove bidirectional relationship between source and target element
	 */
	public void unmap(EObject source) {
		mapping.remove(source);
	}

	/**
	 * Decide whether there is a bidirectional relationship between source and
	 * target element
	 *
	 * @return true whether there is a bidirectional relationship, false
	 *         otherwise
	 */
	public boolean isMapped(EObject source, EObject target) {
		return getTarget(source) == target && getSource(target) == source;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		for (final EObject key : mapping.keySet()) {
			final EObject target = mapping.get(key);
			sb.append("map " + key + " to:" + target + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return sb.toString();
	}
}
