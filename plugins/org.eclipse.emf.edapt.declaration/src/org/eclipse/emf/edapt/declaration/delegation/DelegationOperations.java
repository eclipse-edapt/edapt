package org.eclipse.emf.edapt.declaration.delegation;

import org.eclipse.emf.edapt.declaration.EdaptLibrary;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 9BD38028B3377E9110CEC619A333C25A
 */
@EdaptLibrary(label = "Delegation Operations", description = "Delegation " +
		"operations move features along the delegation structure.")
public class DelegationOperations extends LibraryImplementation {

	/** Constructor. */
	public DelegationOperations() {
		addOperation(CollectFeature.class);
		addOperation(CombineFeature.class);
		addOperation(ExtractAndGroupAttribute.class);
		addOperation(ExtractClass.class);
		addOperation(ExtractExistingClass.class);
		addOperation(FlattenHierarchy.class);
		addOperation(InlineClass.class);
		addOperation(MoveFeature.class);
		addOperation(PartitionComposite.class);
		addOperation(PropagateFeature.class);
		addOperation(UnfoldClass.class);
	}
}
