package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: 2FC1950C05D879731EEED5ABDD9BC6F3
 */
@Operation(identifier = "extractSubClass", label = "Extract Subclass", description = "In the metamodel, a feature is extracted into a new subclass and the feature is made mandatory. In the model, all instances of the superclass that have the feature set are migrated to the new subclass.")
public class ExtractSubClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The superclass from which the feature is extracted")
	public EClass superClass;

	/** {@description} */
	@Parameter(description = "The feature to be extracted")
	public EStructuralFeature feature;

	/** {@description} */
	@Restriction(parameter = "feature")
	public List<String> checkFeature(EStructuralFeature feature) {
		if (!superClass.getEStructuralFeatures().contains(feature)) {
			return Collections
					.singletonList("The feature has to belong to the super class");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@Parameter(description = "The name of the new subclass")
	public String className;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!metamodel.getESubTypes(superClass).isEmpty()) {
			result.add("The super class may not have a sub class");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {

		// metamodel adaptation
		EPackage ePackage = superClass.getEPackage();
		EClass subClass = MetamodelUtils.newEClass(ePackage, className,
				superClass);
		subClass.getEStructuralFeatures().add(feature);
		feature.setLowerBound(1);

		// model migration
		for (Instance instance : model.getInstances(superClass)) {
			if (instance.isSet(feature)) {
				instance.migrate(subClass);
			}
		}
	}
}
