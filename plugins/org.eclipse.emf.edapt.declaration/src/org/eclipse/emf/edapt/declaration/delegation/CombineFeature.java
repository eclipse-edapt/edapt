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
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 3786A9B7666DDCE8867BE87A43066803
 */
@EdaptOperation(identifier = "combineFeature", label = "Combine Features over References", description = "In the metamodel, a number of features are combined in to a single feature by moving it over references to the same class. In the model, the values of the features are moved accordingly.")
public class CombineFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The features to be combined")
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
		for (final EReference reference : references) {
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
		final EClass eClass = references.get(0).getEReferenceType();
		final EStructuralFeature mainFeature = features.get(0);

		// metamodel adaptation
		eClass.getEStructuralFeatures().add(mainFeature);
		for (final EStructuralFeature feature : features) {
			if (feature != mainFeature) {
				metamodel.delete(feature);
			}
		}

		// model migration
		for (int i = 0; i < references.size(); i++) {
			final EReference reference = references.get(i);
			final EStructuralFeature feature = features.get(i);
			for (final Instance instance : model.getAllInstances(reference
				.getEContainingClass())) {
				final Object value = instance.unset(feature);
				final Instance ref = instance.get(reference);
				if (ref != null) {
					ref.set(mainFeature, value);
				}
			}
		}
	}
}
