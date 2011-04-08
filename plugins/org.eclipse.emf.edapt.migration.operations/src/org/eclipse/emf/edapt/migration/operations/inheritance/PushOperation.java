package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 5BAB81D34A7A7B67AC240D70D4A1D774
 */
@Operation(label = "Push down Operation", description = "In the metamodel, an operation is pushed down to its sub classes. In the model, nothing needs to be done.")
public class PushOperation extends OperationBase {

	/** {@description} */
	@Parameter(description = "The operation to be pushed down")
	public EOperation operation;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EClass superClass = operation.getEContainingClass();
		List<EClass> subClasses = metamodel.getESubTypes(superClass);

		// metamodel adaptation
		boolean first = true;
		for (EClass subClass : subClasses) {
			if (first) {
				subClass.getEOperations().add(operation);
			} else {
				EOperation clone = MetamodelUtils.copy(operation);
				subClass.getEOperations().add(clone);
			}
			first = false;
		}
	}
}
