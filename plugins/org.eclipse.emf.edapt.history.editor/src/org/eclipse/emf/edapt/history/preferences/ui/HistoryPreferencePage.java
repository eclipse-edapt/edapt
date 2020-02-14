/*******************************************************************************
 * Copyright (c) 2019 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christian W. Damus - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.history.preferences.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Top-level preference page for the Edapt history management.
 */
public class HistoryPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Initializes me.
	 */
	public HistoryPreferencePage() {
		super();
	}

	@Override
	public void init(IWorkbench workbench) {
		// Nothing to initialize
	}

	@Override
	protected Control createContents(Composite parent) {
		final Composite result = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(result);

		createClearDialogsPreference(result);

		return result;
	}

	private void createClearDialogsPreference(Composite parent) {
		new Label(parent, SWT.NONE).setText("Forget all decisions remembered by dialogs:"); //$NON-NLS-1$
		final Button clearDialogs = new Button(parent, SWT.PUSH);
		clearDialogs.setText("Clear Dialogs"); //$NON-NLS-1$
		clearDialogs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final boolean delete = MessageDialog.openConfirm(getShell(), "Clear Dialogs", //$NON-NLS-1$
					"Are you sure you want to forget all responses to dialogs via the \"Remember my decision\" option?"); //$NON-NLS-1$

				if (delete) {
					ResourcePreferences.deleteAll();
				}
			}
		});
	}

}
