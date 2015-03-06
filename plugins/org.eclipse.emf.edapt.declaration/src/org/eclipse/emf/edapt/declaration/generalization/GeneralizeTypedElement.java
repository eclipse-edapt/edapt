package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.ocl.utilities.TypedElement;

/**
 * Abstract base class for operations that generalize a {@link TypedElement}.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 675AB4008152C2FD8258AAD42866260F
 */
public abstract class GeneralizeTypedElement extends OperationImplementation {

	/** Get the typed element which is generalized. */
	protected abstract ETypedElement getTypedElement();

	/** {@description} */
	@EdaptParameter(description = "The new lower bound")
	public int lowerBound;

	/** {@description} */
	@EdaptParameter(description = "The new upper bound")
	public int upperBound;

	/** {@description} */
	@EdaptConstraint(description = "The multiplicity must be the same or more general")
	public boolean checkSameOrExtendedMultiplicity() {
		return lowerBound <= getTypedElement().getLowerBound()
			&& (upperBound >= getTypedElement().getUpperBound() || upperBound == -1);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		lowerBound = getTypedElement().getLowerBound();
		upperBound = getTypedElement().getUpperBound();
	}

	/** {@inheritDoc} */
	@Override
	protected void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		getTypedElement().setLowerBound(lowerBound);
		getTypedElement().setUpperBound(upperBound);
	}
}
