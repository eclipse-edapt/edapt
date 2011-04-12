package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * @levd.rating YELLOW Hash: 2E8D4BF98500A2AF7BDB16178B50A00C
 */
@Operation(identifier = "collectFeature", label = "Collect Feature over Reference", description = "In the metamodel, a feature is moved opposite to a multi-valued reference. In the model, the values of the feature are aggregated accordingly.")
public class CollectFeature extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature to be moved")
	public EStructuralFeature feature;

	/** {@description} */
	@Parameter(description = "The reference opposite to which the feature is moved")
	public EReference reference;

	/** {@description} */
	@Restriction(parameter = "reference")
	public List<String> checkReference(EReference reference) {
		List<String> result = new ArrayList<String>();
		if (!((feature.isMany() && reference.isMany()) || !reference.isMany())) {
			result.add("Both feature and reference must be "
					+ "multi-valued or the reference must be single-valued");
		}
		if (!reference.getEReferenceType().getEStructuralFeatures().contains(
				feature)) {
			result.add("The feature must belong to the reference's type");
		}
		return result;
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
					((List) target.get(feature)).addAll(sourceValue);
				}
			} else {
				Instance source = target.get(reference);
				if (source != null) {
					target.set(feature, source.unset(feature));
				}
			}
		}
		for (Instance source : model.getAllInstances(sourceClass)) {
			deleteFeatureValue(source, feature);
		}
	}
}
