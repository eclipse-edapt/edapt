package org.eclipse.emf.edapt.declaration.replacement;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelFactory;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: C5E03012DB3475DF44B1E9D967EC97FE
 */
@EdaptOperation(label = "Class to Association", description = "")
public class ClassToAssociation extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "Class to be replaced by an association")
	public EClass eClass;

	/** {@description} */
	@EdaptConstraint(restricts = "eClass", description = "The class must not have any sub types.")
	public boolean checkClassSubClasses(EClass eClass, Metamodel metamodel) {
		return metamodel.getESubTypes(eClass).isEmpty();
	}

	/** {@description} */
	@EdaptParameter(description = "Reference to the source class of the association")
	public EReference sourceReference;

	/** {@description} */
	@EdaptConstraint(restricts = "sourceReference", description = "The source reference must be single-valued and not containment.")
	public boolean checkSourceReference(EReference sourceReference) {
		return !sourceReference.isMany() && !sourceReference.isContainment();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "sourceReference", description = "The source reference must be defined in the class")
	public boolean checkSourceReferenceInClass(EReference sourceReference) {
		return eClass.getEStructuralFeatures().contains(sourceReference);
	}

	/** {@description} */
	@EdaptParameter(description = "Reference to the target class of the association")
	public EReference targetReference;

	/** {@description} */
	@EdaptConstraint(restricts = "targetReference", description = "The target reference must be single-valued and not containment.")
	public boolean checkTargetReference(EReference targetReference) {
		return !targetReference.isMany() && !targetReference.isContainment();
	}

	/** {@description} */
	@EdaptConstraint(restricts = "targetReference", description = "The source reference must be defined in the class")
	public boolean checkTargetReferenceInClass(EReference targetReference) {
		return eClass.getEStructuralFeatures().contains(targetReference);
	}

	/** {@description} */
	@EdaptParameter(description = "Name of the new association")
	public String associationName;

	/** {@description} */
	@EdaptConstraint(description = "The class may only be targeted by one containment "
			+ "reference and the opposite of source and target reference.")
	public boolean checkReferences(Metamodel metamodel) {
		List<EReference> references = metamodel.getInverse(eClass,
				EcorePackage.eINSTANCE.getEReference_EReferenceType());
		for (EReference reference : references) {
			if (reference.isContainment()
					|| sourceReference.getEOpposite() == reference
					|| targetReference.getEOpposite() == reference) {
				continue;
			}
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected void execute(Metamodel metamodel, Model model) {

		// metamodel adaptation
		EClass sourceClass = sourceReference.getEReferenceType();
		EClass targetClass = targetReference.getEReferenceType();

		EReference association = MetamodelFactory.newEReference(sourceClass,
				associationName, targetClass, 0, -1);

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			Instance source = instance.getLink(sourceReference);
			Instance target = instance.getLink(targetReference);
			source.add(association, target);
			model.delete(instance);
		}

		// metamodel adaptation
		if (sourceReference.getEOpposite() != null) {
			metamodel.delete(sourceReference.getEOpposite());
		}
		metamodel.delete(sourceReference);
		if (targetReference.getEOpposite() != null) {
			metamodel.delete(targetReference.getEOpposite());
		}
		metamodel.delete(targetReference);
		List<EReference> references = metamodel.getInverse(eClass,
				EcorePackage.eINSTANCE.getEReference_EReferenceType());
		for (EReference reference : references) {
			metamodel.delete(reference);
		}
		metamodel.delete(eClass);
	}
}
