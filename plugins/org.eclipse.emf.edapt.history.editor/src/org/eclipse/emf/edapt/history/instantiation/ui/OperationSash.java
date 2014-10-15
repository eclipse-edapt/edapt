/*******************************************************************************
 * Copyright (c) 2006, 2009 Markus Herrmannsdoerfer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Herrmannsdoerfer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.IExtentProvider;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.declaration.Constraint;
import org.eclipse.emf.edapt.history.instantiation.OperationInstanceHelper;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Sash containing the operation, parameter and constraint viewer
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationSash extends SashForm {

	/**
	 * Viewer for the operations
	 */
	private OperationViewer operationViewer;

	/**
	 * Viewer for the parameters
	 */
	private ParameterViewer parameterViewer;

	/**
	 * Viewer for the violated constraints
	 */
	private ConstraintViewer constraintViewer;

	/**
	 * Helper to assemble operation instances
	 */
	private final OperationInstanceHelper helper;

	/**
	 * Constructor
	 * 
	 * @param parent
	 */
	public OperationSash(Composite parent, IExtentProvider provider) {
		super(parent, SWT.VERTICAL);

		helper = new OperationInstanceHelper(provider);

		init();
	}

	/**
	 * Initialize the sash
	 */
	private void init() {
		initOperationViewer();
		initParameterViewer();
		initConstraintViewer();

		// listen to selection of operation
		operationViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						OperationInstance operationInstance = SelectionUtils
								.getSelectedElement(event.getSelection());
						if (operationInstance != null) {
							parameterViewer.setInput(operationInstance);
							updateConstraints(operationInstance);
						}
					}

				});

		setWeights(new int[] { 3, 2, 2 });
	}

	/**
	 * Initialize operation viewer
	 */
	private void initOperationViewer() {

		Composite composite = new Composite(this, SWT.None);
		composite.setLayout(new GridLayout(1, false));

		Label operationLabel = new Label(composite, SWT.None);
		operationLabel.setText("Applicable Operations:");
		operationLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		operationViewer = new OperationViewer(composite);
		operationViewer.getTable().setLayoutData(
				new GridData(GridData.FILL_BOTH));
	}

	/**
	 * Initialize parameter viewer
	 */
	private void initParameterViewer() {
		Composite composite = new Composite(this, SWT.None);
		composite.setLayout(new GridLayout(1, false));

		Label parameterLabel = new Label(composite, SWT.None);
		parameterLabel.setText("Parameters:");
		parameterLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		parameterViewer = new ParameterViewer(composite, this);
		parameterViewer.getTable().setLayoutData(
				new GridData(GridData.FILL_BOTH));
	}

	/**
	 * Initialize constraint viewer
	 */
	private void initConstraintViewer() {
		Composite composite = new Composite(this, SWT.None);
		composite.setLayout(new GridLayout(1, false));

		Label constraintLabel = new Label(composite, SWT.None);
		constraintLabel.setText("Violated constraints:");
		constraintLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		constraintViewer = new ConstraintViewer(composite);
		constraintViewer.getTable().setLayoutData(
				new GridData(GridData.FILL_BOTH));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setFocus() {
		if (operationViewer != null) {
			return operationViewer.getControl().setFocus();
		}
		return false;
	}

	/**
	 * Get the selected operation
	 * 
	 * @return Operation
	 */
	public OperationInstance getSelectedOperation() {
		OperationInstance operationInstance = SelectionUtils
				.getSelectedElement(operationViewer.getSelection());
		return operationInstance;

	}

	/**
	 * Revalidate operation constraints on an instance and set enablement of
	 * execution button accordingly
	 * 
	 * @param operationInstance
	 *            Instance of operation
	 * @return true if no constraint is violated, false otherwise
	 */
	protected boolean updateConstraints(OperationInstance operationInstance) {

		List<Constraint> violatedConstraints = helper
				.getViolatedConstraints(operationInstance);
		constraintViewer.setInput(violatedConstraints);

		return violatedConstraints.isEmpty();
	}

	/**
	 * Update the offered operations by means of a selection of elements
	 */
	@SuppressWarnings("unchecked")
	public void updateOperations(IStructuredSelection structuredSelection) {

		List<EObject> selectedElements = new ArrayList<EObject>();
		for (Iterator i = structuredSelection.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof EObject) {
				EObject element = (EObject) o;
				selectedElements.add(element);
			}
		}

		List<OperationInstance> possibleOperations = helper
				.getPossibleOperations(selectedElements);

		updateViewers(possibleOperations);
	}

	/**
	 * Update the viewers
	 * 
	 * @param possibleOperations
	 */
	protected void updateViewers(List<OperationInstance> possibleOperations) {
		operationViewer.setInput(possibleOperations);
		parameterViewer.setInput(null);
		constraintViewer.setInput(new ArrayList<Constraint>());
	}

	/**
	 * Getter for operation instance helper
	 * 
	 * @return Operation instance helper
	 */
	protected OperationInstanceHelper getHelper() {
		return helper;
	}

	/** Returns operationViewer. */
	public OperationViewer getOperationViewer() {
		return operationViewer;
	}
}
