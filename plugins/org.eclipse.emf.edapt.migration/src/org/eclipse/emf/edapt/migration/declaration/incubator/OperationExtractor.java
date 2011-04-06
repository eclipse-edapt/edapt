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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;

/**
 * Helper class to extract the declaration of an operation from its
 * implementation.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationExtractor {

	/**
	 * Extract the declaration of an operation from its implementation in a
	 * class.
	 */
	@SuppressWarnings("unchecked")
	public Operation extractOperation(Class c) {

		org.eclipse.emf.edapt.migration.declaration.incubator.Operation o = (org.eclipse.emf.edapt.migration.declaration.incubator.Operation) c
				.getAnnotation(org.eclipse.emf.edapt.migration.declaration.incubator.Operation.class);
		if (o != null) {
			Operation operation = DeclarationFactory.eINSTANCE
					.createOperation();
			operation.setName(c.getName());
			operation.setLabel(o.label());
			operation.setDescription(o.description());

			for (Field field : c.getFields()) {
				addParameter(operation, field);
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
		org.eclipse.emf.edapt.migration.declaration.incubator.Parameter p = field
				.getAnnotation(org.eclipse.emf.edapt.migration.declaration.incubator.Parameter.class);

		if (p != null) {
			Parameter parameter = DeclarationFactory.eINSTANCE
					.createParameter();
			if (operation.getMainParameter() == null) {
				parameter.setMain(true);
			}
			operation.getParameters().add(parameter);

			parameter.setName(field.getName());
			parameter.setRequired(true);
			parameter.setDescription(p.description());

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

}
