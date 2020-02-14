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
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.Collection;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.common.ui.MetamodelEditorViewBase;
import org.eclipse.emf.edapt.history.instantiation.ExecuteCommand;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.history.recorder.ui.EcoreEditorDetector;
import org.eclipse.emf.edapt.internal.common.IExtentProvider;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Browser to provide coupled evolution operations.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationBrowser extends MetamodelEditorViewBase implements
	IExtentProvider {

	/** Identifier of this view an in the plugin.xml. */
	public static final String ID = "org.eclipse.emf.edapt.history.instantiation.view.operationsBrowser"; //$NON-NLS-1$

	/** Button to execute an operation. */
	private Button executeButton;

	/** Sash to render size of table viewers changeable. */
	private OperationSash operationSash;

	/** Returns widget for operation representation. */
	public OperationSash getOperationSash() {
		return operationSash;
	}

	/** {@inheritDoc} */
	@Override
	protected void editorChanged(EcoreEditor oldEditor) {
		if (oldEditor != null) {
			setPartName("Operation Browser"); //$NON-NLS-1$
		}

		final EcoreEditor newEditor = getEditor();
		if (newEditor != null) {
			setPartName("Operation Browser - " + newEditor.getTitle()); //$NON-NLS-1$
			selectionChanged((IStructuredSelection) newEditor.getSelection());
		} else {
			operationSash.updateViewers(null);
			executeButton.setEnabled(false);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void createContents(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		operationSash = new OperationSash(parent, this) {
			@Override
			public boolean updateConstraints(OperationInstance operationInstance) {
				final boolean valid = super.updateConstraints(operationInstance);
				if (valid) {
					executeButton.setEnabled(true);
					executeButton.setFocus();
				} else {
					executeButton.setEnabled(false);
				}
				return valid;
			}
		};
		operationSash.setLayoutData(new GridData(GridData.FILL_BOTH));
		getSite().setSelectionProvider(operationSash.getOperationViewer());

		initButtons(parent);
	}

	/** Initialize buttons. */
	private void initButtons(Composite parent) {
		final Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));

		executeButton = new Button(composite, SWT.None);
		executeButton.setText("Execute"); //$NON-NLS-1$
		executeButton.setEnabled(false);
		executeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeOperation();
			}
		});
	}

	/** {@inheritDoc} */
	@Override
	protected void selectionChanged(IStructuredSelection structuredSelection) {
		operationSash.updateOperations(structuredSelection);
		executeButton.setEnabled(false);
	}

	/** Getter for extent. */
	@Override
	public MetamodelExtent getExtent() {
		final EcoreEditor editor = getEditor();
		final EcoreEditorDetector detector = EcoreEditorDetector.getInstance();
		final EditingDomainListener listener = detector.getListener(editor);
		if (listener != null) {
			return listener.getExtent();
		}
		final Collection<EPackage> rootPackages = MetamodelUtils
			.getAllRootPackages(getEditor().getEditingDomain()
				.getResourceSet());
		return new MetamodelExtent(rootPackages);
	}

	/** {@inheritDoc} */
	@Override
	public void setFocus() {
		operationSash.setFocus();
	}

	/** Execute the currently selected operation. */
	private void executeOperation() {
		final OperationInstance operationInstance = operationSash
			.getSelectedOperation();

		final MetamodelExtent extent = getExtent();
		if (!extent.isConsistent()) {
			if (!MessageDialog
				.openConfirm(Display.getDefault().getActiveShell(),
					"Metamodel inconsistent", //$NON-NLS-1$
					"Metamodel inconsistent. Do you really want to execute this operation?")) { //$NON-NLS-1$
				return;
			}
		}

		final ExecuteCommand command = new ExecuteCommand(operationInstance,
			extent);
		final CommandStack commandStack = getEditor().getEditingDomain()
			.getCommandStack();
		commandStack.execute(command);

		if (!extent.isConsistent()) {
			if (!MessageDialog
				.openConfirm(
					Display.getDefault().getActiveShell(),
					"Metamodel inconsistent", //$NON-NLS-1$
					"Metamodel inconsistent. Do you really want to keep the result of this operation?")) { //$NON-NLS-1$
				commandStack.undo();
			}
		}

		selectionChanged((IStructuredSelection) getEditor()
			.getSelection());
	}
}
