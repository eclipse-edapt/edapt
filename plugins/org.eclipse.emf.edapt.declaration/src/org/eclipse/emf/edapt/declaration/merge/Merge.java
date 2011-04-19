package org.eclipse.emf.edapt.declaration.merge;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
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
 * @levd.rating YELLOW Hash: 04D33B590F9BB8E3F630C8D6A02EBECE
 */
@EdaptOperation(identifier = "merge", label = "Merge Reference into Another", description = "In the metamodel, a reference is deleted. In the model, the values of this reference are merged to a compatible reference.")
public class Merge extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference that is deleted")
	public EReference toMerge;

	/** {@description} */
	@EdaptParameter(description = "The reference to which the values are merged")
	public EReference mergeTo;

	/** {@description} */
	@EdaptConstraint(restricts = "mergeTo", description = "The references must be different from each other")
	public boolean checkReferencesDifferent(EReference mergeTo) {
		return toMerge != mergeTo;
	}

	/** {@description} */
	@EdaptConstraint(description = "The types of the references must be compatible")
	public boolean checkReferenceTypesCompatible() {
		return mergeTo.getEReferenceType().isSuperTypeOf(
				toMerge.getEReferenceType());
	}

	/** {@description} */
	@EdaptConstraint(restricts = "mergeTo", description = "The reference to merge to must be multi-valued")
	public boolean checkMergeToMany(EReference mergeTo) {
		return mergeTo.isMany();
	}

	/** {@description} */
	@EdaptConstraint(description = "The reference to merge to must be available in the context class")
	public boolean checkMergeToAvailableInContextClass() {
		EClass contextClass = toMerge.getEContainingClass();
		return contextClass.getEAllStructuralFeatures().contains(mergeTo);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass contextClass = toMerge.getEContainingClass();

		// model migration
		for (Instance instance : model.getAllInstances(contextClass)) {
			Object toMergeValue = instance.unset(toMerge);
			List mergeToValue = (List) instance.get(mergeTo);
			if (toMerge.isMany()) {
				mergeToValue.addAll((List) toMergeValue);
			} else {
				mergeToValue.add(toMergeValue);
			}
		}

		// metamodel adaptation
		metamodel.delete(toMerge);
		if (toMerge.getEOpposite() != null) {
			metamodel.delete(toMerge.getEOpposite());
		}
	}
}
