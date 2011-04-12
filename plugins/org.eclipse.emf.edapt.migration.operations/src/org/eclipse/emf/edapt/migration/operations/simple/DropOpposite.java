package org.eclipse.emf.edapt.migration.operations.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.declaration.incubator.Restriction;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 171BF598904806296D52325A83B25A1B
 */
@Operation(identifier = "dropOpposite", label = "Drop Opposite Relationship", description = "In the metamodel, the opposite relationship between to references is dropped. In the model, nothing needs to be done.")
public class DropOpposite extends OperationBase {

	/** {@description} */
	@Parameter(description = "The reference whose opposite relationship should be dropped")
	public EReference reference;

	/** {@description} */
	@Restriction(parameter = "reference")
	public List<String> checkReference(EReference reference) {
		List<String> result = new ArrayList<String>();
		if (reference.getEOpposite() == null) {
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
