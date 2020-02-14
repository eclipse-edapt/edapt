/*******************************************************************************
 * Copyright (c) 2006, 2009 Markus Herrmannsdoerfer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Markus Herrmannsdoerfer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.declaration.Constraint;
import org.eclipse.emf.edapt.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.internal.common.IExtentProvider;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.TypeUtils;
import org.eclipse.emf.edapt.internal.declaration.OperationRegistry;
import org.eclipse.emf.edapt.internal.migration.execution.internal.OperationInstanceConverter;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.ParameterInstance;
import org.eclipse.emf.edapt.spi.migration.Repository;

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

		final List<OperationInstance> result = new ArrayList<OperationInstance>();

		// iterate over all registered operations
		final Collection<Operation> operations = OperationRegistry.getInstance()
			.getOperations();
		for (final Operation operation : operations) {

			if (operation.refines()) {
				continue;
			}

			final Parameter mainParameter = operation.getMainParameter();

			// check consistency of selected elements with type of main
			// parameter
			if (mainParameter != null) {
				final EClass type = (EClass) mainParameter.getClassifier();
				if (TypeUtils.ancestor(TypeUtils
					.commonSuperClass(selectedElements), type)) {

					if (selectedElements.size() > 1) {
						if (mainParameter.isMany()) {
							final OperationInstance operationInstance = createOperationInstance(
								operation, selectedElements);
							if (isApplicable(operationInstance)) {
								result.add(operationInstance);
							}
						}
					} else {
						final OperationInstance operationInstance = createOperationInstance(
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
		final Repository repository = OperationInstanceConverter
			.createEmptyRepository(getExtent());
		final OperationImplementation operationBase = OperationInstanceConverter
			.convert(operationInstance, repository.getMetamodel());

		final List<String> messages = operationBase.checkRestriction(
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

		final HistoryFactory factory = HistoryFactory.eINSTANCE;

		final OperationInstance operationInstance = factory.createOperationInstance();
		operationInstance.setName(operation.getName());

		// create instance of main parameter and initialize with selected
		// elements
		for (final Parameter parameter : operation.getParameters()) {
			final ParameterInstance parameterInstance = factory
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
		final Repository repository = OperationInstanceConverter
			.createEmptyRepository(getExtent());
		final OperationImplementation operationBase = OperationInstanceConverter
			.convert(operationInstance, repository.getMetamodel());
		final List<String> messages = operationBase.checkPreconditions(repository
			.getMetamodel());

		final List<Constraint> violatedConstraints = new ArrayList<Constraint>();
		for (final String message : messages) {
			Constraint constraint = getConstraint(operationInstance
				.getOperation(), message);
			if (constraint == null) {
				constraint = DeclarationFactory.eINSTANCE.createConstraint();
				constraint.setDescription(message);
			}
			violatedConstraints.add(constraint);
		}
		OperationInstanceConverter.convert(operationBase, operationInstance);

		return violatedConstraints;
	}

	/** Create a constraint with a certain message. */
	private Constraint getConstraint(Operation operation, String message) {
		for (final Constraint constraint : operation.getConstraints()) {
			if (message.equals(constraint.getDescription())) {
				return constraint;
			}
		}
		return null;
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
