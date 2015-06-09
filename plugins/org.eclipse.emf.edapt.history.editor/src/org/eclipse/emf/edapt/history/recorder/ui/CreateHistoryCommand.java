/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.recorder.ui;

import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;

/**
 * Command to create the history.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CreateHistoryCommand extends AbstractCommand {

	/**
	 * The editor for which an {@link EditingDomainListener} needs to be
	 * created.
	 */
	private final EcoreEditor editor;

	/** The {@link URI} of the history that is created. */
	private final URI historyURI;

	/** The metamodel resources to be recorded. */
	private final List<Resource> metamodelResources;

	/** Constructor. */
	public CreateHistoryCommand(EcoreEditor editor,
		List<Resource> metamodelResources, URI historyURI) {
		this.editor = editor;
		this.metamodelResources = metamodelResources;
		this.historyURI = historyURI;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean prepare() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void execute() {
		EcoreEditorDetector.getInstance().addEditorAndCreateHistory(editor,
			metamodelResources, historyURI);
	}

	/** {@inheritDoc} */
	@Override
	public boolean canUndo() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void redo() {
		// not required
	}
}
