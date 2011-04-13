package org.eclipse.emf.edapt.declaration.generalization;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
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
 * @levd.rating YELLOW Hash: DFE1C4DCC43423ED591D0D83A3A847C3
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
	@EdaptRestriction(parameter = "superType")
	public List<String> checkSuperType(EClass superType) {
		List<String> result = new ArrayList<String>();
		List<EClass> superTypes = eClass.getESuperTypes();
		if (!superTypes.contains(superType)) {
			result.add("The super type to be removed "
					+ "actually has to be a super type");
		}
		boolean any = false;
		for (EClass s : superTypes) {
			if (s.getEAllSuperTypes().contains(superType)) {
				any = false;
			}
		}
		if (!any) {
			result.add("The super type to be removed must be "
					+ "subsumed by one of the other super types");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.getESuperTypes().remove(superType);
	}
}
