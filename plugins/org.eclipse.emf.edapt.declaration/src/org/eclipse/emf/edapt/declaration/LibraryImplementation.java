package org.eclipse.emf.edapt.declaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for implementations of libraries.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: ED693FC827AE06FD146F3B51CB0124E8
 */
public class LibraryImplementation {

	/** The operations provided by this library. */
	private final List<Class<? extends OperationImplementation>> operations = new ArrayList<Class<? extends OperationImplementation>>();

	/** The sub libraries provided by this library. */
	private final List<Class<? extends LibraryImplementation>> libraries = new ArrayList<Class<? extends LibraryImplementation>>();

	/** Add the implementation of an operation to this library. */
	protected void addOperation(
			Class<? extends OperationImplementation> operation) {
		operations.add(operation);
	}

	/** Add the implementation of a sub library to this library. */
	protected void addLibrary(Class<? extends LibraryImplementation> library) {
		libraries.add(library);
	}

	/** Get the implementations of the operations provided by this library. */
	public List<Class<? extends OperationImplementation>> getOperations() {
		return operations;
	}

	/** Get the implementations of the sub libraries provided by this library. */
	public List<Class<? extends LibraryImplementation>> getLibraries() {
		return libraries;
	}
}
