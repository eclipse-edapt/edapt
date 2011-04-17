package org.eclipse.emf.edapt.declaration.replacement;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
 * @levd.rating YELLOW Hash: AFDAC6FA33326ED5B4B297D1839DFD44
 */
@EdaptOperation(identifier = "notChangeableToSuppressedSetVisibility", label = "Not Changeable to Suppressed Set Visibility", description = "In the metamodel, a reference is made changeable, and at the same time its setter is suppressed. Nothing is changed in the model.")
public class NotChangeableToSuppressedSetVisibility extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The reference to be made changeable")
	public EReference reference;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		reference.setChangeable(true);
		EcoreUtil.setAnnotation(reference,
				"http://www.eclipse.org/emf/2002/GenModel",
				"suppressedSetVisibility", "true");
	}
}