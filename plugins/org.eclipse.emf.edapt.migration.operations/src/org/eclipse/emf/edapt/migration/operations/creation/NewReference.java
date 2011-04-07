package org.eclipse.emf.edapt.migration.operations.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
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
 * @levd.rating YELLOW Hash: 1C7D25B594C82C723D28FFB636383515
 */
@Operation(label = "Create Reference", description = "In the metamodel, a new reference is created. Nothing is changed in the model.")
public class NewReference extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class in which the reference is created")
	public EClass eClass;

	/** {@description} */
	@Parameter(description = "The name of the new reference")
	public String name;

	/** {@description} */
	@Parameter(description = "The type of the new reference")
	public EClass type;

	/** {@description} */
	@Parameter(description = "The lower bound of the new reference")
	public int lowerBound;

	/** {@description} */
	@Parameter(description = "The upper bound of the new reference")
	public int upperBound;

	/** {@description} */
	@Parameter(description = "Whether the new reference is a containment reference")
	public Boolean containment;

	/** {@description} */
	@Parameter(description = "The opposite reference of the new reference", optional = true)
	public EReference opposite;

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		lowerBound = 0;
		upperBound = 1;
		containment = false;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EReference reference = MetamodelUtils.newEReference(eClass, name, type,
				lowerBound, upperBound, containment);
		if (opposite != null) {
			model.setEOpposite(reference, opposite);
		}
	}
}
