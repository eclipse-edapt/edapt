package org.eclipse.emf.edapt.declaration.delegation;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * @levd.rating YELLOW Hash: B1A7CCFB373BE8799ACF7F5731B2DBE0
 */
@EdaptOperation(identifier = "collectFeature", label = "Collect Feature over Reference", description = "In the metamodel, a feature is moved opposite to a multi-valued reference. In the model, the values of the feature are aggregated accordingly.")
public class CollectFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature to be moved")
	public EStructuralFeature feature;

	/** {@description} */
	@EdaptParameter(description = "The reference opposite to which the feature is moved")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "Both feature and reference must be multi-valued or the reference must be single-valued")
	public boolean checkReferenceMultiplicity(EReference reference) {
		return (feature.isMany() && reference.isMany()) || !reference.isMany();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The feature must belong to the reference's type")
	public boolean checkFeatureBelongsToReferenceType(EReference reference) {
		return reference.getEReferenceType().getEStructuralFeatures()
				.contains(feature);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EClass sourceClass = feature.getEContainingClass();
		EClass targetClass = reference.getEContainingClass();

		// metamodel adaptation
		targetClass.getEStructuralFeatures().add(feature);

		// model migration
		for (Instance target : model.getAllInstances(targetClass)) {
			if (reference.isMany()) {
				for (Instance source : target.getLinks(reference)) {
					List sourceValue = source.unset(feature);
					target.<List> get(feature).addAll(sourceValue);
				}
			} else {
				Instance source = target.get(reference);
				if (source != null) {
					if (feature.isMany()) {
						target.<List> get(feature).addAll(
								source.<List> unset(feature));
					} else {
						target.set(feature, source.unset(feature));
					}
				}
			}
		}
		for (Instance source : model.getAllInstances(sourceClass)) {
			deleteFeatureValue(source, feature);
		}
	}
}
