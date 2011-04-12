package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: B1D0C51FDB3BF4F2537AC3070A94AFF2
 */
@Operation(identifier = "extractClass", label = "Extract Class", description = "In the metamodel, a number of features are extracted to a new class. This new class is accessible from the context class through a new containment reference. In the model, the values of the features are extracted to a new instance accordingly.")
public class ExtractClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The context class from which the features are extracted")
	public EClass contextClass;

	/** {@description} */
	@Parameter(description = "The features to be extracted")
	public List<EStructuralFeature> features;

	/** {@description} */
	@Restriction(parameter = "features")
	public List<String> checkFeatures(EStructuralFeature feature) {
		if (!contextClass.getEStructuralFeatures().contains(feature)) {
			return Collections
					.singletonList("The features have to belong to the same class");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@Parameter(description = "The package in which the extracted class is created")
	public EPackage ePackage;

	/** {@description} */
	@Parameter(description = "The name of the extracted class")
	public String className;

	/** {@description} */
	@Parameter(description = "The name of the new containment reference from context to extracted class")
	public String referenceName;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (ePackage.getEClassifier(className) != null) {
			result.add("A classifier with the same name already exists");
		}
		EStructuralFeature feature = contextClass
				.getEStructuralFeature(referenceName);
		if (feature != null && !features.contains(feature)) {
			result.add("A feature with the same name already exists");
		}
		return result;
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
