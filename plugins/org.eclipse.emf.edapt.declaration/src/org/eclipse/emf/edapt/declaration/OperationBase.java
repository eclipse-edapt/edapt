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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
 * @levd.rating RED Rev:
 */
public abstract class OperationBase {

	/** Execute the operation. */
	public abstract void execute(Metamodel metamodel, Model model)
			throws MigrationException;

	/** Check the preconditions of the operation. */
	public final List<String> checkPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		result.addAll(checkRequiredParameters());
		result.addAll(checkRestrictions(metamodel));
		result.addAll(checkCustomPreconditions(metamodel));
		return result;
	}

	/** Check whether a required parameter is set. */
	@SuppressWarnings("unchecked")
	private Collection<? extends String> checkRequiredParameters() {
		List<String> result = new ArrayList<String>();
		for (Field field : getClass().getFields()) {
			org.eclipse.emf.edapt.declaration.EdaptParameter p = field
					.getAnnotation(org.eclipse.emf.edapt.declaration.EdaptParameter.class);

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

	/** Check the parameter restrictions of the operation. */
	private List<String> checkRestrictions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		for (Method method : getClass().getMethods()) {
			try {
				EdaptRestriction restriction = method
						.getAnnotation(EdaptRestriction.class);
				if (restriction != null) {
					String parameterName = restriction.parameter();
					checkRestriction(metamodel, method, parameterName, result);
				}
			} catch (Exception e) {
				// if we ignore all exceptions, then we are on the safe side.
			}
		}
		return result;
	}

	/** Invoke a restriction method an add the result to a list. */
	@SuppressWarnings("unchecked")
	private void invokeRestrictionAndAddResult(Method method, Object value,
			Metamodel metamodel, List<String> result)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Class<?>[] parameterTypes = method.getParameterTypes();
		List<String> messages = new ArrayList<String>();
		if (parameterTypes.length > 1) {
			messages = (List<String>) method.invoke(this, value, metamodel);
		} else {
			messages = (List<String>) method.invoke(this, value);
		}
		for (String message : messages) {
			if (!result.contains(message)) {
				result.add(message);
			}
		}
	}

	public List<String> checkRestriction(Metamodel metamodel,
			String parameterName) {
		List<String> result = new ArrayList<String>();
		for (Method method : getClass().getMethods()) {
			try {
				EdaptRestriction restriction = method
						.getAnnotation(EdaptRestriction.class);
				if (restriction != null) {
					if (parameterName.equals(restriction.parameter())) {
						checkRestriction(metamodel, method, parameterName,
								result);
					}
				}
			} catch (Exception e) {
				// if we ignore all exceptions, then we are on the safe side.
			}
		}
		return result;
	}

	private void checkRestriction(Metamodel metamodel, Method method,
			String parameterName, List<String> result)
			throws NoSuchFieldException, IllegalAccessException,
			InvocationTargetException {
		Field field = getClass().getField(parameterName);
		Object value = field.get(this);
		if (value != null) {
			if (field.getType() == List.class) {
				for (Object v : (List) value) {
					invokeRestrictionAndAddResult(method, v, metamodel, result);
				}
			} else {
				invokeRestrictionAndAddResult(method, value, metamodel, result);
			}
		}
	}

	/** Check custom preconditions of the operation. */
	protected List<String> checkCustomPreconditions(
			@SuppressWarnings("unused") Metamodel metamodel) {
		return Collections.emptyList();
	}

	/** Initialize the parameters of the operation. */
	public void initialize(@SuppressWarnings("unused") Metamodel metamodel) {
		// to be implemented by subclasses
	}

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

	protected boolean hasSameValue(List<? extends EObject> elements,
			EStructuralFeature feature) {
		if (elements.isEmpty()) {
			return true;
		}
		Object referenceValue = elements.get(0).eGet(feature);
		return hasValue(elements, feature, referenceValue);
	}

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

	protected boolean isOfType(List<? extends EObject> elements, EClass eClass) {
		for (EObject element : elements) {
			if (element.eClass() != eClass) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isOfSameType(List<? extends EObject> elements) {
		if(elements.isEmpty()) {
			return true;
		}
		return isOfType(elements, elements.get(0).eClass());
	}

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
