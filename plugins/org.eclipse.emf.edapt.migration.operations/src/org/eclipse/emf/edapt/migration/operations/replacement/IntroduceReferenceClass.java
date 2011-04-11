package org.eclipse.emf.edapt.migration.operations.replacement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: F9B991F21366773EF8BB3E047D3F73E8
 */
@Operation(label = "Association to Class", description = "In the metamodel, a reference is replaced by a reference class. More specifically, the reference class is now contained by the source class. In the model, links conforming to the reference are replaced by instances of the reference class.")
public class IntroduceReferenceClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference to be replaced by a reference class")
	public EReference reference;

	/** {@description} */
	@Parameter(description = "The name of the reference class")
	public String className;

	/** {@description} */
	@Parameter(description = "The name of the opposite reference to the source class", optional = true)
	public String sourceReferenceName;

	/** {@description} */
	@Parameter(description = "The name of the opposite reference to the target class", optional = true)
	public String targetReferenceName;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (reference.isContainment()) {
			result.add("Reference is not allowed to be containment");
		}
		EReference opposite = reference.getEOpposite();
		if (opposite == null) {
			result.add("Reference has to have an opposite");
		} else if (opposite.isContainment()) {
			result.add("Opposite reference is not allowed to be containment");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EReference opposite = reference.getEOpposite();

		// metamodel adaptation
		EClass sourceClass = reference.getEContainingClass();
		EClass targetClass = reference.getEReferenceType();

		EPackage contextPackage = sourceClass.getEPackage();
		EClass referenceClass = MetamodelUtils.newEClass(contextPackage,
				className);

		model.setEOpposite(reference, null);
		reference.setEType(referenceClass);
		opposite.setEType(referenceClass);

		reference.setContainment(true);

		// model migration
		for (Instance target : model.getAllInstances(targetClass)) {
			target.unset(opposite);
		}
		for (Instance source : model.getAllInstances(sourceClass)) {
			if (reference.isMany()) {
				for (Instance target : source.<List<Instance>> unset(reference)) {
					Instance referenceInstance = model
							.newInstance(referenceClass);
					source.add(reference, referenceInstance);
					put(target, opposite, referenceInstance);
				}
			} else {
				Instance target = source.unset(reference);
				if (target != null) {
					Instance referenceInstance = model
							.newInstance(referenceClass);
					source.set(reference, referenceInstance);
					put(target, opposite, referenceInstance);
				}
			}
		}

		// metamodel adaptation
		createOppositeReference(model, reference, sourceReferenceName);
		createOppositeReference(model, opposite, targetReferenceName);
	}

	/** Put a value into a reference depending on the multiplicity. */
	private void put(Instance target, EReference opposite,
			Instance referenceInstance) {
		if (opposite.isMany()) {
			target.add(opposite, referenceInstance);
		} else {
			target.set(opposite, referenceInstance);
		}
	}

	/** Create an opposite reference if the name is not null. */
	private void createOppositeReference(Model model, EReference reference,
			String oppositeReferenceName) {
		if (oppositeReferenceName != null) {
			EReference sourceReference = MetamodelUtils.newEReference(reference
					.getEReferenceType(), oppositeReferenceName, reference
					.getEContainingClass(), 1, 1, false);
			model.setEOpposite(sourceReference, reference);
		}
	}
}
