package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 4BC927F0EC381FE31F91728A6AD62908
 */
@EdaptOperation(identifier = "inlineSuperClass", label = "Inline Super Class", description = "In the metamodel, a super class is inlined into its sub classes. More specifically, its features are propagated to the sub classes. In the model, the values of these features have to be adapted accordingly.")
public class InlineSuperClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The super class to be inlined")
	public EClass superClass;

	/** {@description} */
	@EdaptConstraint(restricts = "superClass", description = "The super class must be abstract")
	public boolean checkSuperClassAbstract(EClass superClass) {
		return !MetamodelUtils.isConcrete(superClass);
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model)
			throws MigrationException {

		for (EStructuralFeature feature : new ArrayList<EStructuralFeature>(
				superClass.getEStructuralFeatures())) {
			PushFeature operation = new PushFeature();
			operation.feature = feature;
			operation.checkAndExecute(metamodel, model);
		}

		List<EClass> subClasses = metamodel.getESubTypes(superClass);
		for (EClass subClass : subClasses) {
			subClass.getESuperTypes().remove(superClass);
			for (EClass superSuperClass : superClass.getESuperTypes()) {
				if (!subClass.getEAllSuperTypes().contains(superSuperClass)) {
					subClass.getESuperTypes().add(superSuperClass);
				}
			}
		}

		metamodel.delete(superClass);
	}
}
