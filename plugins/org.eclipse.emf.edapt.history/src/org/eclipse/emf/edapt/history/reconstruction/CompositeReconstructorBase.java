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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.Release;


/**
 * A reconstructor that follows the metamodel history and which call other reconstructors that can be plugged in
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class CompositeReconstructorBase implements IReconstructor {

	/**
	 * List of reconstructors
	 */
	private List<IReconstructor> reconstructors;
	
	/**
	 * Mapping from old to new elements
	 */
	protected Mapping mapping;
	
	/**
	 * Metamodel extent
	 */
	protected MetamodelExtent extent;

	/**
	 * Resource set of the reproduced metamodel
	 */
	protected ResourceSet resourceSet;
	
	/**
	 * Default constructor
	 *
	 */
	public CompositeReconstructorBase() {
		reconstructors = new ArrayList<IReconstructor>();
		resourceSet = new ResourceSetImpl();
	}
	
	/**
	 * Reconstruct until a certain version
	 * 
	 * @param targetRelease Target version
	 * @param before Whether reconstruction stops before or after version
	 */
	public void reconstruct(Release targetRelease, boolean before) {
		
		History originalHistory = targetRelease.getHistory();
		doReconstruct(targetRelease, originalHistory, before);
	}
	
	/**
	 * Reconstruct until a certain change
	 * 
	 * @param targetChange Target change
	 * @param before Whether reconstruction stops before or after change
	 */
	public void reconstruct(Change targetChange, boolean before) {
		
		History originalHistory = targetChange.getRelease().getHistory();
		doReconstruct(targetChange, originalHistory, before);
	}
	
	/**
	 * Reconstruct until a certain change or version
	 * (since the target change may be null, the history cannot be inferred and so has to be provided)
	 * 
	 * @param target Target change or version
	 * @param originalHistory History
	 * @param before Whether reconstruction stops before or after change or version
	 */
	protected abstract void doReconstruct(EObject target, History originalHistory, boolean before);

	/**
	 * {@inheritDoc}
	 */
	public void init(Mapping mapping, MetamodelExtent extent) {
		for(Iterator<IReconstructor> i = reconstructors.iterator(); i.hasNext(); ) {
			IReconstructor r = i.next();
			r.init(mapping, extent);
		}
	}
	
	/**
	 * Add a reconstructor
	 */
	public void addReconstructor(IReconstructor reconstructor) {
		reconstructors.add(reconstructor);
	}

	/**
	 * {@inheritDoc}
	 */
	public void startHistory(History originalHistory) {
		for(Iterator<IReconstructor> i = reconstructors.iterator(); i.hasNext(); ) {
			IReconstructor r = i.next();
			r.startHistory(originalHistory);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void endHistory(History originalHistory) {
		for(Iterator<IReconstructor> i = reconstructors.iterator(); i.hasNext(); ) {
			IReconstructor r = i.next();
			r.endHistory(originalHistory);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void startChange(Change change) {
		for(Iterator<IReconstructor> i = reconstructors.iterator(); i.hasNext(); ) {
			IReconstructor r = i.next();
			r.startChange(change);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void endChange(Change change) {
		for(Iterator<IReconstructor> i = reconstructors.iterator(); i.hasNext(); ) {
			IReconstructor r = i.next();
			r.endChange(change);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void startRelease(Release originalRelease) {
		for(Iterator<IReconstructor> i = reconstructors.iterator(); i.hasNext(); ) {
			IReconstructor r = i.next();
			r.startRelease(originalRelease);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void endRelease(Release originalRelease) {
		for(Iterator<IReconstructor> i = reconstructors.iterator(); i.hasNext(); ) {
			IReconstructor r = i.next();
			r.endRelease(originalRelease);
		}
	}

	/**
	 * Getter for extent
	 */
	public MetamodelExtent getExtent() {
		return extent;
	}

	/**
	 * Getter for mapping
	 */
	public Mapping getMapping() {
		return mapping;
	}

	/**
	 * Getter for resource set
	 */
	public ResourceSet getResourceSet() {
		return resourceSet;
	}
}
