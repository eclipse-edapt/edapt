package org.eclipse.emf.edapt.declaration.replacement;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
 * @levd.rating YELLOW Hash: 8351FA5E2EF80E5543BE9089A274ECFC
 */
@EdaptOperation(identifier = "notChangeableToSuppressedSetVisibility", label = "Not Changeable to Suppressed Set Visibility", description = "In the metamodel, a reference is made changeable, and at the same time its setter is suppressed. Nothing is changed in the model.", breaking = false)
public class NotChangeableToSuppressedSetVisibility extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference to be made changeable")
	public EReference reference;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		reference.setChangeable(true);
		EcoreUtil.setAnnotation(reference,
			"http://www.eclipse.org/emf/2002/GenModel", //$NON-NLS-1$
			"suppressedSetVisibility", "true"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
