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
package org.eclipse.emf.edapt.history.recorder.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.common.ui.EcoreUIUtils;
import org.eclipse.emf.edapt.common.ui.PartAdapter;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.recorder.AddResourceCommand;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.history.recorder.IResourceLoadListener;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Detect when an {@link EcoreEditor} is opened and attach an {@link EditingDomainListener} to it (singleton).
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
		final IWorkbenchPage activePage = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();
		activePage.addPartListener(this);

		mapping = new HashMap<EcoreEditor, EditingDomainListener>();

		final IEditorReference[] editorReferences = activePage.getEditorReferences();
		for (int i = 0, n = editorReferences.length; i < n; i++) {
			final IEditorReference editorReference = editorReferences[i];
			final IEditorPart editor = editorReference.getEditor(false);
			if (editor instanceof EcoreEditor) {
				final EcoreEditor ecoreEditor = (EcoreEditor) editor;
				addEditor(ecoreEditor);
			}
		}
	}

	/**
	 * Attach an {@link EditingDomainListener} to an {@link EcoreEditor}.
	 */
	public void addEditor(final EcoreEditor editor) {
		final IRunnableWithProgress attachRunnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				EditingDomainListener listener = getListener(editor);

				if (listener == null) {
					listener = new EditingDomainListener(editor.getEditingDomain());
				}
				if (listener.loadHistory()) {
					validateListener(editor, listener);
				}
			}
		};
		try {
			new ProgressMonitorDialog(editor.getSite().getShell()).run(true, false, attachRunnable);
		} catch (final InvocationTargetException ex) {
			LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), ex);
		} catch (final InterruptedException ex) {
			LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), ex);
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
		final EditingDomainListener listener) {
		mapping.put(editor, listener);
		editor.addPropertyListener(this);
		hackAdapterFactory(editor);

		if (listener.isListening()) {
			listener.endListening();
		}
		listener.beginListening();
		listener.addResourceListener(new IResourceLoadListener() {

			@Override
			public void resourceLoaded(Resource resource) {
				addHistory(listener, resource);
			}
		});
	}

	/** Ask the user whether a resource should be added to the history. */
	private void addHistory(final EditingDomainListener listener,
		Resource resource) {
		final boolean addHistory = MessageDialog.openQuestion(Display.getDefault()
			.getActiveShell(), "Resource loaded", //$NON-NLS-1$
			"A resource has been loaded. " //$NON-NLS-1$
				+ "Do you want to add it to the history?"); //$NON-NLS-1$
		if (addHistory) {
			final CommandStack commandStack = listener.getEditingDomain()
				.getCommandStack();
			commandStack.execute(new AddResourceCommand(listener, resource));
		}
	}

	/**
	 * Hack the adapter factory of an Ecore editor so that it uses the adapter
	 * factory registry instead of the reflective adapter factory.
	 */
	private void hackAdapterFactory(EcoreEditor editor) {
		final ComposedAdapterFactory composedAdapterFactory = (ComposedAdapterFactory) editor
			.getAdapterFactory();
		final AdapterFactory adapterFactory = composedAdapterFactory
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
		final EditingDomainListener listener = mapping.get(editor);
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
			final EcoreEditor editor = (EcoreEditor) part;
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
			final EcoreEditor editor = (EcoreEditor) part;
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
	@Override
	public void propertyChanged(Object source, int propId) {
		if (propId == IEditorPart.PROP_DIRTY) {
			final EcoreEditor editor = (EcoreEditor) source;
			final EditingDomainListener listener = mapping.get(editor);
			listener.resetRecorder();
		}
	}
}
