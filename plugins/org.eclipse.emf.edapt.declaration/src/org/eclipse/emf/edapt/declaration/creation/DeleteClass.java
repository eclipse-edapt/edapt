package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: CB8254F4F8FDDD8D1C7B628B6D467D1F
 */
@EdaptOperation(identifier = "deleteClass", label = "Delete Class", description = "In the metamodel, a class that is no longer used is deleted. In the model, nothing is changed.")
public class DeleteClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The class to be deleted")
	public EClass eClass;

	/** {@description} */
	@EdaptConstraint(restricts = "eClass", description = "The class must not be the target of a reference")
	public boolean checkEClass(EClass eClass, Metamodel metamodel) {
		return metamodel.getInverse(eClass,
				EcorePackage.eINSTANCE.getETypedElement_EType()).isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "eClass", description = "The class must not have sub classes")
	public boolean checkClassNoSubTypes(EClass eClass, Metamodel metamodel) {
		return metamodel.getESubTypes(eClass).isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "eClass", description = "The class must not have super classes")
	public boolean checkClassNoSuperTypes(EClass eClass) {
		return eClass.getESuperTypes().isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		metamodel.delete(eClass);
	}
}