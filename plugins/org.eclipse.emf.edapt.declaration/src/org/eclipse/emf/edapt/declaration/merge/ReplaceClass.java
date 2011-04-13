package org.eclipse.emf.edapt.declaration.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
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
 * @levd.rating YELLOW Hash: 6A70910BEC0A96859C0AF1AFD36F50AA
 */
@EdaptOperation(identifier = "replaceClass", label = "Replace Class", description = "In the metamodel, a class is deleted. In the model, instances of this class are migrated to another class based on a mapping of features.")
public class ReplaceClass extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class to be replaced")
	public EClass toReplace;

	/** {@description} */
	@EdaptParameter(description = "The class by which it is replaced")
	public EClass replaceBy;

	/** {@description} */
	@EdaptParameter(description = "The features to be replaced")
	public List<EStructuralFeature> featuresToReplace = new ArrayList<EStructuralFeature>();

	/** {@description} */
	@EdaptRestriction(parameter = "featuresToReplace")
	public List<String> checkFeaturesToReplace(
			EStructuralFeature featuresToReplace) {
		if (!toReplace.getEAllStructuralFeatures().contains(featuresToReplace)) {
			return Collections.singletonList("The replace features must "
					+ "be defined in the replaced class");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@EdaptParameter(description = "The features by which they are replaced (in the same order)")
	public List<EStructuralFeature> featuresReplaceBy = new ArrayList<EStructuralFeature>();

	/** {@description} */
	@EdaptRestriction(parameter = "featuresReplaceBy")
	public List<String> checkFeaturesReplaceBy(
			EStructuralFeature featuresReplaceBy) {
		if (replaceBy.getEAllStructuralFeatures().contains(featuresReplaceBy)) {
			return Collections.singletonList("The replacing features must "
					+ "be defined in the replacing class");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!metamodel.getESubTypes(toReplace).isEmpty()) {
			result.add("The class to be replaced must not have sub types");
		}
		if (featuresToReplace.size() != featuresReplaceBy.size()) {
			result.add("The replaced and replacing features "
					+ "have to be of the same size");
		}
		if (replaceBy != null) {
			if (!featuresToReplace.containsAll(MetamodelUtils.subtractFeatures(
					toReplace, replaceBy))) {
				result.add("The replace features must cover all "
						+ "features from the difference between the class to "
						+ "replace and the class by which it is replaced");
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		for (EReference reference : metamodel.<EReference> getInverse(
				toReplace, EcorePackage.Literals.ETYPED_ELEMENT__ETYPE)) {
			reference.setEType(replaceBy);
		}
		metamodel.delete(toReplace);

		// model migration
		for (Instance instance : model.getAllInstances(toReplace)) {
			instance.migrate(replaceBy);
			for (int i = 0; i < featuresToReplace.size(); i++) {
				instance.set(featuresReplaceBy.get(i), instance
						.unset(featuresToReplace.get(i)));
			}
		}
	}
}
