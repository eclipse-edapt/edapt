package org.eclipse.emf.edapt.declaration.generalization;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 6832681E74A069D655EBF4672CF70D81
 */
@EdaptOperation(identifier = "specializeReferenceType", label = "Specialize Reference Type", description = "In the metamodel, the type of a reference can be specialized to its subclass, in case it is abstract and has only one subclass. In the model, nothing is changed.")
public class SpecializeReferenceType extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The reference whose type is specialized")
	public EReference reference;

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The old type of the reference must be abstract")
	public boolean checkReference(EReference reference) {
		EClass oldType = reference.getEReferenceType();
		return !MetamodelUtils.isConcrete(oldType);
	}

	/** {@description} */
	@EdaptConstraint(restricts = "reference", description = "The old type must not have any other subclass")
	public boolean checkOldTypeSingleSubType(EReference reference,
			Metamodel metamodel) {
		EClass oldType = reference.getEReferenceType();
		return metamodel.getESubTypes(oldType).size() == 1;
	}

	/** {@description} */
	@EdaptParameter(description = "The new type of the reference")
	public EClass type;

	/** {@description} */
	@EdaptConstraint(restricts = "type", description = "The new type of the reference must be a subclass of its old type")
	public boolean checkType(EClass type) {
		EClass oldType = reference.getEReferenceType();
		return type.getESuperTypes().contains(oldType);
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		reference.setEType(type);
	}
}
