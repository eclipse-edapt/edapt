/*******************************************************************************
 * Copyright (c) 2006, 2009 Markus Herrmannsdoerfer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Markus Herrmannsdoerfer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.edapt.internal.declaration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.edapt.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.spi.migration.MigrationPlugin;

/**
 * Registry for all operations (singleton). A set of operations is registered as
 * a Eclipse extension.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 5D6B6EEEA0BB217D3762F008E642AA6F
 */
public class OperationRegistry {

	/** Singleton instance. */
	private static OperationRegistry instance;

	/** All registered operations identified by name. */
	private Map<String, Operation> operations;

	/** Operations that need to be performed before an operation. */
	private Map<Operation, List<Operation>> beforeOperations;

	/** Operations that need to be performed after an operation. */
	private Map<Operation, List<Operation>> afterOperations;

	/** List of registered operation containers. */
	private Library rootLibrary;

	/** Private default constructor. */
	private OperationRegistry() {
		registerLibrary();
		initDependencies();
	}

	/** Initialize the dependencies between the operations. */
	private void initDependencies() {
		for (final Operation operation : getOperations()) {
			if (operation.getBefore() != null) {
				final Operation before = getOperation(operation.getBefore());
				addBefore(operation, before);
			}
			if (operation.getAfter() != null) {
				final Operation after = getOperation(operation.getAfter());
				addAfter(operation, after);
			}
		}
	}

	/** Get the operations that need to be performed before an operation. */
	public List<Operation> getBefore(Operation operation) {
		final List<Operation> operations = beforeOperations.get(operation);
		if (operations == null) {
			return Collections.emptyList();
		}
		return operations;
	}

	/**
	 * Add a dependency saying that an operation needs to be performed before an
	 * operation.
	 */
	private void addBefore(Operation operation, Operation before) {
		List<Operation> operations = beforeOperations.get(before);
		if (operations == null) {
			operations = new ArrayList<Operation>();
			beforeOperations.put(before, operations);
		}
		operations.add(operation);
	}

	/** Get the operations that need to be performed after an operation. */
	public List<Operation> getAfter(Operation operation) {
		final List<Operation> operations = afterOperations.get(operation);
		if (operations == null) {
			return Collections.emptyList();
		}
		return operations;
	}

	/**
	 * Add a dependency saying that an operation needs to be performed after an
	 * operation.
	 */
	private void addAfter(Operation operation, Operation after) {
		List<Operation> operations = afterOperations.get(after);
		if (operations == null) {
			operations = new ArrayList<Operation>();
			afterOperations.put(after, operations);
		}
		operations.add(operation);
	}

	/** Register the operations defined by extensions. */
	private void registerLibrary() {
		operations = new HashMap<String, Operation>();
		beforeOperations = new HashMap<Operation, List<Operation>>();
		afterOperations = new HashMap<Operation, List<Operation>>();
		rootLibrary = DeclarationFactory.eINSTANCE.createLibrary();
		rootLibrary.setName("root"); //$NON-NLS-1$
		rootLibrary.setLabel("Root library"); //$NON-NLS-1$
		rootLibrary.setDescription("Root library of the operation registry"); //$NON-NLS-1$

		if (Platform.isRunning()) {
			final IExtensionRegistry extensionRegistry = Platform
				.getExtensionRegistry();
			final IConfigurationElement[] configurationElements = extensionRegistry
				.getConfigurationElementsFor("org.eclipse.emf.edapt.operations"); //$NON-NLS-1$

			for (int i = 0, n = configurationElements.length; i < n; i++) {
				final IConfigurationElement configurationElement = configurationElements[i];
				register(configurationElement);
			}
		} else {
			registerLibrary(BaseLibrary.class);
		}
	}

	/** Register a configuration element. */
	@SuppressWarnings("unchecked")
	private void register(IConfigurationElement configurationElement) {
		try {
			final Class c = configurationElement.createExecutableExtension("class") //$NON-NLS-1$
				.getClass();
			if ("operation".equals(configurationElement.getName())) { //$NON-NLS-1$
				registerOperation(c);
			} else if ("library".equals(configurationElement.getName())) { //$NON-NLS-1$
				registerLibrary(c);
			}
		} catch (final CoreException e) {
			// do not register operations that are declared erroneously.
		}
	}

	/** Register the implementation of an operation. */
	public void registerOperation(Class<? extends OperationImplementation> c) {
		final Operation operation = new OperationExtractor().extractOperation(c);
		if (operation != null) {
			initOperation(operation);
			rootLibrary.getOperations().add(operation);
		}
	}

	/**
	 * Register one operation (if the name is not already occupied by another
	 * operation).
	 */
	private void initOperation(Operation operation) {
		final String name = operation.getName();
		if (operations.get(name) != null) {
			LoggingUtils.logError(MigrationPlugin.getPlugin(),
				"Duplicate operation name: " + name); //$NON-NLS-1$
		}
		operations.put(name, operation);
	}

	/** Register the implementation of a library. */
	public void registerLibrary(Class<? extends LibraryImplementation> c) {
		final Library library = new LibraryExtractor().extractLibrary(c);
		if (library != null) {
			initLibrary(library);
			rootLibrary.getLibraries().add(library);
		}
	}

	/** Register the operations provided by a library. */
	private void initLibrary(Library library) {
		for (final Operation operation : library.getOperations()) {
			initOperation(operation);
		}
		for (final Library subLibrary : library.getLibraries()) {
			initLibrary(subLibrary);
		}
	}

	/** Getter for instance. */
	public static OperationRegistry getInstance() {
		if (instance == null) {
			instance = new OperationRegistry();
		}
		return instance;
	}

	/** Get a list of all operations. */
	public Collection<Operation> getOperations() {
		return operations.values();
	}

	/** Get an operation by name. */
	public Operation getOperation(String name) {
		return operations.get(name);
	}

	/** Get the root libraries. */
	public List<Library> getRootLibraries() {
		return new ArrayList<Library>(rootLibrary.getLibraries());
	}

	/** Get the root operations. */
	public List<Operation> getRootOperations() {
		return new ArrayList<Operation>(rootLibrary.getOperations());
	}
}
