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
package org.eclipse.emf.edapt.migration.declaration.incubator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

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
	public abstract void execute(Metamodel metamodel, Model model);

	/** Check the preconditions of the operation. */
	public final List<String> checkPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		result.addAll(checkRestrictions(metamodel));
		result.addAll(checkCustomPreconditions(metamodel));
		return result;
	}

	/** Check the parameter restrictions of the operation. */
	@SuppressWarnings("unchecked")
	private List<String> checkRestrictions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		for (Method method : getClass().getMethods()) {
			try {
				Restriction restriction = method
						.getAnnotation(Restriction.class);
				if (restriction != null) {
					String parameterName = restriction.parameter();
					Field field = getClass().getField(parameterName);
					Object value = field.get(this);
					if (value != null) {
						if (field.getType() == List.class) {
							for (Object v : (List) value) {
								invokeRestrictionAndAddResult(method, v,
										metamodel, result);
							}
						} else {
							invokeRestrictionAndAddResult(method, value,
									metamodel, result);
						}
					}
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

	/** Check custom preconditions of the operation. */
	protected List<String> checkCustomPreconditions(
			@SuppressWarnings("unused") Metamodel metamodel) {
		return Collections.emptyList();
	}

	/** Initialize the parameters of the operation. */
	public void initialize(@SuppressWarnings("unused") Metamodel metamodel) {
		// to be implemented by subclasses
	}
}
