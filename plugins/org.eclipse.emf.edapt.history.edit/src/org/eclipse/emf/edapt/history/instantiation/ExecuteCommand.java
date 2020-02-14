/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edapt.history.recorder.IChangeProvider;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edit.command.ChangeCommand;

/**
 * A generic command to execute an operation instance
 * (saving the changes in order to be undoable)
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ExecuteCommand extends ChangeCommand implements IChangeProvider {

	/**
	 * Interpreter for the operation instance
	 */
	private final OperationInterpreter interpreter;

	/**
	 * Operation instance
	 */
	private final OperationInstance operationInstance;

	/**
	 * Default constructor
	 *
	 * @param operationInstance Operation instance
	 * @param extent Class extent
	 */
	@SuppressWarnings("unchecked")
	public ExecuteCommand(OperationInstance operationInstance, MetamodelExtent extent) {
		super((Collection) extent.getRootPackages());

		this.operationInstance = operationInstance;

		interpreter = new OperationInterpreter(operationInstance, extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		// execute operation
		interpreter.execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	// @Override
	public List<Change> getChanges(List<PrimitiveChange> changes) {
		final OperationChange change = HistoryFactory.eINSTANCE.createOperationChange();
		change.setOperation(operationInstance);

		change.getChanges().addAll(changes);

		return (List) Collections.singletonList(change);
	}
}
