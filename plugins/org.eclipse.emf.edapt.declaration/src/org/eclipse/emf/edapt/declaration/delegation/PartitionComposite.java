package org.eclipse.emf.edapt.declaration.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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
 * @levd.rating YELLOW Hash: 4E4704236E71158A5892E984413264F2
 */
@EdaptOperation(identifier = "partitionComposite", label = "Introduce Composite Pattern", description = "In the metamodel, the composite design pattern is introduced. More specifically, a class is refined by two sub classes - one for composite and one for leaf elements, and a reference is moved to the composite class. In addition, the class is made abstract. In the model, instances of that class are migrated based on whether the reference is populated or not.")
public class PartitionComposite extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class which is refined")
	public EClass eClass;

	/** {@description} */
	@EdaptRestriction(parameter = "eClass")
	public List<String> checkEClass(EClass eClass, Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!metamodel.getESubTypes(eClass).isEmpty()) {
			result.add("The class must not have sub classes");
		}
		return result;
	}

	/** {@description} */
	@EdaptParameter(description = "The name of the composite class")
	public String compositeName;

	/** {@description} */
	@EdaptParameter(description = "The name of the leaf class")
	public String leafName;

	/** {@description} */
	@EdaptParameter(description = "The reference for composite elements")
	public EReference childReference;

	/** {@description} */
	@EdaptRestriction(parameter = "childReference")
	public List<String> checkChildReference(EReference childReference) {
		List<String> result = new ArrayList<String>();
		if (!childReference.isContainment()) {
			result.add("The child reference must be a containment reference");
		}
		if (!eClass.getEStructuralFeatures().contains(childReference)) {
			result.add("The child reference must be defined by the class");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		eClass.setAbstract(true);
		EPackage ePackage = eClass.getEPackage();
		EClass compositeClass = MetamodelUtils.newEClass(ePackage,
				compositeName, eClass);
		compositeClass.getEStructuralFeatures().add(childReference);
		EClass leafClass = MetamodelUtils.newEClass(ePackage, leafName, eClass);

		// model migration
		for (Instance instance : model.getInstances(eClass)) {
			if (instance.getLinks(childReference).isEmpty()) {
				instance.migrate(leafClass);
			} else {
				instance.migrate(compositeClass);
			}
		}
	}
}
