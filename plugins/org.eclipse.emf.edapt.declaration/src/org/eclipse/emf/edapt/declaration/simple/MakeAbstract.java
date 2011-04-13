package org.eclipse.emf.edapt.declaration.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
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
 * @levd.rating YELLOW Hash: 9B4317436BA9EB25D78F2AF18361C7AD
 */
@EdaptOperation(identifier = "makeAbstract", label = "Make Class Abstract", description = "In the metamodel, a class is made abstract. In a model, instances of this class are migrated to a chosen subclass.")
public class MakeAbstract extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class to be made abstract")
	public EClass eClass;
	
	/** {@description} */
	@EdaptRestriction(parameter = "eClass")
	public List<String> checkEClass(EClass eClass) {
		List<String> result = new ArrayList<String>();
		if (eClass.isAbstract()) {
			result.add("The class is already abstract");
		}
		return result;
	}

	/** {@description} */
	@EdaptParameter(description = "The subclass to which instances are migrated")
	public EClass subClass;

	/** {@description} */
	@EdaptRestriction(parameter = "subClass")
	public List<String> checkSubClass(EClass subClass) {
		List<String> result = new ArrayList<String>();
		if (!subClass.getEAllSuperTypes().contains(eClass)) {
			result.add("The class has to be a super type of the sub class");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.setAbstract(true);

		// model migration
		for (Instance instance : model.getInstances(eClass)) {
			instance.migrate(subClass);
		}
	}
}
