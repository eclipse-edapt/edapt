package org.eclipse.emf.edapt.migration.operations.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: B91FE43712435F4F88DEE463B3D5AA13
 */
@Operation(identifier = "splitStringAttribute", label = "Split String Attribute", description = "In the metamodel, a new String-typed attribute is created. In the model, the value of another String-typed attribute is split among the two attributes by means of a regular expression.")
public class SplitStringAttribute extends OperationBase {

	/** {@description} */
	@Parameter(description = "The feature whose values are split")
	public EAttribute toSplit;

	/** {@description} */
	@Parameter(description = "The class in which the new feature is created")
	public EClass context;

	/** {@description} */
	@Restriction(parameter = "context")
	public List<String> checkContext(EClass context, Metamodel metamodel) {
		EClass eClass = toSplit.getEContainingClass();
		if (context != eClass
				&& !metamodel.getEAllSubTypes(eClass).contains(context)) {
			return Collections.singletonList("The class with the new "
					+ "attribute must be a subclass of the class with "
					+ "the attribute to be split");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@Parameter(description = "The name of the new attribute")
	public String attributeName;

	/** {@description} */
	@Parameter(description = "The regular expression")
	public String pattern;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (toSplit.getEType() != EcorePackage.Literals.ESTRING) {
			result.add("The type of the attribute to split has to be String");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		EAttribute newAttribute = MetamodelUtils.newEAttribute(context,
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
