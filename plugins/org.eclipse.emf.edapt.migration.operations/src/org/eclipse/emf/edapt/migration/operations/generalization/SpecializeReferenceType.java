package org.eclipse.emf.edapt.migration.operations.generalization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 51DC68B5EEEED698AE01F6D31FA61D01
 */
@Operation(label = "Specialize Reference Type", description = "In the metamodel, the type of a reference can be specialized to its subclass, in case it is abstract and has only one subclass. In the model, nothing is changed.")
public class SpecializeReferenceType extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference whose type is specialized")
	public EReference reference;

	/** {@description} */
	@Parameter(description = "The new type of the reference")
	public EClass type;

	/** {@description} */
	@Restriction(parameter = "type")
	public List<String> checkType(EClass type) {
		EClass oldType = reference.getEReferenceType();
		if (type.getESuperTypes().contains(oldType)) {
			return Collections.singletonList("The new type of the reference "
					+ "must be a subclass of its old type");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EClass oldType = reference.getEReferenceType();
		if (MetamodelUtils.isConcrete(oldType)) {
			result.add("The old type of the reference must be abstract");
		}
		if (metamodel.getESubTypes(oldType).size() > 1) {
			result.add("The old type must not have any other subclass");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		reference.setEType(type);
	}
}
