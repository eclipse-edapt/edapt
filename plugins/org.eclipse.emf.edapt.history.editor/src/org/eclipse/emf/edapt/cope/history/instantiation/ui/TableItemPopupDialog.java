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
package org.eclipse.emf.edapt.cope.history.instantiation.ui;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;

/**
 * Popup dialog which will be displayed below a table item and will span the whole table
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class TableItemPopupDialog extends PopupDialog {

	/**
	 * Table item
	 */
	private final TableItem tableItem;
	
	/**
	 * Description to be shown in popup dialog
	 */
	private final String description;

	/**
	 * Default constructor
	 * 
	 * @param tableItem Table item
	 * @param title Title of dialog
	 * @param description Information shown in dialog
	 */
	public TableItemPopupDialog(TableItem tableItem, String title, String description) {
		super(tableItem.getParent().getShell(),
				PopupDialog.INFOPOPUP_SHELLSTYLE, true, false,
				false, false, false, title, null);
		
		this.tableItem = tableItem;
		this.description = (description == null)?"":description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void adjustBounds() {
		Rectangle itemBounds = tableItem.getBounds();
		Point relative = new Point(itemBounds.x, itemBounds.y);
		Point absolute = tableItem.getParent().toDisplay(relative);
		
		Rectangle tableBounds = tableItem.getParent().getBounds();
		
		Point computedSize = getShell().computeSize(tableBounds.width - itemBounds.x,
				SWT.DEFAULT);

		Rectangle proposedBounds = new Rectangle(absolute.x, absolute.y
				+ itemBounds.height, tableBounds.width - itemBounds.x, computedSize.y);
		getShell().setBounds(proposedBounds);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);
		
		Label text = new Label(parent, SWT.WRAP | SWT.V_SCROLL);
		text.setText(description);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		text.setLayoutData(gridData);
		return parent;
	}
}