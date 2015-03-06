package org.eclipse.emf.edapt.declaration.simple;

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
 * @levd.rating YELLOW Hash: 6E0986D7D8C26B699E457A7061DBD88A
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
		metamodel.setEOpposite(reference, null);
	}
}
