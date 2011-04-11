package org.eclipse.emf.edapt.migration.operations.replacement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 98B388D3FE09A19828F4551B7312E6C2
 */
@Operation(label = "Suppressed Set Visibility to Not Changeable", description = "In the metamodel, the setter of a reference is made visible again, and at the same time it is made non-changeable. Nothing is changed in the model.")
public class SuppressedSetVisibilityToNotChangeable extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference whose setter is made visible again")
	public EReference reference;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (getAnnotation() == null) {
			result.add("Suppressed Set Visibility must be present");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {

		EAnnotation annotation = getAnnotation();

		reference.setChangeable(false);
		if (annotation.getDetails().size() > 1) {
			annotation.getDetails().remove("suppressedSetVisibility");
		} else {
			metamodel.delete(annotation);
		}
	}

	/** Get the "suppressedSetVisibility" annotation. */
	private EAnnotation getAnnotation() {
		for (EAnnotation annotation : reference.getEAnnotations()) {
			for (Entry<String, String> detail : annotation.getDetails()
					.entrySet()) {
				if ("suppressedSetVisibility".equals(detail.getKey())) {
					return annotation;
				}
			}
		}
		return null;
	}
}
