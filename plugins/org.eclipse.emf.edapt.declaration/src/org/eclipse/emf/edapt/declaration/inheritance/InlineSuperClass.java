package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 6E5AA872EFE3005C773993D2408C3884
 */
@EdaptOperation(identifier = "inlineSuperClass", label = "Inline Super Class", description = "In the metamodel, a super class is inlined into its sub classes. More specifically, its features are propagated to the sub classes. In the model, the values of these features have to be adapted accordingly.")
public class InlineSuperClass extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The super class to be inlined")
	public EClass superClass;

	/** {@description} */
	@EdaptConstraint(restricts = "superClass", description = "The super class must be abstract")
	public boolean checkSuperClassAbstract(EClass superClass) {
		return !MetamodelUtils.isConcrete(superClass);
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {

		for (EStructuralFeature feature : new ArrayList<EStructuralFeature>(
				superClass.getEStructuralFeatures())) {
			PushFeature operation = new PushFeature();
			operation.feature = feature;
			operation.execute(metamodel, model);
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
