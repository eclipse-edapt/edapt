package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: BB672265AA506A1C0C8875B2FC75412D
 */
@EdaptOperation(identifier = "copyFeature", label = "Copy Feature", description = "In the metamodel, a feature is copied, giving it a new name. In the model, the values are copied, accordingly.")
public class CopyFeature extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The feature to be copied")
	public EStructuralFeature feature;

	/** {@description} */
	@EdaptParameter(description = "The name of the copy")
	public String name;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass contextClass = feature.getEContainingClass();

		// metamodel adaptation
		EStructuralFeature copiedFeature = MetamodelUtils.copy(feature);
		copiedFeature.setName(name);
		contextClass.getEStructuralFeatures().add(copiedFeature);
		if (copiedFeature instanceof EReference) {
			EReference copiedReference = (EReference) copiedFeature;
			if (copiedReference.isContainment()) {
				copiedReference.setContainment(false);
			}
		}

		// model migration
		for (Instance instance : model.getAllInstances(contextClass)) {
			instance.set(copiedFeature, instance.get(feature));
		}
	}
}
