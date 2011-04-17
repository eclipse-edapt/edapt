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
package org.eclipse.emf.edapt.tests.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Persistency;

/**
 * Split containment across different resources
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class SplitContainment {

	/**
	 * Test split across different resources
	 */
	public static void main(String[] args) throws IOException {

		URI contextURI = URIUtils.getURI("data/node");
		URI metamodelURI = contextURI.appendSegment("node.ecore");
		URI modelURI = contextURI.appendSegment("node.xmi");
		URI targetURI = contextURI.appendSegment("split.xmi");
		
		split(metamodelURI, modelURI, targetURI, 12);
	}

	/**
	 * Split containment across n+1 resources where targetURI denotes the root resource
	 */
	private static void split(URI metamodelURI, URI sourceURI, URI targetURI, int n) throws IOException {
		
		Metamodel metamodel = Persistency.loadMetamodel(metamodelURI);
		
		ResourceSet resourceSet = ResourceUtils.loadResourceSet(sourceURI, metamodel.getEPackages());
		
		Resource mainResource = resourceSet.getResources().get(0);
		mainResource.setURI(targetURI);
		
		List<EObject> elements = getAllContents(mainResource);
		elements.removeAll(mainResource.getContents());
		
		Random random = new Random();
		for(int i = 1; i <= n; i++) {
			int index = random.nextInt(elements.size());
			EObject element = elements.remove(index);
			String name = targetURI.trimFileExtension().lastSegment() + i;
			String extension = targetURI.fileExtension();
			URI uri = targetURI.trimSegments(1).appendSegment(name).appendFileExtension(extension);
			Resource resource = resourceSet.createResource(uri);
			resource.getContents().add(element);
		}
		
		ResourceUtils.saveResourceSet(resourceSet);
	}

	/**
	 * Get all the contents of a resource
	 */
	private static List<EObject> getAllContents(Resource resource) {
		List<EObject> elements = new ArrayList<EObject>();
		
		for(Iterator<EObject> i = resource.getAllContents(); i.hasNext(); ) {
			elements.add(i.next());
		}
		return elements;
	}
}
