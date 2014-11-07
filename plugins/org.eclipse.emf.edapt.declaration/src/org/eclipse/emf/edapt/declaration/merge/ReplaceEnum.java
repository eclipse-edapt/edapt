package org.eclipse.emf.edapt.declaration.merge;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcorePackage;
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
 * @levd.rating YELLOW Hash: E4E53225C0587233ACB4456803A7E86B
 */
@EdaptOperation(identifier = "replaceEnum", label = "Replace Enumeration", description = "In the metamodel, an enumeration is replaced by another one. More specifically, the enumeration is deleted and the other enumeration used instead. In the model, the values of this enumeration are replaced based on a mapping of literals.")
public class ReplaceEnum extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The enumeration to be replaced")
	public EEnum toReplace;

	/** {@description} */
	@EdaptParameter(description = "The enumeration by which it is replaced")
	public EEnum replaceBy;

	/** {@description} */
	@EdaptParameter(description = "The literals to be replaced")
	public List<EEnumLiteral> literalsToReplace;

	/** {@description} */
	@EdaptConstraint(restricts = "literalsToReplace", description = "The replaced literals must "
			+ "belong to the replaced enumeration")
	public boolean checkLiteralsToReplaceCommonEnumeration(
			EEnumLiteral literalsToReplace) {
		return toReplace.getELiterals().contains(literalsToReplace);
	}

	/** {@description} */
	@EdaptParameter(description = "The literals by which they are replaced (in the same order)")
	public List<EEnumLiteral> literalsReplaceBy;

	/** {@description} */
	@EdaptConstraint(restricts = "literalsReplaceBy", description = "The replacing literals must "
			+ "belong to the replacing enumeration")
	public boolean checkLiteralsReplaceBy(EEnumLiteral literalsReplaceBy) {
		return replaceBy.getELiterals().contains(literalsReplaceBy);
	}

	/** {@description} */
	@EdaptConstraint(description = "The replacing and replaced literals must be of the same size")
	public boolean checkLiteralsSameSize() {
		return literalsReplaceBy.size() == literalsToReplace.size();
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
