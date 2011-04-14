package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: C8759419E5DF1AA71A88C9FD89FEF61C
 */
@EdaptOperation(identifier = "removeSuperfluousSuperType", label = "Remove Superfluous Super Type", description = "In the metamodel, a super type is removed from a class that is already inherited from another super class. In the model, nothing is changed, as this super type is superfluous.")
public class RemoveSuperfluousSuperType extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class from which the super type is removed")
	public EClass eClass;

	/** {@description} */
	@EdaptParameter(description = "The super type to be removed")
	public EClass superType;

	/** {@description} */
	@EdaptConstraint(restricts = "superType", description = "The super type to be removed actually has to be a super type")
	public boolean checkSuperType(EClass superType) {
		return eClass.getESuperTypes().contains(superType);
	}

	/** {@description} */
	@EdaptConstraint(restricts = "superType", description = "The super type to be removed must be subsumed by one of the other super types")
	public boolean checkSuperTypeSubsumed(EClass superType) {
		for (EClass s : eClass.getESuperTypes()) {
			if (s.getEAllSuperTypes().contains(superType)) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.getESuperTypes().remove(superType);
	}
}
