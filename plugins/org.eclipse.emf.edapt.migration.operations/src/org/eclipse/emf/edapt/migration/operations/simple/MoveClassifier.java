package org.eclipse.emf.edapt.migration.operations.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: A5219CEA6197749192E02C64AF634FD2
 */
@Operation(label = "Move Classifier", description = "In the metamodel, a classifier is moved to a different package. In the model, nothing is changed.")
public class MoveClassifier extends OperationBase {

	/** {@description} */
	@Parameter(description = "The classifier to be moved")
	public EClassifier classifier;

	/** {@description} */
	@Parameter(description = "The package to which the classifier is moved")
	public EPackage targetPackage;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (classifier.getEPackage() == targetPackage) {
			result.add("The classifier must not be already "
					+ "part of the target package");
		}
		if (targetPackage != null
				&& targetPackage.getEClassifier(classifier.getName()) != null) {
			result.add("A classifier with the same name "
					+ "exists in the target package");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		targetPackage.getEClassifiers().add(classifier);
	}
}
