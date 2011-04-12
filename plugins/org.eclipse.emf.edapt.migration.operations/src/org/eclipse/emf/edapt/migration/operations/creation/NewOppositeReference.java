package org.eclipse.emf.edapt.migration.operations.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 5A4ADB004FD92006DAAED5E883F23ED1
 */
@Operation(identifier = "newOppositeReference", label = "Create Opposite Reference", description = "In the metamodel, an opposite is created for a reference. In the model, the opposite direction needs to be set.")
public class NewOppositeReference extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference for which the opposite is created")
	public EReference reference;

	/** {@description} */
	@Restriction(parameter = "reference")
	public List<String> checkReference(EReference reference) {
		List<String> result = new ArrayList<String>();
		if (reference.getEOpposite() != null) {
			result.add("The reference must not already have an opposite");
		}
		return result;
	}

	/** {@description} */
	@Parameter(description = "The name of the opposite reference")
	public String name;

	/** {@description} */
	@Parameter(description = "The lower bound of the opposite reference")
	public int lowerBound = 0;

	/** {@description} */
	@Parameter(description = "The upper bound of the opposite reference")
	public int upperBound;

	/** {@description} */
	@Parameter(description = "Whether the opposite reference is changeable")
	public Boolean changeable = true;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (reference.isContainment() && upperBound != 1) {
			result.add("In case of a containment reference, "
					+ "the upper bound of the opposite reference must be 1.");
		}
		if (!reference.isContainment() && upperBound == 1) {
			result.add("In case of a cross reference, "
					+ "the upper bound of the opposite reference must be -1.");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (upperBound == 0) {
			upperBound = reference.isContainment() ? 1 : -1;
		}
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
