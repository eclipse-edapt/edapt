package org.eclipse.emf.edapt.declaration.replacement;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 7F529BB92C7A9BC7EC388CC3510AF704
 */
@EdaptOperation(identifier = "replaceIdentifierByReference", label = "Identifier to Reference", description = "In the metamodel, an attribute that references elements by identifier is replaced by a reference. In the model, its values are replaced by references to that element.")
public class ReplaceIdentifierByReference extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The referencing attribute")
	public EAttribute referencingAttribute;

	/** {@description} */
	@EdaptParameter(description = "The referenced attribute")
	public EAttribute referencedAttribute;

	/** {@description} */
	@EdaptConstraint(description = "Referencing and referenced attribute must be of the same type")
	public boolean checkAttributesSameType() {
		return referencedAttribute == null
				|| referencingAttribute.getEType() == referencedAttribute
						.getEType();
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
