/*******************************************************************************
 * Copyright (c) 2007, 2019 BMW Car IT, Technische Universitaet Muenchen, Christian W. Damus, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 * Christian W. Damus - bugs 529599, 544155
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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.common.ui.EcoreUIUtils;
import org.eclipse.emf.edapt.common.ui.PartAdapter;
import org.eclipse.emf.edapt.history.preferences.ui.PromptKind;
import org.eclipse.emf.edapt.history.preferences.ui.ResourcePreferences;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.recorder.AddResourceCommand;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.history.recorder.IResourceLoadListener;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;
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
	// This must be package-visible at least to avoid compiler generating a synthetic
	// method for access from an inner class resulting in deadlock on the class
	// monitor during class initialization at start-up. cf. http://eclip.se/529599
	/* package */ void validateListener(EcoreEditor editor,
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
	private void addHistory(final EditingDomainListener listener, Resource resource) {
		final List<EPackage> rootPackages = ResourceUtils.getRootElements(resource, EPackage.class);
		if (rootPackages.isEmpty()) {
			return; // Nothing to worry about
		}

		final boolean addHistory = shouldAddHistory(listener, resource, rootPackages);
		if (addHistory) {
			final CommandStack commandStack = listener.getEditingDomain()
				.getCommandStack();
			commandStack.execute(new AddResourceCommand(listener, resource));
		}
	}

	/**
	 * Query whether we should add the given packages for a {@code resource} to the history, prompting the user
	 * if necessary.
	 *
	 * @param listener the editing-domain listener in the context of a history
	 * @param resource a resource that was loaded in the editing domain and contains Ecore packages
	 * @param rootPackages the Ecore packages contained by the {@code resource}
	 * @return whether to add the packages to the history
	 */
	private boolean shouldAddHistory(final EditingDomainListener listener, Resource resource,
		final List<EPackage> rootPackages) {

		final EditingDomain domain = listener.getEditingDomain();
		final IPreferenceStore store = ResourcePreferences.getPreferences(domain);
		final String resourceKey = "ignore." //$NON-NLS-1$
			+ domain.getResourceSet().getURIConverter().normalize(resource.getURI()).toString();
		store.setDefault(resourceKey, MessageDialogWithToggle.PROMPT);

		final boolean result;
		final PromptKind ignore = PromptKind.get(store, resourceKey);
		switch (ignore) {
		case ALWAYS:
			// Don't need to prompt: user previously answered yes
			result = true;
			break;
		case NEVER:
			// Don't need to prompt: user previously answered no
			result = false;
			break;
		default: // case PROMPT:
			// Need to prompt: haven't remembered a previous answer
			String title;
			String message;
			if (rootPackages.size() == 1) {
				title = "Add EPackage to History"; //$NON-NLS-1$
				message = NLS.bind("An Ecore resource has been loaded. " + //$NON-NLS-1$
					"Add the EPackage ''{0}'' to the history?", //$NON-NLS-1$
					getNSURI(rootPackages.get(0)));
			} else {
				title = "Add EPackages to History"; //$NON-NLS-1$
				final StringBuilder nsURIs = new StringBuilder();
				final String newline = System.getProperty("line.separator"); //$NON-NLS-1$
				for (final EPackage next : rootPackages) {
					nsURIs.append(newline);
					nsURIs.append("  "); //$NON-NLS-1$
					nsURIs.append(getNSURI(next));
				}
				message = NLS.bind("An Ecore resource has been loaded. " + //$NON-NLS-1$
					"Add these EPackages to the history?{0}", //$NON-NLS-1$
					nsURIs);
			}

			final MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(
				Display.getDefault().getActiveShell(), title, message,
				"Remember my decision", false, store, resourceKey); //$NON-NLS-1$

			result = dialog.getReturnCode() == IDialogConstants.YES_ID;
			break;
		}
		return result;
	}

	private String getNSURI(EPackage ePackage) {
		String result = ePackage.getNsURI();
		if (result == null || result.isEmpty()) {
			// During development, maybe it doesn't have an NS URI, yet
			result = String.valueOf(EcoreUtil.getURI(ePackage));
		}
		return result;
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
