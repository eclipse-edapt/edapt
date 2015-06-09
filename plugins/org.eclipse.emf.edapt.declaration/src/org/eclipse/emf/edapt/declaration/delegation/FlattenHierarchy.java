package org.eclipse.emf.edapt.declaration.delegation;

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
 * @levd.rating YELLOW Hash: EBBFFC0379B87F347211CB5AF166D379
 */
@EdaptOperation(identifier = "flattenHierarchy", label = "Flatten Containment Hierarchy", description = "In the metamodel, a containment hierarchy is flattened. More specifically, the reference to denote the root as well as the reference to denote the children are replaced by a containment reference. In the model, the corresponding hierarchies are flattened accordingly.")
public class FlattenHierarchy extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference to denote the root node")
	public EReference rootReference;

	/** {@description} */
	@EdaptConstraint(restricts = "rootReference", description = "The root reference must be a single-valued containment reference.")
	public boolean checkRootReferenceSingleValueContainment(
		EReference rootReference) {
		return !rootReference.isMany() && rootReference.isContainment();
	}

	/** {@description} */
	@EdaptParameter(description = "The reference to denote the children nodes")
	public EReference childrenReference;

	/** {@description} */
	@EdaptConstraint(restricts = "childrenReference", description = "The children reference must be defined by the node class.")
	public boolean checkChildrenReferenceInNodeClass(EReference reference) {
		final EClass nodeClass = rootReference.getEReferenceType();
		return nodeClass.getEStructuralFeatures().contains(reference);
	}

	/** {@description} */
	@EdaptConstraint(restricts = "childrenReference", description = "The children reference must be a multi-valued containment reference.")
	public boolean checkChildrenReferenceManyValuedContainment(
		EReference childrenReference) {
		return childrenReference.isMany() && childrenReference.isContainment();
	}

	/** {@description} */
	@EdaptParameter(description = "The reference which replaces the containment hierarchy")
	public String referenceName;

	/** {@description} */
	@EdaptConstraint(description = "The type of the children reference must be the node class.")
	public boolean checkChildrenReferenceType() {
		return childrenReference.getEType() == rootReference
			.getEReferenceType();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EClass rootClass = rootReference.getEContainingClass();
		final EClass nodeClass = rootReference.getEReferenceType();

		// metamodel adaptation
		metamodel.delete(rootReference);
		metamodel.delete(childrenReference);
		final EReference containerReference = MetamodelFactory.newEReference(rootClass,
			referenceName, nodeClass, 0, -1, true);

		// model migration
		for (final Instance root : model.getAllInstances(rootClass)) {
			final Instance node = root.unset(rootReference);
			if (node != null) {
				root.add(containerReference, node);
				visitNode(root, containerReference, node);
			}
		}
	}

	/** Flatten one level in the hierarchy. */
	private void visitNode(Instance root, EReference containerReference,
		Instance node) {
		final List<Instance> children = node.unset(childrenReference);
		root.<List<Instance>> get(containerReference).addAll(children);
		for (final Instance child : children) {
			visitNode(root, containerReference, child);
		}
	}
}
