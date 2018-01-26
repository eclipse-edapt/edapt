package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 4F7EA37ADF9D83B621B5F11CB0B82016
 */
@EdaptOperation(identifier = "removeSuperfluousSuperType", label = "Remove Superfluous Super Type", description = "In the metamodel, a super type is removed from a class that is already inherited from another super class. In the model, nothing is changed, as this super type is superfluous.", breaking = false)
public class RemoveSuperfluousSuperType extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The class from which the super type is removed")
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
		for (final EClass s : eClass.getESuperTypes()) {
			if (s.getEAllSuperTypes().contains(superType)) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		for (final EClass superType : eClass.getESuperTypes()) {
			if (checkSuperTypeSubsumed(superType)) {
				this.superType = superType;
				break;
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.getESuperTypes().remove(superType);
	}
}
