package org.eclipse.emf.edapt.declaration.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
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
 * @levd.rating YELLOW Hash: 07445202FC45211ED83C0892AD808CB7
 */
@EdaptOperation(identifier = "replaceEnum", label = "Replace Enumeration", description = "In the metamodel, an enumeration is replaced by another one. More specifically, the enumeration is deleted and the other enumeration used instead. In the model, the values of this enumeration are replaced based on a mapping of literals.")
public class ReplaceEnum extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The enumeration to be replaced")
	public EEnum toReplace;

	/** {@description} */
	@EdaptParameter(description = "The enumeration by which it is replaced")
	public EEnum replaceBy;

	/** {@description} */
	@EdaptParameter(description = "The literals to be replaced")
	public List<EEnumLiteral> literalsToReplace;

	/** {@description} */
	@EdaptRestriction(parameter = "literalsToReplace")
	public List<String> checkLiteralsToReplace(EEnumLiteral literalsToReplace) {
		if (toReplace.getELiterals().contains(literalsToReplace)) {
			return Collections.singletonList("The replaced literals must "
					+ "belong to the replaced enumeration");
		}
		return Collections.emptyList();
	}

	/** {@description} */
	@EdaptParameter(description = "The literals by which they are replaced (in the same order)")
	public List<EEnumLiteral> literalsReplaceBy;

	/** {@description} */
	@EdaptRestriction(parameter = "literalsReplaceBy")
	public List<String> checkLiteralsReplaceBy(EEnumLiteral literalsReplaceBy) {
		if (!replaceBy.getELiterals().contains(literalsReplaceBy)) {
			return Collections.singletonList("The replacing literals must "
					+ "belong to the replacing enumeration");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (literalsReplaceBy.size() != literalsToReplace.size()) {
			result.add("The replacing and replaced "
					+ "literals must be of the same size");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		List<EAttribute> attributes = metamodel.<EAttribute> getInverse(
				toReplace, EcorePackage.Literals.EATTRIBUTE__EATTRIBUTE_TYPE);
		for (EAttribute attribute : attributes) {
			attribute.setEType(replaceBy);
		}
		metamodel.delete(toReplace);

		// model migration
		for (EAttribute attribute : attributes) {
			EClass eClass = attribute.getEContainingClass();
			for (Instance instance : model.getAllInstances(eClass)) {
				if (instance.isSet(attribute)) {
					Object value = instance.get(attribute);
					int index = literalsToReplace.indexOf(value);
					instance.set(attribute, literalsReplaceBy.get(index));
				}
			}
		}
	}
}
