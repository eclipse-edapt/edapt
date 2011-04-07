package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: ACA67F3B97716E068D6B5D96F9F0C39C
 */
@Operation(label = "Fold Class", description = "In the metamodel, a number of features are extracted into an existing class. More specifically, a containment reference to the extracted class is created and the features are replaced by features of the extracted class. In the model, the values of the features are moved accordingly to a new instance of the extracted class.")
public class ExtractExistingClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The features to be extracted")
	public List<EStructuralFeature> toReplace;

	/** {@description} */
	@Parameter(description = "The extracted class")
	public EClass extractedClass;

	/** {@description} */
	@Parameter(description = "The features of the extracted class by which they are replaced (in the same order)")
	public List<EStructuralFeature> replaceBy;

	/** {@description} */
	@Restriction(parameter = "replaceBy")
	public List<String> checkReplaceBy(EStructuralFeature feature) {
		if (!extractedClass.getEAllStructuralFeatures().contains(feature)) {
			return Collections
					.singletonList("The features to replace must be defined in the extracted class");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@Parameter(description = "The name of the containment reference")
	public String referenceName;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if(toReplace.size() != replaceBy.size()) {
			result.add("The replaced and replacing features must be of the same size");
		}
		for(EStructuralFeature source : toReplace) {
			EStructuralFeature target = replaceBy.get(toReplace.indexOf(source));
			if(source.getEType() != target.getEType()) {
				result.add("The features must be of the same type");
			}
			if(source.isMany() != target.isMany()) {
				result.add("The features must be of the same multiplicity");
			}
		}		
		return result;
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
