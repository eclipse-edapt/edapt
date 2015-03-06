package org.eclipse.emf.edapt.declaration.doc;

import java.io.IOException;
import java.util.Comparator;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.declaration.BaseLibrary;
import org.eclipse.emf.edapt.internal.declaration.OperationRegistry;

public class OperationExtractor {

	public static void main(String[] args) throws IOException {
		DeclarationPackage.eINSTANCE.getConstraint();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
				new XMIResourceFactoryImpl());

		OperationRegistry.getInstance().registerLibrary(BaseLibrary.class);
		for (Library rootLibrary : OperationRegistry.getInstance()
				.getRootLibraries()) {
			
			for (Library library : rootLibrary.getLibraries()) {
				ECollections.sort(library.getOperations(),
						new Comparator<Operation>() {
							public int compare(Operation o1, Operation o2) {
								return o1.getLabel().compareTo(o2.getLabel());
							}
						});
			}
		}

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI
				.createFileURI("model/library.declaration"));
		resource.getContents().addAll(
				OperationRegistry.getInstance().getRootLibraries());

		ResourceUtils.saveResourceSet(resourceSet);
	}

}
