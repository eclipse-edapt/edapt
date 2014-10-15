package org.eclipse.emf.edapt.declaration.merge;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelFactory;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 178845EC818964963C16FEBB3FF1EBD1
 */
@EdaptOperation(identifier = "splitStringAttribute", label = "Split String Attribute", description = "In the metamodel, a new String-typed attribute is created. In the model, the value of another String-typed attribute is split among the two attributes by means of a regular expression.")
public class SplitStringAttribute extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The feature whose values are split")
	public EAttribute toSplit;
	
	/** {@description} */
	@EdaptConstraint(restricts = "toSplit", description = "The type of the attribute to split has to be String")
	public boolean checkAttributeTypeString(EAttribute toSplit) {
		return toSplit.getEType() == EcorePackage.Literals.ESTRING;
	}

	/** {@description} */
	@EdaptParameter(description = "The class in which the new feature is created")
	public EClass context;

	/** {@description} */
	@EdaptConstraint(restricts = "context", description = "The class with the new attribute must be a subclass of the class with the attribute to be split")
	public boolean checkContext(EClass context, Metamodel metamodel) {
		EClass eClass = toSplit.getEContainingClass();
		return context == eClass
				|| metamodel.getEAllSubTypes(eClass).contains(context);
	}

	/** {@description} */
	@EdaptParameter(description = "The name of the new attribute")
	public String attributeName;

	/** {@description} */
	@EdaptParameter(description = "The regular expression")
	public String pattern;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		EAttribute newAttribute = MetamodelFactory.newEAttribute(context,
				attributeName, EcorePackage.Literals.ESTRING);

		// model migration
		for (Instance instance : model.getAllInstances(context)) {
			String value = instance.get(toSplit);
			String[] fragments = value.split(pattern);
			if (fragments.length > 1) {
				instance.set(toSplit, fragments[0]);
				instance.set(newAttribute, fragments[1]);
			}
		}
	}
}
