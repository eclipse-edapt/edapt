package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: C643742FE6A451C96426FB8DC2696DCC
 */
@EdaptOperation(identifier = "newOppositeReference", label = "Create Opposite Reference", description = "In the metamodel, an opposite is created for a reference. In the model, the opposite direction needs to be set.", breaking = true)
public class NewOppositeReference extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference for which the opposite is created")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must not already have an opposite")
	public boolean checkReference(EReference reference) {
		return reference.getEOpposite() == null;
	}

	/** {@description} */
	@EdaptParameter(description = "The name of the opposite reference")
	public String name;

	/** {@description} */
	@EdaptParameter(description = "The lower bound of the opposite reference")
	public int lowerBound = 0;

	/** {@description} */
	@EdaptParameter(description = "The upper bound of the opposite reference")
	public int upperBound;

	/** {@description} */
	@EdaptParameter(description = "Whether the opposite reference is changeable")
	public Boolean changeable = true;

	/** {@description} */
	@EdaptConstraint(description = "In case of a containment reference, the upper bound of the opposite reference must be 1.")
	public boolean checkContainmentSingleValued() {
		return !reference.isContainment() || upperBound == 1;
	}

	/** {@description} */
	@EdaptConstraint(description = "In case of a cross reference, the upper bound of the opposite reference must be -1.")
	public boolean checkCrossReferenceManyValued() {
		return reference.isContainment() || upperBound == -1;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		upperBound = reference.isContainment() ? 1 : -1;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		final EClass eClass = reference.getEReferenceType();
		final EClass type = reference.getEContainingClass();

		// metamodel adaptation
		final EReference opposite = MetamodelFactory.newEReference(eClass, name, type,
			lowerBound, upperBound, false);
		metamodel.setEOpposite(reference, opposite);
		opposite.setChangeable(changeable);
	}
}
