package org.eclipse.emf.edapt.declaration.generalization;

import java.util.ArrayList;
import java.util.List;

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
 * @levd.rating YELLOW Hash: C73857ED78F90A15BAB6BB5EEE65FA26
 */
@EdaptOperation(identifier = "specializeSuperType", label = "Specialize Super Type", description = "In the metamodel, the super type of a class is replaced by one of its sub classes. In the model, nothing is modified.")
public class SpecializeSuperType extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The class whose super type is specialized")
	public EClass eClass;

	/** {@description} */
	@EdaptParameter(description = "The super type which is replaced")
	public EClass toReplace;

	/** {@description} */
	@EdaptConstraint(restricts = "toReplace", description = "The super type to be replaced must be a super type of the class")
	public boolean checkToReplaceSuperType(EClass toReplace) {
		return eClass.getESuperTypes().contains(toReplace);
	}

	/** {@description} */
	@EdaptParameter(description = "The sub class by which is replaced")
	public EClass replaceBy;

	/** {@description} */
	@EdaptConstraint(description = "The replacing super type must be a sub type of the replaced super type")
	public boolean checkToReplaceSubType() {
		return replaceBy.getEAllSuperTypes().contains(toReplace);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (eClass.getESuperTypes().size() == 1) {
			toReplace = eClass.getESuperTypes().get(0);
			List<EClass> subClasses = new ArrayList<EClass>(metamodel
					.getESubTypes(toReplace));
			subClasses.remove(eClass);
			if (subClasses.size() == 1) {
				replaceBy = subClasses.get(0);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.getESuperTypes().remove(toReplace);
		eClass.getESuperTypes().add(replaceBy);
	}
}
