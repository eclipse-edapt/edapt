package org.eclipse.emf.edapt.declaration.delegation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 1461A20889B02B650804F895245EB12B
 */
@EdaptOperation(identifier = "combineFeature", label = "Combine Features over References", description = "In the metamodel, a number of features are combined in to a single feature by moving it over references to the same class. In the model, the values of the features are moved accordingly.")
public class CombineFeature extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The features to be combined")
	public List<EStructuralFeature> features;

	/** {@description} */
	@EdaptParameter(description = "The references over which the features are moved (in the same order)")
	public List<EReference> references;

	/** Check whether a reference is allowed. */
	@EdaptRestriction(parameter = "reference")
	public List<String> checkReferences(EReference reference) {
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!references.isEmpty()) {
			EClass eClass = references.get(0).getEReferenceType();
			for (EReference reference : references) {
				if (reference.getEType() != eClass) {
					result
							.add("All references must have the same class as type");
					break;
				}
			}
		}
		if (features.size() != references.size()) {
			result.add("There must be an equal number "
					+ "of features and references");
		}
		for (EReference reference : references) {
			if (reference.getEContainingClass() != features.get(
					references.indexOf(reference)).getEContainingClass()) {
				result.add("Each feature has to belong "
						+ "to its reference's class");
				break;
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass eClass = references.get(0).getEReferenceType();
		EStructuralFeature mainFeature = features.get(0);

		// metamodel adaptation
		eClass.getEStructuralFeatures().add(mainFeature);
		for (EStructuralFeature feature : features) {
			if (feature != mainFeature) {
				metamodel.delete(feature);
			}
		}

		// model migration
		for (int i = 0; i < references.size(); i++) {
			EReference reference = references.get(i);
			EStructuralFeature feature = features.get(i);
			for (Instance instance : model.getAllInstances(reference
					.getEContainingClass())) {
				Object value = instance.unset(feature);
				Instance ref = instance.get(reference);
				if (ref != null) {
					ref.set(mainFeature, value);
				}
			}
		}
	}
}
