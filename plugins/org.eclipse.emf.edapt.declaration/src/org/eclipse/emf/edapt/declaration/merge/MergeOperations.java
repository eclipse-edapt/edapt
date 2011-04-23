package org.eclipse.emf.edapt.declaration.merge;

import org.eclipse.emf.edapt.declaration.EdaptLibrary;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 4292A6694823D7D64B965D2FFEF4114A
 */
@EdaptLibrary(label = "Merge Operations", description = "Merge operations merge " +
		"several metamodel elements of the same type into a single element, " +
		"whereas split operations split a metamodel element into several elements " +
		"of the same type.")
public class MergeOperations extends LibraryImplementation {

	/** Constructor. */
	public MergeOperations() {
		addOperation(Merge.class);
		addOperation(PartitionReference.class);
		addOperation(ReplaceClass.class);
		addOperation(ReplaceClassBySibling.class);
		addOperation(ReplaceEnum.class);
		addOperation(ReplaceLiteral.class);
		addOperation(SplitStringAttribute.class);
		addOperation(UniteReferences.class);
	}
}
