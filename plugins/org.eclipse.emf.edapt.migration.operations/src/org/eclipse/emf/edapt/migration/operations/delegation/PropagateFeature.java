package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: E26C4382ACDFC71E1304B18D549AE656
 */
@Operation(label = "Propagate Feature over References", description = "In the metamodel, a feature is propagated opposite to a number of references. More specifically, the feature is created in each of the classes which are sources of the references. In the model, the values of that feature are moved accordingly.")
public class PropagateFeature extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature to be propagated")
	public EStructuralFeature mainFeature;

	/** {@description} */
	@Parameter(description = "The references opposite to which the feature is propagated")
	public List<EReference> references;

	/** {@description} */
	@Restriction(parameter = "references")
	public List<String> checkReferences(EReference reference) {
		EClass eClass = mainFeature.getEContainingClass();
		if (reference.getEType() != eClass) {
			return Collections.singletonList("Every reference has to target "
					+ "the class with the feature");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		boolean first = true;
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		for (EReference reference : references) {
			EStructuralFeature feature = null;
			if (first) {
				feature = mainFeature;
				first = false;
			} else {
				feature = MetamodelUtils.copy(mainFeature);
			}
			features.add(feature);
			reference.getEContainingClass().getEStructuralFeatures().add(
					feature);
		}

		// model migration
		for (int i = 0; i < references.size(); i++) {
			EReference reference = references.get(i);
			EStructuralFeature feature = features.get(i);
			for (Instance instance : model.getAllInstances(reference
					.getEContainingClass())) {
				Instance ref = instance.get(reference);
				if (ref != null) {
					Object value = ref.unset(mainFeature);
					instance.set(feature, value);
				}
			}
		}
	}
}
