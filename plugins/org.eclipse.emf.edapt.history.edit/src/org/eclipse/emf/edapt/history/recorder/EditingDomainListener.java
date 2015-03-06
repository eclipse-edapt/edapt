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
package org.eclipse.emf.edapt.history.recorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Listener for an {@link EditingDomain}.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class EditingDomainListener {

	/** Resource with metamodel history. */
	private Resource historyResource;

	/** Listener which transforms the commands executed to sequences of changes. */
	private CommandStackListener commandStackListener;

	/** Editing domain. */
	private final EditingDomain editingDomain;

	/** Flag to indicate whether listener is active or not. */
	private boolean listening;

	/** Listener which listens to resource loading events. */
	private final Adapter resourceListener = new EContentAdapter() {
		@Override
		public void notifyChanged(Notification notification) {
			final Object notifier = notification.getNotifier();
			if (notifier instanceof Resource) {
				if (notification.getFeatureID(Resource.class) == Resource.RESOURCE__IS_LOADED) {
					final Resource resource = (Resource) notifier;
					if (!isRecorded(resource)) {
						notifyListeners(resource);
					}
				}
			} else if (notifier instanceof ResourceSet) {
				if (notification.getFeatureID(ResourceSet.class) == ResourceSet.RESOURCE_SET__RESOURCES) {
					super.notifyChanged(notification);
				}
			}
		}

		/** {@inheritDoc} */
		@Override
		protected void setTarget(Resource target) {
			basicSetTarget(target);
		}
	};

	/** Listeners that get notified when a new resource is loaded. */
	private final List<IResourceLoadListener> listeners = new ArrayList<IResourceLoadListener>();

	/** Constructor. */
	public EditingDomainListener(EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
		listening = false;
	}

	/** Start the listener. */
	public void beginListening() {
		if (!isListening()) {
			commandStackListener = new CommandStackListener(
				editingDomain.getCommandStack(), historyResource);
			commandStackListener.beginListening();
			editingDomain.getResourceSet().eAdapters().add(resourceListener);

			listening = true;
		} else {
			throw new IllegalStateException("Listener already activated"); //$NON-NLS-1$
		}
	}

	/** Stop the listener. */
	public void endListening() {
		if (isListening()) {
			commandStackListener.endListening();
			editingDomain.getResourceSet().eAdapters().remove(resourceListener);

			listening = false;
		} else {
			throw new IllegalStateException("Listener already deactivated"); //$NON-NLS-1$
		}
	}

	/** Returns listening. */
	public boolean isListening() {
		return listening;
	}

	/** Reset the recorder if it no longer works on the real metamodel. */
	public void resetRecorder() {
		commandStackListener.resetRecorder();
	}

	/** Load the history from a metamodel resource */
	public boolean loadHistory() {
		final ResourceSet resourceSet = editingDomain.getResourceSet();
		final URI uri = HistoryUtils.getHistoryURI(resourceSet.getResources().get(0));
		historyResource = resourceSet.createResource(uri);

		try {
			historyResource.load(null);
			EcoreUtil.resolveAll(historyResource);
			return true;
		} catch (final IOException e) {
			resourceSet.getResources().remove(historyResource);
			return false;
		}
	}

	/** Check whether the history is already recorded for a metamodel. */
	public boolean isRecorded(Resource metamodel) {
		final List<EPackage> rootPackages = ResourceUtils.getRootElements(metamodel,
			EPackage.class);
		final List<EPackage> historyRootPackages = getHistory().getRootPackages();
		for (final EPackage rootPackage : rootPackages) {
			if (historyRootPackages.contains(rootPackage)) {
				return true;
			}
		}
		return false;
	}

	/** Add a metamodel resource to the history. */
	public void addHistory(Resource metamodel) {
		final List<EPackage> rootPackages = ResourceUtils.getRootElements(metamodel,
			EPackage.class);
		getExtent().addRootPackages(rootPackages);

		HistoryUtils.setHistoryURI(metamodel, historyResource.getURI());

		final HistoryGenerator generator = new HistoryGenerator(rootPackages);
		final List<Change> changes = generator.generate().getFirstRelease()
			.getChanges();
		getHistory().getLastRelease().getChanges().addAll(changes);
	}

	/** Create history for a certain metamodel. */
	public void createHistory(List<Resource> metamodelResources) {
		final URI historyURI = HistoryUtils.getDefaultHistoryURI(metamodelResources
			.get(0));
		createHistory(metamodelResources, historyURI);
	}

	/** Create history for a certain metamodel. */
	public void createHistory(List<Resource> metamodelResources, URI historyURI) {
		historyResource = editingDomain.getResourceSet().createResource(
			historyURI);

		final List<EPackage> rootPackages = new ArrayList<EPackage>();
		for (final Resource resource : metamodelResources) {
			HistoryUtils.setHistoryURI(resource, historyURI);
			rootPackages.addAll(ResourceUtils.getRootElements(resource,
				EPackage.class));
		}
		final History history = new HistoryGenerator(rootPackages).generate();
		historyResource.getContents().add(history);
	}

	/** Release a metamodel. */
	public void release() {
		final Release currentRelease = getHistory().getLastRelease();
		if (!currentRelease.getChanges().isEmpty()) {
			currentRelease.setDate(new Date());

			final HistoryFactory factory = HistoryFactory.eINSTANCE;
			final Release version = factory.createRelease();
			getHistory().getReleases().add(version);
		}
	}

	/** Get the history that is listened to. */
	public History getHistory() {
		final History history = (History) historyResource.getContents().get(0);
		return history;
	}

	/** Returns editingDomain. */
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	/** Returns extent. */
	public MetamodelExtent getExtent() {
		return commandStackListener.getExtent();
	}

	/** Add a listener. */
	public void addResourceListener(IResourceLoadListener listener) {
		listeners.add(listener);
	}

	/** Remove a listener. */
	public void removeResourceListener(IResourceLoadListener listener) {
		listeners.remove(listener);
	}

	/** Notify the listeners. */
	private void notifyListeners(Resource resource) {
		for (final IResourceLoadListener listener : listeners) {
			listener.resourceLoaded(resource);
		}
	}
}
