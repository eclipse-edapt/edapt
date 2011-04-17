package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 6692AA71F8497C1DD8C524B2FF0C26E1
 */
@EdaptOperation(identifier = "deleteOppositeReference", label = "Delete Opposite Reference", description = "In the metamodel, the opposite of a reference is deleted. In the model, its values are deleted, too.")
public class DeleteOppositeReference extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The reference whose opposite should be deleted")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference needs to define an opposite")
	public boolean checkReferenceOpposite(EReference reference) {
		return reference.getEOpposite() != null;
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