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
 * @levd.rating YELLOW Hash: 30D18B4B7A0160B0A42E31AE81FE0520
 */
@EdaptOperation(identifier = "inlineClass", label = "Inline Class", description = "In the metamodel, a class reachable through a single-valued containment reference is inlined. More specifically, its features are moved to the source class of the reference. In the model, the values of these features are moved accordingly.")
public class InlineClass extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference to the class to be inlined")
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
		EClass inlinedClass = reference.getEReferenceType();
		EList<EClass> subTypes = metamodel.getInverse(inlinedClass,
				EcorePackage.eINSTANCE.getEClass_ESuperTypes());
		return subTypes.isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(description = "The class to be inlined must not be a type of another reference")
	public boolean checkInlinedClassNotTargetedByReference(Metamodel metamodel) {
		EClass inlinedClass = reference.getEReferenceType();
		for (ETypedElement element : metamodel.<ETypedElement> getInverse(
				inlinedClass, EcorePackage.eINSTANCE.getETypedElement_EType())) {
			if (element instanceof EReference) {
				EReference reference = (EReference) element;
				EReference eOpposite = reference.getEOpposite();
				EList<EStructuralFeature> features = inlinedClass
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
		EClass inlinedClass = reference.getEReferenceType();
		EClass contextClass = reference.getEContainingClass();
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>(
				inlinedClass.getEStructuralFeatures());

		// metamodel adaptation
		contextClass.getEStructuralFeatures().addAll(features);
		for (EStructuralFeature feature : features) {
			if (feature instanceof EReference) {
				EReference reference = (EReference) feature;
				if (reference.getEOpposite() != null) {
					reference.getEOpposite().setEType(contextClass);
				}
			}
		}
		metamodel.delete(reference);
		metamodel.delete(inlinedClass);

		// model migration
		for (Instance contextElement : model.getAllInstances(contextClass)) {
			Instance inlinedElement = contextElement.unset(reference);
			if (inlinedElement != null) {
				for (EStructuralFeature feature : features) {
					contextElement.set(feature, inlinedElement.unset(feature));
				}
				model.delete(inlinedElement);
			}
		}
	}
}
