package org.eclipse.emf.edapt.migration.operations.replacement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 419BA09CF5AAE6C41B33171BD2170AF7
 */
@Operation(identifier = "replaceIdentifierByReference", label = "Identifier to Reference", description = "In the metamodel, an attribute that references elements by identifier is replaced by a reference. In the model, its values are replaced by references to that element.")
public class ReplaceIdentifierByReference extends OperationBase {

	/** {@description} */
	@Parameter(description = "The referencing attribute")
	public EAttribute referencingAttribute;

	/** {@description} */
	@Parameter(description = "The referenced attribute")
	public EAttribute referencedAttribute;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (referencedAttribute != null) {
			if (referencingAttribute.getEType() != referencedAttribute
					.getEType()) {
				result.add("Referencing and referenced attribute "
						+ "must be of the same type");
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EClass referencedClass = referencedAttribute.getEContainingClass();

		// metamodel adaptation
		EClass referencingClass = referencingAttribute.getEContainingClass();
		EReference referencingReference = MetamodelUtils.newEReference(
				referencingClass, referencingAttribute.getName(),
				referencedClass, referencingAttribute.getLowerBound(),
				referencingAttribute.getUpperBound());
		metamodel.delete(referencingAttribute);

		// model migration
		Map<Object, Instance> referencedElements = new HashMap<Object, Instance>();
		for (Instance referencedElement : model
				.getAllInstances(referencedClass)) {
			referencedElements.put(referencedElement.get(referencedAttribute),
					referencedElement);
		}
		for (Instance referencingElement : model
				.getAllInstances(referencingClass)) {
			Object reference = referencingElement.unset(referencingAttribute);
			referencingElement.set(referencingReference, referencedElements
					.get(reference));
		}
	}
}
