package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
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
 * @levd.rating YELLOW Hash: B38C02821017F9D5B8560A3C5C3DD4B5
 */
@EdaptOperation(identifier = "pullOperation", label = "Pull up Operation", description = "In the metamodel, a number of operations are pulled up into a common super class. In the model, nothing needs to be done.")
public class PullOperation extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The operations to be pulled up")
	public List<EOperation> operations;

	/** {@description} */
	@EdaptParameter(description = "The super class to which the operations are pulled")
	public EClass targetClass;

	/** {@description} */
	@EdaptConstraint(restricts = "targetClass", description = "The operations' classes must have a common super type")
	public boolean checkTargetClass(EClass targetClass) {
		for (EOperation operation : operations) {
			if (!operation.getEContainingClass().getESuperTypes().contains(
					targetClass)) {
				return false;
			}
		}
		return true;
	}

	/** {@description} */
	@EdaptConstraint(description = "The operations' multiplicities have to be the same")
	public boolean checkOperationsSameMultiplicity() {
		return hasSameValue(operations, EcorePackage.eINSTANCE
				.getETypedElement_LowerBound())
				&& hasSameValue(operations, EcorePackage.eINSTANCE
						.getETypedElement_UpperBound());
	}

	/** {@description} */
	@EdaptConstraint(description = "The operations' types have to be the same")
	public boolean checkOperationsSameType() {
		return hasSameValue(operations, EcorePackage.eINSTANCE
				.getETypedElement_EType());
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
