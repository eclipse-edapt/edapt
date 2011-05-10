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
package org.eclipse.emf.edapt.history.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.Release;

/**
 * Helper methods to deal with the metamodel history
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class HistoryUtils {

	/** Source for the annotation to maintain the history URI. */
	private static final String HISTORY_ANNOTATION_SOURCE = "http://www.eclipse.org/edapt";

	/** Key for the annotation to maintain the history URI. */
	private static final String HISTORY_ANNOTATION_KEY = "historyURI";

	/**
	 * Constructor
	 */
	private HistoryUtils() {
		// hidden, since this class only provides static helper methods
	}

	/**
	 * Extension of a history file
	 */
	public static final String HISTORY_FILE_EXTENSION = HistoryPackage.eNS_PREFIX;

	/**
	 * Get the resource with the root metamodel from a resource set
	 * 
	 * @param resourceSet
	 *            Resource set
	 * @return Resource
	 */
	public static Resource getRootResource(ResourceSet resourceSet) {
		Resource resource = getHistoryResource(resourceSet);
		History history = (History) resource.getContents().get(0);
		EPackage rootPackage = history.getRootPackages().get(0);
		return rootPackage.eResource();
	}

	/**
	 * Get the resource with the history from a resource set
	 * 
	 * @param resourceSet
	 *            Resource set
	 * @return Resource
	 */
	public static Resource getHistoryResource(ResourceSet resourceSet) {
		for (Iterator<Resource> i = resourceSet.getResources().iterator(); i
				.hasNext();) {
			Resource resource = i.next();
			String extension = resource.getURI().fileExtension();
			if (HistoryUtils.HISTORY_FILE_EXTENSION.equals(extension)) {
				return resource;
			}
		}
		return null;
	}

	/** Get the URI of the history model. */
	public static URI getHistoryURI(Resource metamodelResource) {
		URI metamodelURI = metamodelResource.getURI();
		List<EPackage> packages = ResourceUtils.getRootElements(
				metamodelResource, EPackage.class);
		if (!packages.isEmpty()) {
			EPackage p = packages.get(0);
			String value = EcoreUtil.getAnnotation(p,
					HISTORY_ANNOTATION_SOURCE, HISTORY_ANNOTATION_KEY);
			if (value != null) {
				URI relativeURI = URI.createFileURI(value);
				URI historyURI = relativeURI.resolve(metamodelURI);
				return historyURI;
			}
		}
		return getDefaultHistoryURI(metamodelResource);
	}

	/** Get URI where the history has to be stored for a certain metamodel. */
	public static URI getDefaultHistoryURI(Resource metamodelResource) {
		return URIUtils.replaceExtension(metamodelResource.getURI(),
				HISTORY_FILE_EXTENSION);
	}

	/** Set the URI of the history model. */
	public static void setHistoryURI(Resource metamodelResource, URI historyURI) {
		if (getDefaultHistoryURI(metamodelResource).equals(historyURI)) {
			return;
		}
		List<EPackage> packages = ResourceUtils.getRootElements(
				metamodelResource, EPackage.class);
		if (!packages.isEmpty()) {
			URI relativeURI = URIUtils.getRelativePath(historyURI,
					metamodelResource.getURI());
			EPackage p = packages.get(0);
			EcoreUtil.setAnnotation(p, HISTORY_ANNOTATION_SOURCE,
					HISTORY_ANNOTATION_KEY, relativeURI.toString());
		}
	}

	/** Get a release with a certain number. */
	public static Release getRelease(Collection<Release> releases, int number) {
		for (Release release : releases) {
			if (release.getNumber() == number) {
				return release;
			}
		}
		return null;
	}
	
	/** Get the minimum release of a set of releases. */
	public static Release getMinimumRelease(Set<Release> releases) {
		Release minRelease = null;
		for (Release release : releases) {
			if (minRelease == null) {
				minRelease = release;
			} else {
				if (release.getNumber() < minRelease.getNumber()) {
					minRelease = release;
				}
			}
		}
		return minRelease;
	}
}
