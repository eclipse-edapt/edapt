package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 90F610E3128C1FBDDC38CA75DD9D5091
 */
@EdaptOperation(identifier = "newGMFConstraint", label = "Create GMF Constraint", description = "In the metamodel, a new constraint is introduced. Nothing is changed in the model.")
public class NewGMFConstraint extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The metamodel element in which context the constraint is created")
	public EModelElement element;

	/** {@description} */
	@EdaptParameter(description = "The OCL expression of the constraint")
	public String ocl;

	/** {@description} */
	@EdaptParameter(description = "The description of the constraint")
	public String description;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EAnnotation annotation = MetamodelUtils.newEAnnotation(element,
				"http://www.eclipse.org/gmf/2005/constraints");
		MetamodelUtils.newEStringToStringMapEntry(annotation, "ocl", ocl);
		MetamodelUtils.newEStringToStringMapEntry(annotation, "description",
				description);
	}
}
