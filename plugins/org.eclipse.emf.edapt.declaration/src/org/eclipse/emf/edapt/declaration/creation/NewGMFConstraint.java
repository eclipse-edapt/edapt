package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.common.AnnotateModelElement;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 960EDA620641B97CAA70F1949FC9EEBC
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
		EAnnotation annotation = MetamodelUtils.newEAnnotation(element,
				"http://www.eclipse.org/gmf/2005/constraints");
		MetamodelUtils.newEStringToStringMapEntry(annotation, "ocl", ocl);
		MetamodelUtils.newEStringToStringMapEntry(annotation, "description",
				description);
	}
}
