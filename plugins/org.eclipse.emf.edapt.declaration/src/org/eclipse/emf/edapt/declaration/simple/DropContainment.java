package org.eclipse.emf.edapt.declaration.simple;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 9D04AFEEB1C0A0AB745DDC056898F00E
 */
@EdaptOperation(identifier = "dropContainment", label = "Drop Containment", description = "In the metamodel, the containment of a reference is dropped. At the same time, a new container reference is created in a container class. In the model, elements previously contained by the first reference have to be contained by the new container reference. It is assumed that these elements are indirectly contained in an instance of the container class.", breaking = true)
public class DropContainment extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference whose containment is dropped")
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
	@SuppressWarnings({ "unchecked" })
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EClass contextClass = reference.getEContainingClass();

		// metamodel adaptation
		reference.setContainment(false);
		final EReference containerReference = MetamodelFactory.newEReference(
			containerClass, containerReferenceName, reference
				.getEReferenceType(),
			0, -1, true);

		// model migration
		for (final Instance contextElement : model.getAllInstances(contextClass)) {
			Instance containerElement = contextElement;
			while (containerElement != null
				&& !containerElement.instanceOf(containerClass)) {
				containerElement = containerElement.getContainer();
			}
			final Object value = contextElement.get(reference);
			if (reference.isMany()) {
				((List) containerElement.get(containerReference))
					.addAll((List) value);
			} else if (value != null) {
				containerElement.add(containerReference, value);
			}
		}
	}
}
