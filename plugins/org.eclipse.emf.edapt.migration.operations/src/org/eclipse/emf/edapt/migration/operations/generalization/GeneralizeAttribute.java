package org.eclipse.emf.edapt.migration.operations.generalization;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 81C697C1D584352C55BEDAE451C05DF6
 */
@Operation(label = "Generalize Attribute", description = "In the metamodel, the multiplicity of an attribute is generalized. In the model, nothing is changed.")
public class GeneralizeAttribute extends OperationBase {

	/** {@description} */
	@Parameter(description = "The attribute to be generalized")
	public EAttribute attribute;

	/** {@description} */
	@Parameter(description = "The new lower bound of the attribute")
	public int lowerBound;

	/** {@description} */
	@Parameter(description = "The new upper bound of the attribute")
	public int upperBound;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (lowerBound > attribute.getLowerBound()
				|| (upperBound < attribute.getUpperBound() && attribute
						.getUpperBound() != -1)) {
			result.add("The multiplicity must be the same or more general");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		lowerBound = attribute.getLowerBound();
		upperBound = attribute.getUpperBound();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		attribute.setLowerBound(lowerBound);
		attribute.setUpperBound(upperBound);
	}
}
