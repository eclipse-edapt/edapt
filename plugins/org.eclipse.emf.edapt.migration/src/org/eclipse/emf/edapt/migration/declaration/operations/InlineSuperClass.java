package org.eclipse.emf.edapt.migration.declaration.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.common.EcoreUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

@Operation(label = "Inline Super Class", description = "In the metamodel, a super class is inlined into its sub classes. More specifically, its features are propagated to the sub classes. In the model, the values of these features have to be adapted accordingly.")
public class InlineSuperClass extends OperationBase {

	@Parameter(description = "The super class to be inlined")
	public EClass superClass;

	@Restriction(parameter = "superClass")
	public List<String> checkSuperClass(EClass superClass) {
		if (!superClass.isAbstract()) {
			return Collections
					.singletonList("The super class must be abstract");
		}
		return Collections.emptyList();
	}

	@Override
	public void execute(Metamodel metamodel, Model model) {
		for(EStructuralFeature feature : new ArrayList<EStructuralFeature>(superClass.getEStructuralFeatures())) {
			PushFeature pushFeature = new PushFeature();
			pushFeature.feature = feature;
			pushFeature.execute(metamodel, model);
		}
		
		List<EClass> subClasses = (List<EClass>) EcoreUtils.getInverse(
				superClass, EcorePackage.Literals.ECLASS__ESUPER_TYPES,
				metamodel.getEPackages());
		for(EClass subClass : subClasses) {
			subClass.getESuperTypes().remove(superClass);
			for(EClass superSuperClass : superClass.getESuperTypes()) {
				if(!subClass.getEAllSuperTypes().contains(superSuperClass)) {
					subClass.getESuperTypes().add(superSuperClass);
				}
			}
		}
		
		EcoreUtil.delete(superClass);
	}
}
