package org.eclipse.emf.edapt.declaration.replacement;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: EFEF0E6FC56A2B1350A5596C8D6C8060
 */
@EdaptOperation(identifier = "introduceReferenceClass", label = "Association to Class", description = "In the metamodel, a reference is replaced by a reference class. More specifically, the reference class is now contained by the source class. In the model, links conforming to the reference are replaced by instances of the reference class.", breaking = true)
public class IntroduceReferenceClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference to be replaced by a reference class")
	public EReference reference;

	/** {@description} */
	@EdaptParameter(description = "The name of the reference class")
	public String className;

	/** {@description} */
	@EdaptParameter(description = "The name of the opposite reference to the source class", optional = true)
	public String sourceReferenceName;

	/** {@description} */
	@EdaptParameter(description = "The name of the opposite reference to the target class", optional = true)
	public String targetReferenceName;

	/** {@description} */
	@EdaptConstraint(description = "Reference has to have an opposite")
	public boolean checkReferenceOpposite() {
		return reference.getEOpposite() != null;
	}

	/** {@description} */
	@EdaptConstraint(description = "Opposite reference is not allowed to be containment")
	public boolean checkOppositeNotContainment() {
		return reference.getEOpposite() == null
			|| !reference.getEOpposite().isContainment();
	}

	/** {@description} */
	@EdaptConstraint(description = "Reference is not allowed to be containment")
	public boolean checkReferenceNotContainment() {
		return !reference.isContainment();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		final EReference opposite = reference.getEOpposite();

		// metamodel adaptation
		final EClass sourceClass = reference.getEContainingClass();
		final EClass targetClass = reference.getEReferenceType();

		final EPackage contextPackage = sourceClass.getEPackage();
		final EClass referenceClass = MetamodelFactory.newEClass(contextPackage,
			className);

		metamodel.setEOpposite(reference, null);
		reference.setEType(referenceClass);
		opposite.setEType(referenceClass);

		reference.setContainment(true);

		// model migration
		for (final Instance target : model.getAllInstances(targetClass)) {
			target.unset(opposite);
		}
		for (final Instance source : model.getAllInstances(sourceClass)) {
			if (reference.isMany()) {
				for (final Instance target : source.<List<Instance>> unset(reference)) {
					final Instance referenceInstance = model
						.newInstance(referenceClass);
					source.add(reference, referenceInstance);
					put(target, opposite, referenceInstance);
				}
			} else {
				final Instance target = source.unset(reference);
				if (target != null) {
					final Instance referenceInstance = model
						.newInstance(referenceClass);
					source.set(reference, referenceInstance);
					put(target, opposite, referenceInstance);
				}
			}
		}

		// metamodel adaptation
		if (sourceReferenceName != null) {
			final EReference sourceReference = MetamodelFactory.newEReference(
				referenceClass, sourceReferenceName, sourceClass, 1, 1,
				false);
			metamodel.setEOpposite(reference, sourceReference);
		}
		if (targetReferenceName != null) {
			final EReference targetReference = MetamodelFactory.newEReference(
				referenceClass, targetReferenceName, targetClass, 1, 1,
				false);
			metamodel.setEOpposite(opposite, targetReference);
		}
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
}
