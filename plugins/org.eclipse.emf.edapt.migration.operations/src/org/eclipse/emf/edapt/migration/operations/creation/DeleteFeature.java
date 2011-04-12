package org.eclipse.emf.edapt.migration.operations.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: CEAD8EA0392233119D11244476976DE7
 */
@Deprecated
@Operation(identifier = "deleteFeature", label = "Delete Feature", description = "In the metamodel, a feature is deleted. In the model, its values are deleted, too.")
public class DeleteFeature extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature to be deleted")
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
