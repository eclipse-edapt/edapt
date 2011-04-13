package org.eclipse.emf.edapt.history.instantiation.ui;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.ui.IValueValidator;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
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

	private final ParameterInstance parameterInstance;

	private final MetamodelExtent extent;

	private final Method method;

	private final OperationBase operationBase;

	/**
	 * Constructor
	 */
	public ParameterValueValidator(ParameterInstance parameterInstance,
			MetamodelExtent extent) {
		this.parameterInstance = parameterInstance;
		this.extent = extent;

		operationBase = OperationInstanceConverter.convert(
				(OperationInstance) parameterInstance.eContainer(),
				OperationInstanceConverter.createEmptyRepository(extent)
						.getMetamodel());
		method = getRestriction();
	}

	private Method getRestriction() {
		for (Method method : operationBase.getClass().getMethods()) {
			EdaptRestriction restriction = method.getAnnotation(EdaptRestriction.class);
			if (restriction != null) {
				if (parameterInstance.getName().equals(restriction.parameter())) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPossibleValue(Object element) {
		if (element instanceof EClass
				&& ((EClass) element).getEPackage() == EcorePackage.eINSTANCE) {
			return false;
		}

		try {
			if (method != null) {
				return ((List) method.invoke(operationBase, element)).isEmpty();
			}
		} catch (Exception e) {

		}
		return parameterInstance.getParameter().getClassifier().isInstance(
				element);
	}

	private Class getClass(EClassifier eClassifier) {
		if (eClassifier.getInstanceClass() != null) {
			return eClassifier.getInstanceClass();
		}
		try {
			return HistoryEditorPlugin.getPlugin().getBundle().loadClass(
					"org.eclipse.emf.ecore." + eClassifier.getName());
		} catch (Exception e) {
			return null;
		}
	}
}
