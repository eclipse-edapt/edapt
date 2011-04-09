package org.eclipse.emf.edapt.migration.operations.merge;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: A71A319FAA4F852391C02FE2A36121DF
 */
@Operation(label = "Replace Literal", description = "In the metamodel, an enum literal is removed and replaced by another one. In the model, the enum's values are replaced accordingly.")
public class ReplaceLiteral extends OperationBase {

	/** {@description} */
	@Parameter(description = "The enum literal to replace")
	public EEnumLiteral toReplace;

	/** {@description} */
	@Parameter(description = "The enum literal by which it is replaced")
	public EEnumLiteral replaceBy;

	/** {@description} */
	@Restriction(parameter = "replaceBy")
	public List<String> checkReplaceBy(EEnumLiteral replaceBy) {
		EEnum contextEnum = toReplace.getEEnum();
		if (!contextEnum.getELiterals().contains(replaceBy)) {
			return Collections.singletonList("The enum literal by which "
					+ "it is replace must belong to the same enum.");
		}
		return Collections.emptyList();
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
