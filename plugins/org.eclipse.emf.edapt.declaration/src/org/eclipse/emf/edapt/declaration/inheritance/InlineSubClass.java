package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
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
 * @levd.rating YELLOW Hash: 694680BD230529BBFDCC6DA5044ABECD
 */
@EdaptOperation(identifier = "inlineSubClass", label = "Inline Sub Class", description = "In the metamodel, the sub class is deleted. In the model, all instances of this sub class are migrated to its super class.")
public class InlineSubClass extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class to be inlined")
	public EClass subClass;

	/** {@description} */
	@EdaptRestriction(parameter = "subClass")
	public List<String> checkSubClass(EClass subClass, Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!subClass.getESuperTypes().isEmpty()) {
			EClass superClass = subClass.getESuperTypes().get(0);
			if (subClass.getESuperTypes().size() != 1) {
				result.add("The sub class must have exactly one super type");
			} else if (!MetamodelUtils.isConcrete(superClass)) {
				result.add("The super class must not be abstract");
			}
		}
		if (!metamodel.getESubTypes(subClass).isEmpty()) {
			result.add("The sub class must not have sub types");
		}
		if (!subClass.getEStructuralFeatures().isEmpty()) {
			result.add("The sub class must not have features");
		}
		return result;
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
