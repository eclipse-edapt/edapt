package org.eclipse.emf.edapt.declaration.replacement;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 5B7747DFD5031268253054D789641EE5
 */
@EdaptOperation(identifier = "volatileToOpposite", label = "Volatile to Opposite Reference", description = "In the metamodel, a reference is changed from being volatile to an opposite. In the model, the opposite direction needs to be set.")
public class VolatileToOpposite extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference which is changed from volatile to opposite")
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
		metamodel.setEOpposite(opposite, reference);
	}
}
