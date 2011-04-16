package org.eclipse.emf.edapt.declaration.merge;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
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
 * @levd.rating YELLOW Hash: 198372C125F0736D508A6CDA5205E899
 */
@Deprecated
@EdaptOperation(identifier = "replaceClassBySibling", label = "Replace Class by Sibling", description = "In the metamodel, a class is deleted. In the model, its instances are migrated to a class sharing the same super class.")
public class ReplaceClassBySibling extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The class to be replaced")
	public EClass toReplace;

	/** {@description} */
	@EdaptParameter(description = "The class by which it is replaced")
	public EClass replaceBy;

	/** {@description} */
	@EdaptConstraint(restricts = "replaceBy", description = "The replacing class must have exactly one super class")
	public boolean checkReplaceBySingleSuperClass(EClass replaceBy) {
		return replaceBy.getESuperTypes().size() == 1;
	}

	/** {@description} */
	@EdaptConstraint(restricts = "replaceBy", description = "The super classes of "
			+ "replaced and replacing class must be the same")
	public boolean checkSameSuperClasses(EClass replaceBy) {
		return toReplace.getESuperTypes().get(0) == replaceBy.getESuperTypes()
				.get(0);
	}

	/** {@description} */
	@EdaptConstraint(restricts = "toReplace", description = "The replaced class must not have any features")
	public boolean checkToReplaceNoFeatures(EClass toReplace) {
		return toReplace.getEStructuralFeatures().isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "toReplace", description = "The replaced class must have exactly one super class")
	public boolean checkToReplaceSingleSuperClass(EClass toReplace) {
		return toReplace.getESuperTypes().size() == 1;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		for (EReference reference : metamodel.<EReference> getInverse(
				toReplace, EcorePackage.Literals.EREFERENCE__EREFERENCE_TYPE)) {
			reference.setEType(replaceBy);
		}
		metamodel.delete(toReplace);

		// model migration
		for (Instance instance : model.getAllInstances(toReplace)) {
			instance.migrate(replaceBy);
		}
	}
}
