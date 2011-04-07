package org.eclipse.emf.edapt.migration.operations.delegation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: AB19D61624C3FE4172A4D208B0E81B58
 */
@Operation(label = "Extract and Group Attribute", description = "In the metamodel, an attribute is extracted into a new class. This extracted class is contained by an existing container class and referenced from the context class. In the model, an instance of the extracted class is created for each different value of the extracted attribute.")
public class ExtractAndGroupAttribute extends OperationBase {

	/** {@description} */
	@Parameter(description = "The attribute to be extracted")
	public EAttribute extractedAttribute;

	/** {@description} */
	@Parameter(description = "The package in which the extracted class is created")
	public EPackage contextPackage;

	/** {@description} */
	@Parameter(description = "The name of the extracted class")
	public String extractedClassName;

	/** {@description} */
	@Parameter(description = "The reference from the context class to the extracted class")
	public String referenceName;

	/** {@description} */
	@Parameter(description = "The container class for the extracted class")
	public EClass containerClass;

	/** {@description} */
	@Parameter(description = "The name of the containment reference from the container class to the extracted class")
	public String containerReferenceName;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (extractedAttribute.isMany()) {
			result.add("The extracted attribute must be single-valued");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EClass contextClass = extractedAttribute.getEContainingClass();

		// metamodel adaptation
		EClass extractedClass = MetamodelUtils.newEClass(contextPackage,
				extractedClassName);
		extractedClass.getEStructuralFeatures().add(extractedAttribute);
		extractedAttribute.setLowerBound(1);

		EReference reference = MetamodelUtils.newEReference(contextClass,
				referenceName, extractedClass, 0, 1, false);

		EReference containerReference = MetamodelUtils.newEReference(
				containerClass, containerReferenceName, extractedClass, 0, -1,
				true);

		// model migration
		for (Instance contextElement : model.getAllInstances(contextClass)) {
			Object value = contextElement.unset(extractedAttribute);
			if (value != null) {
				Instance containerElement = contextElement;
				while (containerElement != null
						&& !(containerElement.instanceOf(containerClass))) {
					containerElement = containerElement.getContainer();
				}
				if (containerElement != null) {
					Instance extractedElement = getExtractedElement(
							containerElement, containerReference,
							extractedAttribute, value);
					if (extractedElement == null) {
						extractedElement = model.newInstance(extractedClass);
						extractedElement.set(extractedAttribute, value);
						containerElement.add(containerReference,
								extractedElement);
					}
					contextElement.set(reference, extractedElement);
				}
			}
		}
	}

	/** Get the extracted element that has a certain value for an attribute. */
	private Instance getExtractedElement(Instance containerElement,
			EReference containerReference, EAttribute extractedAttribute,
			Object value) {
		List<Instance> elements = containerElement.get(containerReference);
		for (Instance element : elements) {
			if (value.equals(element.get(extractedAttribute))) {
				return element;
			}
		}
		return null;
	}
}
