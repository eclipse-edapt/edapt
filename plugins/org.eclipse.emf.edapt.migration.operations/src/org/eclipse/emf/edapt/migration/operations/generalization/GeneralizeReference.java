package org.eclipse.emf.edapt.migration.operations.generalization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: D062D5E356A02FEA841B33272708F307
 */
@Operation(label = "Generalize Reference", description = "In the metamodel, either the type or the multiplicity of a reference is generalized. In the model, nothing is changed.")
public class GeneralizeReference extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference to be generalized")
	public EReference reference;

	/** {@description} */
	@Parameter(description = "The new type of the reference")
	public EClass type;

	/** {@description} */
	@Restriction(parameter = "type")
	public List<String> checkType(EClass type) {
		EClass referenceType = reference.getEReferenceType();
		if (type != referenceType
				&& !referenceType.getEAllSuperTypes().contains(type)) {
			return Collections
					.singletonList("The type must be the same or more general");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@Parameter(description = "The new lower bound of the reference")
	public int lowerBound;

	/** {@description} */
	@Parameter(description = "The new upper bound of the reference")
	public int upperBound;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (lowerBound > reference.getLowerBound()
				|| (upperBound < reference.getUpperBound() && reference
						.getUpperBound() != -1)) {
			result.add("The multiplicity must be the same or more general");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		type = reference.getEReferenceType();
		lowerBound = reference.getLowerBound();
		upperBound = reference.getUpperBound();
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
		reference.setLowerBound(lowerBound);
		reference.setUpperBound(upperBound);
	}
}
