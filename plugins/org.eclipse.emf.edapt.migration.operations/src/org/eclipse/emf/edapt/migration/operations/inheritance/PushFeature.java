package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 3A10850C9B3A8C2BA3E3B07C3FD6E50E
 */
@Operation(identifier = "pushFeature", label = "Push down Feature", description = "In the metamodel, a feature is pushed down to its sub classes. In the model, values are changed accordingly.")
public class PushFeature extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature to be pushed down")
	public EStructuralFeature feature;

	/** {@description} */
	@Restriction(parameter = "feature")
	public List<String> checkFeature(EStructuralFeature feature,
			Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EClass superClass = feature.getEContainingClass();
		if (feature instanceof EReference) {
			EReference reference = (EReference) feature;
			if (reference.getEOpposite() != null
					&& metamodel.getESubTypes(superClass).size() != 1) {
				result.add("If the feature has an opposite, "
						+ "then the super class may only have one sub type.");
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass superClass = feature.getEContainingClass();
		List<EClass> subClasses = metamodel.getESubTypes(superClass);

		boolean first = true;
		for (EClass subClass : subClasses) {
			if (first) {
				subClass.getEStructuralFeatures().add(feature);
				if (feature instanceof EReference) {
					EReference reference = (EReference) feature;
					if (reference.getEOpposite() != null) {
						reference.getEOpposite().setEType(subClass);
					}
				}
			} else {
				// metamodel adaptation
				EStructuralFeature clone = MetamodelUtils.copy(feature);
				subClass.getEStructuralFeatures().add(clone);

				// model migration
				for (Instance instance : model.getAllInstances(subClass)) {
					instance.set(clone, instance.unset(feature));
				}
			}
			first = false;
		}

		if (MetamodelUtils.isConcrete(superClass)) {
			for (Instance instance : model.getInstances(superClass)) {
				deleteFeatureValue(instance, feature);
			}
		}
	}
}
