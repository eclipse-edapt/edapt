package org.eclipse.emf.edapt.declaration.inheritance;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: 827E297EE5BBD2768DD6FA3CC630975B
 */
@EdaptOperation(identifier = "extractSubClass", label = "Extract Subclass", description = "In the metamodel, a feature is extracted into a new subclass and the feature is made mandatory. In the model, all instances of the superclass that have the feature set are migrated to the new subclass.")
public class ExtractSubClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The superclass from which the feature is extracted")
	public EClass superClass;

	/** {@description} */
	@EdaptParameter(description = "The feature to be extracted")
	public EStructuralFeature feature;

	/** {@description} */
	@EdaptConstraint(restricts = "feature", description = "The feature has to belong to the super class")
	public boolean checkFeature(EStructuralFeature feature) {
		return superClass.getEStructuralFeatures().contains(feature);
	}

	/** {@description} */
	@EdaptParameter(description = "The name of the new subclass")
	public String className;

	/** {@description} */
	@EdaptConstraint(restricts = "superClass", description = "The super class may not have a sub class")
	public boolean checkSuperClassNoSubTypes(EClass superClass,
			Metamodel metamodel) {
		return metamodel.getESubTypes(superClass).isEmpty();
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
