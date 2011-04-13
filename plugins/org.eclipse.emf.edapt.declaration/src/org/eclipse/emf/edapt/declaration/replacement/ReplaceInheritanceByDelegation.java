package org.eclipse.emf.edapt.declaration.replacement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ReferenceSlot;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: F06EAA5353670EC3634D79810EFA66A0
 */
@EdaptOperation(identifier = "replaceInheritanceByDelegation", label = "Inheritance to Delegation", description = "In the metamodel, inheritance from a super class is replaced by delegation to this class. More specifically, the super class is removed and a containment reference to this class is created. In the model, the contents associated to the super class are extracted to a separate instance of the super class.")
public class ReplaceInheritanceByDelegation extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class from which the super class is removed")
	public EClass subClass;

	/** {@description} */
	@EdaptParameter(description = "The super class to be removed")
	public EClass superClass;

	/** {@description} */
	@EdaptRestriction(parameter = "superClass")
	public List<String> checkSuperClass(EClass superClass) {
		if (!subClass.getESuperTypes().contains(superClass)) {
			return Collections
					.singletonList("The super class must be a super type of the sub class");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@EdaptParameter(description = "The name of the reference to the super class")
	public String referenceName;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		subClass.getESuperTypes().remove(superClass);
		EReference delegation = MetamodelUtils.newEReference(subClass,
				referenceName, superClass, 1, 1, true);

		// model migration
		for (Instance instance : model.getAllInstances(subClass)) {
			Instance delegate = model.newInstance(superClass);
			instance.set(delegation, delegate);
			for (EStructuralFeature feature : superClass
					.getEAllStructuralFeatures()) {
				delegate.set(feature, instance.unset(feature));
			}
			for (ReferenceSlot slot : new ArrayList<ReferenceSlot>(instance
					.getReferences())) {
				EReference reference = slot.getEReference();
				Instance source = slot.getInstance();
				if (reference.getEReferenceType().isSuperTypeOf(superClass)) {
					if (reference.isMany()) {
						source.remove(reference, instance);
						source.add(reference, delegate);
					} else {
						source.set(reference, delegate);
					}
					if (reference.isContainment()) {
						instance.unset(delegation);
						model.delete(instance);
					}
				}
			}
		}
	}
}
