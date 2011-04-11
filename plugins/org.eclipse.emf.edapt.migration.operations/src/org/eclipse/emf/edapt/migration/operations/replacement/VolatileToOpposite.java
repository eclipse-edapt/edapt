package org.eclipse.emf.edapt.migration.operations.replacement;

import java.util.ArrayList;
import java.util.List;

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
 * @levd.rating YELLOW Hash: 8AC258749B2910757219681FDA26C88C
 */
@Operation(label = "Volatile to Opposite Reference", description = "In the metamodel, a reference is changed from being volatile to an opposite. In the model, the opposite direction needs to be set.")
public class VolatileToOpposite extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference which is changed from volatile to opposite")
	public EReference reference;

	/** {@description} */
	@Parameter(description = "The reference which is going to be the opposite")
	public EReference opposite;

	/** {@description} */
	@Parameter(description = "Whether the reference is going to be changeable")
	public Boolean changeable = true;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (reference.getEOpposite() != null) {
			result.add("Reference must not already have an opposite");
		}
		if (!reference.isVolatile()) {
			result.add("Reference must be volatile");
		}
		if (reference.getEType() != opposite.getEContainingClass()
				|| reference.getEContainingClass() != opposite.getEType()) {
			result.add("Reference and opposite "
					+ "must be compatible with each other");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		reference.setVolatile(false);
		reference.setTransient(false);
		reference.setDerived(false);
		reference.setChangeable(changeable);
		model.setEOpposite(opposite, reference);
	}
}
