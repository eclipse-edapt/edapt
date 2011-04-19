package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 6D7A1EE075C2BF5122AD639334D22622
 */
@EdaptOperation(identifier = "newOppositeReference", label = "Create Opposite Reference", description = "In the metamodel, an opposite is created for a reference. In the model, the opposite direction needs to be set.")
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
		EClass eClass = reference.getEReferenceType();
		EClass type = reference.getEContainingClass();

		// metamodel adaptation
		EReference opposite = MetamodelUtils.newEReference(eClass, name, type,
				lowerBound, upperBound, false);
		model.setEOpposite(reference, opposite);
		opposite.setChangeable(changeable);
	}
}
