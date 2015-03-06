package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.common.AnnotateModelElement;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: D3D3B72BCE45BCA4EDE3D09DA3E2894C
 */
@EdaptOperation(identifier = "newGMFConstraint", label = "Create GMF Constraint", description = "In the metamodel, a new constraint is introduced. Nothing is changed in the model.")
public class NewGMFConstraint extends AnnotateModelElement {

	/** {@description} */
	@EdaptParameter(description = "The OCL expression of the constraint")
	public String ocl;

	/** {@description} */
	@EdaptParameter(description = "The description of the constraint")
	public String description;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EAnnotation annotation = MetamodelFactory.newEAnnotation(element,
			"http://www.eclipse.org/gmf/2005/constraints"); //$NON-NLS-1$
		MetamodelFactory.newEStringToStringMapEntry(annotation, "ocl", ocl); //$NON-NLS-1$
		MetamodelFactory.newEStringToStringMapEntry(annotation, "description", //$NON-NLS-1$
			description);
	}
}
