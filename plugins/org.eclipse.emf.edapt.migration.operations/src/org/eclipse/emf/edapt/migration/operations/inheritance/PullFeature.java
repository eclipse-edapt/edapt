package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.operations.generalization.GeneralizeReference;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: ECCF7A2562AF3A30C591E50465C90AD0
 */
@Operation(label = "Pull up Feature", description = "In the metamodel, a number of features are pulled up into a common super class. In the model, values are changed accordingly.")
public class PullFeature extends OperationBase {

	/** {@description} */
	@Parameter(description = "The features to be pulled up")
	public List<EStructuralFeature> features;

	/** {@description} */
	@Parameter(description = "The super class to which the features are pulled")
	public EClass targetClass;

	/** {@description} */
	@Restriction(parameter = "targetClass")
	public List<String> checkTargetClass(EClass targetClass) {
		for (EStructuralFeature feature : features) {
			if (!feature.getEContainingClass().getESuperTypes().contains(
					targetClass)) {
				return Collections.singletonList("The features' classes "
						+ "must have a common super type");
			}
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!hasSameValue(features, EcorePackage.Literals.ETYPED_ELEMENT__ETYPE)) {
			result.add("The features' types have to be the same");
		}
		if (!hasSameValue(features,
				EcorePackage.Literals.ETYPED_ELEMENT__LOWER_BOUND)
				|| !hasSameValue(features,
						EcorePackage.Literals.ETYPED_ELEMENT__UPPER_BOUND)) {
			result.add("The features' multiplicities have to be the same");
		}
		if (isOfType(features, EcorePackage.Literals.EREFERENCE)) {
			if (!hasSameValue(features,
					EcorePackage.Literals.EREFERENCE__CONTAINMENT)) {
				result.add("The features have to be "
						+ "all containment references or not");
			}
			if (!hasValue(features,
					EcorePackage.Literals.EREFERENCE__EOPPOSITE, null)) {
				result.add("The features must not have opposite references");
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (targetClass == null) {
			List<EClass> superTypes = features.get(0).getEContainingClass()
					.getESuperTypes();
			if (!superTypes.isEmpty()) {
				targetClass = superTypes.get(0);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EStructuralFeature mainFeature = features.get(0);

		targetClass.getEStructuralFeatures().add(mainFeature);
		if (mainFeature instanceof EReference) {
			EReference mainReference = (EReference) mainFeature;
			if (mainReference.getEOpposite() != null) {
				GeneralizeReference operation = new GeneralizeReference();
				operation.reference = mainReference.getEOpposite();
				operation.type = targetClass;
				operation.initialize(metamodel);
				operation.execute(metamodel, model);
			}
		}
		for (EStructuralFeature feature : features) {
			if (feature != mainFeature) {
				ReplaceFeature operation = new ReplaceFeature();
				operation.toReplace = feature;
				operation.replaceBy = mainFeature;
				operation.execute(metamodel, model);
			}
		}
	}
}
