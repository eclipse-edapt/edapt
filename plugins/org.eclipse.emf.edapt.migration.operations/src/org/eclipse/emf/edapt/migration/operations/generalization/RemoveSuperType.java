package org.eclipse.emf.edapt.migration.operations.generalization;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: FFF6A3733C03CF77D57DF243915166F2
 */
@Operation(label = "Remove Super Type", description = "In the metamodel, a super type is removed from a class. In the model, the values of the features inherited from that super type (including its super types) are deleted.")
public class RemoveSuperType extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class from which the super type is removed")
	public EClass eClass;

	/** {@description} */
	@Parameter(description = "The super type to be removed")
	public EClass superType;

	/** {@description} */
	@Restriction(parameter = "superType")
	public List<String> checkSuperType(EClass superType) {
		if (!eClass.getESuperTypes().contains(superType)) {
			return Collections.singletonList("The super type to be removed "
					+ "actually has to be a super type of the class");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		superType = eClass.getESuperTypes().get(0);
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
