package org.eclipse.emf.edapt.declaration.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;
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
 * @levd.rating YELLOW Hash: 7F5E5E3ACDF093A538D59014F969B364
 */
@EdaptOperation(identifier = "inlineClass", label = "Inline Class", description = "In the metamodel, a class reachable through a single-valued containment reference is inlined. More specifically, its features are moved to the source class of the reference. In the model, the values of these features are moved accordingly.")
public class InlineClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference to the class to be inlined")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must not have an opposite")
	public boolean checkReference(EReference reference) {
		return reference.getEOpposite() == null;
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The multiplicity of the reference must be single-valued")
	public boolean checkReferenceSingleValued(EReference reference) {
		return !reference.isMany();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must be containment")
	public boolean checkReferenceContainment(EReference reference) {
		return reference.isContainment();
	}

	/** {@description} */
	@EdaptConstraint(description = "The class to be inlined must not have sub classes")
	public boolean checkInlinedClassNoSubTypes(Metamodel metamodel) {
		final EClass inlinedClass = reference.getEReferenceType();
		final EList<EClass> subTypes = metamodel.getInverse(inlinedClass,
			EcorePackage.eINSTANCE.getEClass_ESuperTypes());
		return subTypes.isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(description = "The class to be inlined must not be a type of another reference")
	public boolean checkInlinedClassNotTargetedByReference(Metamodel metamodel) {
		final EClass inlinedClass = reference.getEReferenceType();
		for (final ETypedElement element : metamodel.<ETypedElement> getInverse(
			inlinedClass, EcorePackage.eINSTANCE.getETypedElement_EType())) {
			if (element instanceof EReference) {
				final EReference reference = (EReference) element;
				final EReference eOpposite = reference.getEOpposite();
				final EList<EStructuralFeature> features = inlinedClass
					.getEStructuralFeatures();
				if (eOpposite != null && !features.contains(eOpposite)) {
					return false;
				}
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EClass inlinedClass = reference.getEReferenceType();
		final EClass contextClass = reference.getEContainingClass();
		final List<EStructuralFeature> features = new ArrayList<EStructuralFeature>(
			inlinedClass.getEStructuralFeatures());

		// metamodel adaptation
		contextClass.getEStructuralFeatures().addAll(features);
		for (final EStructuralFeature feature : features) {
			if (feature instanceof EReference) {
				final EReference reference = (EReference) feature;
				if (reference.getEOpposite() != null) {
					reference.getEOpposite().setEType(contextClass);
				}
			}
		}
		metamodel.delete(reference);
		metamodel.delete(inlinedClass);

		// model migration
		for (final Instance contextElement : model.getAllInstances(contextClass)) {
			final Instance inlinedElement = contextElement.unset(reference);
			if (inlinedElement != null) {
				for (final EStructuralFeature feature : features) {
					contextElement.set(feature, inlinedElement.unset(feature));
				}
				model.delete(inlinedElement);
			}
		}
	}
}
