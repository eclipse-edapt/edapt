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

import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edit.command.ChangeCommand;

/**
 * Command to release the current version and create a new version
 * (cannot be undone)
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReleaseCommand extends ChangeCommand {

	/**
	 * Listener attached to Ecore editor
	 */
	private final EditingDomainListener listener;

	/**
	 * Label for the current version
	 */
	private final String label;

	/**
	 * Constructor
	 *
	 * @param listener Listener
	 * @param label
	 */
	public ReleaseCommand(EditingDomainListener listener, String label) {
		super(listener.getHistory());

		this.listener = listener;
		this.label = label;
	}

	/**
	 * Constructor
	 *
	 * @param listener Listener
	 */
	public ReleaseCommand(EditingDomainListener listener) {
		this(listener, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doExecute() {
		listener.getHistory().getLastRelease().setLabel(label);
		listener.release();
	}
}
