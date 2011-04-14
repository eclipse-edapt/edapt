package org.eclipse.emf.edapt.declaration.replacement;

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
 * @levd.rating YELLOW Hash: A70CBEA22B2D0A3816E741782ADBF624
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
	@EdaptConstraint(restricts = "opposite", description = "Reference and opposite "
			+ "must be compatible with each other")
	public boolean checkOppositeCompatible(EReference opposite) {
		return reference.getEType() == opposite.getEContainingClass()
				&& reference.getEContainingClass() == opposite.getEType();
	}

	/** {@description} */
	@EdaptParameter(description = "Whether the reference is going to be changeable")
	public Boolean changeable = true;

	/** {@description} */
	@EdaptConstraint(description = "Reference must be volatile")
	public boolean checkReferenceVolatile() {
		return reference.isVolatile();
	}

	/** {@description} */
	@EdaptConstraint(description = "Reference must not already have an opposite")
	public boolean checkReferenceNoOpposite() {
		return reference.getEOpposite() == null;
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
