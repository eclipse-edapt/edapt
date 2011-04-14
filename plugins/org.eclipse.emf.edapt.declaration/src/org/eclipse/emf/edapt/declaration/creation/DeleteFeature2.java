package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: AB032D69BA05CC4C063DC9FE60ECE68F
 */
@EdaptOperation(identifier = "deleteFeature2", label = "Delete Feature", description = "In the metamodel, a feature is deleted. In the model, its values are deleted, too.")
public class DeleteFeature2 extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The feature to be deleted")
	public EStructuralFeature feature;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass eClass = feature.getEContainingClass();

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			deleteFeatureValue(instance, feature);
		}

		// metamodel adaptation
		metamodel.delete(feature);
		if (feature instanceof EReference) {
			EReference reference = (EReference) feature;
			if (reference.getEOpposite() != null) {
				metamodel.delete(reference.getEOpposite());
			}
		}
	}
}
