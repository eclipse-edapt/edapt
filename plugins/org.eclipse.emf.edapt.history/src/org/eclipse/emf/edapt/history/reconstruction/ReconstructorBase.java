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

import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.Release;

/**
 * Empty base class for reconstructors
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class ReconstructorBase implements IReconstructor {

	/**
	 * {@inheritDoc}
	 */
	// @Override
	@Override
	public void endChange(Change originalChange) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 */
	// @Override
	@Override
	public void endHistory(History originalHistory) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 */
	// @Override
	@Override
	public void endRelease(Release originalRelease) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 */
	// @Override
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 */
	// @Override
	@Override
	public void startChange(Change originalChange) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 */
	// @Override
	@Override
	public void startHistory(History originalHistory) {
		// empty implementation
	}

	/**
	 * {@inheritDoc}
	 */
	// @Override
	@Override
	public void startRelease(Release originalRelease) {
		// empty implementation
	}

}
