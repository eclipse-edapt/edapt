package org.eclipse.emf.edapt.migration.operations.delegation;

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
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: E36D2033870AFBF5A440722075FFF533
 */
@Operation(label = "Flatten Containment Hierarchy", description = "In the metamodel, a containment hierarchy is flattened. More specifically, the reference to denote the root as well as the reference to denote the children are replaced by a containment reference. In the model, the corresponding hierarchies are flattened accordingly.")
public class FlattenHierarchy extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference to denote the root node")
	public EReference rootReference;

	/** {@description} */
	@Parameter(description = "The reference to denote the children nodes")
	public EReference childrenReference;

	/** {@description} */
	@Restriction(parameter = "childrenReference")
	public List<String> checkChildrenReference(EReference reference) {
		List<String> result = new ArrayList<String>();
		EClass nodeClass = rootReference.getEReferenceType();
		if (!nodeClass.getEStructuralFeatures().contains(reference)) {
			result.add("The children reference must be "
					+ "defined by the node class.");
		}
		return result;
	}

	/** {@description} */
	@Parameter(description = "The reference which replaces the containment hierarchy")
	public String referenceName;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EClass nodeClass = rootReference.getEReferenceType();
		if (rootReference.isMany() || !rootReference.isContainment()) {
			result.add("The root reference must be a single-valued "
					+ "containment reference.");
		}
		if (!childrenReference.isMany() || !childrenReference.isContainment()) {
			result.add("The children reference must be a "
					+ "multi-valued containment reference.");
		}
		if (childrenReference.getEType() != nodeClass) {
			result.add("The type of the children reference "
					+ "must be the node class.");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass rootClass = rootReference.getEContainingClass();
		EClass nodeClass = rootReference.getEReferenceType();

		// metamodel adaptation
		metamodel.delete(rootReference);
		metamodel.delete(childrenReference);
		EReference containerReference = MetamodelUtils.newEReference(rootClass,
				referenceName, nodeClass, 0, -1, true);

		// model migration
		for (Instance root : model.getAllInstances(rootClass)) {
			Instance node = root.unset(rootReference);
			if (node != null) {
				root.add(containerReference, node);
				visitNode(root, containerReference, node);
			}
		}
	}

	/** Flatten one level in the hierarchy. */
	private void visitNode(Instance root, EReference containerReference,
			Instance node) {
		List<Instance> children = node.unset(childrenReference);
		root.<List<Instance>> get(containerReference).addAll(children);
		for (Instance child : children) {
			visitNode(root, containerReference, child);
		}
	}
}
