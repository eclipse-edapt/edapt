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
 * @levd.rating YELLOW Hash: EB606535E52A970731004DE5BF7C9C02
 */
@EdaptOperation(identifier = "replaceLiteral", label = "Replace Literal", description = "In the metamodel, an enum literal is removed and replaced by another one. In the model, the enum's values are replaced accordingly.")
public class ReplaceLiteral extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The enum literal to replace")
	public EEnumLiteral toReplace;

	/** {@description} */
	@EdaptParameter(description = "The enum literal by which it is replaced")
	public EEnumLiteral replaceBy;

	/** {@description} */
	@EdaptConstraint(restricts = "replaceBy", description = "The enum literal by which it is replace must belong to the same enum.")
	public boolean checkReplaceBy(EEnumLiteral replaceBy) {
		return toReplace.getEEnum().getELiterals().contains(replaceBy);
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EEnum contextEnum = toReplace.getEEnum();

		// metamodel adaptation
		metamodel.delete(toReplace);

		// model migration
		List<EAttribute> attributes = metamodel.getInverse(contextEnum,
				EcorePackage.Literals.EATTRIBUTE__EATTRIBUTE_TYPE);
		for (EAttribute attribute : attributes) {
			EClass contextClass = attribute.getEContainingClass();
			for (Instance instance : model.getAllInstances(contextClass)) {
				if (instance.get(attribute) == toReplace) {
					instance.set(attribute, replaceBy);
				}
			}
		}
	}
}
