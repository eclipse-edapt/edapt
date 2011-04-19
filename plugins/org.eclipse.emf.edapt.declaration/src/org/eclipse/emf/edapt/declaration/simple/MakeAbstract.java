package org.eclipse.emf.edapt.declaration.simple;

import org.eclipse.emf.ecore.EClass;
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
 * @levd.rating YELLOW Hash: F1B800B5BE3D5AD4D920562EFA551982
 */
@EdaptOperation(identifier = "makeAbstract", label = "Make Class Abstract", description = "In the metamodel, a class is made abstract. In a model, instances of this class are migrated to a chosen subclass.")
public class MakeAbstract extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The class to be made abstract")
	public EClass eClass;

	/** {@description} */
	@EdaptConstraint(restricts = "eClass", description = "The class is already abstract")
	public boolean checkClassAbstract(EClass eClass) {
		return !eClass.isAbstract();
	}

	/** {@description} */
	@EdaptParameter(description = "The subclass to which instances are migrated")
	public EClass subClass;

	/** {@description} */
	@EdaptConstraint(restricts = "subClass", description = "The class has to be a super type of the sub class")
	public boolean checkSubClass(EClass subClass) {
		return subClass.getEAllSuperTypes().contains(eClass);
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
