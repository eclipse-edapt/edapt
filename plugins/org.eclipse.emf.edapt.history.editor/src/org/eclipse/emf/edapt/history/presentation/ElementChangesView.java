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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.common.ui.ModelSash;
import org.eclipse.emf.edapt.common.ui.SyncedMetamodelEditorViewBase;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.NonDelete;
import org.eclipse.emf.edapt.spi.history.ValueChange;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * View to show related changes
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ElementChangesView extends SyncedMetamodelEditorViewBase {

	/**
	 * Identifier of the view like in the plugin.xml
	 */
	public static final String ID = ElementChangesView.class.getName();

	/**
	 * Sash to show the history reduced to the related changes
	 */
	private ModelSash sash;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createContents(Composite parent) {

		sash = new ModelSash(parent, SWT.None);

		sash.getStructureViewer().setSorter(null);
		sash.getStructureViewer().addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				getEditor().getViewer().setSelection(new StructuredSelection(selection.getFirstElement()), true);
			}

		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		sash.getStructureViewer().getTree().setFocus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void selectionChanged(IStructuredSelection structuredSelection) {
		if (structuredSelection.size() == 1) {
			final Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof EObject) {
				update((EObject) firstElement);
			}
		}
	}

	/**
	 * Update the view
	 */
	private void update(EObject element) {

		final History history = getHistory();
		sash.getStructureViewer().setInput(history);

		final List<Change> changes = getChanges(element);
		final Set<EObject> elements = enrich(changes);

		sash.getStructureViewer().setFilters(new ViewerFilter[] { new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
				Object element) {
				return elements.contains(element);
			}

		} });

		sash.getStructureViewer().expandToLevel(2);
	}

	/**
	 * Enrich changes to paths leading to them
	 */
	private Set<EObject> enrich(List<Change> changes) {
		final Set<EObject> elements = new HashSet<EObject>();

		for (final Change change : changes) {
			EObject element = change;
			while (element != null) {
				elements.add(element);
				element = element.eContainer();
			}
		}

		return elements;
	}

	/**
	 * Get the changes related to an element
	 */
	private List<Change> getChanges(EObject element) {
		if (element instanceof Change && getElement((Change) element) != null) {
			element = getElement((Change) element);
		}
		final List<Change> changes = new ArrayList<Change>();
		final History history = getHistory();

		for (final Iterator<EObject> i = history.eAllContents(); i.hasNext();) {
			final EObject historyElement = i.next();
			if (historyElement instanceof Change) {
				final Change change = (Change) historyElement;
				if (getElement(change) == element) {
					changes.add(change);
				}
			}
		}

		return changes;
	}

	/**
	 * Get the history
	 */
	private History getHistory() {
		final Resource resource = HistoryUtils.getHistoryResource(getEditor().getEditingDomain().getResourceSet());
		return (History) resource.getContents().get(0);
	}

	/**
	 * Get the element of a change
	 */
	private EObject getElement(Change change) {
		if (change instanceof NonDelete) {
			return ((NonDelete) change).getElement();
		}
		else if (change instanceof Delete) {
			return ((Delete) change).getElement();
		}
		else if (change instanceof ValueChange) {
			return ((ValueChange) change).getElement();
		}
		return null;
	}
}
