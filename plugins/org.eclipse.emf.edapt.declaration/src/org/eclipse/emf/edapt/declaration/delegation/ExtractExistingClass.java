package org.eclipse.emf.edapt.declaration.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: F8B92BCDD44F0BB6EB73DCBACB549241
 */
@EdaptOperation(identifier = "extractExistingClass", label = "Fold Class", description = "In the metamodel, a number of features are extracted into an existing class. More specifically, a containment reference to the extracted class is created and the features are replaced by features of the extracted class. In the model, the values of the features are moved accordingly to a new instance of the extracted class.")
public class ExtractExistingClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The features to be extracted")
	public List<EStructuralFeature> toReplace;

	/** {@description} */
	@EdaptParameter(description = "The extracted class")
	public EClass extractedClass;

	/** {@description} */
	@EdaptParameter(description = "The features of the extracted class by which they are replaced (in the same order)")
	public List<EStructuralFeature> replaceBy = new ArrayList<EStructuralFeature>();

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
		final EClass contextClass = toReplace.get(0).getEContainingClass();

		final EReference reference = MetamodelFactory.newEReference(contextClass,
			referenceName, extractedClass, 1, 1, true);
		for (final EStructuralFeature feature : toReplace) {
			metamodel.delete(feature);
		}

		for (final Instance contextInstance : model.getAllInstances(contextClass)) {
			final Instance extractedInstance = model.newInstance(extractedClass);
			contextInstance.set(reference, extractedInstance);
			for (int i = 0; i < toReplace.size(); i++) {
				final Object value = contextInstance.unset(toReplace.get(i));
				extractedInstance.set(replaceBy.get(i), value);
			}
		}
	}
}
