package org.eclipse.emf.edapt.migration.declaration.operations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.EcoreUtils;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

@Operation(label = "Push down Feature", description = "In the metamodel, a feature is pushed down to its sub classes. In the model, values are changed accordingly.")
public class PushFeature extends OperationBase {

	@Parameter(description = "The feature to be pushed down")
	public EStructuralFeature feature;

	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();

		EClass superClass = feature.getEContainingClass();

		if (feature instanceof EReference) {
			EReference reference = (EReference) feature;
			if (reference.getEOpposite() != null
					&& EcoreUtils.getInverse(superClass,
							EcorePackage.Literals.ECLASS__ESUPER_TYPES,
							metamodel.getEPackages()).size() != 1) {
				result
						.add("If the feature has an opposite, then the super class may only have one sub type.");
			}
		}

		return result;
	}

	@Override
	public void execute(Metamodel metamodel, Model model) {
		boolean first = true;
		EClass superClass = feature.getEContainingClass();
		List<EClass> subClasses = (List<EClass>) EcoreUtils.getInverse(superClass,
				EcorePackage.Literals.ECLASS__ESUPER_TYPES,
				metamodel.getEPackages());
		for(EClass subClass : subClasses) {
			if(first) {
				subClass.getEStructuralFeatures().add(feature);
				if(feature instanceof EReference) {
					EReference reference = (EReference) feature;
					if(reference.getEOpposite() != null) {
						reference.getEOpposite().setEType(subClass);
					}
				}
			}
			else {
				// metamodel adaptation
				EStructuralFeature clone = (EStructuralFeature) MetamodelUtils.copy(feature);
				subClass.getEStructuralFeatures().add(clone);
		
				// model migration
				for (Instance instance : model.getAllInstances(subClass)) {
					instance.set(clone, instance.unset(feature));
				}
			}
			first = false;
		}
		
		if(!superClass.isAbstract()) {
			for (Instance instance : model.getInstances(superClass)) {
				deleteFeatureValue(instance, feature);
			}
		}
	}
}
