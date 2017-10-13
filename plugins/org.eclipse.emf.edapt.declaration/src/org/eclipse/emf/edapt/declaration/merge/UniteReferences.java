package org.eclipse.emf.edapt.declaration.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.internal.common.TypeUtils;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: F7479C6CE1ED48B166FD63B9297C2359
 */
@EdaptOperation(identifier = "uniteReferences", label = "Unite References", description = "In the metamodel, a number of references are united into a single reference which obtains their common super type as type. In the model, their values have to be moved accordingly.", breaking = true)
public class UniteReferences extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The references which are united")
	public List<EReference> references;

	/** {@description} */
	@EdaptParameter(description = "The name of the single reference which unites all the references")
	public String unitedReferenceName;

	/** {@description} */
	@EdaptConstraint(description = "The references must be all either cross or containment references")
	public boolean checkReferencesSameContainment() {
		return hasSameValue(references,
			EcorePackage.Literals.EREFERENCE__CONTAINMENT);
	}

	/** {@description} */
	@EdaptConstraint(description = "The references have to belong to the same class")
	public boolean checkReferencesSameClass() {
		return hasSameValue(references, EcorePackage.eINSTANCE
			.getEStructuralFeature_EContainingClass());
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EReference mainReference = references.get(0);
		final EClass contextClass = mainReference.getEContainingClass();

		// metamodel adaptation
		final List<EClass> referenceTypes = new ArrayList<EClass>();
		for (final EReference reference : references) {
			referenceTypes.add(reference.getEReferenceType());
			metamodel.delete(reference);
		}
		final EClass type = TypeUtils.leastCommonAncestor(referenceTypes);
		final EReference unitedReference = MetamodelFactory
			.newEReference(contextClass, unitedReferenceName, type, 0, -1,
				mainReference.isContainment());

		// model migration
		for (final Instance contextElement : model.getAllInstances(contextClass)) {
			for (final EReference reference : references) {
				if (reference.isMany()) {
					final List values = contextElement.unset(reference);
					((List) contextElement.get(unitedReference)).addAll(values);
				} else {
					final Instance value = contextElement.unset(reference);
					if (value != null) {
						contextElement.add(unitedReference, value);
					}
				}
			}
		}
	}
}
