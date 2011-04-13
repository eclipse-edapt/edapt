package org.eclipse.emf.edapt.declaration.replacement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: DAE9180C71AFF0D7BC04276291959BB4
 */
@EdaptOperation(identifier = "subClassesToEnumeration", label = "Sub Classes to Enumeration", description = "In the metamodel, the subclasses of a class are replaced by an enumeration. An enumeration with literals for all subclasses is created and an enumeration attribute is created in the class. Finally, all subclasses are deleted, and the class is made concrete. In the model, instances of a subclass are migrated to the class, setting the enumeration attribute to the appropriate literal.")
public class SubClassesToEnumeration extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The context class")
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

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (MetamodelUtils.isConcrete(contextClass)) {
			result.add("The context class must be abstract");
		}
		if (metamodel.getESubTypes(contextClass).isEmpty()) {
			result.add("The context class must have sub types");
		}
		for (EClass subClass : metamodel.getESubTypes(contextClass)) {
			if (!metamodel.getESubTypes(subClass).isEmpty()) {
				result.add("The sub types must not have sub types again");
				break;
			}
		}
		return result;
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
		EEnum enumeration = MetamodelUtils.newEEnum(ePackage, enumName);
		EAttribute attribute = MetamodelUtils.newEAttribute(contextClass,
				attributeName, enumeration, 1, 1);

		contextClass.setAbstract(false);

		List<EClass> subClasses = new ArrayList<EClass>();
		int i = 0;
		for (EClass subClass : metamodel.getESubTypes(contextClass)) {
			EEnumLiteral literal = MetamodelUtils.newEEnumLiteral(enumeration,
					subClass.getName());
			literal.setValue(i);
			subClasses.add(subClass);
			metamodel.delete(subClass);
			i++;
		}

		// model migration
		i = 0;
		for (EClass subClass : subClasses) {
			EEnumLiteral literal = enumeration.getELiterals().get(i);
			for (Instance instance : model.getInstances(subClass)) {
				instance.migrate(contextClass);
				instance.set(attribute, literal);
			}
			i++;
		}
	}
}
