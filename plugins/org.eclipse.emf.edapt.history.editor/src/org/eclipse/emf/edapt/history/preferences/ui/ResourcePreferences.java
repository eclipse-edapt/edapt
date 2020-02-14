/*******************************************************************************
 * Copyright (c) 2019 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christian W. Damus - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.history.preferences.ui;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.internal.common.FileUtils;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;

/**
 * Manager of preferences for resources by URI.
 */
public final class ResourcePreferences {

	/** Location in the metadata area of the directory containing preference stores. */
	private static final IPath STORE_LOCATION;

	private static final Map<URI, IPersistentPreferenceStore> preferenceStores = new HashMap<URI, IPersistentPreferenceStore>();

	static {
		final IPath stateLocation = HistoryEditorPlugin.getPlugin().getStateLocation();
		STORE_LOCATION = stateLocation.append("resources"); //$NON-NLS-1$
	}

	/**
	 * Not instantiable by clients.
	 */
	private ResourcePreferences() {
		super();
	}

	/**
	 * Get the preference store for the principal resource of an editing domain.
	 * That principal resource is the history resource, if any, otherwise the resource
	 * that is being edited in that domain.
	 *
	 * @param editingDomain an editing domain
	 * @return the preference store for its principal resource
	 */
	public synchronized static IPreferenceStore getPreferences(EditingDomain editingDomain) {
		final ResourceSet rset = editingDomain.getResourceSet();
		// Prefer the history resource, if any, else the resource being edited
		final Resource resource = getHistoryOrMainResource(rset);
		return getPreferences(rset.getURIConverter().normalize(resource.getURI()));
	}

	private static Resource getHistoryOrMainResource(ResourceSet rset) {
		Resource result = HistoryUtils.getHistoryResource(rset);

		if (result == null) {
			result = rset.getResources().get(0);
		}

		return result;
	}

	/**
	 * Get the preference store for the resource identified by the given URI.
	 *
	 * @param uri the reosurce URI
	 * @return the preference store for that resource
	 */
	public synchronized static IPreferenceStore getPreferences(URI uri) {
		IPreferenceStore result = preferenceStores.get(uri);

		if (result == null) {
			result = createPreferenceStore(uri);
		}

		return result;
	}

	private static IPreferenceStore createPreferenceStore(URI uri) {
		final IPath storeLocation;

		try {
			storeLocation = STORE_LOCATION.append(URLEncoder.encode(uri.toString(), "UTF-8") + ".prefs"); //$NON-NLS-1$//$NON-NLS-2$
		} catch (final UnsupportedEncodingException e) {
			throw new Error("UTF-8 encoding not supported"); //$NON-NLS-1$
		}

		final File storeFile = storeLocation.toFile();

		final PreferenceStore result = new PreferenceStore(storeFile.getAbsolutePath());

		if (storeFile.exists()) {
			try {
				result.load();
			} catch (final IOException e) {
				HistoryEditorPlugin.INSTANCE.log(e);
			}
		}

		try {
			register(result, uri);
		} catch (final CoreException e) {
			HistoryEditorPlugin.getPlugin().getLog().log(e.getStatus());
		}

		return result;
	}

	private static void register(IPersistentPreferenceStore store, URI uri) throws CoreException {
		if (preferenceStores.isEmpty()) {
			// Registering the first store.
			installWorkspaceSaveParticipant();
		}

		preferenceStores.put(uri, store);
	}

	private static void installWorkspaceSaveParticipant() throws CoreException {
		ResourcesPlugin.getWorkspace().addSaveParticipant(
			HistoryEditorPlugin.getPlugin().getSymbolicName(),
			new ISaveParticipant() {

				@Override
				public void saving(ISaveContext context) throws CoreException {
					savePreferenceStores();
				}

				@Override
				public void prepareToSave(ISaveContext context) throws CoreException {
					// Not interesting
				}

				@Override
				public void doneSaving(ISaveContext context) {
					// Not interesting
				}

				@Override
				public void rollback(ISaveContext context) {
					// Not interesting
				}
			});
	}

	private static void savePreferenceStores() throws CoreException {
		Map<URI, IPersistentPreferenceStore> toSave;

		synchronized (ResourcePreferences.class) {
			toSave = new HashMap<URI, IPersistentPreferenceStore>(preferenceStores);
		}

		for (final Iterator<Map.Entry<URI, IPersistentPreferenceStore>> iter = toSave.entrySet().iterator(); iter
			.hasNext();) {

			if (!iter.next().getValue().needsSaving()) {
				iter.remove();
			}
		}

		if (!toSave.isEmpty()) {
			final List<URI> failed = new ArrayList<URI>(toSave.size());
			final File storeDirectory = STORE_LOCATION.toFile();
			if (storeDirectory.exists() && !storeDirectory.isDirectory()) {
				throw new CoreException(new Status(IStatus.ERROR, HistoryEditorPlugin.getPlugin().getSymbolicName(),
					String.format("Cannot create preference store. Path exists but is not a directory: %s", //$NON-NLS-1$
						STORE_LOCATION)));
			} else if (!storeDirectory.exists()) {
				storeDirectory.mkdirs();
			}

			for (final Map.Entry<URI, IPersistentPreferenceStore> next : toSave.entrySet()) {
				try {
					next.getValue().save();
				} catch (final IOException e) {
					HistoryEditorPlugin.INSTANCE.log(e);
					failed.add(next.getKey());
				}
			}

			if (!failed.isEmpty()) {
				throw new CoreException(new Status(IStatus.ERROR, HistoryEditorPlugin.getPlugin().getSymbolicName(),
					String.format("Failed to save resource preferences: %s.", failed))); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Unset all resource preferences and delete them from the workspace metadata area.
	 */
	static void deleteAll() {
		synchronized (ResourcePreferences.class) {
			preferenceStores.clear();
		}

		FileUtils.deleteContents(STORE_LOCATION.toFile());
	}
}
