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
package org.eclipse.emf.edapt.declaration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.declaration.DeclarationPlugin;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.Operation;
import org.osgi.framework.Bundle;


/**
 * Registry for all operations (singleton) A set of operations is registered as
 * a Eclipse extension
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationRegistry {

	/**
	 * Singleton instance
	 */
	private static OperationRegistry instance;

	/**
	 * All registered operations identified by name
	 */
	private Map<String, Operation> operations;

	/**
	 * Operations that need to be performed before an operation
	 */
	private Map<Operation, List<Operation>> beforeOperations;

	/**
	 * Operations that need to be performed after an operation
	 */
	private Map<Operation, List<Operation>> afterOperations;

	/**
	 * List of registered operation containers
	 */
	private List<Library> libraries;

	/**
	 * Private default constructor
	 * 
	 */
	private OperationRegistry() {
		registerLibrary();
		initDependencies();
	}

	/**
	 * Initialize the dependencies between the operations
	 */
	private void initDependencies() {
		for (Operation operation : getOperations()) {
			if (operation.getBefore() != null) {
				Operation before = getOperation(operation.getBefore());
				addBefore(operation, before);
			}
			if (operation.getAfter() != null) {
				Operation after = getOperation(operation.getAfter());
				addAfter(operation, after);
			}
		}
	}

	/**
	 * Get the operations that need to be performed before an operation
	 */
	public List<Operation> getBefore(Operation operation) {
		List<Operation> operations = beforeOperations.get(operation);
		if (operations == null) {
			return Collections.emptyList();
		}
		return operations;
	}

	/**
	 * Add a dependency saying that an operation needs to be performed before an
	 * operation
	 */
	private void addBefore(Operation operation, Operation before) {
		List<Operation> operations = beforeOperations.get(before);
		if (operations == null) {
			operations = new ArrayList<Operation>();
			beforeOperations.put(before, operations);
		}
		operations.add(operation);
	}

	/**
	 * Get the operations that need to be performed after an operation
	 */
	public List<Operation> getAfter(Operation operation) {
		List<Operation> operations = afterOperations.get(operation);
		if (operations == null) {
			return Collections.emptyList();
		}
		return operations;
	}

	/**
	 * Add a dependency saying that an operation needs to be performed after an
	 * operation
	 */
	private void addAfter(Operation operation, Operation after) {
		List<Operation> operations = afterOperations.get(after);
		if (operations == null) {
			operations = new ArrayList<Operation>();
			afterOperations.put(after, operations);
		}
		operations.add(operation);
	}

	/**
	 * Register the operations defined by extensions
	 * 
	 */
	private void registerLibrary() {
		operations = new HashMap<String, Operation>();
		beforeOperations = new HashMap<Operation, List<Operation>>();
		afterOperations = new HashMap<Operation, List<Operation>>();

		libraries = new ArrayList<Library>();

		if (Platform.isRunning()) {
			IExtensionRegistry extensionRegistry = Platform
					.getExtensionRegistry();
			IConfigurationElement[] configurationElements = extensionRegistry
					.getConfigurationElementsFor("org.eclipse.emf.edapt.operations");

			for (int i = 0, n = configurationElements.length; i < n; i++) {
				IConfigurationElement configurationElement = configurationElements[i];
				registerLibrary(configurationElement);
			}
		} else {
			try {
				// initialize package
				DeclarationPackage.eINSTANCE.getOperation();

				File dir = new File(
						"../org.eclipse.emf.edapt.declaration/operations/");
				for (File file : dir.listFiles()) {
					if (file.getName().endsWith(".declaration")) {
						URL declarationURL = file.toURI().toURL();
						registerLibrary(declarationURL);
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Register the operations defined by one extension
	 * 
	 * @param configurationElement
	 *            Plugin configuration element
	 */
	private void registerLibrary(IConfigurationElement configurationElement) {
		String declarationPath = configurationElement
				.getAttribute("declaration");

		IContributor contributor = configurationElement.getContributor();
		String bundleName = contributor.getName();
		Bundle bundle = Platform.getBundle(bundleName);

		URL declarationURL = bundle.getResource(declarationPath);

		registerLibrary(declarationURL);
	}

	/**
	 * Register the operations defined within a file
	 * 
	 * @param declarationURL
	 *            URL to the file
	 */
	private void registerLibrary(URL declarationURL) {
		try {
			Library library = extractLibrary(declarationURL);
			libraries.add(library);
			for (Iterator<Operation> i = library.getOperations().iterator(); i
					.hasNext();) {
				Operation o = i.next();
				initOperation(o);
			}
		} catch (Exception e) {
			if (Platform.isRunning()) {
				LoggingUtils.logError(DeclarationPlugin.getPlugin(), e);
			} else {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Load the declaration from a URL
	 * 
	 * @param url
	 * @return Library
	 * @throws Exception
	 */
	private Library extractLibrary(URL url) throws Exception {
		URI uri = URIUtils.getURI(url);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		resource.load(null);
		Library library = (Library) resource.getContents().get(0);
		return library;
	}

	/**
	 * Register one operation (if the name is not already occupied by another
	 * operation)
	 * 
	 * @param operation
	 *            Operation
	 */
	private void initOperation(Operation operation) {
		String name = operation.getName();
		if (operations.get(name) != null) {
			LoggingUtils.logError(DeclarationPlugin.getPlugin(),
					"Duplicate operation name: " + name);
		}
		operations.put(name, operation);
	}

	/**
	 * Getter for instance
	 * 
	 * @return Instance
	 */
	public static OperationRegistry getInstance() {
		if (instance == null) {
			instance = new OperationRegistry();
		}
		return instance;
	}

	/**
	 * Get a list of all operations
	 * 
	 * @return List of operations
	 */
	public Collection<Operation> getOperations() {
		return operations.values();
	}

	/**
	 * Get an operation by name
	 * 
	 * @param name
	 *            Name
	 * @return Operation
	 */
	public Operation getOperation(String name) {
		return operations.get(name);
	}

	/**
	 * Get the list of registered operation containers
	 * 
	 * @return Libraries
	 */
	public List<Library> getLibraries() {
		return libraries;
	}
}
