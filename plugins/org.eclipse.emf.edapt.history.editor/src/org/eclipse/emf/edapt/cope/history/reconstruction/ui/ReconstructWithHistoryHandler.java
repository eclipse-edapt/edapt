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
package org.eclipse.emf.edapt.cope.history.reconstruction.ui;

import org.eclipse.emf.edapt.cope.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.cope.history.reconstruction.HistoryReconstructor;

/**
 * Action to reconstruct a metamodel together with its history until a version
 * or right before a change.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReconstructWithHistoryHandler extends ReconstructHandler {
	
	/** {@inheritDoc} */
	@Override
	protected void customizeReconstructor(EcoreForwardReconstructor reconstructor) {
		reconstructor.addReconstructor(new HistoryReconstructor());
	}
}
