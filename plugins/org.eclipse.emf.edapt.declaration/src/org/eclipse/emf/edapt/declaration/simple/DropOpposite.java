package org.eclipse.emf.edapt.declaration.simple;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 051B5FCE94C950F2ED717FC924675CBC
 */
@EdaptOperation(identifier = "dropOpposite", label = "Drop Opposite Relationship", description = "In the metamodel, the opposite relationship between to references is dropped. In the model, nothing needs to be done.")
public class DropOpposite extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference whose opposite relationship should be dropped")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "Reference must have an opposite")
	public boolean checkReferenceOpposite(EReference reference) {
		return reference.getEOpposite() != null;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		model.setEOpposite(reference, null);
	}
}
