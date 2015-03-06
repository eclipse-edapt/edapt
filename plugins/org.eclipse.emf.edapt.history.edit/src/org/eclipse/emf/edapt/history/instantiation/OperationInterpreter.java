/*******************************************************************************
 * Copyright (c) 2006, 2009 Markus Herrmannsdoerfer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Markus Herrmannsdoerfer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation;

import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.migration.execution.internal.OperationInstanceConverter;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.provider.HistoryEditPlugin;
import org.eclipse.emf.edapt.spi.migration.Repository;

/**
 * Interpreter for an operation.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationInterpreter {

	/** Instance of the operation */
	private final OperationInstance operationInstance;

	/** Metamodel extent */
	private final MetamodelExtent extent;

	/** Constructor. */
	public OperationInterpreter(OperationInstance operationInstance,
		MetamodelExtent extent) {
		this.operationInstance = operationInstance;
		this.extent = extent;
	}

	/** Execute the operation. */
	public void execute() {
		final Repository repository = OperationInstanceConverter
			.createEmptyRepository(extent);
		final OperationImplementation operation = OperationInstanceConverter.convert(
			operationInstance, repository.getMetamodel());
		try {
			operation.checkAndExecute(repository.getMetamodel(), repository
				.getModel());
		} catch (final MigrationException e) {
			LoggingUtils.logError(HistoryEditPlugin.getPlugin(), e);
		}
	}
}
