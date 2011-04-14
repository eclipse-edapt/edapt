package org.eclipse.emf.edapt.declaration.replacement;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * @levd.rating YELLOW Hash: 63BA926950F0C0C6B2F5BBAB124A7908
 */
@EdaptOperation(identifier = "operationToVolatile", label = "Operation to Volatile Feature", description = "In the metamodel, an operation is transformed into a volatile feature. In the model, nothing needs to be done.")
public class OperationToVolatile extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The operation to be transformed")
	public EOperation operation;

	/** {@description} */
	@EdaptConstraint(description = "The operation must not have parameters")
	public boolean checkCustomPreconditions() {
		return operation.getEParameters().isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		EClass eClass = operation.getEContainingClass();
		EClassifier type = operation.getEType();

		String name = operation.getName();
		if (name.startsWith("get")) {
			name = name.substring(3);
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}

		EStructuralFeature feature = null;
		if (type instanceof EClass) {
			feature = MetamodelUtils.newEReference(eClass, name, (EClass) type,
					operation.getLowerBound(), operation.getUpperBound());
		} else {
			feature = MetamodelUtils.newEAttribute(eClass, name,
					(EDataType) type, operation.getLowerBound(), operation
							.getUpperBound());
		}
		feature.setVolatile(true);
		feature.setTransient(true);
		feature.setDerived(true);
		feature.setChangeable(false);

		metamodel.delete(operation);
	}
}
