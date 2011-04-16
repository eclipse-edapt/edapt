package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 248D92BE2937E58B4C99A056F66F91D1
 */
@EdaptOperation(identifier = "pushOperation", label = "Push down Operation", description = "In the metamodel, an operation is pushed down to its sub classes. In the model, nothing needs to be done.")
public class PushOperation extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The operation to be pushed down")
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
