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
package org.eclipse.emf.edapt.history.recorder.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.common.ui.EcoreUIUtils;
import org.eclipse.emf.edapt.common.ui.PartAdapter;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;


/**
 * Detect when an {@link EcoreEditor} is opened and attach an
 * {@link EditingDomainListener} to it (singleton).
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class EcoreEditorDetector extends PartAdapter implements
		IPropertyListener {

	/**
	 * Mapping of {@link EcoreEditor} to attached {@link EditingDomainListener}.
	 */
	private final Map<EcoreEditor, EditingDomainListener> mapping;

	/** Singleton instance. */
	private static EcoreEditorDetector instance = new EcoreEditorDetector();

	/** Getter for singleton instance. */
	public static EcoreEditorDetector getInstance() {
		return instance;
	}

	/**
	 * Private default constructor.
	 */
	private EcoreEditorDetector() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		activePage.addPartListener(this);

		mapping = new HashMap<EcoreEditor, EditingDomainListener>();

		IEditorReference[] editorReferences = activePage.getEditorReferences();
		for (int i = 0, n = editorReferences.length; i < n; i++) {
			IEditorReference editorReference = editorReferences[i];
			IEditorPart editor = editorReference.getEditor(false);
			if (editor instanceof EcoreEditor) {
				EcoreEditor ecoreEditor = (EcoreEditor) editor;
				addEditor(ecoreEditor);
			}
		}
	}

	/**
	 * Attach an {@link EditingDomainListener} to an {@link EcoreEditor}.
	 */
	public void addEditor(EcoreEditor editor) {
		EditingDomainListener listener = getListener(editor);

		if (listener == null) {
			listener = new EditingDomainListener(editor.getEditingDomain());
		}
		if (listener.loadHistory()) {
			validateListener(editor, listener);
		}
	}

	/**
	 * Attach an {@link EditingDomainListener} to an {@link EcoreEditor} and
	 * create a history to record the changes.
	 */
	public void addEditorAndCreateHistory(EcoreEditor editor,
			List<Resource> metamodelResources, URI historyURI) {
		EditingDomainListener listener = getListener(editor);

		if (listener == null) {
			listener = new EditingDomainListener(editor.getEditingDomain());
			listener.createHistory(metamodelResources, historyURI);
			validateListener(editor, listener);
		}
	}

	/**
	 * Validate and start the listener.
	 */
	private void validateListener(EcoreEditor editor,
			EditingDomainListener listener) {
		mapping.put(editor, listener);
		editor.addPropertyListener(this);
		hackAdapterFactory(editor);

		if (listener.isListening()) {
			listener.endListening();
		}
		listener.beginListening();
	}

	/**
	 * Hack the adapter factory of an Ecore editor so that it uses the adapter
	 * factory registry instead of the reflective adapter factory.
	 */
	private void hackAdapterFactory(EcoreEditor editor) {
		ComposedAdapterFactory composedAdapterFactory = (ComposedAdapterFactory) editor
				.getAdapterFactory();
		AdapterFactory adapterFactory = composedAdapterFactory
				.getFactoryForType(HistoryPackage.eINSTANCE.getHistory());
		if (adapterFactory instanceof ReflectiveItemProviderAdapterFactory) {
			// remove the reflective adapter factory
			composedAdapterFactory.removeAdapterFactory(adapterFactory);
			// add an adapterfactory using the registry
			composedAdapterFactory
					.addAdapterFactory(new ComposedAdapterFactory(
							ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		}
	}

	/**
	 * Detach an {@link EditingDomainListener} from an {@link EcoreEditor}.
	 */
	private void removeEditor(EcoreEditor editor) {
		EditingDomainListener listener = mapping.get(editor);
		if (listener != null) {
			listener.endListening();
			editor.removePropertyListener(this);
		}
		mapping.remove(editor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partOpened(IWorkbenchPart part) {
		if (part instanceof EcoreEditor) {
			EcoreEditor editor = (EcoreEditor) part;
			if (EcoreUIUtils.isMetamodelEditor(editor)) {
				addEditor(editor);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof EcoreEditor) {
			EcoreEditor editor = (EcoreEditor) part;
			removeEditor(editor);
		}
	}

	/**
	 * Get the {@link EditingDomainListener} for a {@link EcoreEditor}.
	 */
	public EditingDomainListener getListener(EcoreEditor editor) {
		return mapping.get(editor);
	}

	/**
	 * {@inheritDoc}
	 */
	public void propertyChanged(Object source, int propId) {
		if (propId == IEditorPart.PROP_DIRTY) {
			EcoreEditor editor = (EcoreEditor) source;
			EditingDomainListener listener = mapping.get(editor);
			listener.resetRecorder();
		}
	}
}
