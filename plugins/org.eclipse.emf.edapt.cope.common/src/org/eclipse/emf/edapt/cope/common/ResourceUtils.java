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
package org.eclipse.emf.edapt.cope.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Helper methods to load and save EMF resources.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class ResourceUtils {

	/** Scheme for platform resources. */
	public static final String PLATFORM_SCHEME = "platform";

	/** File extension for Ecore model. */
	public static final String ECORE_FILE_EXTENSION = EcorePackage.eNS_PREFIX;

	static {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
				new XMIResourceFactoryImpl());
	}

	/** Constructor. */
	private ResourceUtils() {
		// hidden, since this class only provides static helper methods
	}

	/** Load EMF model based on {@link URI} and root packages. */
	public static ResourceSet loadResourceSet(URI modelURI,
			List<EPackage> ePackages) throws IOException {
		return loadResourceSet(Collections.singletonList(modelURI), ePackages);
	}

	/**
	 * Load EMF model based on a set of {@link URI} and root packages.
	 */
	public static ResourceSet loadResourceSet(List<URI> modelURIs,
			List<EPackage> ePackages) throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		register(ePackages, resourceSet.getPackageRegistry());

		Map<String, Object> options = new HashMap<String, Object>();
		for (URI modelURI : modelURIs) {
			if (isPathmap(modelURI)) {
				continue;
			}
			Resource resource = resourceSet.createResource(modelURI);
			try {
				resource.load(options);
			} catch (Resource.IOWrappedException e) {
				// ignore
				EList<Diagnostic> errors = resource.getErrors();
				if (errors.size() > 0) {
					System.err.println(resource.getURI() + ": " + errors.size()
							+ " errors");
				} else {
					throw e;
				}
			}
		}

		ResourceUtils.resolveAll(resourceSet);

		return resourceSet;
	}

	/** Load EMF model based on {@link URI}. */
	public static ResourceSet loadResourceSet(URI modelURI) throws IOException {
		return loadResourceSet(modelURI, new ArrayList<EPackage>());
	}

	/** Recursively register packages to a registry. */
	public static Map<String, EPackage> register(List<EPackage> ePackages,
			Registry registry) {
		Map<String, EPackage> backup = new HashMap<String, EPackage>();
		register(ePackages, registry, backup);
		return backup;
	}

	/** Recursively register packages to a registry. */
	private static void register(List<EPackage> ePackages, Registry registry,
			Map<String, EPackage> backup) {
		for (EPackage p : ePackages) {
			String nsURI = p.getNsURI();
			EPackage old = (EPackage) registry.put(nsURI, p);
			backup.put(nsURI, old);
			register(p.getESubpackages(), registry, backup);
		}
	}

	/** Load EMF model based on file name and root packages. */
	public static ResourceSet loadResourceSet(String fileName,
			List<EPackage> ePackages) throws IOException {
		return loadResourceSet(URI.createFileURI(fileName), ePackages);
	}

	/**
	 * Load EMF model based on file name (use the packages already added to the
	 * registry).
	 */
	public static ResourceSet loadResourceSet(String fileName) throws IOException {
		return loadResourceSet(fileName, new ArrayList<EPackage>());
	}

	/** Save model based on {@link ResourceSet}. */
	public static void saveResourceSet(ResourceSet resourceSet) throws IOException {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.FALSE);
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI() == null
					|| resource.getURI().isPlatformPlugin()
					|| isPathmap(resource.getURI())) {
				continue;
			}
			resource.save(options);
		}
	}

	/** Determines whether this URI is a path map URI. */
	public static boolean isPathmap(URI uri) {
		return "pathmap".equals(uri.scheme());
	}

	/** Load a resource based on a {@link URI}. */
	public static Resource loadResource(URI uri) throws IOException {
		ResourceSet resourceSet = loadResourceSet(uri);
		if (!resourceSet.getResources().isEmpty()) {
			return resourceSet.getResources().get(0);
		}
		return null;
	}

	/** Load a resource based on a {@link URI} and return its root element. */
	@SuppressWarnings("unchecked")
	public static <V extends EObject> V loadElement(URI uri) throws IOException {
		Resource resource = loadResource(uri);
		if (!resource.getContents().isEmpty()) {
			return (V) resource.getContents().get(0);
		}
		return null;
	}

	/** Save an element to a {@link URI} and return the resource. */
	public static Resource saveElement(URI uri, EObject element)
			throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(element);
		resource.save(null);
		return resource;
	}

	/** Check whether a file exists for a certain URI. */
	public static boolean exists(URI uri) throws IOException {
		try {
			URIUtils.getURL(uri).openStream().close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	/** Get the root model elements. */
	public static <E extends EObject> List<E> getRootElements(
			ResourceSet resourceSet, Class<E> type) {
		List<E> rootElements = new ArrayList<E>();
		for (Resource resource : resourceSet.getResources()) {
			rootElements.addAll(getRootElements(resource, type));
		}
		return rootElements;
	}

	/** Get the root model elements. */
	@SuppressWarnings("unchecked")
	public static <E extends EObject> List<E> getRootElements(
			Resource resource, Class<E> type) {
		List<E> rootElements = new ArrayList<E>();
		for (EObject rootElement : resource.getContents()) {
			if (type.isInstance(rootElement)) {
				rootElements.add((E) rootElement);
			}
		}
		return rootElements;
	}

	/** Decide whether a resource is a platform resource. */
	public static boolean isPlatformResource(Resource resource) {
		return resource != null && PLATFORM_SCHEME.equals(resource.getURI().scheme());
	}

	/** Resolve all referenced resources within a {@link ResourceSet}. */
	public static void resolveAll(ResourceSet resourceSet) {
		Set<Resource> resolved = new HashSet<Resource>();
		boolean newFound = true;
		while(newFound) {
			newFound = false;
			for(Resource r : new ArrayList<Resource>(resourceSet.getResources())) {
				if(!resolved.contains(r)) {
					newFound = true;
					org.eclipse.emf.ecore.util.EcoreUtil.resolveAll(r);
					resolved.add(r);
				}
			}
		}
	}
}
