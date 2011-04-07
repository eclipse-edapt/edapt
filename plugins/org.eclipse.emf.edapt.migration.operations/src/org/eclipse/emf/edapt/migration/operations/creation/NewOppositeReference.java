package org.eclipse.emf.edapt.migration.operations.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 19F2B36886429EEACD51623A3C382A59
 */
@Operation(label = "Create Opposite Reference", description = "In the metamodel, an opposite is created for a reference. In the model, the opposite direction needs to be set.")
public class NewOppositeReference extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference for which the opposite is created")
	public EReference reference;

	/** {@description} */
	@Parameter(description = "The name of the opposite reference")
	public String name;

	/** {@description} */
	@Parameter(description = "The lower bound of the opposite reference")
	public int lowerBound;

	/** {@description} */
	@Parameter(description = "The upper bound of the opposite reference")
	public int upperBound;

	/** {@description} */
	@Parameter(description = "Whether the opposite reference is changeable")
	public Boolean changeable;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (reference.getEOpposite() != null) {
			result.add("The reference must not already have an opposite");
		}
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
		lowerBound = 0;
		upperBound = reference.isContainment() ? 1 : -1;
		changeable = true;
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
