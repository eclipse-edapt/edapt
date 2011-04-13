package org.eclipse.emf.edapt.declaration.generalization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: A6704A70D29E503D896F6BE32A080EF7
 */
@EdaptOperation(identifier = "specializeReference", label = "Specialize Reference", description = "In the metamodel, either the type or the multiplicity of a reference is specialized. In the model, values no longer conforming to the new type or multiplicity are removed.")
public class SpecializeReference extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference to be generalized")
	public EReference reference;

	/** {@description} */
	@EdaptParameter(description = "The new type of the reference")
	public EClass type;

	/** {@description} */
	@EdaptRestriction(parameter = "type")
	public List<String> checkType(EClass type) {
		if (type != reference.getEType()
				&& !type.getEAllSuperTypes().contains(reference.getEType())) {
			return Collections
					.singletonList("The type must be the same or more special");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@EdaptParameter(description = "The new lower bound of the reference")
	public int lowerBound = -1;

	/** {@description} */
	@EdaptParameter(description = "The new upper bound of the reference")
	public int upperBound;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (lowerBound < reference.getLowerBound()
				|| (upperBound > reference.getUpperBound() && reference
						.getUpperBound() != -1)) {
			result.add("The multiplicity must be the same or more special");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		type = reference.getEReferenceType();
		if (lowerBound == -1) {
			lowerBound = reference.getLowerBound();
		}
		if (upperBound == 0) {
			upperBound = reference.getUpperBound();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		if (reference.getEType() != type) {
			reference.setEType(type);
			if (reference.getEOpposite() != null) {
				type.getEStructuralFeatures().add(reference.getEOpposite());
			}
		}

		// model migration
		for (Instance instance : model.getAllInstances(reference
				.getEContainingClass())) {
			filterValueByType(instance, reference, type, model);
			filterValueByMultiplicity(instance, reference, upperBound, model);
		}

		// metamodel adaptation
		reference.setLowerBound(lowerBound);
		reference.setUpperBound(upperBound);
	}

	/** Remove all values that do not conform to a certain type. */
	private void filterValueByType(Instance instance, EReference reference,
			EClass type, Model model) {
		if (reference.isMany()) {
			List<Instance> values = new ArrayList<Instance>(instance
					.getLinks(reference));
			for (Instance value : values) {
				if (!value.instanceOf(type)) {
					instance.remove(reference, value);
					if (reference.isContainment()) {
						model.delete(value);
					}
				}
			}
		} else {
			Instance value = instance.get(reference);
			if (value != null) {
				if (!value.instanceOf(type)) {
					instance.unset(reference);
					if (reference.isContainment()) {
						model.delete(value);
					}
				}
			}
		}
	}

	/** Remove all values that do not conform to a certain upper bound. */
	private void filterValueByMultiplicity(Instance instance,
			EReference reference, int upperBound, Model model) {
		if (reference.isMany()) {
			List<Instance> values = new ArrayList<Instance>(instance
					.getLinks(reference));
			if (upperBound == 1 && values.size() > 1) {
				int i = 0;
				for (Instance value : values) {
					if (i >= upperBound) {
						instance.remove(reference, value);
						if (reference.isContainment()) {
							model.delete(value);
						}
					}
					i++;
				}
			}
		}
	}
}
