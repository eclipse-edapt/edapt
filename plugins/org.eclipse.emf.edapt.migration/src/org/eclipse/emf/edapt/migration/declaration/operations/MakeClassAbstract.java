package org.eclipse.emf.edapt.migration.declaration.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.EcoreUtils;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

@Operation(label = "Make Class Abstract", description = "In the metamodel, a class is made abstract. In a model, instances of this class are migrated to a chosen subclass.")
public class MakeClassAbstract extends OperationBase {

	@Parameter(description = "The class to be made abstract")
	public EClass eClass;

	@Parameter(description = "The subclass to which instances are migrated")
	public EClass subClass;

	@Restriction(parameter = "subClass")
	public List<String> checkSubClass(EClass subClass) {
		if (!subClass.getEAllSuperTypes().contains(eClass)) {
			return Collections
					.singletonList("The class has to be a super type of the sub class");
		}
		return Collections.emptyList();
	}

	@Override
	public void initialize(Metamodel metamodel) {
		if (subClass == null) {
			List<EClass> subClasses = (List<EClass>) EcoreUtils.getInverse(
					eClass, EcorePackage.Literals.ECLASS__ESUPER_TYPES,
					metamodel.getEPackages());
			if (!subClasses.isEmpty()) {
				subClass = subClasses.get(0);
			}
		}
	}

	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (eClass.isAbstract()) {
			result.add("The class is already abstract");
		}
		return result;
	}

	@Override
	public void execute(Metamodel metamodel, Model model) {
		eClass.setAbstract(true);

		for (Instance instance : model.getInstances(eClass)) {
			instance.migrate(subClass);
		}
	}

}