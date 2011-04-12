package org.eclipse.emf.edapt.migration.operations.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: C7A1E773D125014A110A83B1EAD30D7E
 */
@Deprecated
@Operation(identifier = "replaceClassBySibling", label = "Replace Class by Sibling", description = "In the metamodel, a class is deleted. In the model, its instances are migrated to a class sharing the same super class.")
public class ReplaceClassBySibling extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class to be replaced")
	public EClass toReplace;

	/** {@description} */
	@Parameter(description = "The class by which it is replaced")
	public EClass replaceBy;
	
	/** {@description} */
	@Restriction(parameter = "replaceBy")
	public List<String> checkReplaceBy(EClass replaceBy) {
		List<String> result = new ArrayList<String>();
		if (replaceBy.getESuperTypes().size() != 1) {
			result.add("The replacing class must have exactly one super class");
		}
		if (toReplace.getESuperTypes().size() > 0
				&& replaceBy.getESuperTypes().size() > 0) {
			if (toReplace.getESuperTypes().get(0) != replaceBy.getESuperTypes()
					.get(0)) {
				result.add("The super classes of "
						+ "replaced and replacing class must be the same");
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (toReplace.getESuperTypes().size() != 1) {
			result.add("The replaced class must have exactly one super class");
		}
		if (!toReplace.getEStructuralFeatures().isEmpty()) {
			result.add("The replaced class must not have any features");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		for (EReference reference : metamodel.<EReference> getInverse(
				toReplace, EcorePackage.Literals.EREFERENCE__EREFERENCE_TYPE)) {
			reference.setEType(replaceBy);
		}
		metamodel.delete(toReplace);

		// model migration
		for (Instance instance : model.getAllInstances(toReplace)) {
			instance.migrate(replaceBy);
		}
	}
}
