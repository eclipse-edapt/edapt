package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
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
 * @levd.rating YELLOW Hash: 87725436564AD910476BE661181899D7
 */
@Deprecated
@EdaptOperation(identifier = "deleteFeature", label = "Delete Feature", description = "In the metamodel, a feature is deleted. In the model, its values are deleted, too.")
public class DeleteFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature to be deleted")
	public EStructuralFeature feature;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass eClass = feature.getEContainingClass();

		// metamodel adaptation
		metamodel.delete(feature);

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			deleteFeatureValue(instance, feature);
		}
	}
}
