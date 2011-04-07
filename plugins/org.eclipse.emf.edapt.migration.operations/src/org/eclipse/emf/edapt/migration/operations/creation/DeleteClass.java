package org.eclipse.emf.edapt.migration.operations.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
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
 * @levd.rating YELLOW Hash: 20916EBD48183771A792B5F6570285BB
 */
@Operation(label = "Delete Class", description = "In the metamodel, a class that is no longer used is deleted. In the model, nothing is changed.")
public class DeleteClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class to be deleted")
	public EClass eClass;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
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
