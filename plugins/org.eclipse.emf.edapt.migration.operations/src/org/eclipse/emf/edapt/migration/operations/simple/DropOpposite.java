package org.eclipse.emf.edapt.migration.operations.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
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
 * @levd.rating YELLOW Hash: 171BF598904806296D52325A83B25A1B
 */
@Operation(label = "Drop Opposite Relationship", description = "In the metamodel, the opposite relationship between to references is dropped. In the model, nothing needs to be done.")
public class DropOpposite extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference whose opposite relationship should be dropped")
	public EReference reference;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if(reference.getEOpposite() == null) {
			result.add("Reference must have an opposite");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		model.setEOpposite(reference, null);
	}
}
