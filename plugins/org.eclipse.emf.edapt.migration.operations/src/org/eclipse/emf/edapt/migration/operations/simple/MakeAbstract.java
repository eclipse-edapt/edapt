package org.eclipse.emf.edapt.migration.operations.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.migration.Instance;
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
 * @levd.rating YELLOW Hash: 9B4317436BA9EB25D78F2AF18361C7AD
 */
@Operation(label = "Make Class Abstract", description = "In the metamodel, a class is made abstract. In a model, instances of this class are migrated to a chosen subclass.")
public class MakeAbstract extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class to be made abstract")
	public EClass eClass;

	/** {@description} */
	@Parameter(description = "The subclass to which instances are migrated")
	public EClass subClass;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (eClass.isAbstract()) {
			result.add("The class is already abstract");
		}
		if (subClass != null && !subClass.getEAllSuperTypes().contains(eClass)) {
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
