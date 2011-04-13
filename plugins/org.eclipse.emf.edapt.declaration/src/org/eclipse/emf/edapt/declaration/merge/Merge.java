package org.eclipse.emf.edapt.declaration.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
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
 * @levd.rating YELLOW Hash: 4ED07FDDC32ED4FAACD2D70F94AC00CF
 */
@EdaptOperation(identifier = "merge", label = "Merge Reference into Another", description = "In the metamodel, a reference is deleted. In the model, the values of this reference are merged to a compatible reference.")
public class Merge extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference that is deleted")
	public EReference toMerge;

	/** {@description} */
	@EdaptParameter(description = "The reference to which the values are merged")
	public EReference mergeTo;

	/** {@description} */
	@EdaptRestriction(parameter = "mergeTo")
	public List<String> checkMergeTo(EReference mergeTo) {
		List<String> result = new ArrayList<String>();
		EClass contextClass = toMerge.getEContainingClass();
		if (!contextClass.getEAllStructuralFeatures().contains(mergeTo)) {
			result.add("The reference to merge to "
					+ "must be available in the context class");
		}
		if (!mergeTo.isMany()) {
			result.add("The reference to merge to must be multi-valued");
		}
		if (!mergeTo.getEReferenceType().isSuperTypeOf(
				toMerge.getEReferenceType())) {
			result.add("The types of the references must be compatible");
		}
		if (toMerge == mergeTo) {
			result.add("The references must be different from each other");
		}
		return result;
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
