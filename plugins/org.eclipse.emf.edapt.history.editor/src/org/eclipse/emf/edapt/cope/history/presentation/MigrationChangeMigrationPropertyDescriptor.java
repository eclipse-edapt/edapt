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
package org.eclipse.emf.edapt.cope.history.presentation;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.edapt.cope.history.MigrationChange;
import org.eclipse.emf.edapt.cope.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.cope.history.presentation.util.SpecialEditorInput;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;


/**
 * Property descriptor to open an editor on a migration change
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigrationChangeMigrationPropertyDescriptor extends
		PropertyDescriptor {

	/**
	 * Migration change
	 */
	private MigrationChange change;

	/**
	 * Constructor
	 * 
	 * @param change
	 * @param itemPropertyDescriptor
	 */
	public MigrationChangeMigrationPropertyDescriptor(MigrationChange change,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(change, itemPropertyDescriptor);
		this.change = change;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CellEditor createPropertyEditor(Composite composite) {
		return new ExtendedDialogCellEditor(composite, getLabelProvider()) {

			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				IStorageEditorInput editorInput = new SpecialEditorInput(change, itemPropertyDescriptor);
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, "org.codehaus.groovy.eclipse.editor.GroovyEditor");
				} catch (PartInitException e) {
					HistoryEditorPlugin.INSTANCE.log(e);
				}
				return null;
			}
			
		};
	}
}
