package org.eclipse.emf.edapt.declaration.inheritance;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 1B0F9E96639F314E70F6EEFF7437E475
 */
@Deprecated
@EdaptOperation(identifier = "replaceFeature", label = "Replace Feature", description = "In the metamodel, a feature is replace by another one. In the model, the values are moved accordingly.")
public class ReplaceFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature to be replaced")
	public EStructuralFeature toReplace;

	/** {@description} */
	@EdaptParameter(description = "The feature by which it is replaced")
	public EStructuralFeature replaceBy;

	/** {@description} */
	@EdaptConstraint(restricts = "replaceBy", description = "The feature to replace "
		+ "must be defined in a sub class of the one "
		+ "with the feature by which it is replaced")
	public boolean checkFeaturesInCompatibleClasses(EStructuralFeature replaceBy) {
		final EClass subClass = toReplace.getEContainingClass();
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
		final EClass subClass = toReplace.getEContainingClass();

		// metamodel adaptation
		metamodel.delete(toReplace);

		// model migration
		for (final Instance instance : model.getAllInstances(subClass)) {
			instance.set(replaceBy, instance.unset(toReplace));
		}
	}
}
