package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
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
 * @levd.rating YELLOW Hash: CA9266BA5F399A6B93B4AF9202494D60
 */
@EdaptOperation(identifier = "imitateSuperType", label = "Unfold Superclass", description = "In the metamodel, a superclass is removed from a subclass, while all its features are copied into the subclass. In the model, values are changed accordingly.")
public class ImitateSuperType extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The subclass")
	public EClass subClass;

	/** {@description} */
	@EdaptParameter(description = "The superclass")
	public EClass superClass;

	/** {@description} */
	@EdaptConstraint(restricts = "superClass", description = "The super class has to be a super type of the sub class")
	public boolean checkSuperClass(EClass superClass) {
		return subClass.getESuperTypes().contains(superClass);
	}

	/** {@description} */
	@EdaptConstraint(description = "The super class must not be target of a reference")
	public boolean checkSuperClassNoReferenceTarget(Metamodel metamodel) {
		return metamodel.getInverse(superClass,
				EcorePackage.eINSTANCE.getETypedElement_EType()).isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (superClass == null && !subClass.getESuperTypes().isEmpty()) {
			superClass = subClass.getESuperTypes().get(0);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		List<EStructuralFeature> features = superClass.getEStructuralFeatures();

		// metamodel adaptation
		subClass.getESuperTypes().remove(superClass);
		subClass.getESuperTypes().addAll(superClass.getESuperTypes());

		List<EStructuralFeature> clones = new ArrayList<EStructuralFeature>();
		for (EStructuralFeature feature : features) {
			EStructuralFeature clone = MetamodelUtils.copy(feature);
			subClass.getEStructuralFeatures().add(clone);
			clones.add(clone);
		}

		// model migration
		for (Instance instance : model.getAllInstances(subClass)) {
			for (int i = 0; i < features.size(); i++) {
				instance.set(clones.get(i), instance.unset(features.get(i)));
			}
		}
	}
}
