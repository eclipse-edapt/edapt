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

import java.net.URL;
import java.util.List;

import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.provider.HistoryEditPlugin;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Table viewer to display applicable operations
 * (A list of operation instances is expected as input)
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationViewer extends TableViewer {

	/**
	 * Operation icon
	 */
	private Image operationImage;

	/**
	 * Default constructor
	 *
	 * @param parent Parent composite
	 */
	public OperationViewer(Composite parent) {
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

		init();
	}

	/**
	 * Initialize viewer
	 *
	 */
	private void init() {

		final URL url = (URL) HistoryEditPlugin.INSTANCE.getImage("full/obj16/OperationInstance"); //$NON-NLS-1$
		final ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		operationImage = imageDescriptor.createImage();

		final Table operationTable = getTable();

		// content provider
		setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				final List list = (List) inputElement;
				return list.toArray();
			}

			@Override
			public void dispose() {
				// not required
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// not required
			}

		});

		// label provider
		setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return operationImage;
				default:
					return null;
				}
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				final OperationInstance operationInstance = (OperationInstance) element;
				final Operation operation = operationInstance.getOperation();
				switch (columnIndex) {
				case 0:
					return operation.getLabel();
				case 1:
					return operation.getDescription();
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

		// show operation description upon pressing F1
		operationTable.addHelpListener(new HelpListener() {

			@Override
			public void helpRequested(HelpEvent e) {

				if (operationTable.getSelectionCount() > 0) {
					final TableItem tableItem = operationTable.getSelection()[0];
					showDescription(tableItem);
				}
			}

		});

		addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				if (operationTable.getSelectionCount() > 0) {
					final TableItem tableItem = operationTable.getSelection()[0];
					showDescription(tableItem);
				}
			}

		});

		setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				final OperationInstance oi1 = (OperationInstance) e1;
				final OperationInstance oi2 = (OperationInstance) e2;

				return oi1.getOperation().getLabel().compareTo(
					oi2.getOperation().getLabel());
			}
		});

		addFilter(new ViewerFilter() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean select(Viewer viewer, Object parentElement,
				Object element) {
				final OperationInstance operationInstance = (OperationInstance) element;
				return !operationInstance.getOperation().isDeprecated();
			}

		});
	}

	/**
	 * Show the description of an operation which is associated to a table item
	 *
	 * @param tableItem Table item
	 */
	private void showDescription(TableItem tableItem) {
		final OperationInstance operationInstance = (OperationInstance) tableItem.getData();
		final Operation operation = operationInstance.getOperation();
		final PopupDialog dialog = new TableItemPopupDialog(tableItem, operation.getLabel(),
			operation.getDescription());
		dialog.open();
	}
}
