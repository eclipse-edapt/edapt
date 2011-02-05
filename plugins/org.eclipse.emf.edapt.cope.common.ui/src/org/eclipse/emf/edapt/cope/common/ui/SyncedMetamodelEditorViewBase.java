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
package org.eclipse.emf.edapt.cope.common.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Base class for views where the listening to the editor's selection can be
 * turned off
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class SyncedMetamodelEditorViewBase extends
		MetamodelEditorViewBase {

	/**
	 * Whether the view gets notified about changed of the editor's selection
	 */
	private boolean listening = true;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createActions() {
		Action syncAction = new Action("Sync", IAction.AS_CHECK_BOX) {

			{
				setChecked(true);
				setImageDescriptor(PlatformUI.getWorkbench()
						.getSharedImages().getImageDescriptor(
								ISharedImages.IMG_ELCL_SYNCED));
				setDisabledImageDescriptor(PlatformUI.getWorkbench()
						.getSharedImages().getImageDescriptor(
								ISharedImages.IMG_ELCL_SYNCED_DISABLED));
			}

			@Override
			public void run() {
				setListening(isChecked());
			}
		};

		getViewSite().getActionBars().getToolBarManager().add(
				syncAction);
	}

	/**
	 * Change the status of the listening
	 */
	protected void setListening(boolean listening) {
		if (this.listening == listening) {
			return;
		}

		this.listening = listening;
		if (listening) {
			installSelectionChangedListener();
			selectionChanged((IStructuredSelection) getEditor().getSelection());
		} else {
			uninstallSelectionChangedListener();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void installSelectionChangedListener() {
		if (listening) {
			super.installSelectionChangedListener();
		}
	}
}
