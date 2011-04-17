package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 5863D83B24D97519835DFB5C1FD5A8A9
 */
@EdaptOperation(identifier = "generalizeAttribute", label = "Generalize Attribute", description = "In the metamodel, the multiplicity of an attribute is generalized. In the model, nothing is changed.")
public class GeneralizeAttribute extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The attribute to be generalized")
	public EAttribute attribute;

	/** {@description} */
	@EdaptParameter(description = "The new lower bound of the attribute")
	public int lowerBound;

	/** {@description} */
	@EdaptParameter(description = "The new upper bound of the attribute")
	public int upperBound;

	/** {@description} */
	@EdaptConstraint(description = "The multiplicity must be the same or more general")
	public boolean checkSameOrExtendedMultiplicity() {
		return lowerBound <= attribute.getLowerBound()
				&& (upperBound >= attribute.getUpperBound() || upperBound == -1);
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
