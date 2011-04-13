package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
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
 * @levd.rating YELLOW Hash: D70D05C6BE1B386EBCBCCA5A3543C800
 */
@EdaptOperation(identifier = "pullOperation", label = "Pull up Operation", description = "In the metamodel, a number of operations are pulled up into a common super class. In the model, nothing needs to be done.")
public class PullOperation extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The operations to be pulled up")
	public List<EOperation> operations;

	/** {@description} */
	@EdaptParameter(description = "The super class to which the operations are pulled")
	public EClass targetClass;
	
	/** {@description} */
	@EdaptRestriction(parameter = "targetClass")
	public List<String> checkTargetClass(EClass targetClass) {
		for(EOperation operation : operations) {
			if(!operation.getEContainingClass().getESuperTypes().contains(targetClass)) {
				return Collections.singletonList("The operations' classes must have a common super type");
			}
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!hasSameValue(operations,
				EcorePackage.Literals.ETYPED_ELEMENT__ETYPE)) {
			result.add("The operations' types have to be the same");
		}
		if (!hasSameValue(operations,
				EcorePackage.Literals.ETYPED_ELEMENT__LOWER_BOUND)
				|| !hasSameValue(operations,
						EcorePackage.Literals.ETYPED_ELEMENT__UPPER_BOUND)) {
			result.add("The operations' multiplicities have to be the same");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (targetClass == null) {
			List<EClass> superTypes = operations.get(0).getEContainingClass()
					.getESuperTypes();
			if (!superTypes.isEmpty()) {
				targetClass = superTypes.get(0);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EOperation mainOperation = operations.get(0);

		// metamodel adaptation
		targetClass.getEOperations().add(mainOperation);
		for (EOperation operation : operations) {
			if (operation != mainOperation) {
				metamodel.delete(operation);
			}
		}
	}
}
