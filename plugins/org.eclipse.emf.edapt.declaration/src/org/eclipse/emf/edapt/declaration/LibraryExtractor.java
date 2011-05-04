package org.eclipse.emf.edapt.declaration;

import java.util.List;

/**
 * Helper class to extract the declaration of a library from its implementation.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 4F3CC1B1FE235350B7F5897890546186
 */
public class LibraryExtractor {

	/** Extract the declaration of a library from its implementation in a class. */
	public Library extractLibrary(Class<? extends LibraryImplementation> c) {
		EdaptLibrary libraryAnnotation = c.getAnnotation(EdaptLibrary.class);
		if (libraryAnnotation != null) {
			Library library = DeclarationFactory.eINSTANCE.createLibrary();
			library.setName(c.getName());
			library.setImplementation(c);
			library.setLabel(libraryAnnotation.label());
			library.setDescription(libraryAnnotation.description());
			try {
				extractChildren(c, library);
				return library;
			} catch (InstantiationException e) {
				// return null
			} catch (IllegalAccessException e) {
				// return null
			}
		}
		return null;
	}

	/** Extract the children of a library implementation. */
	private void extractChildren(Class<? extends LibraryImplementation> c,
			Library library) throws InstantiationException, IllegalAccessException {
		LibraryImplementation libraryImplementation = c.newInstance();
		List<Class<? extends LibraryImplementation>> libraryClasses = libraryImplementation
				.getLibraries();
		for (Class<? extends LibraryImplementation> libraryClass : libraryClasses) {
			Library subLibrary = extractLibrary(libraryClass);
			if (subLibrary != null) {
				library.getLibraries().add(subLibrary);
			}
		}
		List<Class<? extends OperationImplementation>> operationClasses = libraryImplementation
				.getOperations();
		for (Class<? extends OperationImplementation> operationClass : operationClasses) {
			Operation operation = extractOperation(operationClass);
			if (operation != null) {
				library.getOperations().add(operation);
			}
		}
	}

	/** Extract the declaration of an operation from its implementation. */
	private Operation extractOperation(
			Class<? extends OperationImplementation> c) {
		return new OperationExtractor().extractOperation(c);
	}
}
