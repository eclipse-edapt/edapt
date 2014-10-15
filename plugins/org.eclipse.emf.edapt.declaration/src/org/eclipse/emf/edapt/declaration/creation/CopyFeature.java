package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 431444D5692052D25F8B50E6DD96D147
 */
@EdaptOperation(identifier = "copyFeature", label = "Copy Feature", description = "In the metamodel, a feature is copied, giving it a new name. In the model, the values are copied, accordingly.")
public class CopyFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature to be copied")
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
