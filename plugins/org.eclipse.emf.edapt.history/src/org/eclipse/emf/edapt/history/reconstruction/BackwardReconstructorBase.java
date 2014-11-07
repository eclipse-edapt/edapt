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

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.InitializerChange;
import org.eclipse.emf.edapt.spi.history.MigrateableChange;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.history.ValueChange;


/**
 * Reconstructor visiting the history in backward direction
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class BackwardReconstructorBase extends CompositeReconstructorBase {

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
		
		init(originalHistory);		
		init(mapping, extent);
		
		this.target = target;
		this.before = before;
		
		endHistory(originalHistory);
		
		try {
			List<Release> releases = originalHistory.getReleases();
			for(int i = releases.size()-1; i >= 0; i--) {
				Release release = releases.get(i);
				doReconstruct(release);
			}
		}
		catch(FinishedException e) {
			// reconstruction is finished
		}
		
		startHistory(originalHistory);
	}

	/**
	 * Initialize mapping and extent from the history
	 */
	private void init(History originalHistory) {
		mapping = new Mapping();
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		Collection<EPackage> rootPackages = copier.copyAll(originalHistory.getRootPackages());
		copier.copyReferences();
		for(Entry<EObject, EObject> entry : copier.entrySet()) {
			mapping.map(entry.getKey(), entry.getValue());
		}
		extent = new MetamodelExtent(rootPackages);
	}
	
	/**
	 * Perform reconstruction of a release
	 */
	private void doReconstruct(Release originalRelease) {
		
		if(!before && originalRelease == target) {
			throw new FinishedException();
		}
		
		endRelease(originalRelease);
		
		List<Change> changes = originalRelease.getChanges();
		for(int i = changes.size()-1; i >= 0; i--) {
			Change change = changes.get(i);
			doReconstruct(change);
		}
		
		startRelease(originalRelease);
		
		if(before && originalRelease == target) {
			throw new FinishedException();
		}
	}
	
	/**
	 * Perform reconstruction of a change
	 */
	private void doReconstruct(Change originalChange) {
		
		if(!before && originalChange == target) {
			throw new FinishedException();
		}
		
		endChange(originalChange);
		
		if(originalChange instanceof CompositeChange) {
			CompositeChange compositeChange = (CompositeChange) originalChange;
			List<PrimitiveChange> changes = compositeChange.getChanges();
			for(int i = changes.size()-1; i >= 0; i--) {
				Change change = changes.get(i);
				doReconstruct(change);
			}
		}
		else if(originalChange instanceof MigrationChange) {
			MigrationChange migrationChange = (MigrationChange) originalChange;
			List<MigrateableChange> changes = migrationChange.getChanges();
			for(int i = changes.size()-1; i >= 0; i--) {
				Change change = changes.get(i);
				doReconstruct(change);
			}
		}
		else if(originalChange instanceof InitializerChange) {
			InitializerChange create = (InitializerChange) originalChange;
			List<ValueChange> changes = create.getChanges();
			for(int i = changes.size()-1; i >= 0; i--) {
				ValueChange change = changes.get(i);
				doReconstruct(change);
			}
		}
		
		startChange(originalChange);
		
		if(before && originalChange == target) {
			throw new FinishedException();
		}
	}
}
