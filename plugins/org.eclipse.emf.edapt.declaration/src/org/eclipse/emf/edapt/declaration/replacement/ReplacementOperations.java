package org.eclipse.emf.edapt.declaration.replacement;

import org.eclipse.emf.edapt.declaration.EdaptLibrary;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: EE232451B9D42D697448390198CD6531
 */
@EdaptLibrary(label = "Replacement Operations", description = "Replacement " +
		"operations replace one metamodeling construct by another, equivalent " +
		"construct.")
public class ReplacementOperations extends LibraryImplementation {

	/** Constructor. */
	public ReplacementOperations() {
		addOperation(EnumerationToSubClasses.class);
		addOperation(IntroduceReferenceClass.class);
		addOperation(NotChangeableToSuppressedSetVisibility.class);
		addOperation(OperationToVolatile.class);
		addOperation(ReplaceIdentifierByReference.class);
		addOperation(ReplaceInheritanceByDelegation.class);
		addOperation(SubClassesToEnumeration.class);
		addOperation(SuppressedSetVisibilityToNotChangeable.class);
		addOperation(VolatileToOpposite.class);
		addOperation(ClassToAssociation.class);
	}
}
