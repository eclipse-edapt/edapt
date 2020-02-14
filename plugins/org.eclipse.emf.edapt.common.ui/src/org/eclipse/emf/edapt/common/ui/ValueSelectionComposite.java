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
package org.eclipse.emf.edapt.common.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IFontDecorator;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A composite to select a value. The possible values are represented as a tree.
 * Selection criteria can be entered through a text field.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ValueSelectionComposite extends Composite {

	/**
	 * The filter
	 */
	protected Pattern filter;

	/**
	 * Viewer to display filtered values
	 */
	private TreeViewer filteredViewer;

	/**
	 * Whether multiple values can be selected
	 */
	private final boolean multi;

	/**
	 * The label provider
	 */
	protected final AdapterFactoryLabelProvider labelProvider;

	/**
	 * Root elements of area in which values can be found
	 */
	private final Collection valueArea;

	/**
	 * Current value that is selected
	 */
	private final Object value;

	/**
	 * Validator to determine possible values.
	 */
	private final IValueValidator validator;

	/**
	 * Constructor
	 */
	public ValueSelectionComposite(Composite parent,
		AdapterFactoryLabelProvider labelProvider, Object value,
		boolean multi, Collection valueArea, IValueValidator validator) {
		super(parent, SWT.None);

		this.labelProvider = labelProvider;
		this.multi = multi;
		this.value = value;
		this.valueArea = valueArea;
		this.validator = validator;

		filter = Pattern.compile(".*"); //$NON-NLS-1$

		init();
	}

	/**
	 * Initialize the composite.
	 */
	private void init() {
		final GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		setLayout(layout);
		final GridData data = new GridData(GridData.FILL_BOTH);
		setLayoutData(data);

		createFilterText();
		createFilteredViewer();

		selectValue();
	}

	/**
	 * Create the text field to provider the filter pattern
	 */
	private void createFilterText() {
		final Text filterText = new Text(this, SWT.BORDER);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL);
		filterText.setLayoutData(data);

		filterText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setFilter(filterText.getText());
			}

		});

		filterText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					filteredViewer.getControl().setFocus();
				}
			}
		});

		filterText.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
					filteredViewer.getControl().setFocus();
					final EObject matchingElement = getFirstMatchingElement();
					if (matchingElement != null) {
						/* set selection */
						filteredViewer.setSelection(new StructuredSelection(matchingElement));
					}
				}
			}
		});
	}

	/**
	 * Set the filter pattern
	 */
	private void setFilter(String pattern) {
		final StringBuffer escaped = new StringBuffer();

		final String[] starSplit = pattern.split("\\*"); //$NON-NLS-1$
		for (int i = 0, n = starSplit.length; i < n; i++) {
			if (i > 0) {
				escaped.append(".*"); //$NON-NLS-1$
			}
			final String star = starSplit[i];
			final String[] questionSplit = star.split("\\?"); //$NON-NLS-1$
			for (final String question : questionSplit) {
				if (i > 0) {
					escaped.append(".?"); //$NON-NLS-1$
				}
				escaped.append(Pattern.quote(question));
			}
		}

		escaped.append(".*"); //$NON-NLS-1$

		filter = Pattern.compile(escaped.toString(), Pattern.CASE_INSENSITIVE);
		refreshFilteredViewer();
	}

	/**
	 * Refresh the filtered viewer
	 */
	protected void refreshFilteredViewer() {
		filteredViewer.getControl().setRedraw(false);
		filteredViewer.refresh(true);
		filteredViewer.expandAll();
		final ISelection oldSelection = filteredViewer.getSelection();
		final Object firstValue = valueArea.iterator().next();
		filteredViewer.setSelection(new StructuredSelection(firstValue), true);
		filteredViewer.setSelection(oldSelection);
		filteredViewer.getControl().setRedraw(true);
	}

	/**
	 * Create the list to display the values
	 */
	private void createFilteredViewer() {
		int style = SWT.BORDER;
		if (multi) {
			style |= SWT.MULTI;
		}

		filteredViewer = new TreeViewer(this, style);
		final GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 400;
		data.heightHint = 400;
		filteredViewer.getControl().setLayoutData(data);

		filteredViewer.setContentProvider(new AdapterFactoryContentProvider(
			labelProvider.getAdapterFactory()) {
			@Override
			public Object[] getElements(Object object) {
				final Collection objects = (Collection) object;
				return objects.toArray();
			}
		});
		filteredViewer.setLabelProvider(new DecoratingLabelProvider(
			labelProvider, new Decorator()));

		filteredViewer.addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
				Object element) {
				if (select(element)) {
					return true;
				}
				for (final Object child : ((ITreeContentProvider) filteredViewer
					.getContentProvider()).getChildren(element)) {
					if (select(viewer, element, child)) {
						return true;
					}
				}
				return false;
			}

			private boolean select(Object element) {
				return isSelectable(element);
			}

		});

		filteredViewer.setInput(valueArea);

		filteredViewer.expandAll();
	}

	/**
	 * Checks whether the selection contains only valid values
	 */
	public boolean validSelection() {
		final List<Object> elements = getSelectedElements();
		if (elements.size() == 0) {
			return false;
		}
		for (final Object element : elements) {
			if (!validator.isPossibleValue(element)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Select the current value.
	 */
	@SuppressWarnings("unchecked")
	private void selectValue() {
		if (value != null) {
			if (multi) {
				if (value instanceof Collection) {
					filteredViewer.setSelection(new StructuredSelection(
						new ArrayList((Collection) value)));
				}
			} else {
				filteredViewer.setSelection(new StructuredSelection(value));
			}
		}
	}

	/**
	 * Return the first matching element
	 */
	private EObject getFirstMatchingElement() {
		final TreeItem item = getFirstMatchingItem(filteredViewer.getTree()
			.getItems());
		if (item != null) {
			return (EObject) item.getData();
		}
		return null;
	}

	/**
	 * Get the first tree item that represents a value
	 */
	private TreeItem getFirstMatchingItem(TreeItem[] items) {
		for (int i = 0, n = items.length; i < n; i++) {
			TreeItem item = items[i];
			if (isSelectable(item.getData())) {
				return item;
			}
			item = getFirstMatchingItem(item.getItems());
			if (item != null) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Get the selected elements
	 */
	public List<Object> getSelectedElements() {
		return SelectionUtils
			.getSelectedElements(filteredViewer.getSelection(), Object.class);
	}

	/**
	 * Add a listener to listen to changes of the selection.
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		filteredViewer.addSelectionChangedListener(listener);
	}

	/**
	 * Remove a listener to listen to changes of the selection.
	 */
	public void removeSelectionChangedListener(
		ISelectionChangedListener listener) {
		filteredViewer.removeSelectionChangedListener(listener);
	}

	/**
	 * Add a listener to listen to double clicks.
	 */
	public void addDoubleClickListener(IDoubleClickListener listener) {
		filteredViewer.addDoubleClickListener(listener);
	}

	/**
	 * Remove a listener to listen to double clicks.
	 */
	public void removeDoubleClickListener(IDoubleClickListener listener) {
		filteredViewer.removeDoubleClickListener(listener);
	}

	/**
	 * Whether an element can be selected based on the current filter.
	 */
	private boolean isSelectable(Object element) {
		if (validator.isPossibleValue(element)) {
			final String label = labelProvider.getText(element);
			final Matcher matcher = filter.matcher(label);
			return matcher.matches();
		}
		return false;
	}

	/**
	 * Decorator to highlight elements which can be selected
	 */
	private class Decorator implements ILabelDecorator, IFontDecorator {

		/** {@inheritDoc} */
		@Override
		public Image decorateImage(Image image, Object element) {
			return null;
		}

		/** {@inheritDoc} */
		@Override
		public String decorateText(String text, Object element) {
			return null;
		}

		/** {@inheritDoc} */
		@Override
		public void addListener(ILabelProviderListener listener) {
			// not required
		}

		/** {@inheritDoc} */
		@Override
		public void dispose() {
			// not required
		}

		/** {@inheritDoc} */
		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/** {@inheritDoc} */
		@Override
		public void removeListener(ILabelProviderListener listener) {
			// not required
		}

		/** {@inheritDoc} */
		@Override
		public Font decorateFont(Object element) {
			if (isSelectable(element)) {
				return JFaceResources.getFontRegistry().getBold(
					JFaceResources.DIALOG_FONT);
			}
			return null;
		}
	}
}
