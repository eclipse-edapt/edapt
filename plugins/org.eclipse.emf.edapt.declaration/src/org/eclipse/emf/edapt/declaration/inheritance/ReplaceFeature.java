package org.eclipse.emf.edapt.declaration.inheritance;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * @levd.rating YELLOW Hash: 432E1E10C309240783FB3E58A2B29113
 */
@Deprecated
@EdaptOperation(identifier = "replaceFeature", label = "Replace Feature", description = "In the metamodel, a feature is replace by another one. In the model, the values are moved accordingly.")
public class ReplaceFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The feature to be replaced")
	public EStructuralFeature toReplace;

	/** {@description} */
	@EdaptParameter(description = "The feature by which it is replaced")
	public EStructuralFeature replaceBy;

	/** {@description} */
	@EdaptConstraint(restricts = "replaceBy", description = "The feature to replace "
			+ "must be defined in a sub class of the one "
			+ "with the feature by which it is replaced")
	public boolean checkFeaturesInCompatibleClasses(EStructuralFeature replaceBy) {
		EClass subClass = toReplace.getEContainingClass();
		return subClass.getEAllStructuralFeatures().contains(replaceBy);
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must be of the same cardinality")
	public boolean checkFeaturesSameMultiplicity() {
		return toReplace.isMany() == replaceBy.isMany();
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must be of the same type")
	public boolean checkFeaturesSameType() {
		return toReplace.getEType() == replaceBy.getEType();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass subClass = toReplace.getEContainingClass();

		// metamodel adaptation
		metamodel.delete(toReplace);

		// model migration
		for (Instance instance : model.getAllInstances(subClass)) {
			instance.set(replaceBy, instance.unset(toReplace));
		}
	}
}
