package org.eclipse.emf.edapt.declaration.replacement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 8AC258749B2910757219681FDA26C88C
 */
@EdaptOperation(identifier = "volatileToOpposite", label = "Volatile to Opposite Reference", description = "In the metamodel, a reference is changed from being volatile to an opposite. In the model, the opposite direction needs to be set.")
public class VolatileToOpposite extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference which is changed from volatile to opposite")
	public EReference reference;

	/** {@description} */
	@EdaptParameter(description = "The reference which is going to be the opposite")
	public EReference opposite;
	
	/** {@description} */
	@EdaptRestriction(parameter = "opposite")
	public List<String> checkOpposite(EReference opposite) {
		if (reference.getEType() != opposite.getEContainingClass()
				|| reference.getEContainingClass() != opposite.getEType()) {
			return Collections.singletonList("Reference and opposite "
					+ "must be compatible with each other");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@EdaptParameter(description = "Whether the reference is going to be changeable")
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
