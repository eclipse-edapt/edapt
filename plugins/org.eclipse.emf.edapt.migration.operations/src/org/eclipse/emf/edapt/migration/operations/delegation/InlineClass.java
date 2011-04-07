package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;
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
 * @levd.rating YELLOW Hash: 45570AD3BAC3BF88335E1B821A98387E
 */
@Operation(label = "Inline Class", description = "In the metamodel, a class reachable through a single-valued containment reference is inlined. More specifically, its features are moved to the source class of the reference. In the model, the values of these features are moved accordingly.")
public class InlineClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference to the class to be inlined")
	public EReference reference;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EClass inlinedClass = reference.getEReferenceType();
		if (reference.getEOpposite() != null) {
			result.add("The reference must not have an opposite");
		}
		if (reference.isMany()) {
			result.add("The multiplicity of the reference "
					+ "must be single-valued");
		}
		if (!reference.isContainment()) {
			result.add("The reference must be containment");
		}
		EList<EClass> subTypes = metamodel.getInverse(inlinedClass,
				EcorePackage.eINSTANCE.getEClass_ESuperTypes());
		if (!subTypes.isEmpty()) {
			result.add("The class to be inlined must not have sub classes");
		}
		for (ETypedElement element : metamodel.<ETypedElement> getInverse(
				inlinedClass, EcorePackage.eINSTANCE.getETypedElement_EType())) {
			if (element instanceof EReference) {
				EReference reference = (EReference) element;
				EReference eOpposite = reference.getEOpposite();
				EList<EStructuralFeature> features = inlinedClass
						.getEStructuralFeatures();
				if (eOpposite != null && !features.contains(eOpposite)) {
					result.add("The class to be inlined must not be a "
							+ "type of another reference");
					break;
				}
			}
		}
		return result;
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
