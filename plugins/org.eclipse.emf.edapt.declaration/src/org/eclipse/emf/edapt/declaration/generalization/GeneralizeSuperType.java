package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 46DEE928672A2762CB56899D0FE31BD0
 */
@EdaptOperation(identifier = "generalizeSuperType", label = "Generalize Super Type", description = "In the metamodel, the super type of a class is replaced by its super types. In the model, the values of the features that the class inherits from that super type (excluding its super types) are deleted.")
public class GeneralizeSuperType extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The class of which the super type is replaced")
	public EClass eClass;

	/** {@description} */
	@EdaptParameter(description = "The super type to be replaced by its super types")
	public EClass superType;

	/** {@description} */
	@EdaptConstraint(restricts = "superType", description = "The super type to remove actually has to be a super type")
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
		eClass.getESuperTypes().addAll(superType.getESuperTypes());

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			for (EStructuralFeature feature : superType
					.getEStructuralFeatures()) {
				deleteFeatureValue(instance, feature);
			}
		}
	}
}
