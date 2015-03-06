package org.eclipse.emf.edapt.declaration.replacement;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: BF27B6965D9A89E8000263BF9AC8CB88
 */
@EdaptOperation(identifier = "operationToVolatile", label = "Operation to Volatile Feature", description = "In the metamodel, an operation is transformed into a volatile feature. In the model, nothing needs to be done.")
public class OperationToVolatile extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The operation to be transformed")
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
		final EClass eClass = operation.getEContainingClass();
		final EClassifier type = operation.getEType();

		String name = operation.getName();
		if (name.startsWith("get")) { //$NON-NLS-1$
			name = name.substring(3);
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}

		EStructuralFeature feature = null;
		if (type instanceof EClass) {
			feature = MetamodelFactory.newEReference(eClass, name, (EClass) type,
				operation.getLowerBound(), operation.getUpperBound());
		} else {
			feature = MetamodelFactory.newEAttribute(eClass, name,
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
