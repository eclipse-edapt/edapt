package org.eclipse.emf.edapt.declaration.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 62D3F92D106E86F9EB6DBC148BF82DCC
 */
@EdaptOperation(identifier = "extractClass", label = "Extract Class", description = "In the metamodel, a number of features are extracted to a new class. This new class is accessible from the context class through a new containment reference. In the model, the values of the features are extracted to a new instance accordingly.")
public class ExtractClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The context class from which the features are extracted")
	public EClass contextClass;

	/** {@description} */
	@EdaptParameter(optional = true, description = "The features to be extracted")
	public List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();

	/** {@description} */
	@EdaptConstraint(restricts = "features", description = "The features have to belong to the same class")
	public boolean checkFeatures(EStructuralFeature feature) {
		return contextClass.getEStructuralFeatures().contains(feature);
	}

	/** {@description} */
	@EdaptParameter(description = "The package in which the extracted class is created")
	public EPackage ePackage;

	/** {@description} */
	@EdaptParameter(description = "The name of the extracted class")
	public String className;

	/** {@description} */
	@EdaptParameter(description = "The name of the new containment reference from context to extracted class")
	public String referenceName;

	/** {@description} */
	@EdaptConstraint(description = "A classifier with the same name already exists")
	public boolean checkUniqueClassifierName() {
		return ePackage.getEClassifier(className) == null;
	}

	/** {@description} */
	@EdaptConstraint(description = "A feature with the same name already exists")
	public boolean checkUniqueFeatureName() {
		EStructuralFeature feature = contextClass
				.getEStructuralFeature(referenceName);
		return feature == null || features.contains(feature);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (ePackage == null) {
			ePackage = contextClass.getEPackage();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {

		// metamodel adaptation
		EClass extractedClass = MetamodelUtils.newEClass(ePackage, className);
		EReference reference = MetamodelUtils.newEReference(contextClass,
				referenceName, extractedClass, 1, 1, true);
		extractedClass.getEStructuralFeatures().addAll(features);
		for (EStructuralFeature feature : features) {
			if (feature instanceof EReference) {
				EReference r = (EReference) feature;
				if (r.getEOpposite() != null) {
					r.getEOpposite().setEType(extractedClass);
				}
			}
		}

		// model migration
		for (Instance contextInstance : model.getAllInstances(contextClass)) {
			Instance extractedInstance = model.newInstance(extractedClass);
			contextInstance.set(reference, extractedInstance);
			for (EStructuralFeature feature : features) {
				extractedInstance.set(feature, contextInstance.unset(feature));
			}
		}
	}
}