package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelFactory;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: B8DB4998514788FC73F9E379BC561FDE
 */
@EdaptOperation(identifier = "extractSuperClass2", label = "Extract Super Class", description = "In the metamodel, a super class is extracted from a number of sub classes. In the model, nothing is changed.")
public class ExtractSuperClass2 extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The classes from which the super class is extracted")
	public List<EClass> subClasses;

	/** {@description} */
	@EdaptParameter(optional = true, description = "The features to be extracted")
	public List<EStructuralFeature> toExtract = new ArrayList<EStructuralFeature>();

	/** {@description} */
	@EdaptParameter(description = "The package in which the super class is created")
	public EPackage ePackage;

	/** {@description} */
	@EdaptParameter(description = "The name of the super class")
	public String superClassName;

	/** {@description} */
	@EdaptParameter(description = "Whether the super class is abstract")
	public Boolean abstr = true;

	/** {@description} */
	@EdaptParameter(optional = true, description = "The super classes of the sub class which become super classes of the super class")
	public List<EClass> superSuperClasses = new ArrayList<EClass>();

	/** {@description} */
	@EdaptConstraint(restricts = "superSuperClasses", description = "The sub classes must have the super classes as common super classes")
	public boolean checkSuperSuperClasses(EClass superSuperClass) {
		for (EClass subClass : subClasses) {
			if (!subClass.getESuperTypes().contains(superSuperClass)) {
				return false;
			}
		}
		return true;
	}

	/** {@description} */
	@EdaptConstraint(description = "The features must not have opposite references")
	public boolean checkSameOpposite() {
		return !isOfType(toExtract, EcorePackage.eINSTANCE.getEReference())
				|| hasValue(toExtract, EcorePackage.eINSTANCE
						.getEReference_EOpposite(), null);
	}

	/** {@description} */
	@EdaptConstraint(description = "The features have to be all containment references or not")
	public boolean checkSameContainment() {
		return !isOfType(toExtract, EcorePackage.eINSTANCE.getEReference())
				|| hasSameValue(toExtract, EcorePackage.eINSTANCE
						.getEReference_Containment());
	}

	/** {@description} */
	@EdaptConstraint(description = "The features have to be all attributes or references")
	public boolean checkSameClass() {
		return isOfSameType(toExtract);
	}

	/** {@description} */
	@EdaptConstraint(description = "The features' multiplicities have to be the same")
	public boolean checkSameMultiplicity() {
		EcorePackage mmm = EcorePackage.eINSTANCE;
		return hasSameValue(toExtract, mmm.getETypedElement_LowerBound())
				&& hasSameValue(toExtract, mmm.getETypedElement_UpperBound());
	}

	/** {@description} */
	@EdaptConstraint(description = "The features' types have to be the same")
	public boolean checkSameType() {
		return hasSameValue(toExtract, EcorePackage.eINSTANCE
				.getETypedElement_EType());
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
	public void execute(Metamodel metamodel, Model model)
			throws MigrationException {
		// metamodel adaptation
		EClass superClass = MetamodelFactory.newEClass(ePackage, superClassName,
				superSuperClasses, abstr);
		for (EClass subClass : subClasses) {
			subClass.getESuperTypes().add(superClass);
			subClass.getESuperTypes().removeAll(superSuperClasses);
		}

		if (!toExtract.isEmpty()) {
			PullFeature operation = new PullFeature();
			operation.features = toExtract;
			operation.targetClass = superClass;
			operation.checkAndExecute(metamodel, model);
		}
	}
}
