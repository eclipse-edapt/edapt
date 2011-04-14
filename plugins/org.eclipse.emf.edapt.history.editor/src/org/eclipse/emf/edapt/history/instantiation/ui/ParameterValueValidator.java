package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.ui.IValueValidator;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.execution.OperationInstanceConverter;

/**
 * Validator for parameters
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ParameterValueValidator implements IValueValidator {

	/** The parameter whose possible values should be restricted. */
	private final ParameterInstance parameterInstance;

	/**
	 * The operation implementation based on which the parameters can be
	 * restricted.
	 */
	private final OperationBase operationBase;

	/** The current metamodel. */
	private final Metamodel metamodel;

	/** Constructor. */
	public ParameterValueValidator(ParameterInstance parameterInstance,
			MetamodelExtent extent) {
		this.parameterInstance = parameterInstance;

		metamodel = OperationInstanceConverter.createEmptyRepository(extent)
				.getMetamodel();
		operationBase = OperationInstanceConverter.convert(
				(OperationInstance) parameterInstance.eContainer(), metamodel);
	}

	/** {@inheritDoc} */
	public boolean isPossibleValue(Object element) {
		if (element instanceof EClass
				&& ((EClass) element).getEPackage() == EcorePackage.eINSTANCE) {
			return false;
		}

		if (parameterInstance.getParameter().getClassifier()
				.isInstance(element)) {
			List<String> messages = operationBase.checkRestriction(
					parameterInstance.getName(), element, metamodel);
			return messages.isEmpty();
		}
		return false;
	}
}
