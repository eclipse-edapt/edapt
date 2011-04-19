package org.eclipse.emf.edapt.declaration.delegation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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
 * @levd.rating YELLOW Hash: 321908B493C6F89CF4E9001958F610A5
 */
@EdaptOperation(identifier = "partitionComposite", label = "Introduce Composite Pattern", description = "In the metamodel, the composite design pattern is introduced. More specifically, a class is refined by two sub classes - one for composite and one for leaf elements, and a reference is moved to the composite class. In addition, the class is made abstract. In the model, instances of that class are migrated based on whether the reference is populated or not.")
public class PartitionComposite extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The class which is refined")
	public EClass eClass;

	/** {@description} */
	@EdaptConstraint(restricts = "eClass", description = "The class must not have sub classes")
	public boolean checkEClassNoSubTypes(EClass eClass, Metamodel metamodel) {
		return metamodel.getESubTypes(eClass).isEmpty();
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
	@EdaptConstraint(restricts = "childReference", description = "The child reference must be a containment reference")
	public boolean checkChildReferenceContainment(EReference childReference) {
		return childReference.isContainment();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "childReference", description = "The child reference must be defined by the class")
	public boolean checkChildReferenceParentClass(EReference childReference) {
		return eClass.getEStructuralFeatures().contains(childReference);
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
