package org.eclipse.emf.edapt.declaration.delegation;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 92A023B7F2C535962246670FFB2B5BCF
 */
@EdaptOperation(identifier = "extractAndGroupAttribute", label = "Extract and Group Attribute", description = "In the metamodel, an attribute is extracted into a new class. This extracted class is contained by an existing container class and referenced from the context class. In the model, an instance of the extracted class is created for each different value of the extracted attribute.", breaking = true)
public class ExtractAndGroupAttribute extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The attribute to be extracted")
	public EAttribute extractedAttribute;

	/** {@description} */
	@EdaptConstraint(restricts = "extractedAttribute", description = "The extracted attribute must be single-valued")
	public boolean checkExtractedAttribute(EAttribute extractedAttribute) {
		return !extractedAttribute.isMany();
	}

	/** {@description} */
	@EdaptParameter(description = "The package in which the extracted class is created")
	public EPackage contextPackage;

	/** {@description} */
	@EdaptParameter(description = "The name of the extracted class")
	public String extractedClassName;

	/** {@description} */
	@EdaptParameter(description = "The reference from the context class to the extracted class")
	public String referenceName;

	/** {@description} */
	@EdaptParameter(description = "The container class for the extracted class")
	public EClass containerClass;

	/** {@description} */
	@EdaptParameter(description = "The name of the containment reference from the container class to the extracted class")
	public String containerReferenceName;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		final EClass contextClass = extractedAttribute.getEContainingClass();

		// metamodel adaptation
		final EClass extractedClass = MetamodelFactory.newEClass(contextPackage,
			extractedClassName);
		extractedClass.getEStructuralFeatures().add(extractedAttribute);
		extractedAttribute.setLowerBound(1);

		final EReference reference = MetamodelFactory.newEReference(contextClass,
			referenceName, extractedClass, 0, 1, false);

		final EReference containerReference = MetamodelFactory.newEReference(
			containerClass, containerReferenceName, extractedClass, 0, -1,
			true);

		// model migration
		for (final Instance contextElement : model.getAllInstances(contextClass)) {
			final Object value = contextElement.unset(extractedAttribute);
			if (value != null) {
				Instance containerElement = contextElement;
				while (containerElement != null
					&& !containerElement.instanceOf(containerClass)) {
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
		final List<Instance> elements = containerElement.get(containerReference);
		for (final Instance element : elements) {
			if (value.equals(element.get(extractedAttribute))) {
				return element;
			}
		}
		return null;
	}
}
