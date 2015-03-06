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

/**
 * Base class for mappings between model elements
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class MappingBase {

	/**
	 * Resolver that resolves in the target direction of the mapping
	 */
	private final ResolverBase targetResolver = new ResolverBase() {

		@Override
		protected EObject doResolve(EObject source) {
			return getTarget(source);
		}

	};

	/**
	 * Resolver that resolves in the source direction of the mapping
	 */
	private final ResolverBase sourceResolver = new ResolverBase() {

		@Override
		protected EObject doResolve(EObject source) {
			return getSource(source);
		}

	};

	/**
	 * Get the source element for a target element (to be implemented by
	 * subclasses)
	 */
	public abstract EObject getSource(EObject target);

	/**
	 * Get the target element for a source element (to be implemented by
	 * subclasses)
	 */
	public abstract EObject getTarget(EObject source);

	/**
	 * Resolve a target element(s) for source element(s) (Return source
	 * element(s) in case it (they) cannot be resolved)
	 */
	public <V> V resolveTarget(V source) {
		return targetResolver.resolve(source);
	}

	/**
	 * Copy an element and automatically resolve links to the metamodel from
	 * source to target.
	 */
	public EObject copyResolveTarget(EObject source, boolean deepCopy) {
		return targetResolver.copyResolve(source, deepCopy);
	}

	/**
	 * Copy an element and automatically resolve links to the metamodel from
	 * source to target.
	 */
	public EObject copyResolveTarget(EObject source) {
		return copyResolveTarget(source, true);
	}

	/**
	 * Resolve a source element(s) for target element(s) (Return target
	 * element(s) in case it (they) cannot be resolved)
	 */
	public <V> V resolveSource(V target) {
		return sourceResolver.resolve(target);
	}

	/**
	 * Copy an element and automatically resolve links to the metamodel from
	 * target to source.
	 */
	public EObject copyResolveSource(EObject target, boolean deepCopy) {
		return sourceResolver.copyResolve(target, deepCopy);
	}

	/**
	 * Copy an element and automatically resolve links to the metamodel from
	 * target to source.
	 */
	public EObject copyResolveSource(EObject target) {
		return copyResolveSource(target, true);
	}
}
