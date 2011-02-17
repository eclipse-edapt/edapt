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
	 * Change description to be applied to undo changes of the
	 * {@link RepairMetamodelIntegrityCommand}.
	 */
	private final ChangeDescription changeDescription;

	/** Constructor. */
	public RepairHistoryIntegrityCommand(RepairMetamodelIntegrityCommand command) {
		this.changeDescription = command.getChangeDescription();
	}

	/** {@inheritDoc} */
	@Override
	protected boolean prepare() {
		return true;
	}

	/** {@inheritDoc} */
	public void execute() {
		changeDescription.applyAndReverse();
	}

	/** {@inheritDoc} */
	@Override
	public void undo() {
		execute();
	}

	/** {@inheritDoc} */
	public void redo() {
		execute();
	}
}