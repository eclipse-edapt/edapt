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
package org.eclipse.emf.edapt.cope.history.instantiation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.cope.common.IExtentProvider;
import org.eclipse.emf.edapt.cope.common.MetamodelExtent;
import org.eclipse.emf.edapt.cope.common.TypeUtils;
import org.eclipse.emf.edapt.cope.declaration.Constraint;
import org.eclipse.emf.edapt.cope.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.cope.declaration.Operation;
import org.eclipse.emf.edapt.cope.declaration.OperationRegistry;
import org.eclipse.emf.edapt.cope.declaration.Parameter;
import org.eclipse.emf.edapt.cope.declaration.Variable;
import org.eclipse.emf.edapt.cope.history.HistoryFactory;
import org.eclipse.emf.edapt.cope.history.OperationInstance;
import org.eclipse.emf.edapt.cope.history.ParameterInstance;
import org.eclipse.emf.edapt.cope.history.VariableInstance;
import org.eclipse.emf.edapt.cope.migration.execution.GroovyEvaluator;


/**
 * Helper to deal with operation instances
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationInstanceHelper {

	/**
	 * Extent provider
	 */
	private final IExtentProvider provider;

	/**
	 * Constructor
	 */
	public OperationInstanceHelper(IExtentProvider provider) {
		this.provider = provider;
	}

	/**
	 * Create operation instances for operations which are applicable on a list
	 * of selected elements
	 * 
	 * @param selectedElements
	 *            Selected elements
	 * @return List of instances of applicable operations
	 */
	public List<OperationInstance> getPossibleOperations(
			List<EObject> selectedElements) {

		List<OperationInstance> result = new ArrayList<OperationInstance>();

		// iterate over all registered operations
		Collection<Operation> operations = OperationRegistry.getInstance()
				.getOperations();
		for (Operation operation : operations) {

			if (operation.refines()) {
				continue;
			}

			Parameter mainParameter = operation.getMainParameter();

			// check consistency of selected elements with type of main
			// parameter
			if (mainParameter != null) {
				EClass type = (EClass) mainParameter.getClassifier();
				if (TypeUtils.ancestor(TypeUtils
						.commonSuperClass(selectedElements), type)) {

					if (selectedElements.size() > 1) {
						if (mainParameter.isMany()) {
							OperationInstance operationInstance = createOperationInstance(
									operation, selectedElements);
							result.add(operationInstance);
						}
					} else {
						OperationInstance operationInstance = createOperationInstance(
								operation, selectedElements);
						result.add(operationInstance);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Evaluate the helper variables of an operation instance
	 * 
	 * @param operationInstance
	 *            Operation instance
	 */
	public void evaluateVariables(OperationInstance operationInstance) {
		operationInstance.getVariables().clear();
		Operation operation = operationInstance.getOperation();

		for (Variable variable : operation.getVariables()) {
			// create variable instance
			VariableInstance variableInstance = HistoryFactory.eINSTANCE
					.createVariableInstance();
			variableInstance.setName(variable.getName());
			operationInstance.getVariables().add(variableInstance);

			// set value of type instance
			try {
				Object value = evaluateScript(operationInstance, variable
						.getInitExpression());
				variableInstance.setValue(value);
			} catch (RuntimeException e) {
				// ignore
			}
		}
	}

	/**
	 * Create an instance of an operation based on the list of selected elements
	 * 
	 * @param operation
	 *            Operation
	 * @param selectedElements
	 *            List of selected elements
	 * @return Operation instance
	 */
	public OperationInstance createOperationInstance(Operation operation,
			List<EObject> selectedElements) {

		HistoryFactory factory = HistoryFactory.eINSTANCE;

		OperationInstance operationInstance = factory.createOperationInstance();
		operationInstance.setName(operation.getName());

		// create instance of main parameter and initialize with selected
		// elements
		{
			Parameter parameter = operation.getMainParameter();

			ParameterInstance parameterInstance = factory
					.createParameterInstance();
			parameterInstance.setName(parameter.getName());
			operationInstance.getParameters().add(parameterInstance);

			if (parameter.isMany()) {
				parameterInstance.setValue(selectedElements);
			} else {
				parameterInstance.setValue(selectedElements.get(0));
			}
		}

		// helper variables have to be evaluated in order to be accessible for
		// init expressions
		evaluateVariables(operationInstance);

		// create instance of the other parameters and initialize
		for (Parameter parameter : operation.getParameters()) {

			if (!parameter.isMain()) {

				ParameterInstance parameterInstance = factory
						.createParameterInstance();
				parameterInstance.setName(parameter.getName());
				operationInstance.getParameters().add(parameterInstance);

				if (parameter.getInitExpression() != null) {
					try {
						Object value = evaluateScript(operationInstance,
								parameter.getInitExpression());
						parameterInstance.setValue(value);
					} catch (RuntimeException e) {
						// ignore
					}
				}
			}
		}

		// re-evaluate helper variables, because they may depend on parameters
		// which have changed
		evaluateVariables(operationInstance);

		return operationInstance;
	}

	/**
	 * Evaluate all constraints of an operation instance and return those which
	 * are not fulfilled
	 * 
	 * @param operationInstance
	 *            Operation instance
	 * @return List of violated constraints
	 */
	public List<Constraint> getViolatedConstraints(
			OperationInstance operationInstance) {
		Operation operation = operationInstance.getOperation();
		List<Constraint> violatedConstraints = new ArrayList<Constraint>();

		// check all constraints
		for (Constraint constraint : operation.getConstraints()) {
			boolean result;
			try {
				result = (Boolean) evaluateScript(operationInstance, constraint
						.getBooleanExpression());
			} catch (RuntimeException e) {
				// strict evaluation: an error means violation
				result = false;
			}

			if (!result) {
				violatedConstraints.add(constraint);
			}
		}

		for (Parameter parameter : operation.getParameters()) {
			if (parameter.isRequired()) {
				ParameterInstance parameterInstance = operationInstance
						.getParameter(parameter.getName());
				Object value = parameterInstance.getValue();
				if (value == null) {
					Constraint constraint = DeclarationFactory.eINSTANCE
							.createConstraint();
					constraint.setLabel("Parameter '" + parameter.getName()
							+ "' must be set");
					constraint.setBooleanExpression(constraint.getLabel());
					violatedConstraints.add(constraint);
				}
			}
		}

		return violatedConstraints;
	}

	/**
	 * Evaluate a script for an operation instance
	 * 
	 * @param operationInstance
	 * @param script
	 * @return Result of evaluation
	 */
	public Object evaluateScript(OperationInstance operationInstance,
			String script) {

		Map<String, Object> variables = new HashMap<String, Object>();
		for (ParameterInstance parameterInstance : operationInstance
				.getParameters()) {
			Parameter parameter = parameterInstance.getParameter();
			variables.put(parameter.getName(), parameterInstance.getValue());
		}
		for (VariableInstance variableInstance : operationInstance
				.getVariables()) {
			Variable variable = variableInstance.getVariable();
			variables.put(variable.getName(), variableInstance.getValue());
		}
		script = "import org.eclipse.emf.ecore.*\n" + script;
		return GroovyEvaluator.getInstance().evaluateCached(script, variables,
				getExtent().getRootPackages());
	}

	/**
	 * Get metamodel extent
	 * 
	 * @return Metamodel extent
	 */
	public MetamodelExtent getExtent() {
		return provider.getExtent();
	}
}
