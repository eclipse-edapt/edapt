package org.eclipse.emf.edapt.declaration.generalization;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 8C7CDFC7A2E7320DCB5C75AE3C2525F5
 */
@EdaptOperation(identifier = "specializeComposition", label = "Specialize Composition", description = "In the metamodel, the type of a containment reference is specialized by a new sub class. In the model, the values of this reference are migrated to the new type.")
public class SpecializeComposition extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The containment reference to be specialized")
	public EReference reference;

	/** {@description} */
	@EdaptRestriction(parameter = "reference")
	public List<String> checkReference(EReference reference) {
		List<String> result = new ArrayList<String>();
		if (!reference.isContainment()) {
			result.add("The reference has to be a containment reference");
		}
		return result;
	}

	/** {@description} */
	@EdaptParameter(description = "The package in which the sub class is created")
	public EPackage ePackage;

	/** {@description} */
	@EdaptParameter(description = "The name of the sub class")
	public String name;

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass superType = reference.getEReferenceType();
		EClass eClass = reference.getEContainingClass();

		// metamodel adaptation
		EClass newType = MetamodelUtils.newEClass(ePackage, name, superType);
		reference.setEType(newType);

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			if (instance.isSet(reference)) {
				Object value = instance.get(reference);
				if (reference.isMany()) {
					List<Instance> valueInstances = (List<Instance>) value;
					for (Instance valueInstance : valueInstances) {
						migrate(valueInstance, superType, newType, model);
					}
				} else if (value != null) {
					Instance valueInstance = (Instance) value;
					migrate(valueInstance, superType, newType, model);
				}
			}
		}
	}

	/** Migrate an instance from a super type to a sub type. */
	private void migrate(Instance valueInstance, EClass superType,
			EClass newType, Model model) {
		if (valueInstance.getEClass() == superType) {
			valueInstance.migrate(newType);
		} else {
			model.delete(valueInstance);
		}
	}
}
