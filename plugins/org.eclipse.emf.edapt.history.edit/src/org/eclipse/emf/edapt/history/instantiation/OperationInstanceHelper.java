/*******************************************************************************
 * Copyright (c) 2006, 2009 Markus Herrmannsdoerfer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Herrmannsdoerfer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.IExtentProvider;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.TypeUtils;
import org.eclipse.emf.edapt.declaration.Constraint;
import org.eclipse.emf.edapt.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;
import org.eclipse.emf.edapt.migration.Repository;
import org.eclipse.emf.edapt.migration.execution.OperationInstanceConverter;

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
		Collection<Operation> operations = org.eclipse.emf.edapt.declaration.OperationRegistry
				.getInstance().getOperations();
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
							if (isApplicable(operationInstance)) {
								result.add(operationInstance);
							}
						}
					} else {
						OperationInstance operationInstance = createOperationInstance(
								operation, selectedElements);
						if (isApplicable(operationInstance)) {
							result.add(operationInstance);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Check whether an operation is applicable based on the main parameter
	 * restrictions.
	 */
	private boolean isApplicable(OperationInstance operationInstance) {
		Repository repository = OperationInstanceConverter
				.createEmptyRepository(getExtent());
		OperationImplementation operationBase = OperationInstanceConverter.convert(
				operationInstance, repository.getMetamodel());

		List<String> messages = operationBase.checkRestriction(
				operationInstance.getOperation().getMainParameter().getName(),
				repository.getMetamodel());

		OperationInstanceConverter.convert(operationBase, operationInstance);

		return messages.isEmpty();
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
		for (Parameter parameter : operation.getParameters()) {
			ParameterInstance parameterInstance = factory
					.createParameterInstance();
			parameterInstance.setName(parameter.getName());
			operationInstance.getParameters().add(parameterInstance);

			if (parameter.isMain()) {
				if (parameter.isMany()) {
					parameterInstance.setValue(selectedElements);
				} else {
					parameterInstance.setValue(selectedElements.get(0));
				}
			}
		}

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
		Repository repository = OperationInstanceConverter
				.createEmptyRepository(getExtent());
		OperationImplementation operationBase = OperationInstanceConverter.convert(
				operationInstance, repository.getMetamodel());
		List<String> messages = operationBase.checkPreconditions(repository
				.getMetamodel());

		List<Constraint> violatedConstraints = new ArrayList<Constraint>();
		for (String message : messages) {
			Constraint constraint = createConstraint(message);
			violatedConstraints.add(constraint);
		}
		OperationInstanceConverter.convert(operationBase, operationInstance);

		return violatedConstraints;
	}

	/** Create a constraint with a certain message. */
	private Constraint createConstraint(String message) {
		Constraint constraint = DeclarationFactory.eINSTANCE.createConstraint();
		constraint.setLabel(message);
		return constraint;
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
