package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 88710860F858893E33856A0FD6675F98
 */
@Operation(identifier = "useSuperClass", label = "Fold Super Class", description = "In the metamodel, a number of features are replaced by features of a new super class. In the model, the values are moved to these features based on a mapping.")
public class UseSuperClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class to which the super class is added")
	public EClass subClass;

	/** {@description} */
	@Parameter(description = "The new super class")
	public EClass superClass;

	/** {@description} */
	@Parameter(description = "The features to be replaced")
	public List<EStructuralFeature> toReplace;

	/** {@description} */
	@Restriction(parameter = "toReplace")
	public List<String> checkToReplace(EStructuralFeature toReplace) {
		if (!subClass.getEStructuralFeatures().contains(toReplace)) {
			return Collections.singletonList("The features to be "
					+ "replaced must belong to the sub class");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@Parameter(description = "The features by which they are replaced (in the same order)")
	public List<EStructuralFeature> replaceBy;

	/** {@description} */
	@Restriction(parameter = "replaceBy")
	public List<String> checkReplaceBy(EStructuralFeature replaceBy) {
		if (!superClass.getEAllStructuralFeatures().contains(replaceBy)) {
			return Collections.singletonList("The features to replace "
					+ "must be available in the super class");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (toReplace.size() != replaceBy.size()) {
			result.add("The number of features to be "
					+ "replaced and to replace them must be the same");
		} else {
			if (!hasSameValue(toReplace, replaceBy,
					EcorePackage.Literals.ETYPED_ELEMENT__ETYPE)) {
				result.add("The features must be of the same type");
			}
			if (!hasSameValue(toReplace, replaceBy,
					EcorePackage.Literals.ETYPED_ELEMENT__MANY)) {
				result.add("The features must be of the same multiplicity");
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {

		subClass.getESuperTypes().add(superClass);
		subClass.getESuperTypes().removeAll(superClass.getEAllSuperTypes());

		for (int i = 0; i < toReplace.size(); i++) {
			ReplaceFeature operation = new ReplaceFeature();
			operation.replaceBy = replaceBy.get(i);
			operation.toReplace = toReplace.get(i);
			operation.execute(metamodel, model);
		}
	}
}
