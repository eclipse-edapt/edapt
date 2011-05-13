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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * Helper class to extract the declaration of an operation from its
 * implementation.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 1F4E0BDEACEB10E61508385028C5B1B7
 */
public class OperationExtractor {

	/**
	 * Extract the declaration of an operation from its implementation in a
	 * class.
	 */
	@SuppressWarnings("unchecked")
	public Operation extractOperation(Class c) {

		EdaptOperation operationAnnotation = (EdaptOperation) c
				.getAnnotation(EdaptOperation.class);
		if (operationAnnotation != null) {
			Operation operation = DeclarationFactory.eINSTANCE
					.createOperation();
			operation.setImplementation(c);
			if (operationAnnotation.identifier().isEmpty()) {
				operation.setName(c.getName());
			} else {
				operation.setName(operationAnnotation.identifier());
			}
			operation.setLabel(operationAnnotation.label());
			operation.setDescription(operationAnnotation.description());
			if (c.getAnnotation(Deprecated.class) != null) {
				operation.setDeprecated(true);
			}

			for (Field field : c.getFields()) {
				addParameter(operation, field);
			}
			for (Method method : c.getMethods()) {
				addConstraint(operation, method);
			}
			return operation;
		}

		return null;
	}

	/**
	 * Add a parameter to the operation declaration based on a field of a class.
	 */
	@SuppressWarnings("unchecked")
	private void addParameter(Operation operation, Field field) {
		EdaptParameter parameterAnnotation = field
				.getAnnotation(EdaptParameter.class);

		if (parameterAnnotation != null) {
			Parameter parameter = DeclarationFactory.eINSTANCE
					.createParameter();
			operation.getParameters().add(parameter);
			if (parameterAnnotation.main()) {
				parameter.setMain(true);
				operation.getParameters().move(0, parameter);
			}

			parameter.setName(field.getName());
			parameter.setDescription(parameterAnnotation.description());

			Class type = setManyAndReturnType(parameter, field);

			parameter.setClassifier(getEcoreType(type));
		}
	}

	/** Determine whether the parameter is many-valued and return its type. */
	@SuppressWarnings("unchecked")
	private Class setManyAndReturnType(Parameter parameter, Field field) {
		Class type = field.getType();
		if (type == List.class) {
			parameter.setMany(true);
			Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				Type a = pt.getActualTypeArguments()[0];
				if (a instanceof Class) {
					type = (Class) a;
				}
			}
		}
		return type;
	}

	/** Get the ecore type for a class. */
	@SuppressWarnings("unchecked")
	private EClassifier getEcoreType(Class type) {
		if (type.getPackage() == EClass.class.getPackage()) {
			return EcorePackage.eINSTANCE.getEClassifier(type.getSimpleName());
		}
		for (EClassifier classifier : EcorePackage.eINSTANCE.getEClassifiers()) {
			if (classifier.getInstanceClass() == type) {
				return classifier;
			}
		}
		return null;
	}

	/**
	 * Add a constraint to the operation declaration based on a method of a
	 * class.
	 */
	private void addConstraint(Operation operation, Method method) {
		EdaptConstraint constraintAnnotation = method
				.getAnnotation(EdaptConstraint.class);

		if (constraintAnnotation != null) {
			Constraint constraint = DeclarationFactory.eINSTANCE
					.createConstraint();
			constraint.setName(method.getName());
			constraint.setDescription(constraintAnnotation.description());
			operation.getConstraints().add(constraint);

			String restricts = constraintAnnotation.restricts();
			if (!restricts.isEmpty()) {
				constraint.setRestricts(operation.getParameter(restricts));
			}
		}
	}

}
