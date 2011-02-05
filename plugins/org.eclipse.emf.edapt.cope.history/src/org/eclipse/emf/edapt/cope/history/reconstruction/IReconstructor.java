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
package org.eclipse.emf.edapt.cope.history.reconstruction;

import org.eclipse.emf.edapt.cope.common.MetamodelExtent;
import org.eclipse.emf.edapt.cope.history.Change;
import org.eclipse.emf.edapt.cope.history.History;
import org.eclipse.emf.edapt.cope.history.Release;


/**
 * Reconstructor to follow the history of a metamodel
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public interface IReconstructor {
	
	/**
	 * Initialization will be called at the very beginning of reconstruction
	 * 
	 * @param mapping Mapping from old to new elements
	 * @param extent Extent
	 */
	public abstract void init(Mapping mapping, MetamodelExtent extent);
	
	/**
	 * Reconstruction of the history starts
	 * 
	 * @param originalHistory Original history
	 */
	public abstract void startHistory(History originalHistory);

	/**
	 * Reconstruction of a release starts
	 * 
	 * @param originalRelease Original release
	 */
	public abstract void startRelease(Release originalRelease);

	/**
	 * Reconstruction of a change starts
	 * 
	 * @param originalChange Original change 
	 */
	public abstract void startChange(Change originalChange);
	
	/**
	 * Reconstruction of a change is finished
	 * 
	 * @param originalChange Original release
	 */
	public abstract void endChange(Change originalChange);
	
	/**
	 * Reconstruction of a release is finished
	 * 
	 * @param originalRelease
	 */
	public abstract void endRelease(Release originalRelease);
	
	/**
	 * Reconstruction of the history is finished
	 * 
	 * @param originalHistory Original history
	 */
	public abstract void endHistory(History originalHistory);

}