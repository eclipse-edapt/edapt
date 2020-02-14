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
 * Table viewer to display violated contraints (A list of constraints is
 * expected as input of the viewer)
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
	private final Image constraintImage;

	/**
	 * Default constructor
	 *
	 * @param parent
	 *            Parent composite
	 */
	public ConstraintViewer(Composite parent) {
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
			| SWT.FULL_SELECTION);

		// icon taken from IDE Workbench plugin
		ImageDescriptor imageDescriptor = IDEWorkbenchPlugin.getIDEImageDescriptor("obj16/error_tsk.png"); //$NON-NLS-1$
		if (imageDescriptor == null) {
			/* fall back for pre neon IDEs */
			imageDescriptor = IDEWorkbenchPlugin.getIDEImageDescriptor("obj16/error_tsk.gif"); //$NON-NLS-1$
		}
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

			@Override
			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {
				final List<Constraint> constraints = (List<Constraint>) inputElement;
				return constraints.toArray();
			}

			@Override
			public void dispose() {
				// not required
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {
				// not required
			}

		});

		// label provider
		setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return constraintImage;
				default:
					return null;
				}
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				final Constraint constraint = (Constraint) element;
				switch (columnIndex) {
				case 0:
					return constraint.getDescription();
				default:
					return ""; //$NON-NLS-1$
				}
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// not required
			}

			@Override
			public void dispose() {
				// not required
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// not required
			}

		});

		// show constraint description upon double click
		constraintTable.addHelpListener(new HelpListener() {

			@Override
			public void helpRequested(HelpEvent e) {

				if (constraintTable.getSelectionCount() > 0) {
					final TableItem tableItem = constraintTable.getSelection()[0];
					showDescription(tableItem);
				}
			}

		});

		addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				if (constraintTable.getSelectionCount() > 0) {
					final TableItem tableItem = constraintTable.getSelection()[0];
					showDescription(tableItem);
				}
			}

		});
	}

	/**
	 * Show the description of a constraint which is associated to a table item
	 *
	 * @param tableItem
	 *            Table item
	 */
	private void showDescription(TableItem tableItem) {
		final Constraint constraint = (Constraint) tableItem.getData();
		final TableItemPopupDialog dialog = new TableItemPopupDialog(tableItem,
			constraint.getDescription(), constraint.getName());
		dialog.open();
	}

}
