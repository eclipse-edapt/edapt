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
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.internal.Persistency;
import org.eclipse.emf.edapt.spi.migration.Metamodel;

/**
 * Split containment across different resources
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
@SuppressWarnings("restriction")
public class SplitContainment {

	/**
	 * Test split across different resources
	 */
	public static void main(String[] args) throws IOException {

		final URI contextURI = URIUtils.getURI("data/node"); //$NON-NLS-1$
		final URI metamodelURI = contextURI.appendSegment("node.ecore"); //$NON-NLS-1$
		final URI modelURI = contextURI.appendSegment("node.xmi"); //$NON-NLS-1$
		final URI targetURI = contextURI.appendSegment("split.xmi"); //$NON-NLS-1$

		split(metamodelURI, modelURI, targetURI, 12);
	}

	/**
	 * Split containment across n+1 resources where targetURI denotes the root resource
	 */
	private static void split(URI metamodelURI, URI sourceURI, URI targetURI, int n) throws IOException {

		final Metamodel metamodel = Persistency.loadMetamodel(metamodelURI);

		final ResourceSet resourceSet = ResourceUtils.loadResourceSet(sourceURI, metamodel.getEPackages());

		final Resource mainResource = resourceSet.getResources().get(0);
		mainResource.setURI(targetURI);

		final List<EObject> elements = getAllContents(mainResource);
		elements.removeAll(mainResource.getContents());

		final Random random = new Random();
		for (int i = 1; i <= n; i++) {
			final int index = random.nextInt(elements.size());
			final EObject element = elements.remove(index);
			final String name = targetURI.trimFileExtension().lastSegment() + i;
			final String extension = targetURI.fileExtension();
			final URI uri = targetURI.trimSegments(1).appendSegment(name).appendFileExtension(extension);
			final Resource resource = resourceSet.createResource(uri);
			resource.getContents().add(element);
		}
		
		ResourceUtils.saveResourceSet(resourceSet, null);
	}

	/**
	 * Get all the contents of a resource
	 */
	private static List<EObject> getAllContents(Resource resource) {
		final List<EObject> elements = new ArrayList<EObject>();

		for (final Iterator<EObject> i = resource.getAllContents(); i.hasNext();) {
			elements.add(i.next());
		}
		return elements;
	}
}
