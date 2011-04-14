package org.eclipse.emf.edapt.declaration.simple;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
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
 * @levd.rating YELLOW Hash: 6BFE85F3AD0DB628CF518D50AC540937
 */
@EdaptOperation(identifier = "makeContainment", label = "Make Reference Containment", description = "In the metamodel, a reference is made containment. In the model, its values are replaced by copies.")
public class MakeContainment extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must not already be containment.")
	public boolean checkReferenceNotContainment(EReference reference) {
		return !reference.isContainment();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass contextClass = reference.getEContainingClass();

		// metamodel adaptation
		reference.setContainment(true);

		// model migration
		for (Instance instance : model.getAllInstances(contextClass)) {
			if (reference.isMany()) {
				List<Instance> values = instance.unset(reference);
				for (Instance value : values) {
					instance.add(reference, value.copy());
				}
			} else {
				Instance value = instance.unset(reference);
				if (value != null) {
					instance.set(reference, value.copy());
				}
			}
		}
	}
}
