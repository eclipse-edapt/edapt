package org.eclipse.emf.edapt.declaration.replacement;

import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: D92E04CFE2E89F29E8C5405C1CA3D22A
 */
@EdaptOperation(identifier = "suppressedSetVisibilityToNotChangeable", label = "Suppressed Set Visibility to Not Changeable", description = "In the metamodel, the setter of a reference is made visible again, and at the same time it is made non-changeable. Nothing is changed in the model.")
public class SuppressedSetVisibilityToNotChangeable extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference whose setter is made visible again")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(description = "Suppressed Set Visibility must be present")
	public boolean checkSuppressedSetVisibilityNotPresent() {
		return getAnnotation() != null;
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
