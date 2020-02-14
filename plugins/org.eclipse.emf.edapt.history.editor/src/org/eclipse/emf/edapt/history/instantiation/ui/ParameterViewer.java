/*******************************************************************************
 * Copyright (c) 2006, 2009 Markus Herrmannsdoerfer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Markus Herrmannsdoerfer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edapt.common.ui.AutoColumnSizeTableViewer;
import org.eclipse.emf.edapt.common.ui.MultiValueSelectionDialog;
import org.eclipse.emf.edapt.common.ui.SingleValueSelectionDialog;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.ParameterInstance;
import org.eclipse.emf.edapt.spi.history.provider.HistoryEditPlugin;
import org.eclipse.emf.edapt.spi.history.provider.HistoryItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Table viewer to display and edit parameter instances of an operation (An
 * operation instance is exected as input)
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ParameterViewer extends AutoColumnSizeTableViewer {

	/**
	 * Label of name column
	 */
	private static final String NAME_COLUMN_PROPERTY = "Name"; //$NON-NLS-1$

	/**
	 * Label of value column
	 */
	private static final String VALUE_COLUMN_PROPERTY = "Value"; //$NON-NLS-1$

	/**
	 * Operation browser
	 */
	private final OperationSash operationSash;

	/**
	 * Parameter icon
	 */
	private Image parameterImage;

	/**
	 * Label provider for cell
	 */
	private ILabelProvider cellLabelProvider;

	/**
	 * Label provider used in viewer label provider
	 */
	private AdapterFactoryLabelProvider dialogLabelProvider;

	/**
	 * Default constructor
	 *
	 * @param parent
	 *            Parent composite
	 * @param operationSash
	 */
	public ParameterViewer(Composite parent, OperationSash operationSash) {
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
			| SWT.FULL_SELECTION);

		this.operationSash = operationSash;

		init();
	}

	/**
	 * Initialize table viewer
	 *
	 */
	private void init() {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory
			.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		adapterFactory
			.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		adapterFactory
			.addAdapterFactory(new HistoryItemProviderAdapterFactory());

		dialogLabelProvider = new AdapterFactoryLabelProvider(adapterFactory);
		cellLabelProvider = new LabelProvider() {

			@Override
			public Image getImage(Object element) {
				if (element instanceof Collection) {
					final Collection collection = (Collection) element;
					if (!collection.isEmpty()) {
						return dialogLabelProvider.getImage(collection
							.iterator().next());
					}
				} else {
					if (element != null) {
						return dialogLabelProvider.getImage(element);
					}
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof Collection) {
					String label = ""; //$NON-NLS-1$
					final Collection c = (Collection) element;
					for (final Iterator i = c.iterator(); i.hasNext();) {
						label += dialogLabelProvider.getText(i.next());
						if (i.hasNext()) {
							label += ", "; //$NON-NLS-1$
						}
					}
					return label;
				}
				return dialogLabelProvider.getText(element);
			}
		};

		final URL url = (URL) HistoryEditPlugin.INSTANCE
			.getImage("full/obj16/ParameterInstance"); //$NON-NLS-1$
		final ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		parameterImage = imageDescriptor.createImage();

		// columns
		final Table parameterTable = getTable();
		parameterTable.setHeaderVisible(true);

		final TableColumn parameterNameColumn = new TableColumn(parameterTable,
			SWT.None);
		parameterNameColumn.setWidth(100);
		parameterNameColumn.setText(NAME_COLUMN_PROPERTY);
		parameterNameColumn.setResizable(false);

		final TableColumn parameterValueColumn = new TableColumn(parameterTable,
			SWT.None);
		parameterValueColumn.setWidth(100);
		parameterValueColumn.setText(VALUE_COLUMN_PROPERTY);
		parameterValueColumn.setResizable(false);

		// content provider
		setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				final OperationInstance operationInstance = (OperationInstance) inputElement;
				return operationInstance.getParameters().toArray();
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
				final ParameterInstance parameterInstance = (ParameterInstance) element;
				switch (columnIndex) {
				case 0:
					return parameterImage;
				case 1:
					return cellLabelProvider.getImage(parameterInstance
						.getValue());
				default:
					return null;
				}
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				final ParameterInstance parameterInstance = (ParameterInstance) element;
				switch (columnIndex) {
				case 0:
					return parameterInstance.getName();
				case 1: {
					final Object value = parameterInstance.getValue();
					return cellLabelProvider.getText(value);
				}
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

		// cell editors
		setColumnProperties(new String[] { NAME_COLUMN_PROPERTY,
			VALUE_COLUMN_PROPERTY });

		final CellEditor[] cellEditors = new CellEditor[2];
		cellEditors[1] = new TextCellEditor(getTable());
		setCellEditors(cellEditors);

		setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				if (NAME_COLUMN_PROPERTY.equals(property)) {
					return false;
				}
				final ParameterInstance parameterInstance = (ParameterInstance) element;
				final Parameter parameter = parameterInstance.getParameter();
				if (parameter.isMain()) {
					return false;
				}
				cellEditors[1] = getCellEditor(parameterInstance);
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				final ParameterInstance parameterInstance = (ParameterInstance) element;
				return parameterInstance.getValue();
			}

			@Override
			public void modify(Object element, String property,
				final Object value) {
				element = ((TableItem) element).getData();
				final ParameterInstance parameterInstance = (ParameterInstance) element;
				final Parameter parameter = parameterInstance.getParameter();

				if (parameter.isMany()) {
					if (parameter.getClassifier() instanceof EClass) {
						if (!(value instanceof Object[])) {
							return;
						}
						final Object[] values = (Object[]) value;
						parameterInstance.setValue(Arrays.asList(values));
					} else {
						parameterInstance.setValue(value);
					}
				} else {
					if (parameter.getClassifier() instanceof EClass) {
						if (!(value instanceof Object[])) {
							return;
						}
						final Object[] values = (Object[]) value;
						if (values.length == 0) {
							parameterInstance.setValue(null);
						} else {
							parameterInstance.setValue(values[0]);
						}
					} else {
						parameterInstance.setValue(value);
					}
				}

				refresh(true);
				operationSash
					.updateConstraints((OperationInstance) parameterInstance
						.eContainer());
			}

		});

		// show parameter description upon double click
		parameterTable.addHelpListener(new HelpListener() {

			@Override
			public void helpRequested(HelpEvent e) {

				if (parameterTable.getSelectionCount() > 0) {
					final TableItem tableItem = parameterTable.getSelection()[0];
					showDescription(tableItem);
				}
			}

		});

		addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				if (parameterTable.getSelectionCount() > 0) {
					final TableItem tableItem = parameterTable.getSelection()[0];
					showDescription(tableItem);
				}
			}

		});
	}

	/**
	 * Show the description of a parameter which is associated to a table item
	 *
	 * @param tableItem
	 *            Table item
	 */
	private void showDescription(TableItem tableItem) {
		final ParameterInstance parameterInstance = (ParameterInstance) tableItem
			.getData();
		final Parameter parameter = parameterInstance.getParameter();
		final PopupDialog dialog = new TableItemPopupDialog(tableItem,
			parameter.getName(), parameter.getDescription());
		dialog.open();
	}

	/**
	 * Get cell editor for a parameter instance based on the type of parameter
	 *
	 * @param parameterInstance
	 *            Parameter instance
	 * @return Cell editor
	 */
	protected CellEditor getCellEditor(final ParameterInstance parameterInstance) {
		final Parameter parameter = parameterInstance.getParameter();

		if (parameter.isMany()) {
			if (parameter.getClassifier() instanceof EClass) {
				return new ExtendedDialogCellEditor(getTable(),
					cellLabelProvider) {

					@Override
					protected Object openDialogBox(Control cellEditorWindow) {

						final ParameterValueValidator selection = new ParameterValueValidator(
							parameterInstance, operationSash.getHelper()
								.getExtent());

						final List values = (List) parameterInstance.getValue();
						final MultiValueSelectionDialog dialog = new MultiValueSelectionDialog(
							cellEditorWindow.getShell(), parameterImage,
							getEditorTitle(parameter), values,
							getRootPackages(), dialogLabelProvider,
							selection);

						dialog.open();
						return dialog.getResult();
					}

				};
			} else if (parameter.getClassifier() instanceof EDataType) {
				return new ExtendedDialogCellEditor(getTable(),
					cellLabelProvider) {

					@Override
					protected Object openDialogBox(Control cellEditorWindow) {

						final EDataType type = (EDataType) parameter.getClassifier();
						final List values = (List) parameterInstance.getValue();

						@SuppressWarnings("deprecation")
						final FeatureEditorDialog dialog = new FeatureEditorDialog(
							cellEditorWindow.getShell(),
							dialogLabelProvider, parameterInstance, type,
							values, getEditorTitle(parameterInstance
								.getParameter()),
							null);

						dialog.open();
						return dialog.getResult();
					}

				};
			}
		} else {
			if (parameter.getClassifier() instanceof EClass) {
				return new ExtendedDialogCellEditor(getTable(),
					cellLabelProvider) {

					@Override
					protected Object openDialogBox(Control cellEditorWindow) {

						final ParameterValueValidator selection = new ParameterValueValidator(
							parameterInstance, operationSash.getHelper()
								.getExtent());

						final SingleValueSelectionDialog dialog = new SingleValueSelectionDialog(
							cellEditorWindow.getShell(), parameterImage,
							getEditorTitle(parameter), parameterInstance
								.getValue(),
							getRootPackages(),
							dialogLabelProvider, selection);

						dialog.open();
						return dialog.getResult();
					}

				};
			} else if (parameter.getClassifier() instanceof EDataType) {
				final EDataType dataType = (EDataType) parameter.getClassifier();
				if (dataType.getInstanceClass() == Boolean.class
					|| dataType.getInstanceClass() == Boolean.TYPE) {

					final List<Object> values = Arrays.asList(new Object[] { Boolean.FALSE,
						Boolean.TRUE });

					return new ExtendedComboBoxCellEditor(getTable(),
						values, dialogLabelProvider, false);
				}
				return new PropertyDescriptor.EDataTypeCellEditor(
					(EDataType) parameter.getClassifier(), getTable());
			}
		}

		return null;
	}

	/**
	 * Helper method to access root packages
	 */
	private Collection<EPackage> getRootPackages() {
		final Collection<EPackage> rootPackages = new ArrayList<EPackage>();
		rootPackages.addAll(operationSash.getHelper().getExtent()
			.getRootPackages());
		rootPackages.add(EcorePackage.eINSTANCE);
		return rootPackages;
	}

	/**
	 * Get table of cell editor dialog
	 *
	 * @param parameter
	 *            Parameter
	 * @return Title
	 */
	public static String getEditorTitle(Parameter parameter) {
		String result = "Parameter "; //$NON-NLS-1$
		result += parameter.getName();
		result += " : "; //$NON-NLS-1$
		if (parameter.isMany()) {
			result += "List<" + parameter.getClassifierName() + ">"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			result += parameter.getClassifierName();
		}
		return result;
	}
}
