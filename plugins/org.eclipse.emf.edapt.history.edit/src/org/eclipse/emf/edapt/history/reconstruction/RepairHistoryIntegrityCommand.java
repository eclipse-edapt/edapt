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
package org.eclipse.emf.edapt.history.reconstruction;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.change.ChangeDescription;

/**
 * Command to adapt the current metamodel version to the version reconstructed
 * from the history.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class RepairHistoryIntegrityCommand extends AbstractCommand {

	/**
	 * Change description to be applied to undo changes of the {@link RepairMetamodelIntegrityCommand}.
	 */
	private final ChangeDescription changeDescription;

	/** Constructor. */
	public RepairHistoryIntegrityCommand(RepairMetamodelIntegrityCommand command) {
		changeDescription = command.getChangeDescription();
	}

	/** {@inheritDoc} */
	@Override
	protected boolean prepare() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void execute() {
		changeDescription.applyAndReverse();
	}

	/** {@inheritDoc} */
	@Override
	public void undo() {
		execute();
	}

	/** {@inheritDoc} */
	@Override
	public void redo() {
		execute();
	}
}