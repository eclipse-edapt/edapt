/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.internal.declaration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.Constraint;
import org.eclipse.emf.edapt.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;

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

		final EdaptOperation operationAnnotation = (EdaptOperation) c
			.getAnnotation(EdaptOperation.class);
		if (operationAnnotation != null) {
			final Operation operation = DeclarationFactory.eINSTANCE
				.createOperation();
			operation.setImplementation(c);
			if (operationAnnotation.identifier().isEmpty()) {
				operation.setName(c.getName());
			} else {
				operation.setName(operationAnnotation.identifier());
			}
			operation.setLabel(operationAnnotation.label());
			operation.setDescription(operationAnnotation.description());
			operation.setBreaking(operationAnnotation.breaking());
			if (c.getAnnotation(Deprecated.class) != null) {
				operation.setDeprecated(true);
			}

			for (final Field field : c.getFields()) {
				addParameter(operation, field);
			}
			for (final Method method : c.getMethods()) {
				addConstraint(operation, method);
			}
			return operation;
		}

		return null;
	}

	/**
	 * Add a parameter to the operation declaration based on a field of a class.
	 */
	private void addParameter(Operation operation, Field field) {
		final EdaptParameter parameterAnnotation = field
			.getAnnotation(EdaptParameter.class);

		if (parameterAnnotation != null) {
			final Parameter parameter = DeclarationFactory.eINSTANCE
				.createParameter();
			operation.getParameters().add(parameter);
			if (parameterAnnotation.main()) {
				parameter.setMain(true);
				operation.getParameters().move(0, parameter);
			}

			parameter.setName(field.getName());
			parameter.setDescription(parameterAnnotation.description());

			final Class type = setManyAndReturnType(parameter, field);

			parameter.setClassifier(getEcoreType(type));
		}
	}

	/** Determine whether the parameter is many-valued and return its type. */
	private Class setManyAndReturnType(Parameter parameter, Field field) {
		Class type = field.getType();
		if (type == List.class) {
			parameter.setMany(true);
			final Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				final ParameterizedType pt = (ParameterizedType) t;
				final Type a = pt.getActualTypeArguments()[0];
				if (a instanceof Class) {
					type = (Class) a;
				}
			}
		}
		return type;
	}

	/** Get the ecore type for a class. */
	private EClassifier getEcoreType(Class type) {
		if (type.getPackage() == EClass.class.getPackage()) {
			return EcorePackage.eINSTANCE.getEClassifier(type.getSimpleName());
		}
		for (final EClassifier classifier : EcorePackage.eINSTANCE.getEClassifiers()) {
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
		final EdaptConstraint constraintAnnotation = method
			.getAnnotation(EdaptConstraint.class);

		if (constraintAnnotation != null) {
			final Constraint constraint = DeclarationFactory.eINSTANCE
				.createConstraint();
			constraint.setName(method.getName());
			constraint.setDescription(constraintAnnotation.description());
			operation.getConstraints().add(constraint);

			final String restricts = constraintAnnotation.restricts();
			if (!restricts.isEmpty()) {
				constraint.setRestricts(operation.getParameter(restricts));
			}
		}
	}

}
