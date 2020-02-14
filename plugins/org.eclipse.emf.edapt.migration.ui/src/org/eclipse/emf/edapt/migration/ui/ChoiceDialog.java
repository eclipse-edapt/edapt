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
package org.eclipse.emf.edapt.migration.ui;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edapt.common.ui.ModelSash;
import org.eclipse.emf.edapt.common.ui.ResizeableDialogBase;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.common.ui.StructureTreeViewer;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.Repository;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * Dialog to choose an element from a set of elements
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ChoiceDialog extends ResizeableDialogBase {

	/**
	 * Choices
	 */
	private final List<?> choices;

	/**
	 * Context instance
	 */
	private final Instance instance;

	/**
	 * Viewer displaying the model
	 */
	private StructureTreeViewer modelViewer;

	/**
	 * Viewer displaying the selection
	 */
	private TableViewer choiceViewer;

	/**
	 * Selected element
	 */
	private Object selectedElement;

	/**
	 * Adapter factory
	 */
	private ComposedAdapterFactory adapterFactory;

	/**
	 * Constructor
	 *
	 * @param instance
	 * @param choices
	 * @param message
	 */
	public ChoiceDialog(Instance instance, List<?> choices, String message) {
		super(new Point(800, 600), "Choice", message); //$NON-NLS-1$

		this.instance = instance;
		this.choices = choices;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		this.adapterFactory = adapterFactory;

		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));

		createModelViewer(sash);
		createChoiceViewer(sash);

		sash.setWeights(new int[] { 1, 1 });

		return parent;
	}

	/**
	 * Create the viewer displaying the model
	 *
	 * @param sash
	 */
	private void createModelViewer(SashForm sash) {

		final ModelSash modelSash = new MigrationModelSash(sash, SWT.None, adapterFactory);
		modelViewer = modelSash.getStructureViewer();
		final Repository repository = instance.getType().getModel().getRepository();
		modelViewer.setInput(repository);
		modelViewer.setSelection(new StructuredSelection(instance), true);

		createContextMenu();
	}

	/**
	 * Create context menu for model viewer
	 */
	private void createContextMenu() {
		final MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				final IStructuredSelection selection = (IStructuredSelection) modelViewer.getSelection();
				if (selection.size() == 1) {
					final Object element = selection.getFirstElement();
					if (element instanceof Instance) {
						final Instance instance = (Instance) element;

						final IEditingDomainItemProvider provider = (IEditingDomainItemProvider) adapterFactory.adapt(
							instance, IEditingDomainItemProvider.class);
						final Collection<?> descriptors = provider.getNewChildDescriptors(instance, null, null);

						for (final Object descriptor : descriptors) {
							if (descriptor instanceof CommandParameter) {
								final CommandParameter parameter = (CommandParameter) descriptor;
								final EReference reference = parameter.getEReference();
								final EClass type = parameter.getEValue().eClass();

								manager.add(new Action("Create " + reference.getName() + ":" + type.getName()) { //$NON-NLS-1$ //$NON-NLS-2$
									@Override
									public void run() {
										final Model model = instance.getType().getModel();
										final Instance child = model.newInstance(type);
										instance.add(reference, child);
										modelViewer.refresh(instance);
									}
								});
							}
						}
					}
				}
			}

		});

		final Menu menu = menuManager.createContextMenu(modelViewer.getControl());
		modelViewer.getControl().setMenu(menu);
	}

	/**
	 * Initialize the viewer displaying the choices
	 *
	 * @param sash
	 */
	private void createChoiceViewer(SashForm sash) {

		choiceViewer = new TableViewer(sash, SWT.BORDER);
		choiceViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				return choices.toArray();
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

		final ILabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
		choiceViewer.setLabelProvider(labelProvider);

		choiceViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				final Object element = SelectionUtils.getSelectedElement(event
					.getSelection(), Object.class);
				modelViewer.setSelection(new StructuredSelection(element));
			}

		});

		choiceViewer.setInput(choices);
		choiceViewer.setSelection(new StructuredSelection(choiceViewer.getElementAt(0)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		selectedElement = SelectionUtils.getSelectedElement(choiceViewer
			.getSelection(), Object.class);
		super.okPressed();
	}

	/** Returns selected. */
	public Object getSelectedElement() {
		return selectedElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close() {
		adapterFactory.dispose();
		return super.close();
	}
}
