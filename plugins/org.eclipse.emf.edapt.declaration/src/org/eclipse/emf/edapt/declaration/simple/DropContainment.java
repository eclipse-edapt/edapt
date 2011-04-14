package org.eclipse.emf.edapt.declaration.simple;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 046CD39EE34F715E29C44ED0EF745586
 */
@EdaptOperation(identifier = "dropContainment", label = "Drop Containment", description = "In the metamodel, the containment of a reference is dropped. At the same time, a new container reference is created in a container class. In the model, elements previously contained by the first reference have to be contained by the new container reference. It is assumed that these elements are indirectly contained in an instance of the container class.")
public class DropContainment extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference whose containment is dropped")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must be containment")
	public boolean checkReferenceContainment(EReference reference) {
		return reference.isContainment();
	}

	/** {@description} */
	@EdaptParameter(description = "The container class in which the containment reference is created")
	public EClass containerClass;

	/** {@description} */
	@EdaptParameter(description = "The name of the new containment reference")
	public String containerReferenceName;

	/** {@inheritDoc} */
	@SuppressWarnings( { "unchecked", "null" })
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass contextClass = reference.getEContainingClass();

		// metamodel adaptation
		reference.setContainment(false);
		EReference containerReference = MetamodelUtils.newEReference(
				containerClass, containerReferenceName, reference
						.getEReferenceType(), 0, -1, true);

		// model migration
		for (Instance contextElement : model.getAllInstances(contextClass)) {
			Instance containerElement = contextElement;
			while (containerElement != null
					&& !(containerElement.instanceOf(containerClass))) {
				containerElement = containerElement.getContainer();
			}
			Object value = contextElement.get(reference);
			if (reference.isMany()) {
				((List) containerElement.get(containerReference))
						.addAll((List) value);
			} else if (value != null) {
				containerElement.add(containerReference, value);
			}
		}
	}
}
