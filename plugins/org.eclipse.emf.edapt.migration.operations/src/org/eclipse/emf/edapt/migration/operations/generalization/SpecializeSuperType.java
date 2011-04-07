package org.eclipse.emf.edapt.migration.operations.generalization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 7C713F2982645977F9A51AF273861A95
 */
@Operation(label = "Specialize Super Type", description = "In the metamodel, the super type of a class is replaced by one of its sub classes. In the model, nothing is modified.")
public class SpecializeSuperType extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class whose super type is specialized")
	public EClass eClass;

	/** {@description} */
	@Parameter(description = "The super type which is replaced")
	public EClass toReplace;

	/** {@description} */
	@Restriction(parameter = "toReplace")
	public List<String> checkToReplace(EClass toReplace) {
		if (!replaceBy.getEAllSuperTypes().contains(toReplace)) {
			return Collections.singletonList("The replacing super type must "
					+ "be a sub type of the replaced super type");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@Parameter(description = "The sub class by which is replaced")
	public EClass replaceBy;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!eClass.getESuperTypes().contains(toReplace)) {
			result.add("The super type to be replaced "
					+ "must be a super type of the class");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.getESuperTypes().remove(toReplace);
		eClass.getESuperTypes().add(replaceBy);
	}
}
