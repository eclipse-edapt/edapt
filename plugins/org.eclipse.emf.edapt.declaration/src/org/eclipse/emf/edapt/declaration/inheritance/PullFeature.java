package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.declaration.generalization.GeneralizeReference;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 294397E02180B9715AEEFB4A82A48B5A
 */
@EdaptOperation(identifier = "pullFeature", label = "Pull up Feature", description = "In the metamodel, a number of features are pulled up into a common super class. In the model, values are changed accordingly.")
public class PullFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The features to be pulled up")
	public List<EStructuralFeature> features;

	/** {@description} */
	@EdaptParameter(description = "The super class to which the features are pulled")
	public EClass targetClass;

	/** {@description} */
	@EdaptConstraint(restricts = "targetClass", description = "The features' classes must have a common super type")
	public boolean checkTargetClassCommonSuperType(EClass targetClass) {
		for (EStructuralFeature feature : features) {
			if (!feature.getEContainingClass().getESuperTypes()
					.contains(targetClass)) {
				return false;
			}
		}
		return true;
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must not have opposite references")
	public boolean checkReferencesOpposite() {
		EcorePackage mmm = EcorePackage.eINSTANCE;
		if (features.size() > 1) {
			return !isOfType(features, mmm.getEReference())
					|| hasValue(features, mmm.getEReference_EOpposite(), null);
		}
		return true;
	}

	/** {@description} */
	@EdaptConstraint(description = "The features have to be all containment references or not")
	public boolean checkReferencesContainment() {
		EcorePackage mmm = EcorePackage.eINSTANCE;
		return !isOfType(features, mmm.getEReference())
				|| hasSameValue(features, mmm.getEReference_Containment());
	}

	/** {@description} */
	@EdaptConstraint(description = "The features' multiplicities have to be the same")
	public boolean checkFeaturesSameMultiplicity() {
		EcorePackage mmm = EcorePackage.eINSTANCE;
		return hasSameValue(features, mmm.getETypedElement_LowerBound())
				&& hasSameValue(features, mmm.getETypedElement_UpperBound());
	}

	/** {@description} */
	@EdaptConstraint(description = "The features' types have to be the same")
	public boolean checkFeaturesSameType() {
		return hasSameValue(features,
				EcorePackage.Literals.ETYPED_ELEMENT__ETYPE);
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
	public void execute(Metamodel metamodel, Model model)
			throws MigrationException {
		EStructuralFeature mainFeature = features.get(0);

		targetClass.getEStructuralFeatures().add(mainFeature);
		if (mainFeature instanceof EReference) {
			EReference mainReference = (EReference) mainFeature;
			if (mainReference.getEOpposite() != null) {
				GeneralizeReference operation = new GeneralizeReference();
				operation.reference = mainReference.getEOpposite();
				operation.initialize(metamodel);
				operation.type = targetClass;
				operation.checkAndExecute(metamodel, model);
			}
		}
		for (EStructuralFeature feature : features) {
			if (feature != mainFeature) {
				ReplaceFeature operation = new ReplaceFeature();
				operation.toReplace = feature;
				operation.replaceBy = mainFeature;
				operation.checkAndExecute(metamodel, model);
			}
		}
	}
}
