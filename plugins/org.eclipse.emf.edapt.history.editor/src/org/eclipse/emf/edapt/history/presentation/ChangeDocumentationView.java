/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.presentation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.spi.history.Add;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edapt.spi.history.Move;
import org.eclipse.emf.edapt.spi.history.NoChange;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Remove;
import org.eclipse.emf.edapt.spi.history.Set;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledFormText;
import org.eclipse.ui.part.ViewPart;

/**
 * View to show documentation for a selected change
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ChangeDocumentationView extends ViewPart implements
	ISelectionListener {

	/**
	 * Identifier like in the plugin.xml
	 */
	public static final String ID = ChangeDocumentationView.class.getName();

	/**
	 * Text widget to display documentation
	 */
	private ScrolledFormText text;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPartControl(Composite parent) {
		final Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

		final GridLayout layout = new GridLayout(1, false);
		final Composite composite = new Composite(parent, SWT.None);
		composite.setBackground(white);
		composite.setLayout(layout);

		text = new ScrolledFormText(composite, true);
		text.setBackground(white);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));

		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getSelectionService().addSelectionListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		if (!text.isDisposed()) {
			text.setFocus();
		}
	}

	/**
	 * Get documentation for a {@link Change}
	 */
	private String getDocumentation(Change change) {
		if (change instanceof OperationChange) {
			final OperationChange operationChange = (OperationChange) change;
			final Operation operation = operationChange.getOperation().getOperation();
			return getDocumentation(operation);
		} else if (change instanceof PrimitiveChange) {
			final PrimitiveChange primitiveChange = (PrimitiveChange) change;
			return getDocumentation(primitiveChange);
		} else if (change instanceof MigrationChange) {
			return getMigrationDocumentation();
		}
		return "<form></form>"; //$NON-NLS-1$
	}

	/**
	 * Get documentation for a {@link MigrationChange}
	 */
	private String getMigrationDocumentation() {
		return assemble(
			"Custom Migration", //$NON-NLS-1$
			"This change attaches a custom migration to a sequence of changes.", //$NON-NLS-1$
			new String[] { "<b>changes</b>: the sequence of changes" }); //$NON-NLS-1$
	}

	/**
	 * Get the documentation for a {@link PrimitiveChange}
	 */
	private String getDocumentation(PrimitiveChange change) {
		final EClass eClass = change.eClass();

		switch (eClass.getClassifierID()) {
		case HistoryPackage.ADD:
			return getAddDocumentation();
		case HistoryPackage.REMOVE:
			return getRemoveDocumentation();
		case HistoryPackage.SET:
			return getSetDocumentation();
		case HistoryPackage.CREATE:
			return getCreateDocumentation();
		case HistoryPackage.DELETE:
			return getDeleteDocumentation();
		case HistoryPackage.MOVE:
			return getMoveDocumentation();
		case HistoryPackage.NO_CHANGE:
			return getNoChangeDocumentation();
		}

		return "<form></form>"; //$NON-NLS-1$
	}

	/**
	 * Get documentation for a {@link NoChange}
	 */
	private String getNoChangeDocumentation() {
		return assemble("Primitive \"No Change\"", //$NON-NLS-1$
			"In the metamodel, nothing is changed.", new String[] {}); //$NON-NLS-1$
	}

	/**
	 * Get documentation for a {@link Move}
	 */
	private String getMoveDocumentation() {
		return assemble(
			"Primitive \"Move\"", //$NON-NLS-1$
			"In the metamodel, an element is moved to a new target. This primitive only applies to containment references.", //$NON-NLS-1$
			new String[] {
				"<b>element</b>: the element which is moved", //$NON-NLS-1$
				"<b>target</b>: the target to which the element is moved", //$NON-NLS-1$
				"<b>reference</b>: the reference to which the element is moved", //$NON-NLS-1$
				"<b>source</b>: the source from which the element is moved" }); //$NON-NLS-1$
	}

	/**
	 * Get documentation for a {@link Delete}
	 */
	private String getDeleteDocumentation() {
		return assemble(
			"Primitive \"Delete\"", //$NON-NLS-1$
			"In the metamodel, an element is deleted from a target. In addition, cross references are removed which target the element or its children. This primitive only applies to containment references.", //$NON-NLS-1$
			new String[] {
				"<b>element</b>: the element which is deleted", //$NON-NLS-1$
				"<b>target</b>: the target from which the element is deleted", //$NON-NLS-1$
				"<b>reference</b>: the reference from which the element is deleted", //$NON-NLS-1$
				"<b>changes</b>: changes that delete the cross references" }); //$NON-NLS-1$
	}

	/**
	 * Get documentation for a {@link Create}
	 */
	private String getCreateDocumentation() {
		return assemble(
			"Primitive \"Create\"", //$NON-NLS-1$
			"In the metamodel, an element is created as a child of a target and initialized. This primitive only applies to containment references.", //$NON-NLS-1$
			new String[] {
				"<b>element</b>: the element which is created", //$NON-NLS-1$
				"<b>target</b>: the target as a child of which the element is created", //$NON-NLS-1$
				"<b>reference</b>: the reference in which the element is created", //$NON-NLS-1$
				"<b>changes</b>: changes that initialize attribute and cross references of the element" }); //$NON-NLS-1$
	}

	/**
	 * Get documentation for a {@link Set}
	 */
	private String getSetDocumentation() {
		return assemble(
			"Primitive \"Set\"", //$NON-NLS-1$
			"In the metamodel, the value of an element's feature is changed. This primitive only applies to single-valued features which are either attribute or cross reference.", //$NON-NLS-1$
			new String[] {
				"<b>element</b>: the element whose feature's value is changed", //$NON-NLS-1$
				"<b>feature</b>: the single-valued feature", //$NON-NLS-1$
				"<b>value</b>: the new value", //$NON-NLS-1$
				"<b>oldValue</b>: the old value" }); //$NON-NLS-1$
	}

	/**
	 * Get documentation for a {@link Remove}
	 */
	private String getRemoveDocumentation() {
		return assemble(
			"Primitive \"Remove\"", //$NON-NLS-1$
			"In the metamodel, a value is removed from a feature of an element. This primitive only applies to many-valued features which are either attribute or cross reference.", //$NON-NLS-1$
			new String[] {
				"<b>element</b>: the element from whose feature the value is removed", //$NON-NLS-1$
				"<b>feature</b>: the multi-valued feature", //$NON-NLS-1$
				"<b>value</b>: the value that is removed" }); //$NON-NLS-1$
	}

	/**
	 * Get documentation for an {@link Add}
	 */
	private String getAddDocumentation() {
		return assemble(
			"Primitive \"Add\"", //$NON-NLS-1$
			"In the metamodel, a value is added to a feature of an element. This primitive only applies to many-valued features which are either attribute or cross reference.", //$NON-NLS-1$
			new String[] {
				"<b>element</b>: the element to whose feature the value is added", //$NON-NLS-1$
				"<b>feature</b>: the multi-valued feature", //$NON-NLS-1$
				"<b>value</b>: the value that is added" }); //$NON-NLS-1$
	}

	/**
	 * Get documentation for an {@link OperationChange}
	 */
	private String getDocumentation(Operation operation) {
		final String[] parameterList = new String[operation.getParameters().size()];
		int i = 0;
		for (final Parameter parameter : operation.getParameters()) {
			parameterList[i++] = "<b>" + parameter.getName() + "</b>: " //$NON-NLS-1$ //$NON-NLS-2$
				+ parameter.getDescription();
		}

		return assemble("Operation \"" + operation.getLabel() + "\"", operation //$NON-NLS-1$ //$NON-NLS-2$
			.getDescription(), parameterList);
	}

	/**
	 * Unset the documentation
	 */
	private void unsetText() {
		text.setText(""); //$NON-NLS-1$
	}

	/**
	 * Assemble the documentation
	 *
	 * @param title
	 *            The title
	 * @param description
	 *            The description
	 * @param list
	 *            The list entries
	 * @return Assembled documentation
	 */
	private String assemble(String title, String description, String[] list) {
		final StringBuffer result = new StringBuffer();

		result.append("<form>"); //$NON-NLS-1$

		result.append("<p><b>"); //$NON-NLS-1$
		result.append(title);
		result.append("</b></p>"); //$NON-NLS-1$

		result.append("<p>"); //$NON-NLS-1$
		result.append(description);
		result.append("</p>"); //$NON-NLS-1$

		for (final String item : list) {
			result.append("<li>"); //$NON-NLS-1$
			result.append(item);
			result.append("</li>"); //$NON-NLS-1$
		}

		result.append("</form>"); //$NON-NLS-1$

		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getSelectionService().removeSelectionListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				final Object element = structuredSelection.getFirstElement();
				if (element instanceof Change) {
					final Change change = (Change) element;
					text.setText(getDocumentation(change));
					return;
				} else if (element instanceof OperationInstance) {
					final OperationInstance operationInstance = (OperationInstance) element;
					final Operation operation = operationInstance.getOperation();
					text.setText(getDocumentation(operation));
					return;
				}
			}
		}
		unsetText();
	}
}
