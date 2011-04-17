package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.MigrationException;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: E2955B639401EDF01AE0FA27C4B8DD0F
 */
@EdaptOperation(identifier = "useSuperClass", label = "Fold Super Class", description = "In the metamodel, a number of features are replaced by features of a new super class. In the model, the values are moved to these features based on a mapping.")
public class UseSuperClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The class to which the super class is added")
	public EClass subClass;

	/** {@description} */
	@EdaptParameter(description = "The new super class")
	public EClass superClass;

	/** {@description} */
	@EdaptParameter(description = "The features to be replaced")
	public List<EStructuralFeature> toReplace;

	/** {@description} */
	@EdaptConstraint(restricts = "toReplace", description = "The features to be replaced must belong to the sub class")
	public boolean checkToReplaceInSubClass(EStructuralFeature toReplace) {
		return subClass.getEStructuralFeatures().contains(toReplace);
	}

	/** {@description} */
	@EdaptParameter(description = "The features by which they are replaced (in the same order)")
	public List<EStructuralFeature> replaceBy;

	/** {@description} */
	@EdaptConstraint(restricts = "replaceBy", description = "The features to replace must be available in the super class")
	public boolean checkReplaceByInSuperClass(EStructuralFeature replaceBy) {
		return superClass.getEAllStructuralFeatures().contains(replaceBy);
	}

	/** {@description} */
	@EdaptConstraint(description = "The number of features to be replaced and to replace them must be the same")
	public boolean checkFeaturesSameSize() {
		return toReplace.size() == replaceBy.size();
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must be of the same multiplicity")
	public boolean checkFeaturesSameMultiplicity() {
		return hasSameValue(toReplace, replaceBy, EcorePackage.eINSTANCE
				.getETypedElement_Many());
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must be of the same type")
	public boolean checkFeaturesSameType() {
		return hasSameValue(toReplace, replaceBy, EcorePackage.eINSTANCE
				.getETypedElement_EType());
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model)
			throws MigrationException {

		subClass.getESuperTypes().add(superClass);
		subClass.getESuperTypes().removeAll(superClass.getEAllSuperTypes());

		for (int i = 0; i < toReplace.size(); i++) {
			ReplaceFeature operation = new ReplaceFeature();
			operation.replaceBy = replaceBy.get(i);
			operation.toReplace = toReplace.get(i);
			operation.checkAndExecute(metamodel, model);
		}
	}
}
