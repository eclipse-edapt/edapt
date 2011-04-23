package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.edapt.declaration.EdaptLibrary;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 0A997ECF227702A4ECCFCA26EC0050A8
 */
@EdaptLibrary(label = "Structural Primitives", description = "Structural " +
		"primitives create or delete metamodel elements.")
public class StructuralPrimitives extends LibraryImplementation {

	/** Constructor. */
	public StructuralPrimitives() {
		addOperation(CopyFeature.class);
		addOperation(CreateEnumeration.class);
		addOperation(DeleteClass.class);
		addOperation(DeleteFeature.class);
		addOperation(DeleteFeature2.class);
		addOperation(DeleteOppositeReference.class);
		addOperation(DeletePackage.class);
		addOperation(NewAttribute.class);
		addOperation(NewClass.class);
		addOperation(NewGMFConstraint.class);
		addOperation(NewOppositeReference.class);
		addOperation(NewReference.class);
	}
}
