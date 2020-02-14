/*******************************************************************************
 * Copyright (c) 2007-2017 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.common;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Generic interface for a processor which is supposed to work with {@link ResourceSet ResourceSets}.
 *
 * @since 1.3
 *
 */
public interface IResourceSetProcessor {

	/**
	 * @param resourceSet the {@link ResourceSet} to process
	 */
	void process(ResourceSet resourceSet);

}
