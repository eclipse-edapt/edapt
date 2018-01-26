package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * @levd.rating YELLOW Hash: EC9969F8F3F4191DB05869AB26EA6796
 */
@EdaptOperation(identifier = "deleteFeature2", label = "Delete Feature", description = "In the metamodel, a feature is deleted. In the model, its values are deleted, too.", breaking = true)
public class DeleteFeature2 extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature to be deleted")
	public EStructuralFeature feature;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EClass eClass = feature.getEContainingClass();

		// model migration
		for (final Instance instance : model.getAllInstances(eClass)) {
			deleteFeatureValue(instance, feature);
		}

		// metamodel adaptation
		metamodel.delete(feature);
		if (feature instanceof EReference) {
			final EReference reference = (EReference) feature;
			if (reference.getEOpposite() != null) {
				metamodel.delete(reference.getEOpposite());
			}
		}
	}
}
