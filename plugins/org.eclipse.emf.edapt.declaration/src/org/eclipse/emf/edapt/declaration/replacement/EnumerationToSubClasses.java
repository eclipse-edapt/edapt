package org.eclipse.emf.edapt.declaration.replacement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
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
 * @levd.rating YELLOW Hash: 32A6A73D90ACD6CD171DBFFA788934A9
 */
@EdaptOperation(identifier= "enumerationToSubClasses", label = "Enumeration to Sub Classes", description = "In the metamodel, an enumeration attribute of a class is replaced by subclasses. The class is made abstract, and a subclass is created for each literal of the enumeration. The enumeration attribute is deleted and also the enumeration, if not used otherwise. In the model, instances the class are migrated to the appropriate subclass according to the value of the enumeration attribute.")
public class EnumerationToSubClasses extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The enumeration attribute")
	public EAttribute enumAttribute;

	/** {@description} */
	@EdaptParameter(description = "The package in which the subclasses are created")
	public EPackage ePackage;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EClass contextClass = enumAttribute.getEContainingClass();
		if (!MetamodelUtils.isConcrete(contextClass)) {
			result.add("The context class must be concrete");
		}
		if (!metamodel.getESubTypes(contextClass).isEmpty()) {
			result.add("The context class must not have sub types");
		}
		if (!(enumAttribute.getEType() instanceof EEnum)) {
			result.add("The type of the attribute must be an enumeration");
		}
		return result;
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
		EClass contextClass = enumAttribute.getEContainingClass();
		EEnum enumeration = (EEnum) enumAttribute.getEAttributeType();

		// metamodel adaptation
		List<EClass> subClasses = new ArrayList<EClass>();
		for (EEnumLiteral literal : enumeration.getELiterals()) {
			EClass subClass = MetamodelUtils.newEClass(ePackage, literal
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
		for (Instance instance : model.getInstances(contextClass)) {
			Object value = instance.unset(enumAttribute);
			int index = enumeration.getELiterals().indexOf(value);
			instance.migrate(subClasses.get(index));
		}
	}
}
