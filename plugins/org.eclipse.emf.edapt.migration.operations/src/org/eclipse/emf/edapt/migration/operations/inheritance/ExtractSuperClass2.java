package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
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
 * @levd.rating YELLOW Hash: 1FF0213AB6CA208B39AA762AEB3BE9FF
 */
@Operation(identifier = "extractSuperClass2", label = "Extract Super Class", description = "In the metamodel, a super class is extracted from a number of sub classes. In the model, nothing is changed.")
public class ExtractSuperClass2 extends OperationBase {

	/** {@description} */
	@Parameter(description = "The classes from which the super class is extracted")
	public List<EClass> subClasses;

	/** {@description} */
	@Parameter(description = "The features to be extracted")
	public List<EStructuralFeature> toExtract;

	/** {@description} */
	@Parameter(description = "The package in which the super class is created")
	public EPackage ePackage;

	/** {@description} */
	@Parameter(description = "The name of the super class")
	public String superClassName;

	/** {@description} */
	@Parameter(description = "Whether the super class is abstract")
	public Boolean abstr = true;

	/** {@description} */
	@Parameter(description = "The super classes of the sub class which become super classes of the super class")
	public List<EClass> superSuperClasses = new ArrayList<EClass>();

	/** {@description} */
	@Restriction(parameter = "superSuperClasses")
	public List<String> checkSuperSuperClasses(EClass superSuperClass) {
		for (EClass subClass : subClasses) {
			if (!subClass.getESuperTypes().contains(superSuperClass)) {
				return Collections.singletonList("The sub classes must "
						+ "have the super classes as common super classes");
			}
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		EStructuralFeature mainFeature = toExtract.isEmpty() ? null : toExtract
				.get(0);
		if (mainFeature != null) {
			for (EStructuralFeature feature : toExtract) {
				if (mainFeature.getEType() != feature.getEType()) {
					result.add("The features' types have to be the same");
					break;
				}
			}
			for (EStructuralFeature feature : toExtract) {
				if (mainFeature.getLowerBound() != feature.getLowerBound()
						|| mainFeature.getUpperBound() != feature
								.getUpperBound()) {
					result.add("The features' multiplicities "
							+ "have to be the same");
					break;
				}
			}
			for (EStructuralFeature feature : toExtract) {
				if (mainFeature.eClass() != feature.eClass()) {
					result.add("The features have "
							+ "to be all attributes or references");
					break;
				}
			}
			if (mainFeature instanceof EReference) {
				EReference mainReference = (EReference) mainFeature;
				for (EStructuralFeature feature : toExtract) {
					if (feature instanceof EReference) {
						EReference reference = (EReference) feature;
						if (mainReference.isContainment()
								&& reference.isContainment()) {
							result.add("The features have to "
									+ "be all containment references or not");
							break;
						}
					}
				}
			}
			for (EStructuralFeature feature : toExtract) {
				if (feature instanceof EReference) {
					EReference reference = (EReference) feature;
					if (reference.getEOpposite() != null) {
						result.add("The features must "
								+ "not have opposite references");
					}
				}
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (ePackage == null) {
			ePackage = subClasses.get(0).getEPackage();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		EClass superClass = MetamodelUtils.newEClass(ePackage, superClassName,
				superSuperClasses, abstr);
		for (EClass subClass : subClasses) {
			subClass.getESuperTypes().add(superClass);
			subClass.getESuperTypes().removeAll(superSuperClasses);
		}

		if (!toExtract.isEmpty()) {
			PullFeature operation = new PullFeature();
			operation.features = toExtract;
			operation.targetClass = superClass;
			operation.execute(metamodel, model);
		}
	}
}
