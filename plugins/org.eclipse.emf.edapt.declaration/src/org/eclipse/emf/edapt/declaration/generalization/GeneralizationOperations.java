package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.edapt.declaration.EdaptLibrary;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 213CDDB0ABF2E060DB587B49981FA476
 */
@EdaptLibrary(label = "Generalization / Specialization Operations", description = "" +
	"Generalization operations generalize feature or super type declarations, " +
	"whereas specialization operations specialize them.")
public class GeneralizationOperations extends LibraryImplementation {

	/** Constructor. */
	public GeneralizationOperations() {
		addOperation(GeneralizeAttribute.class);
		addOperation(GeneralizeReference.class);
		addOperation(GeneralizeSuperType.class);
		addOperation(RemoveSuperfluousSuperType.class);
		addOperation(RemoveSuperType.class);
		addOperation(SpecializeComposition.class);
		addOperation(SpecializeReference.class);
		addOperation(SpecializeReferenceType.class);
		addOperation(SpecializeSuperType.class);
	}
}
