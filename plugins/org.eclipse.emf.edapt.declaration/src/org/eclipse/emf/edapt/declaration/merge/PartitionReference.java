package org.eclipse.emf.edapt.declaration.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 68F716BB2693886FD361F0707D989606
 */
@EdaptOperation(identifier = "partitionReference", label = "Partition Reference", description = "In the metamodel, a reference is partitioned into a number of references according to its type. A sub reference is created for each subclass of the reference's type. Finally, the original reference is deleted. In the model, the value of the reference is partitioned accordingly.", breaking = true)
public class PartitionReference extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference to be partitioned")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must be multi-valued")
	public boolean checkReferenceMany(EReference reference) {
		return reference.isMany();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The type of the reference must be abstract")
	public boolean checkReferenceTypeAbstract(EReference reference) {
		final EClass type = reference.getEReferenceType();
		return !MetamodelUtils.isConcrete(type);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Metamodel metamodel, Model model) {

		final EClass contextClass = reference.getEContainingClass();
		final EClass type = reference.getEReferenceType();

		// metamodel adaptation
		final List<EReference> subReferences = new ArrayList<EReference>();
		for (final EClass subClass : metamodel.getESubTypes(type)) {
			final String name = subClass.getName().substring(0, 1).toLowerCase()
				+ subClass.getName().substring(1);
			final EReference subReference = MetamodelFactory.newEReference(
				contextClass, name, subClass, 0, -1, reference
					.isContainment());
			subReferences.add(subReference);
		}

		metamodel.delete(reference);

		// model migration
		for (final Instance instance : model.getAllInstances(contextClass)) {
			final List<Instance> values = (List<Instance>) instance.unset(reference);
			for (final Instance value : values) {
				final EReference subReference = getReferenceForInstance(
					subReferences, value);
				instance.add(subReference, value);
			}
		}
	}

	/** Find the reference that can hold instances of a certain type. */
	private EReference getReferenceForInstance(List<EReference> references,
		Instance value) {
		for (final EReference reference : references) {
			if (value.instanceOf(reference.getEReferenceType())) {
				return reference;
			}
		}
		return null;
	}
}
