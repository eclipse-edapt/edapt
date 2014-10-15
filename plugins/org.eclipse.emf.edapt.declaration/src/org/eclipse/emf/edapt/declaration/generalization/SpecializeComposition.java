package org.eclipse.emf.edapt.declaration.generalization;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelFactory;
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
 * @levd.rating YELLOW Hash: BA7888C5BD132AB47B43979B03E836F6
 */
@EdaptOperation(identifier = "specializeComposition", label = "Specialize Composition", description = "In the metamodel, the type of a containment reference is specialized by a new sub class. In the model, the values of this reference are migrated to the new type.")
public class SpecializeComposition extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The containment reference to be specialized")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference has to be a containment reference")
	public boolean checkReference(EReference reference) {
		return reference.isContainment();
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
		EClass newType = MetamodelFactory.newEClass(ePackage, name, superType);
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
