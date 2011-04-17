package org.eclipse.emf.edapt.declaration.inheritance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
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
 * @levd.rating YELLOW Hash: 6CFB3401227FD956918314EDBF752F75
 */
@Deprecated
@EdaptOperation(identifier = "extractSuperClass", label = "Extract Super Class", description = "In the metamodel, a number of features of a class are extracted to a new super class. In the model, nothing is changed.")
public class ExtractSuperClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The class from which the features are extracted")
	public EClass subClass;

	/** {@description} */
	@EdaptParameter(optional = true, description = "The features to be extracted")
	public List<EStructuralFeature> toExtract = new ArrayList<EStructuralFeature>();

	/** {@description} */
	@EdaptConstraint(restricts = "toExtract", description = "The features to be extracted must belong to sub class")
	public boolean checkToExtractSameClass(EStructuralFeature toExtract) {
		return subClass.getEStructuralFeatures().contains(toExtract);
	}

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
	@EdaptConstraint(restricts = "superSuperClasses", description = "The super classes to be "
			+ "extracted must be a containsAll of the subclass's super types")
	public boolean checkSuperSuperClasses(EClass superSuperClasses) {
		return subClass.getESuperTypes().contains(superSuperClasses);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		ePackage = subClass.getEPackage();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {

		// metamodel adaptation
		EClass superClass = MetamodelUtils.newEClass(ePackage, superClassName);
		superClass.setAbstract(abstr);
		superClass.getEStructuralFeatures().addAll(toExtract);
		superClass.getESuperTypes().addAll(superSuperClasses);

		subClass.getESuperTypes().add(superClass);
		subClass.getESuperTypes().removeAll(superSuperClasses);

		for (EStructuralFeature feature : toExtract) {
			if (feature instanceof EReference) {
				EReference reference = (EReference) feature;
				if (reference.getEOpposite() != null) {
					reference.getEOpposite().setEType(superClass);
				}
			}
		}
	}
}
