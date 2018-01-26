package org.eclipse.emf.edapt.declaration.simple;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 9817C7692F6D9C48627C869E0F05ACD1
 */
@EdaptOperation(identifier = "moveClassifier", label = "Move Classifier", description = "In the metamodel, a classifier is moved to a different package. In the model, nothing is changed.", breaking = true)
public class MoveClassifier extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The classifier to be moved")
	public EClassifier classifier;

	/** {@description} */
	@EdaptParameter(description = "The package to which the classifier is moved")
	public EPackage targetPackage;

	/** {@description} */
	@EdaptConstraint(description = "A classifier with the same name exists in the target package")
	public boolean checkUniqueClassifierNameInTargetPackage() {
		return targetPackage == null
			|| targetPackage.getEClassifier(classifier.getName()) == null;
	}

	/** {@description} */
	@EdaptConstraint(description = "The classifier must not be already part of the target package")
	public boolean checkClassifierNotInTargetPackage() {
		return classifier.getEPackage() != targetPackage;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		targetPackage.getEClassifiers().add(classifier);
	}
}
