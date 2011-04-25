package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelFactory;
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
 * @levd.rating YELLOW Hash: 7252A31E5F7AA61F0831544115F0E8DF
 */
@EdaptOperation(identifier = "newReference", label = "Create Reference", description = "In the metamodel, a new reference is created. Nothing is changed in the model.")
public class NewReference extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The class in which the reference is created")
	public EClass eClass;

	/** {@description} */
	@EdaptParameter(description = "The name of the new reference")
	public String name;

	/** {@description} */
	@EdaptParameter(description = "The type of the new reference")
	public EClass type;

	/** {@description} */
	@EdaptParameter(description = "The lower bound of the new reference")
	public int lowerBound = 0;

	/** {@description} */
	@EdaptParameter(description = "The upper bound of the new reference")
	public int upperBound = 1;

	/** {@description} */
	@EdaptParameter(description = "Whether the new reference is a containment reference")
	public Boolean containment = false;

	/** {@description} */
	@EdaptParameter(description = "The opposite reference of the new reference", optional = true)
	public EReference opposite;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EReference reference = MetamodelFactory.newEReference(eClass, name, type,
				lowerBound, upperBound, containment);
		if (opposite != null) {
			metamodel.setEOpposite(reference, opposite);
		}
	}
}
