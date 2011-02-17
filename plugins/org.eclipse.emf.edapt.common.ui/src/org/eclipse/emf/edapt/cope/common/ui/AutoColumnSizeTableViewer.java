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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Table viewer which automatically resizes its column when it is resized
 * (Columns will be resized to equal width)
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class AutoColumnSizeTableViewer extends TableViewer {

	/**
	 * Flag to prevent infinite event sequences
	 */
	private boolean resized = false;
	
	/**
	 * Constructor
	 * 
	 * @param parent Parent composite
	 * @param style Style flags
	 */
	public AutoColumnSizeTableViewer(Composite parent, int style) {
		super(parent, style);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void hookControl(Control control) {
		super.hookControl(control);
		
		getTable().addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				if(!resized) {
					resizeColumns();
				}
			}
			
		});
	}
	
	/**
	 * Resize columns to equal width
	 *
	 */
	private void resizeColumns() {
		
		resized = true;
		Table table = getTable();
		int width = table.getClientArea().width / table.getColumnCount();
		TableColumn[] columns = table.getColumns();
		for(int i = 0, n = table.getColumnCount(); i < n; i++) {
			TableColumn column = columns[i];
			column.setWidth(width);
		}
		resized = false;
	}

}
