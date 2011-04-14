package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: 43DD741B001842847771F4B372603548
 */
@EdaptOperation(identifier = "deletePackage", label = "Delete Package", description = "In the metamodel, an empty package is deleted.")
public class DeletePackage extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The package to be deleted")
	public EPackage ePackage;

	/** {@description} */
	@EdaptConstraint(restricts = "ePackage", description = "The package must not contain classifiers")
	public boolean checkPackageNoClassifiers(EPackage ePackage) {
		return ePackage.getEClassifiers().isEmpty();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "ePackage", description = "The package must not contain subpackages")
	public boolean checkPackageNoSubPackages(EPackage ePackage) {
		return ePackage.getESubpackages().isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		metamodel.delete(ePackage);
	}
}
