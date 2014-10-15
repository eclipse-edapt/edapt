package org.eclipse.emf.edapt.declaration.delegation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 261303FAD548FEF5FC9F79DCC0B88F27
 */
@EdaptOperation(identifier = "moveFeature", label = "Move Feature along Reference", description = "In the metamodel, a feature is moved along a single-valued reference. In the model, values are moved accordingly.")
public class MoveFeature extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature to be moved")
	public EStructuralFeature feature;

	/** {@description} */
	@EdaptParameter(description = "The reference along which the feature is moved")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must be available in the same class as the feature")
	public boolean checkReferenceInSameClass(EReference reference) {
		EClass sourceClass = feature.getEContainingClass();
		return sourceClass.getEAllStructuralFeatures().contains(reference);
	}

	/** {@description} */
	@EdaptConstraint(description = "The multiplicity of the reference must be single-valued and obligatory")
	public boolean checkReferenceSingleValued() {
		return reference.getLowerBound() == 1 && reference.getUpperBound() == 1;
	}

	/** {@description} */
	@EdaptConstraint(description = "The multiplicity of its opposite reference must be single-valued")
	public boolean checkReferenceOppositeSingleValued() {
		return reference.getEOpposite().getUpperBound() == 1;
	}

	/** {@description} */
	@EdaptConstraint(description = "A feature with that name already exists in the target class")
	public boolean checkFeatureNameUniqueInTargetClass() {
		EClass targetClass = reference.getEReferenceType();
		return targetClass.getEStructuralFeature(feature.getName()) == null;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EClass sourceClass = feature.getEContainingClass();
		EClass targetClass = reference.getEReferenceType();

		// metamodel adaptation
		targetClass.getEStructuralFeatures().add(feature);

		// model migration
		for (Instance instance : model.getAllInstances(sourceClass)) {
			Instance target = instance.get(reference);
			if (instance.isSet(feature)) {
				Object value = instance.unset(feature);
				target.set(feature, value);
			}
		}
	}
}
