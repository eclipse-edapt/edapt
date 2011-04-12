package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: DEEC7FB54E39E282ACF504C4487F1027
 */
@Operation(identifier = "unfoldClass", label = "Unfold Class", description = "In the metamodel, a class reachable through a single-valued containment reference is unfolded. More specifically, its features are copied to the source class of the reference which is deleted. In the model, the values of these features are moved accordingly.")
public class UnfoldClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference to the class to be unfolded")
	public EReference reference;

	/** {@description} */
	@Restriction(parameter = "reference")
	public List<String> checkReference(EReference reference) {
		List<String> result = new ArrayList<String>();
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
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EClass unfoldedClass = reference.getEReferenceType();
		if (!metamodel.getESubTypes(unfoldedClass).isEmpty()) {
			result.add("The class to be unfolded must not have sub classes");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass unfoldedClass = reference.getEReferenceType();
		EClass contextClass = reference.getEContainingClass();
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>(
				unfoldedClass.getEAllStructuralFeatures());

		// metamodel adaptation
		List<EStructuralFeature> unfoldedFeatures = new ArrayList<EStructuralFeature>();
		for (EStructuralFeature feature : features) {
			EStructuralFeature unfoldedFeature = MetamodelUtils.copy(feature);
			unfoldedFeatures.add(unfoldedFeature);
			if (contextClass.getEStructuralFeature(feature.getName()) != null) {
				unfoldedFeature.setName(unfoldedFeature.getName() + "_"
						+ unfoldedClass.getName());
			}
			contextClass.getEStructuralFeatures().add(unfoldedFeature);
			if (feature instanceof EReference) {
				EReference r = (EReference) feature;
				if (r.getEOpposite() != null) {
					EReference foldedOpposite = MetamodelUtils.copy(r
							.getEOpposite());
					foldedOpposite.setEType(contextClass);
					foldedOpposite.setName(foldedOpposite.getName() + "_"
							+ contextClass.getName());
					r.getEReferenceType().getEStructuralFeatures().add(
							foldedOpposite);
					model.setEOpposite(foldedOpposite,
							(EReference) unfoldedFeature);
				}
			}
		}
		metamodel.delete(reference);

		// model migration
		for (Instance contextElement : model.getAllInstances(contextClass)) {
			Instance unfoldedElement = contextElement.unset(reference);
			if (unfoldedElement != null) {
				int i = 0;
				for (EStructuralFeature feature : features) {
					Object value = unfoldedElement.unset(feature);
					contextElement.set(unfoldedFeatures.get(i), value);
					i++;
				}
				model.delete(unfoldedElement);
			}
		}

	}
}
