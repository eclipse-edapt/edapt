package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: BE376951F5BCF49B25FBC6E1177AB68A
 */
@EdaptOperation(identifier = "generalizeAttribute", label = "Generalize Attribute", description = "In the metamodel, the multiplicity of an attribute is generalized. In the model, nothing is changed.", breaking = true)
public class GeneralizeAttribute extends GeneralizeTypedElement {

	/** {@description} */
	@EdaptParameter(main = true, description = "The attribute to be generalized")
	public EAttribute attribute;

	/** {@inheritDoc} */
	@Override
	protected EStructuralFeature getTypedElement() {
		return attribute;
	}
}
