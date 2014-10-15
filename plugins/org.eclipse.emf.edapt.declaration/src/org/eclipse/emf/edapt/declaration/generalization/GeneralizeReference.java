package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 42FA2FA593134BCDC8FFE0DCCAFC3BC2
 */
@EdaptOperation(identifier = "generalizeReference", label = "Generalize Reference", description = "In the metamodel, either the type or the multiplicity of a reference is generalized. In the model, nothing is changed.")
public class GeneralizeReference extends GeneralizeTypedElement {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference to be generalized")
	public EReference reference;

	/** {@description} */
	@EdaptParameter(description = "The new type of the reference")
	public EClass type;
	
	/** {@inheritDoc} */
	@Override
	protected EStructuralFeature getTypedElement() {
		return reference;
	}

	/** {@description} */
	@EdaptConstraint(restricts = "type", description = "The type must be the same or more general")
	public boolean checkType(EClass type) {
		EClass referenceType = reference.getEReferenceType();
		return type.isSuperTypeOf(referenceType);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		super.initialize(metamodel);
		type = reference.getEReferenceType();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		if (reference.getEType() != type) {
			reference.setEType(type);
			if (reference.getEOpposite() != null) {
				type.getEStructuralFeatures().add(reference.getEOpposite());
			}
		}
		super.execute(metamodel, model);
	}
}
