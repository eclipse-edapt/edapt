package org.eclipse.emf.edapt.declaration.inheritance;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 2CC0776BB65406CC399834E3B02CA0AA
 */
@EdaptOperation(identifier = "inlineSubClass", label = "Inline Sub Class", description = "In the metamodel, the sub class is deleted. In the model, all instances of this sub class are migrated to its super class.")
public class InlineSubClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The class to be inlined")
	public EClass subClass;

	/** {@description} */
	@EdaptConstraint(restricts = "subClass", description = "The super class must not be abstract")
	public boolean checkSubClass(EClass subClass) {
		return MetamodelUtils.isConcrete(subClass.getESuperTypes().get(0));
	}

	/** {@description} */
	@EdaptConstraint(restricts = "subClass", description = "The sub class must have exactly one super type")
	public boolean checkSubClassSingleSuperType(EClass subClass) {
		return subClass.getESuperTypes().size() == 1;
	}

	/** {@description} */
	@EdaptConstraint(restricts = "subClass", description = "The sub class must not have features")
	public boolean checkSubClassNoFeatures(EClass subClass) {
		return subClass.getEStructuralFeatures().isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "subClass", description = "The sub class must not have sub types")
	public boolean checkSubClassNoSubTypes(EClass subClass, Metamodel metamodel) {
		return metamodel.getESubTypes(subClass).isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass superClass = subClass.getESuperTypes().get(0);

		// metamodel adaptation
		metamodel.delete(subClass);

		// model migration
		for (Instance instance : model.getAllInstances(subClass)) {
			instance.migrate(superClass);
		}
	}
}
