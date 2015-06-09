package org.eclipse.emf.edapt.declaration.replacement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: 0D892606BB497749BBDA3AF7AFC8BEF4
 */
@EdaptOperation(identifier = "subClassesToEnumeration", label = "Sub Classes to Enumeration", description = "In the metamodel, the subclasses of a class are replaced by an enumeration. An enumeration with literals for all subclasses is created and an enumeration attribute is created in the class. Finally, all subclasses are deleted, and the class is made concrete. In the model, instances of a subclass are migrated to the class, setting the enumeration attribute to the appropriate literal.")
public class SubClassesToEnumeration extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The context class")
	public EClass contextClass;

	/** {@description} */
	@EdaptParameter(description = "The name of the enumeration attribute")
	public String attributeName;

	/** {@description} */
	@EdaptParameter(description = "The package in which the enumeration is created")
	public EPackage ePackage;

	/** {@description} */
	@EdaptParameter(description = "The name of the enumeration")
	public String enumName;

	/** {@description} */
	@EdaptConstraint(description = "The sub types must not have sub types again")
	public boolean checkContextClassSubTypesNoSubTypes(Metamodel metamodel) {
		for (final EClass subClass : metamodel.getESubTypes(contextClass)) {
			if (!metamodel.getESubTypes(subClass).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/** {@description} */
	@EdaptConstraint(description = "The context class must have sub types")
	public boolean checkContextClassSubTypes(Metamodel metamodel) {
		return !metamodel.getESubTypes(contextClass).isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(description = "The context class must be abstract")
	public boolean checkContextClassAbstract() {
		return !MetamodelUtils.isConcrete(contextClass);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (ePackage == null) {
			ePackage = contextClass.getEPackage();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		final EEnum enumeration = MetamodelFactory.newEEnum(ePackage, enumName);
		final EAttribute attribute = MetamodelFactory.newEAttribute(contextClass,
			attributeName, enumeration, 1, 1);

		contextClass.setAbstract(false);

		final List<EClass> subClasses = new ArrayList<EClass>();
		int i = 0;
		for (final EClass subClass : metamodel.getESubTypes(contextClass)) {
			final EEnumLiteral literal = MetamodelFactory.newEEnumLiteral(enumeration,
				subClass.getName());
			literal.setValue(i);
			subClasses.add(subClass);
			metamodel.delete(subClass);
			i++;
		}

		// model migration
		i = 0;
		for (final EClass subClass : subClasses) {
			final EEnumLiteral literal = enumeration.getELiterals().get(i);
			for (final Instance instance : model.getInstances(subClass)) {
				instance.migrate(contextClass);
				instance.set(attribute, literal);
			}
			i++;
		}
	}
}
