package org.eclipse.emf.edapt.declaration.generalization;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 9308DFB3E173BE5B141E082903B9D16E
 */
@EdaptOperation(identifier = "generalizeAttribute", label = "Generalize Attribute", description = "In the metamodel, the multiplicity of an attribute is generalized. In the model, nothing is changed.")
public class GeneralizeAttribute extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The attribute to be generalized")
	public EAttribute attribute;

	/** {@description} */
	@EdaptParameter(description = "The new lower bound of the attribute")
	public int lowerBound = -1;

	/** {@description} */
	@EdaptParameter(description = "The new upper bound of the attribute")
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
		if (lowerBound == -1) {
			lowerBound = attribute.getLowerBound();
		}
		if (upperBound == 0) {
			upperBound = attribute.getUpperBound();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		attribute.setLowerBound(lowerBound);
		attribute.setUpperBound(upperBound);
	}
}
