package org.eclipse.emf.edapt.migration.operations.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: 1FC0DA6B5D76EB448371291010B943BB
 */
@Operation(identifier = "deletePackage", label = "Delete Package", description = "In the metamodel, an empty package is deleted.")
public class DeletePackage extends OperationBase {

	/** {@description} */
	@Parameter(description = "The package to be deleted")
	public EPackage ePackage;

	/** {@description} */
	@Restriction(parameter = "ePackage")
	public List<String> checkPackage(EPackage ePackage) {
		List<String> result = new ArrayList<String>();
		if (!ePackage.getEClassifiers().isEmpty()) {
			result.add("The package must not contain classifiers");
		}
		if (!ePackage.getESubpackages().isEmpty()) {
			result.add("The package must not contain subpackages");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		metamodel.delete(ePackage);
	}
}
