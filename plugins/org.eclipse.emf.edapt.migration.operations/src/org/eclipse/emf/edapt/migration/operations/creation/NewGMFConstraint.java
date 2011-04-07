package org.eclipse.emf.edapt.migration.operations.creation;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: D23A2473150ADEAA92C9E9ED43ACA5AD
 */
@Operation(label = "Create GMF Constraint", description = "In the metamodel, a new constraint is introduced. Nothing is changed in the model.")
public class NewGMFConstraint extends OperationBase {

	/** {@description} */
	@Parameter(description = "The metamodel element in which context the constraint is created")
	public EModelElement element;

	/** {@description} */
	@Parameter(description = "The OCL expression of the constraint")
	public String ocl;

	/** {@description} */
	@Parameter(description = "The description of the constraint")
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
