package org.eclipse.emf.edapt.declaration.delegation;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 4E40BB4F1A65D29F9CC272E57A1ADC5B
 */
@EdaptOperation(identifier = "combineFeature", label = "Combine Features over References", description = "In the metamodel, a number of features are combined in to a single feature by moving it over references to the same class. In the model, the values of the features are moved accordingly.")
public class CombineFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The features to be combined")
	public List<EStructuralFeature> features;

	/** {@description} */
	@EdaptParameter(description = "The references over which the features are moved (in the same order)")
	public List<EReference> references;

	/** {@description} */
	@EdaptConstraint(description = "All references must have the same class as type")
	public boolean checkReferenceSameType() {
		return hasSameValue(references, EcorePackage.eINSTANCE.getETypedElement_EType());
	}

	/** {@description} */
	@EdaptConstraint(description = "There must be an equal number of features and references")
	public boolean checkFeatureSize() {
		return features.size() == references.size();
	}

	/** {@description} */
	@EdaptConstraint(description = "Each feature has to belong to its reference's class")
	public boolean checkFeatureParent() {
		for (EReference reference : references) {
			if (reference.getEContainingClass() != features.get(
					references.indexOf(reference)).getEContainingClass()) {
				return false;
			}
		}
		return true;
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
