package org.eclipse.emf.edapt.declaration.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 57B0E83E61455DC81C6811837D750C9F
 */
@EdaptOperation(identifier = "unfoldClass", label = "Unfold Class", description = "In the metamodel, a class reachable through a single-valued containment reference is unfolded. More specifically, its features are copied to the source class of the reference which is deleted. In the model, the values of these features are moved accordingly.")
public class UnfoldClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The reference to the class to be unfolded")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must not have an opposite")
	public boolean checkReference(EReference reference) {
		return reference.getEOpposite() == null;
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The multiplicity of the reference must be single-valued")
	public boolean checkReferenceManyValued(EReference reference) {
		return !reference.isMany();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The reference must be containment")
	public boolean checkReferenceContainment(EReference reference) {
		return reference.isContainment();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The class to be unfolded must not have sub classes")
	public boolean checkUnfoldedClassNoSubTypes(EReference reference,
			Metamodel metamodel) {
		EClass unfoldedClass = reference.getEReferenceType();
		return metamodel.getESubTypes(unfoldedClass).isEmpty();
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
					metamodel.setEOpposite(foldedOpposite,
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
