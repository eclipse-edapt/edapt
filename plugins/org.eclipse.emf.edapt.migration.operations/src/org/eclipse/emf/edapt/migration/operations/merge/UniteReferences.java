package org.eclipse.emf.edapt.migration.operations.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.common.TypeUtils;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: CF12DA31D4CCED2A2581B224A04B1DFA
 */
@Operation(identifier = "uniteReferences", label = "Unite References", description = "In the metamodel, a number of references are united into a single reference which obtains their common super type as type. In the model, their values have to be moved accordingly.")
public class UniteReferences extends OperationBase {

	/** {@description} */
	@Parameter(description = "The references which are united")
	public List<EReference> references;

	/** {@description} */
	@Parameter(description = "The name of the single reference which unites all the references")
	public String unitedReferenceName;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EReference mainReference = references.get(0);
		EClass contextClass = mainReference.getEContainingClass();
		if (!contextClass.getEStructuralFeatures().containsAll(references)) {
			result.add("The references have to belong to the same class");
		}
		if (!hasSameValue(references,
				EcorePackage.Literals.EREFERENCE__CONTAINMENT)) {
			result.add("The references must be all "
					+ "either cross or containment references");
		}
		return result;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EReference mainReference = references.get(0);
		EClass contextClass = mainReference.getEContainingClass();

		// metamodel adaptation
		List<EClass> referenceTypes = new ArrayList<EClass>();
		for (EReference reference : references) {
			referenceTypes.add(reference.getEReferenceType());
			metamodel.delete(reference);
		}
		EClass type = TypeUtils.leastCommonAncestor(referenceTypes);
		EReference unitedReference = MetamodelUtils
				.newEReference(contextClass, unitedReferenceName, type, 0, -1,
						mainReference.isContainment());

		// model migration
		for (Instance contextElement : model.getAllInstances(contextClass)) {
			for (EReference reference : references) {
				if (reference.isMany()) {
					List values = contextElement.unset(reference);
					((List) contextElement.get(unitedReference)).addAll(values);
				} else {
					Instance value = contextElement.unset(reference);
					if (value != null) {
						contextElement.add(unitedReference, value);
					}
				}
			}
		}
	}
}
