package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.migration.Instance;
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
 * @levd.rating YELLOW Hash: 4D3CECED09F193D4FD2ECC35353C2202
 */
@Deprecated
@Operation(label = "Replace Feature", description = "In the metamodel, a feature is replace by another one. In the model, the values are moved accordingly.")
public class ReplaceFeature extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature to be replaced")
	public EStructuralFeature toReplace;

	/** {@description} */
	@Parameter(description = "The feature by which it is replaced")
	public EStructuralFeature replaceBy;

	/** {@description} */
	@Restriction(parameter = "replaceBy")
	public List<String> checkReplaceBy(EStructuralFeature replaceBy) {
		EClass subClass = toReplace.getEContainingClass();
		if (!subClass.getEAllStructuralFeatures().contains(replaceBy)) {
			return Collections.singletonList("The feature to replace "
					+ "must be defined in a sub class of the one "
					+ "with the feature by which it is replaced");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if(toReplace.getEType() != replaceBy.getEType()) {
			result.add("The features must be of the same type");
		}
		if(toReplace.isMany() != replaceBy.isMany()) {
			result.add("The features must be of the same cardinality");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass subClass = toReplace.getEContainingClass();

		// metamodel adaptation
		metamodel.delete(toReplace);

		// model migration
		for (Instance instance : model.getAllInstances(subClass)) {
			instance.set(replaceBy, instance.unset(toReplace));
		}
	}
}
