package org.eclipse.emf.edapt.declaration.simple;

import java.util.ArrayList;
import java.util.List;

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
 * @levd.rating YELLOW Hash: 61BC289859929255248F86C8C293B417
 */
@EdaptOperation(identifier = "makeFeatureVolatile", label = "Make Feature Volatile", description = "In the metamodel, a feature is made volatile. In the model, its values have to be deleted.")
public class MakeFeatureVolatile extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The feature to be made volatile")
	public EStructuralFeature feature;

	/** {@description} */
	@EdaptRestriction(parameter = "feature")
	public List<String> checkFeature(EStructuralFeature feature) {
		List<String> result = new ArrayList<String>();
		if (feature.isVolatile()) {
			result.add("Feature must not be volatile");
		}
		return result;
	}

	/** {@description} */
	@EdaptParameter(description = "Whether the feature is transient")
	public Boolean trans = true;

	/** {@description} */
	@EdaptParameter(description = "Whether the feature is derived")
	public Boolean derived = true;

	/** {@description} */
	@EdaptParameter(description = "Whether the feature is changeable")
	public Boolean changeable = false;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		feature.setVolatile(true);
		feature.setTransient(trans);
		feature.setDerived(derived);
		feature.setChangeable(changeable);
		if (feature instanceof EReference) {
			EReference reference = (EReference) feature;
			if (reference.getEOpposite() != null) {
				DropOpposite operation = new DropOpposite();
				operation.reference = reference;
				operation.execute(metamodel, model);
			}
		}

		// model migration
		for (Instance instance : model.getAllInstances(feature
				.getEContainingClass())) {
			deleteFeatureValue(instance, feature);
		}
	}
}
