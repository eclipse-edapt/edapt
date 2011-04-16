/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BMW Car IT - Initial API and implementation
 *     Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.declaration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.MigrationException;

/**
 * Base class for implementations of operations.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: A2C89DB1F6BCDE1CE7E14A2021A028FF
 */
public abstract class OperationImplementation {

	/** Execute the operation. */
	public abstract void execute(Metamodel metamodel, Model model)
			throws MigrationException;

	/** Check the preconditions of the operation. */
	public final List<String> checkPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		result.addAll(checkRequiredParameters());
		result.addAll(checkConstraints(metamodel));
		return result;
	}

	/** Check whether all required parameters are set. */
	@SuppressWarnings("unchecked")
	private Collection<? extends String> checkRequiredParameters() {
		List<String> result = new ArrayList<String>();
		for (Field field : getClass().getFields()) {
			EdaptParameter p = field.getAnnotation(EdaptParameter.class);

			if (p != null && !p.optional()) {
				try {
					Object value = field.get(this);
					if (field.getType() == List.class) {
						if (value == null || ((List) value).isEmpty()) {
							result.add("Parameter '" + field.getName()
									+ "' must be set");
						}
					} else {
						if (value == null) {
							result.add("Parameter '" + field.getName()
									+ "' must be set");
						}
					}
				} catch (Exception e) {
					// if we ignore all exceptions, then we are on the safe
					// side.
				}
			}
		}
		return result;
	}

	/** Check whether all defined constraints are fulfilled. */
	private List<String> checkConstraints(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		for (Method method : getClass().getMethods()) {
			EdaptConstraint constraint = method
					.getAnnotation(EdaptConstraint.class);
			if (constraint != null) {
				if (constraint.restricts().length() > 0) {
					checkRestriction(method, constraint, metamodel, result);
				} else {
					invokeMethodAndAddResult(method, constraint, metamodel,
							result);
				}
			}
		}
		return result;
	}

	/** Check whether a restriction on a parameter value is fulfilled. */
	@SuppressWarnings("unchecked")
	private void checkRestriction(Method method, EdaptConstraint constraint,
			Metamodel metamodel, List<String> result) {
		try {
			Field field = getClass().getField(constraint.restricts());
			Object value = field.get(this);
			if (field.getType() == List.class) {
				for (Object v : (List) value) {
					if (invokeMethodAndAddResult(method, constraint, metamodel,
							result, v)) {
						break;
					}
				}
			} else {
				invokeMethodAndAddResult(method, constraint, metamodel, result,
						value);
			}
		} catch (Exception e) {
			// if we ignore all exceptions, then we are on the safe side.
		}
	}

	/**
	 * Invoke a constraint method with a number of parameters and add the
	 * message to the result if the constraint is not fulfilled.
	 */
	private boolean invokeMethodAndAddResult(Method method,
			EdaptConstraint constraint, Metamodel metamodel,
			List<String> result, Object... parameters) {
		try {
			boolean fulfilled = true;
			if (method.getParameterTypes().length > parameters.length) {
				parameters = Arrays.copyOf(parameters, parameters.length + 1);
				parameters[parameters.length - 1] = metamodel;
				fulfilled = (Boolean) method.invoke(this, parameters);
			} else {
				fulfilled = (Boolean) method.invoke(this, parameters);
			}

			if (!fulfilled) {
				result.add(constraint.description());
				return true;
			}
		} catch (Exception e) {
			// if we ignore all exceptions, then we are on the safe side.
		}
		return false;
	}

	/** Check whether all restrictions on a certain parameter are fulfilled. */
	public List<String> checkRestriction(String parameterName,
			Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		for (Method method : getRestrictions(parameterName)) {
			try {
				EdaptConstraint constraint = method
						.getAnnotation(EdaptConstraint.class);
				checkRestriction(method, constraint, metamodel, result);
			} catch (Exception e) {
				// if we ignore all exceptions, then we are on the safe side.
			}
		}
		return result;
	}

	/** Get the restriction methods for a certain parameter. */
	private List<Method> getRestrictions(String parameterName) {
		List<Method> restrictions = new ArrayList<Method>();
		for (Method method : getClass().getMethods()) {
			EdaptConstraint constraint = method
					.getAnnotation(EdaptConstraint.class);
			if (constraint != null
					&& parameterName.equals(constraint.restricts())) {
				restrictions.add(method);
			}
		}
		return restrictions;
	}

	/**
	 * Check whether all restrictions on a certain parameter value are fulfilled
	 * for a certain value.
	 */
	public List<String> checkRestriction(String parameterName, Object value,
			Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		for (Method method : getRestrictions(parameterName)) {
			EdaptConstraint constraint = method
					.getAnnotation(EdaptConstraint.class);
			invokeMethodAndAddResult(method, constraint, metamodel, result,
					value);
		}
		return result;
	}

	/** Initialize the parameters of the operation. */
	public void initialize(@SuppressWarnings("unused") Metamodel metamodel) {
		// to be implemented by subclasses
	}

	/**
	 * Remove the value of a feature and in case of a containment reference
	 * delete the value from the model.
	 */
	@SuppressWarnings("unchecked")
	protected void deleteFeatureValue(Instance instance,
			EStructuralFeature feature) {
		Object value = instance.unset(feature);
		if (feature instanceof EReference) {
			EReference reference = (EReference) feature;
			if (reference.isContainment()) {
				if (reference.isMany()) {
					for (EObject v : (List<EObject>) value) {
						EcoreUtil.delete(v);
					}
				} else if (value != null) {
					EcoreUtil.delete((EObject) value);
				}
			}
		}
	}

	/**
	 * Check whether a list of elements has the same value for a certain
	 * feature.
	 */
	protected boolean hasSameValue(List<? extends EObject> elements,
			EStructuralFeature feature) {
		if (elements.isEmpty()) {
			return true;
		}
		Object referenceValue = elements.get(0).eGet(feature);
		return hasValue(elements, feature, referenceValue);
	}

	/**
	 * Check whether a list of elements has a certain value for a certain
	 * feature.
	 */
	protected boolean hasValue(List<? extends EObject> elements,
			EStructuralFeature feature, Object referenceValue) {
		for (EObject element : elements) {
			Object value = element.eGet(feature);
			if (!isSame(referenceValue, value)) {
				return false;
			}
		}
		return true;
	}

	/** Check whether two values are the same. */
	private boolean isSame(Object referenceValue, Object value) {
		if (referenceValue != value) {
			if (referenceValue == null || value == null) {
				return false;
			} else if (!referenceValue.equals(value)) {
				return false;
			}
		}
		return true;
	}

	/** Check whether a list of elements is of a certain type. */
	protected boolean isOfType(List<? extends EObject> elements, EClass eClass) {
		for (EObject element : elements) {
			if (element.eClass() != eClass) {
				return false;
			}
		}
		return true;
	}

	/** Check whether a list of elements has the same type. */
	protected boolean isOfSameType(List<? extends EObject> elements) {
		if (elements.isEmpty()) {
			return true;
		}
		return isOfType(elements, elements.get(0).eClass());
	}

	/**
	 * Check whether for two lists of elements the value of a feature of an
	 * element in one list is the same than the value of a feature of the
	 * element at the same index position in the other list.
	 */
	protected boolean hasSameValue(List<? extends EObject> first,
			List<? extends EObject> second, EStructuralFeature feature) {
		if (first.size() != second.size()) {
			return false;
		}
		for (int i = 0; i < first.size(); i++) {
			if (!isSame(first.get(i).eGet(feature), second.get(i).eGet(feature))) {
				return false;
			}
		}
		return true;
	}
}
