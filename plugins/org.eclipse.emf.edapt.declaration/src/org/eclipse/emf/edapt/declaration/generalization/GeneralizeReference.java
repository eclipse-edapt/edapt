package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
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
 * @levd.rating YELLOW Hash: 213F3C0F7937CA73383CEEEA52BA9E27
 */
@EdaptOperation(identifier = "generalizeReference", label = "Generalize Reference", description = "In the metamodel, either the type or the multiplicity of a reference is generalized. In the model, nothing is changed.")
public class GeneralizeReference extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The reference to be generalized")
	public EReference reference;

	/** {@description} */
	@EdaptParameter(description = "The new type of the reference")
	public EClass type;

	/** {@description} */
	@EdaptConstraint(restricts = "type", description = "The type must be the same or more general")
	public boolean checkType(EClass type) {
		EClass referenceType = reference.getEReferenceType();
		return type.isSuperTypeOf(referenceType);
	}

	/** {@description} */
	@EdaptParameter(description = "The new lower bound of the reference")
	public int lowerBound;

	/** {@description} */
	@EdaptParameter(description = "The new upper bound of the reference")
	public int upperBound;

	/** {@description} */
	@EdaptConstraint(description = "The multiplicity must be the same or more general")
	public boolean checkSameOrExtendedMultiplicity() {
		return lowerBound <= reference.getLowerBound()
				&& (upperBound >= reference.getUpperBound() && reference
						.getUpperBound() != -1);
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
