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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.reconstruction.MigratorCodeGenerator;


/**
 * A special generator which weaves additional metamodels into the migrator 
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class SpecialMigratorCodeGenerator extends MigratorCodeGenerator {

	/**
	 * URIs of additional metamodels
	 */
	private List<URI> addMetamodelURIs;

	/**
	 * Constructor
	 * 
	 * @param folder
	 * @param addMetamodelURIs
	 */
	public SpecialMigratorCodeGenerator(URI folder, List<URI> addMetamodelURIs) {
		super(folder);
		
		this.addMetamodelURIs = addMetamodelURIs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("cast")
	@Override
	public void endRelease(Release originalRelease) {
		super.endRelease(originalRelease);

		if(originalRelease.isFirstRelease()) {
			ResourceSet resourceSet = originalRelease.eResource().getResourceSet();
			Collection<EPackage> originalPackages = loadAddMetamodel(resourceSet);
			
			Copier copier = new EcoreUtil.Copier() {
				@Override
				public EObject get(Object key) {
					EObject original = (EObject) key;
					EObject copy = super.get(key);
					
					if(copy == null) {
						return mapping.getTarget(original);
					}
					return copy;
				}
			};
			
			Collection<EPackage> reproducedPackages = (Collection<EPackage>) copier.copyAll(originalPackages);
			copier.copyReferences();
			extent.addRootPackages(reproducedPackages);
		}
	}

	/**
	 * Load additional metamodels
	 * 
	 * @param resourceSet
	 * @return List of packages
	 */
	private Collection<EPackage> loadAddMetamodel(ResourceSet resourceSet) {
		List<EPackage> packages = new ArrayList<EPackage>();
		for(URI addMetamodelURI : addMetamodelURIs) {
			Resource resource = resourceSet.createResource(addMetamodelURI);
			try {
				resource.load(null);
			} catch (IOException e) {
				// ignore
			}
			for(EObject element : resource.getContents()) {
				if(element instanceof EPackage) {
					packages.add((EPackage) element);
				}
			}
		}
		return packages;
	}
}
