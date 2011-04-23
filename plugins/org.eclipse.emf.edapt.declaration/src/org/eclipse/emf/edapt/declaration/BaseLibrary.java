package org.eclipse.emf.edapt.declaration;

import org.eclipse.emf.edapt.declaration.creation.StructuralPrimitives;
import org.eclipse.emf.edapt.declaration.delegation.DelegationOperations;
import org.eclipse.emf.edapt.declaration.generalization.GeneralizationOperations;
import org.eclipse.emf.edapt.declaration.inheritance.InheritanceOperations;
import org.eclipse.emf.edapt.declaration.merge.MergeOperations;
import org.eclipse.emf.edapt.declaration.replacement.ReplacementOperations;
import org.eclipse.emf.edapt.declaration.simple.NonStructuralPrimitives;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 0AB36DAD6E81CD994D1DE333D6ABB791
 */
@EdaptLibrary(label = "Base Library", description = "Library of predefined operations")
public class BaseLibrary extends LibraryImplementation {

	/** Constructor. */
	public BaseLibrary() {
		addLibrary(StructuralPrimitives.class);
		addLibrary(NonStructuralPrimitives.class);
		addLibrary(GeneralizationOperations.class);
		addLibrary(InheritanceOperations.class);
		addLibrary(DelegationOperations.class);
		addLibrary(ReplacementOperations.class);
		addLibrary(MergeOperations.class);
	}
}
