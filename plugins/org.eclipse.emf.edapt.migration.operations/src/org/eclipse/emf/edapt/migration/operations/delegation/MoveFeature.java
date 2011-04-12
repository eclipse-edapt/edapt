package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.Collections;
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
 * @levd.rating YELLOW Hash: 0174E27A328E9B45977B5CD1C8631CA4
 */
@Operation(identifier = "moveFeature", label = "Move Feature along Reference", description = "In the metamodel, a feature is moved along a single-valued reference. In the model, values are moved accordingly.")
public class MoveFeature extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature to be moved")
	public EStructuralFeature feature;

	/** {@description} */
	@Parameter(description = "The reference along which the feature is moved")
	public EReference reference;

	/** {@description} */
	@Restriction(parameter = "reference")
	public List<String> checkReference(EReference reference) {
		EClass sourceClass = feature.getEContainingClass();
		if (!sourceClass.getEAllStructuralFeatures().contains(reference)) {
			return Collections.singletonList("The reference must be available "
					+ "in the same class as the feature");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (reference != null) {
			if (reference.getLowerBound() != 1
					|| reference.getUpperBound() != 1) {
				result.add("The multiplicity of the reference must "
						+ "be single-valued and obligatory");
			}
			if (reference.getEOpposite() != null
					&& reference.getEOpposite().getUpperBound() != 1) {
				result.add("The multiplicity of its opposite "
						+ "reference must be single-valued");
			}
			EClass targetClass = reference.getEReferenceType();
			if (targetClass.getEStructuralFeature(feature.getName()) != null) {
				result.add("A feature with that name already "
						+ "exists in the target class");
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EClass sourceClass = feature.getEContainingClass();
		EClass targetClass = reference.getEReferenceType();

		// metamodel adaptation
		targetClass.getEStructuralFeatures().add(feature);

		// model migration
		for (Instance instance : model.getAllInstances(sourceClass)) {
			Instance target = instance.get(reference);
			if (instance.isSet(feature)) {
				Object value = instance.unset(feature);
				target.set(feature, value);
			}
		}
	}
}
