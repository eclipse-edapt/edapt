package org.eclipse.emf.edapt.declaration.delegation;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
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
 * @levd.rating YELLOW Hash: 9081661C521688FAF6F722C2ABF50B0C
 */
@EdaptOperation(identifier = "extractExistingClass", label = "Fold Class", description = "In the metamodel, a number of features are extracted into an existing class. More specifically, a containment reference to the extracted class is created and the features are replaced by features of the extracted class. In the model, the values of the features are moved accordingly to a new instance of the extracted class.")
public class ExtractExistingClass extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The features to be extracted")
	public List<EStructuralFeature> toReplace;

	/** {@description} */
	@EdaptParameter(description = "The extracted class")
	public EClass extractedClass;

	/** {@description} */
	@EdaptParameter(description = "The features of the extracted class by which they are replaced (in the same order)")
	public List<EStructuralFeature> replaceBy;

	/** {@description} */
	@EdaptConstraint(restricts = "replaceBy", description = "The features to replace must be defined in the extracted class")
	public boolean checkReplaceBy(EStructuralFeature feature) {
		return extractedClass.getEAllStructuralFeatures().contains(feature);
	}

	/** {@description} */
	@EdaptParameter(description = "The name of the containment reference")
	public String referenceName;

	/** {@description} */
	@EdaptConstraint(description = "The replaced and replacing features must be of the same size")
	public boolean checkFeaturesSize() {
		return toReplace.size() == replaceBy.size();
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must be of the same type")
	public boolean checkFeaturesSameType() {
		return hasSameValue(toReplace, replaceBy, EcorePackage.eINSTANCE
				.getETypedElement_EType());
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must be of the same multiplicity")
	public boolean checkFeaturesSameMultiplicity() {
		return hasSameValue(toReplace, replaceBy, EcorePackage.eINSTANCE
				.getETypedElement_Many());
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass contextClass = toReplace.get(0).getEContainingClass();

		EReference reference = MetamodelUtils.newEReference(contextClass,
				referenceName, extractedClass, 1, 1, true);
		for (EStructuralFeature feature : toReplace) {
			metamodel.delete(feature);
		}

		for (Instance contextInstance : model.getAllInstances(contextClass)) {
			Instance extractedInstance = model.newInstance(extractedClass);
			contextInstance.set(reference, extractedInstance);
			for (int i = 0; i < toReplace.size(); i++) {
				Object value = contextInstance.unset(toReplace.get(i));
				extractedInstance.set(replaceBy.get(i), value);
			}
		}
	}
}
