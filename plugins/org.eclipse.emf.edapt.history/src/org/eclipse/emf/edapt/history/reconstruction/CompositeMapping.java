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

import org.eclipse.emf.ecore.EObject;

/**
 * Composition of two mappings into a transitive mapping
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CompositeMapping extends MappingBase {

	/**
	 * Mapping to source metamodel version
	 */
	private final MappingBase sourceMapping;

	/**
	 * Mapping to target metamodel version
	 */
	private final MappingBase targetMapping;

	/**
	 * Constructor
	 */
	public CompositeMapping(MappingBase sourceMapping, MappingBase targetMapping) {
		this.sourceMapping = sourceMapping;
		this.targetMapping = targetMapping;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject getSource(EObject target) {
		final EObject element = targetMapping.getSource(target);
		if (element == null) {
			return null;
		}
		final EObject source = sourceMapping.getTarget(element);
		return source;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject getTarget(EObject source) {
		final EObject element = sourceMapping.getSource(source);
		if (element == null) {
			return null;
		}
		final EObject target = targetMapping.getTarget(element);
		return target;
	}
}
