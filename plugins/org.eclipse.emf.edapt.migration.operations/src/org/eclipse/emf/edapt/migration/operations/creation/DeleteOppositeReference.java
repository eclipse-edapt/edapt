package org.eclipse.emf.edapt.migration.operations.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.migration.Instance;
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
 * @levd.rating YELLOW Hash: 64E0475FD8822A0D9E81D18A5BA8E995
 */
@Operation(label = "Delete Opposite Reference", description = "In the metamodel, the opposite of a reference is deleted. In the model, its values are deleted, too.")
public class DeleteOppositeReference extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference whose opposite should be deleted")
	public EReference reference;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EReference opposite = reference.getEOpposite();
		if(opposite == null) {
			result.add("The reference needs to define an opposite");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EReference opposite = reference.getEOpposite();

		EClass eClass = opposite.getEContainingClass();

		// metamodel adaptation
		metamodel.delete(opposite);

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			deleteFeatureValue(instance, opposite);
		}
	}
}
