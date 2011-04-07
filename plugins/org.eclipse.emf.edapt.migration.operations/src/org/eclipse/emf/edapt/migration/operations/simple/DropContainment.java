package org.eclipse.emf.edapt.migration.operations.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Instance;
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
 * @levd.rating YELLOW Hash: 90E9AA0A98E4B5EA24B0D4ABA8A9B79E
 */
@Operation(label = "Drop Containment", description = "In the metamodel, the containment of a reference is dropped. At the same time, a new container reference is created in a container class. In the model, elements previously contained by the first reference have to be contained by the new container reference. It is assumed that these elements are indirectly contained in an instance of the container class.")
public class DropContainment extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference whose containment is dropped")
	public EReference reference;

	/** {@description} */
	@Parameter(description = "The container class in which the containment reference is created")
	public EClass containerClass;

	/** {@description} */
	@Parameter(description = "The name of the new containment reference")
	public String containerReferenceName;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!reference.isContainment()) {
			result.add("The reference must be containment");
		}
		return result;
	}

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
