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
package org.eclipse.emf.edapt.cope.migration.execution;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edapt.cope.common.ResourceUtils;
import org.eclipse.emf.edapt.cope.migration.Metamodel;
import org.eclipse.emf.edapt.cope.migration.MetamodelResource;
import org.eclipse.emf.edapt.cope.migration.MigrationFactory;
import org.eclipse.emf.edapt.cope.migration.Model;


/**
 * Helper class for loading and saving models
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class Persistency {
	
	/**
	 * Load metamodel based on {@link URI}
	 * 
	 * @param metamodelURI
	 * @return Metamodel
	 * @throws IOException 
	 */
	public static Metamodel loadMetamodel(URI metamodelURI) throws IOException {		
		ResourceSet resourceSet = ResourceUtils.loadResourceSet(metamodelURI);

		return loadMetamodel(resourceSet);
	}

	/**
	 * Create metamodel from a {@link ResourceSet}
	 * 
	 * @param resourceSet
	 * @return Metamodel
	 */
	public static Metamodel loadMetamodel(ResourceSet resourceSet) {
		ResourceUtils.resolveAll(resourceSet);
		Metamodel metamodel = MigrationFactory.eINSTANCE.createMetamodel();
		for(Resource resource : resourceSet.getResources()) {
			MetamodelResource metamodelResource = MigrationFactory.eINSTANCE.createMetamodelResource();
			metamodelResource.setUri(resource.getURI());
			metamodel.getResources().add(metamodelResource);
			for(EObject element : resource.getContents()) {
				if(element instanceof EPackage) {
					EPackage ePackage = (EPackage) element;
					metamodelResource.getRootPackages().add(ePackage);
				}
			}
		}

		return metamodel;
	}

	/**
	 * Load metamodel based on file name
	 * 
	 * @param fileName
	 * @return Metamodel
	 * @throws IOException 
	 */
	public static Metamodel loadMetamodel(String fileName) throws IOException {
		return loadMetamodel(URI.createFileURI(fileName));
	}
	
	/**
	 * Save metamodel based on {@link URI}
	 * 
	 * @param metamodel
	 * @throws IOException 
	 */
	public static void saveMetamodel(Metamodel metamodel) throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		for(MetamodelResource metamodelResource : metamodel.getResources()) {
			Resource resource = resourceSet.createResource(metamodelResource.getUri());
			resource.getContents().addAll(metamodelResource.getRootPackages());		
		}
		
		ResourceUtils.saveResourceSet(resourceSet);
	}
	
	/**
	 * Load model based on {@link URI} for model and metamodel
	 * 
	 * @param modelURI
	 * @param metamodelURI
	 * @return Model
	 * @throws IOException 
	 */
	public static Model loadModel(URI modelURI, URI metamodelURI) throws IOException {
		Metamodel metamodel = loadMetamodel(metamodelURI);
		Model model = loadModel(modelURI, metamodel);
		return model;
	}
	
	/**
	 * Load model based on {@link URI} and metamodel
	 * 
	 * @param modelURI
	 * @param metamodel
	 * @return Model
	 * @throws IOException 
	 */
	public static Model loadModel(URI modelURI, Metamodel metamodel) throws IOException {
		return loadModel(Collections.singletonList(modelURI), metamodel);
	}
	
	/**
	 * Load model based on a set of {@link URI} and metamodel
	 * 
	 * @param modelURIs
	 * @param metamodel
	 * @return Model
	 * @throws IOException
	 */
	public static Model loadModel(List<URI> modelURIs, Metamodel metamodel) throws IOException {
		ResourceSet resourceSet = ResourceUtils.loadResourceSet(modelURIs, metamodel.getEPackages());
		ForwardConverter fConverter = new ForwardConverter();
		Model model = fConverter.convert(resourceSet);
		model.setMetamodel(metamodel);
		return model;
	}	
	
	/**
	 * Load model based on file name and metamodel
	 * 
	 * @param fileName
	 * @param metamodel
	 * @return Model
	 * @throws IOException 
	 */
	public static Model loadModel(String fileName, Metamodel metamodel) throws IOException {		
		return loadModel(URI.createFileURI(fileName), metamodel);
	}

	/**
	 * Save model based on {@link URI}
	 * 
	 * @param model
	 * @throws IOException 
	 */
	public static void saveModel(Model model) throws IOException {
		BackwardConverter bConverter = new BackwardConverter();
		ResourceSet resourceSet = bConverter.convert(model);
		ResourceUtils.saveResourceSet(resourceSet);
	}
}
