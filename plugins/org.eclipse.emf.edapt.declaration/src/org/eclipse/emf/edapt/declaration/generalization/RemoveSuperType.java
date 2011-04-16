package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * @levd.rating YELLOW Hash: D68A02319E0DAA41F0865F923FDF02D0
 */
@EdaptOperation(identifier = "removeSuperType", label = "Remove Super Type", description = "In the metamodel, a super type is removed from a class. In the model, the values of the features inherited from that super type (including its super types) are deleted.")
public class RemoveSuperType extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The class from which the super type is removed")
	public EClass eClass;

	/** {@description} */
	@EdaptParameter(description = "The super type to be removed")
	public EClass superType;

	/** {@description} */
	@EdaptConstraint(restricts = "superType", description = "The super type to be removed actually has to be a super type of the class")
	public boolean checkSuperType(EClass superType) {
		return eClass.getESuperTypes().contains(superType);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (!eClass.getESuperTypes().isEmpty()) {
			superType = eClass.getESuperTypes().get(0);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.getESuperTypes().remove(superType);

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			for (EStructuralFeature feature : superType
					.getEAllStructuralFeatures()) {
				deleteFeatureValue(instance, feature);
			}
		}
	}
}
