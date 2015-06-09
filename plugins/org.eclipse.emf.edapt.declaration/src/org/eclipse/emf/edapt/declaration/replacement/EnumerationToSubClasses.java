package org.eclipse.emf.edapt.declaration.replacement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 71F5CC81FB2D51EC10F15485AAE5C533
 */
@EdaptOperation(identifier = "enumerationToSubClasses", label = "Enumeration to Sub Classes", description = "In the metamodel, an enumeration attribute of a class is replaced by subclasses. The class is made abstract, and a subclass is created for each literal of the enumeration. The enumeration attribute is deleted and also the enumeration, if not used otherwise. In the model, instances the class are migrated to the appropriate subclass according to the value of the enumeration attribute.")
public class EnumerationToSubClasses extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The enumeration attribute")
	public EAttribute enumAttribute;

	/** {@description} */
	@EdaptParameter(description = "The package in which the subclasses are created")
	public EPackage ePackage;

	/** {@description} */
	@EdaptConstraint(description = "The type of the attribute must be an enumeration")
	public boolean checkAttributeTypeEnum() {
		return enumAttribute.getEType() instanceof EEnum;
	}

	/** {@description} */
	@EdaptConstraint(description = "The context class must not have sub types")
	public boolean checkContextClassNoSubTypes(Metamodel metamodel) {
		final EClass contextClass = enumAttribute.getEContainingClass();
		return metamodel.getESubTypes(contextClass).isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(description = "The context class must be concrete")
	public boolean checkContextClassConcrete() {
		final EClass contextClass = enumAttribute.getEContainingClass();
		return MetamodelUtils.isConcrete(contextClass);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (ePackage == null) {
			ePackage = enumAttribute.getEContainingClass().getEPackage();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		final EClass contextClass = enumAttribute.getEContainingClass();
		final EEnum enumeration = (EEnum) enumAttribute.getEAttributeType();

		// metamodel adaptation
		final List<EClass> subClasses = new ArrayList<EClass>();
		for (final EEnumLiteral literal : enumeration.getELiterals()) {
			final EClass subClass = MetamodelFactory.newEClass(ePackage, literal
				.getName(), contextClass);
			subClasses.add(subClass);
		}

		metamodel.delete(enumAttribute);
		if (metamodel.getInverse(enumeration,
			EcorePackage.Literals.EATTRIBUTE__EATTRIBUTE_TYPE).isEmpty()) {
			metamodel.delete(enumeration);
		}

		contextClass.setAbstract(true);

		// model migration
		for (final Instance instance : model.getInstances(contextClass)) {
			final Object value = instance.unset(enumAttribute);
			final int index = enumeration.getELiterals().indexOf(value);
			instance.migrate(subClasses.get(index));
		}
	}
}
