package org.eclipse.emf.edapt.declaration.replacement;

import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 969FA528908C0A717DF22D1164845650
 */
@EdaptOperation(label = "Replacement Operations", description = "Replacement " +
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
	}
}
