package org.eclipse.emf.edapt.declaration.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 20916EBD48183771A792B5F6570285BB
 */
@EdaptOperation(identifier = "deleteClass", label = "Delete Class", description = "In the metamodel, a class that is no longer used is deleted. In the model, nothing is changed.")
public class DeleteClass extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class to be deleted")
	public EClass eClass;

	/** {@description} */
	@EdaptRestriction(parameter = "eClass")
	public List<String> checkEClass(EClass eClass, Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!metamodel.getInverse(eClass,
				EcorePackage.eINSTANCE.getETypedElement_EType()).isEmpty()) {
			result.add("The class must not be the target of a reference");
		}
		if (!metamodel.getESubTypes(eClass).isEmpty()) {
			result.add("The class must not have sub classes");
		}
		if (!eClass.getESuperTypes().isEmpty()) {
			result.add("The class must not have super classes");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		metamodel.delete(eClass);
	}
}
