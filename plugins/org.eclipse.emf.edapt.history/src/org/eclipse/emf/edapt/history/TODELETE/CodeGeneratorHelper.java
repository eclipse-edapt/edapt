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
package org.eclipse.emf.edapt.history.TODELETE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;


/**
 * Helper methods to assemble the code for an operation instance
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CodeGeneratorHelper {

	/**
	 * Metamodel extent
	 */
	private final MetamodelExtent extent;

	/**
	 * Constructor
	 */
	public CodeGeneratorHelper(MetamodelExtent extent) {
		this.extent = extent;
	}

	/**
	 * Assemble the code for the instantiation of an operation
	 * 
	 * @param operationInstance
	 * @return Code
	 */
	public String assembleCode(OperationInstance operationInstance) {
		Operation operation = operationInstance.getOperation();
		return assembleCode(operation.getName(), operationInstance);
	}

	/**
	 * Assemble the code for the instantiation of an operation
	 * 
	 * @param operationInstance
	 * @return Code
	 */
	public String assembleCode(String name, OperationInstance operationInstance) {
		StringBuffer call = new StringBuffer();

		call.append(name + "(");

		for (Iterator<ParameterInstance> i = operationInstance.getParameters()
				.iterator(); i.hasNext();) {
			ParameterInstance parameterInstance = i.next();
			call.append(assembleParameter(parameterInstance));
			if (i.hasNext()) {
				call.append(", ");
			}
		}

		call.append(")\n");
		return call.toString();
	}

	/**
	 * Assemble the code for an instance of a parameter
	 * 
	 * @param parameterInstance
	 * @return Code
	 */
	private String assembleParameter(ParameterInstance parameterInstance) {
		StringBuffer result = new StringBuffer();

		Parameter parameter = parameterInstance.getParameter();
		Object value = parameterInstance.getValue();
		if (parameter.isMany()) {
			result.append("[");
			for (Iterator<?> i = ((Collection<?>) value).iterator(); i
					.hasNext();) {
				Object v = i.next();
				result.append(getRepresentation(v));
				if (i.hasNext()) {
					result.append(", ");
				}
			}
			result.append("]");
		} else {
			if (value == null) {
				result.append("null");
			} else {
				result.append(getRepresentation(value));
			}
		}

		return result.toString();
	}

	/**
	 * Get the textual representation of an object
	 * 
	 * @param v
	 * @return Textual representation
	 */
	public String getRepresentation(Object v) {
		if (v == null) {
			return "null";
		} else if (v instanceof EObject) {
			return getAccessor((EObject) v);
		} else {
			StringBuffer result = new StringBuffer();
			if (v instanceof String) {
				result.append("\"");
			}
			result.append(v.toString());
			if (v instanceof String) {
				result.append("\"");
			}
			return result.toString();
		}
	}

	/**
	 * Get the textual representation of a model element
	 * 
	 * @param element
	 * @return Textual representation
	 */
	public String getAccessor(EObject element) {
		if (element instanceof ENamedElement) {
			return getNamedElementAccessor((ENamedElement) element);
		}
		EReference containmentReference = element.eContainmentFeature();
		EObject container = element.eContainer();
		if (containmentReference.isMany()) {
			return getAccessor(container)
					+ "."
					+ getFeatureName(containmentReference)
					+ "["
					+ ((List<?>) container.eGet(containmentReference))
							.indexOf(element) + "]";
		}
		return getAccessor(container) + "."
				+ getFeatureName(containmentReference);
	}

	/**
	 * Get the textual representation of a named element
	 * 
	 * @param namedElement
	 * @return Textual representation
	 */
	private String getNamedElementAccessor(ENamedElement namedElement) {
		String result = namedElement.getName();
		if (namedElement.eContainer() == EcorePackage.eINSTANCE) {
			return "emf." + result;
		}
		EObject container = namedElement.eContainer();
		if (container == null) {
			if (namedElement instanceof EPackage) {
				EPackage rootPackage = (EPackage) namedElement;
				result = getRootPackageAccessor(rootPackage);
			}
		} else {
			if (namedElement instanceof EOperation) {
				EOperation operation = (EOperation) namedElement;
				result = getOperationAccessor(operation);
			}
			if (container.eClass().getEStructuralFeature(result) != null) {
				result = "_" + result;
			}
			if (container instanceof ENamedElement) {
				ENamedElement namedContainer = (ENamedElement) container;
				result = getNamedElementAccessor(namedContainer) + "."
						+ escape(result);
			}
		}
		return result;
	}

	/**
	 * Get the textual representation of an operation
	 */
	private String getOperationAccessor(EOperation operation) {
		String name = operation.getName();
		EClass eClass = operation.getEContainingClass();
		if (eClass.getEStructuralFeature(name) != null
				|| MetamodelUtils.getOperations(eClass, name).size() > 1) {
			name = "getOperation(\"" + name + "\"";
			for (EParameter parameter : operation.getEParameters()) {
				if (parameter.isMany()) {
					name += ", emf.EEList";
				} else {
					name += ", " + getAccessor(parameter.getEType());
				}
			}
			name += ")";
		}
		return name;
	}

	/**
	 * Get the textual representation of a root package
	 */
	private String getRootPackageAccessor(EPackage ePackage) {
		String name = ePackage.getName();
		List<EPackage> ePackages = getRootPackages(name);
		if (ePackages.size() > 1) {
			return "rp(\"" + ePackage.getNsURI() + "\")";
		}
		return name;
	}

	/**
	 * Get all root packages with a certain name
	 */
	private List<EPackage> getRootPackages(String name) {
		List<EPackage> ePackages = new ArrayList<EPackage>();
		for (EPackage rootPackage : extent.getRootPackages()) {
			if (name.equals(rootPackage.getName())) {
				ePackages.add(rootPackage);
			}
		}
		return ePackages;
	}

	/**
	 * Assemble the name of a feature
	 * 
	 * @param feature
	 * @return Name
	 */
	private static String getFeatureName(EStructuralFeature feature) {
		String name = feature.getName();
		name = escape(name);
		return name;
	}

	/**
	 * Escape certain names
	 * 
	 * @param name
	 * @return Escaped name
	 */
	private static String escape(String name) {
		if ("interface".equals(name) || "abstract".equals(name)
				|| "volatile".equals(name) || "transient".equals(name)
				|| "package".equals(name)) {
			name = "'" + name + "'";
		}
		if ("properties".equals(name) || "instances".equals(name)
				|| "allInstances".equals(name)) {
			name = "_" + name;
		}
		return name;
	}
}
