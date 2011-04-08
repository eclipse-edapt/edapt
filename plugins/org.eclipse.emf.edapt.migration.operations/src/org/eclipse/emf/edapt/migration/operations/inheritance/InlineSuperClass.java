package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 53FD7B048C0E0377621030982A2DEF6F
 */
@Operation(label = "Inline Super Class", description = "In the metamodel, a super class is inlined into its sub classes. More specifically, its features are propagated to the sub classes. In the model, the values of these features have to be adapted accordingly.")
public class InlineSuperClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The super class to be inlined")
	public EClass superClass;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (MetamodelUtils.isConcrete(superClass)) {
			result.add("The super class must be abstract");
		}
		return result;
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
