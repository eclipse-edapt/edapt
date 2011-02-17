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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.InitializerChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.ValueChange;


/**
 * Reconstructor visiting the history in forward direction
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class ForwardReconstructorBase extends CompositeReconstructorBase {

	/**
	 * Target history element
	 */
	private EObject target;

	/**
	 * Whether reconstruction ends before or after target history element
	 */
	private boolean before;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doReconstruct(EObject target, History originalHistory, boolean before) {
		init(mapping = new Mapping(), extent = new MetamodelExtent());
		this.target = target;
		this.before = before;
		
		startHistory(originalHistory);
		
		try {
			for(Release release : originalHistory.getReleases()) {
				doReconstruct(release);
			}
		}
		catch(FinishedException e) {
			// reconstruction is finished
		}
		
		endHistory(originalHistory);
	}
	
	/**
	 * Perform reconstruction of a release
	 */
	private void doReconstruct(Release originalRelease) {
		
		if(before && originalRelease == target) {
			throw new FinishedException();
		}
		
		startRelease(originalRelease);
		
		for(Change change : originalRelease.getChanges()) {
			doReconstruct(change);
		}
		
		endRelease(originalRelease);
		
		if(!before && originalRelease == target) {
			throw new FinishedException();
		}
	}
	
	/**
	 * Perform reconstruction of a change
	 */
	private void doReconstruct(Change originalChange) {
		
		if(before && originalChange == target) {
			throw new FinishedException();
		}
		
		startChange(originalChange);
		
		if(originalChange instanceof CompositeChange) {
			CompositeChange compositeChange = (CompositeChange) originalChange;
			for(Change change : compositeChange.getChanges()) {
				doReconstruct(change);
			}
		}
		else if(originalChange instanceof MigrationChange) {
			MigrationChange migrationChange = (MigrationChange) originalChange;
			for(Change change : migrationChange.getChanges()) {
				doReconstruct(change);
			}
		}
		else if(originalChange instanceof InitializerChange) {
			InitializerChange createChild = (InitializerChange) originalChange;
			for(ValueChange change : createChild.getChanges()) {
				doReconstruct(change);
			}
		}
		
		endChange(originalChange);
		
		if(!before && originalChange == target) {
			throw new FinishedException();
		}
	}
}
