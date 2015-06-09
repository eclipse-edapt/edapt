package org.eclipse.emf.edapt.declaration.inheritance;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: F68EFAD9047D85BBB05F8215A441EA75
 */
@EdaptOperation(identifier = "inlineSubClass", label = "Inline Sub Class", description = "In the metamodel, the sub class is deleted. In the model, all instances of this sub class are migrated to its super class.")
public class InlineSubClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The class to be inlined")
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
		final EClass superClass = subClass.getESuperTypes().get(0);

		// metamodel adaptation
		metamodel.delete(subClass);

		// model migration
		for (final Instance instance : model.getAllInstances(subClass)) {
			instance.migrate(superClass);
		}
	}
}
