package org.eclipse.emf.edapt.migration.operations.generalization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 955A6DCA0B9F2C079568E0FCF08A5120
 */
@Operation(identifier = "generalizeReference", label = "Generalize Reference", description = "In the metamodel, either the type or the multiplicity of a reference is generalized. In the model, nothing is changed.")
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
	public int lowerBound = -1;

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
		if (type == null) {
			type = reference.getEReferenceType();
		}
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
		reference.setLowerBound(lowerBound);
		reference.setUpperBound(upperBound);
	}
}
