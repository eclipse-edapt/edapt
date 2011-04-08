package org.eclipse.emf.edapt.migration.operations.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.declaration.incubator.Restriction;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 7FC59C2C028D62F3F1D319B2C338DE84
 */
@Operation(label = "Unfold Superclass", description = "In the metamodel, a superclass is removed from a subclass, while all its features are copied into the subclass. In the model, values are changed accordingly.")
public class ImitateSuperType extends OperationBase {

	/** {@description} */
	@Parameter(description = "The subclass")
	public EClass subClass;

	/** {@description} */
	@Parameter(description = "The superclass")
	public EClass superClass;

	/** {@description} */
	@Restriction(parameter = "superClass")
	public List<String> checkSuperClass(EClass superClass) {
		if (!subClass.getESuperTypes().contains(superClass)) {
			return Collections.singletonList("The super class has "
					+ "to be a super type of the sub class");
		}
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (!metamodel.getInverse(superClass,
				EcorePackage.eINSTANCE.getETypedElement_EType()).isEmpty()) {
			return Collections.singletonList("The super class must "
					+ "not be target of a reference");
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (!subClass.getESuperTypes().isEmpty()) {
			superClass = subClass.getESuperTypes().get(0);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		List<EStructuralFeature> features = superClass.getEStructuralFeatures();

		// metamodel adaptation
		subClass.getESuperTypes().remove(superClass);
		subClass.getESuperTypes().addAll(superClass.getESuperTypes());

		List<EStructuralFeature> clones = new ArrayList<EStructuralFeature>();
		for (EStructuralFeature feature : features) {
			EStructuralFeature clone = MetamodelUtils.copy(feature);
			subClass.getEStructuralFeatures().add(clone);
			clones.add(clone);
		}

		// model migration
		for (Instance instance : model.getAllInstances(subClass)) {
			for (int i = 0; i < features.size(); i++) {
				instance.set(clones.get(i), instance.unset(features.get(i)));
			}
		}
	}
}
