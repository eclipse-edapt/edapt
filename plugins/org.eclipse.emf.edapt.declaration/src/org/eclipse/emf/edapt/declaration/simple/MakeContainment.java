package org.eclipse.emf.edapt.declaration.simple;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: B74CBF847E481CFC73934807D5AA01A3
 */
@EdaptOperation(identifier = "makeContainment", label = "Make Reference Containment", description = "In the metamodel, a reference is made containment. In the model, its values are replaced by copies.")
public class MakeContainment extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must not already be containment.")
	public boolean checkReferenceNotContainment(EReference reference) {
		return !reference.isContainment();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EClass contextClass = reference.getEContainingClass();

		// metamodel adaptation
		reference.setContainment(true);

		// model migration
		for (final Instance instance : model.getAllInstances(contextClass)) {
			if (reference.isMany()) {
				final List<Instance> values = instance.unset(reference);
				for (final Instance value : values) {
					instance.add(reference, value.copy());
				}
			} else {
				final Instance value = instance.unset(reference);
				if (value != null) {
					instance.set(reference, value.copy());
				}
			}
		}
	}
}
