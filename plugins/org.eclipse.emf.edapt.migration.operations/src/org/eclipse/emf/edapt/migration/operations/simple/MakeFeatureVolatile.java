package org.eclipse.emf.edapt.migration.operations.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 61BC289859929255248F86C8C293B417
 */
@Operation(label = "Make Feature Volatile", description = "In the metamodel, a feature is made volatile. In the model, its values have to be deleted.")
public class MakeFeatureVolatile extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature to be made volatile")
	public EStructuralFeature feature;

	/** {@description} */
	@Parameter(description = "Whether the feature is transient")
	public Boolean trans = true;

	/** {@description} */
	@Parameter(description = "Whether the feature is derived")
	public Boolean derived = true;

	/** {@description} */
	@Parameter(description = "Whether the feature is changeable")
	public Boolean changeable = false;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (feature.isVolatile()) {
			result.add("Feature must not be volatile");
		}
		return result;
	}

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
