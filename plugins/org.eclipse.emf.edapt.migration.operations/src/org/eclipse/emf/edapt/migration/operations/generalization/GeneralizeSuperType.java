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
 * @levd.rating YELLOW Hash: D2B07212D24FDF07106B2E77CA4F4926
 */
@Operation(label = "Generalize Super Type", description = "In the metamodel, the super type of a class is replaced by its super types. In the model, the values of the features that the class inherits from that super type (excluding its super types) are deleted.")
public class GeneralizeSuperType extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class of which the super type is replaced")
	public EClass eClass;

	/** {@description} */
	@Parameter(description = "The super type to be replaced by its super types")
	public EClass superType;

	/** {@description} */
	@Restriction(parameter = "superType")
	public List<String> checkSuperType(EClass superType) {
		if (!eClass.getESuperTypes().contains(superType)) {
			return Collections.singletonList("The super type to remove "
					+ "actually has to be a super type");
		}
		return Collections.emptyList();
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
