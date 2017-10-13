package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 1FBCEE4F43204F11DDB5E245EFF79FC7
 */
@EdaptOperation(identifier = "pushFeature", label = "Push down Feature", description = "In the metamodel, a feature is pushed down to its sub classes. In the model, values are changed accordingly.", breaking = false)
public class PushFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature to be pushed down")
	public EStructuralFeature feature;

	/** {@description} */
	@EdaptConstraint(restricts = "feature", description = "If the feature has an opposite, "
		+ "then the super class may only have one sub type.")
	public boolean checkFeature(EStructuralFeature feature, Metamodel metamodel) {
		final EClass superClass = feature.getEContainingClass();
		if (feature instanceof EReference) {
			final EReference reference = (EReference) feature;
			return reference.getEOpposite() == null
				|| metamodel.getESubTypes(superClass).size() == 1;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EClass superClass = feature.getEContainingClass();
		final List<EClass> subClasses = metamodel.getESubTypes(superClass);

		boolean first = true;
		for (final EClass subClass : subClasses) {
			if (first) {
				subClass.getEStructuralFeatures().add(feature);
				if (feature instanceof EReference) {
					final EReference reference = (EReference) feature;
					if (reference.getEOpposite() != null) {
						reference.getEOpposite().setEType(subClass);
					}
				}
			} else {
				// metamodel adaptation
				final EStructuralFeature clone = MetamodelUtils.copy(feature);
				subClass.getEStructuralFeatures().add(clone);

				// model migration
				for (final Instance instance : model.getAllInstances(subClass)) {
					instance.set(clone, instance.unset(feature));
				}
			}
			first = false;
		}

		if (MetamodelUtils.isConcrete(superClass)) {
			for (final Instance instance : model.getInstances(superClass)) {
				deleteFeatureValue(instance, feature);
			}
		}
	}
}
