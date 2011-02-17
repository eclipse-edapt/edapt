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
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.List;

import org.eclipse.emf.edapt.declaration.Constraint;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;


/**
 * Table viewer to display violated contraints
 * (A list of constraints is expected as input of the viewer)
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
@SuppressWarnings("restriction")
public class ConstraintViewer extends TableViewer {
	
	/**
	 * Constraint icon
	 */
	private Image constraintImage;

	/**
	 * Default constructor
	 * 
	 * @param parent Parent composite
	 */
	public ConstraintViewer(Composite parent) {
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		
		// icon taken from IDE Workbench plugin
		ImageDescriptor imageDescriptor = IDEWorkbenchPlugin.getIDEImageDescriptor("obj16/error_tsk.gif");
		constraintImage = imageDescriptor.createImage();
		
		init();
	}
	
	/**
	 * Initialize table viewer
	 */
	private void init() {
		
		final Table constraintTable = getTable();
		
		// content provider
		setContentProvider(new IStructuredContentProvider() {

			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {
				List<Constraint> constraints = (List<Constraint>) inputElement;
				return constraints.toArray();
			}

			public void dispose() {
				// not required
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// not required
			}
			
		});
		
		// label provider
		setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				switch(columnIndex) {
				case 0: return constraintImage;
				default: return null;
				}
			}

			public String getColumnText(Object element, int columnIndex) {
				Constraint constraint = (Constraint) element;
				switch(columnIndex) {
				case 0: return constraint.getLabel();
				default: return "";
				}
			}

			public void addListener(ILabelProviderListener listener) {
				// not required
			}

			public void dispose() {
				// not required
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
				// not required
			}
			
		});
		
		// show constraint description upon double click
		constraintTable.addHelpListener(new HelpListener() {

			public void helpRequested(HelpEvent e) {

				if(constraintTable.getSelectionCount() > 0) {
					TableItem tableItem = constraintTable.getSelection()[0];
					showDescription(tableItem);
				}
			}
			
		});
		
		addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				
				if(constraintTable.getSelectionCount() > 0) {
					TableItem tableItem = constraintTable.getSelection()[0];
					showDescription(tableItem);
				}
			}
			
		});
	}

	/**
	 * Show the description of a constraint which is associated to a table item
	 * 
	 * @param tableItem Table item
	 */
	private void showDescription(TableItem tableItem) {
		Constraint constraint = (Constraint) tableItem.getData();
		TableItemPopupDialog dialog = new TableItemPopupDialog(tableItem, constraint.getLabel(), constraint.getBooleanExpression());
		dialog.open();
	}

}
