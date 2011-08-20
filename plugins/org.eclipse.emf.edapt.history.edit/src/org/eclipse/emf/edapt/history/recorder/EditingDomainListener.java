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
package org.eclipse.emf.edapt.history.recorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Listener for an {@link EcoreEditor}
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

			listening = true;
		} else {
			throw new IllegalStateException("Listener already activated");
		}
	}

	/** Stop the listener. */
	public void endListening() {
		if (isListening()) {
			commandStackListener.endListening();

			listening = false;
		} else {
			throw new IllegalStateException("Listener already deactivated");
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
		ResourceSet resourceSet = editingDomain.getResourceSet();
		URI uri = HistoryUtils.getHistoryURI(resourceSet.getResources().get(0));
		historyResource = resourceSet.createResource(uri);

		try {
			historyResource.load(null);
			EcoreUtil.resolveAll(historyResource);
			return true;
		} catch (IOException e) {
			resourceSet.getResources().remove(historyResource);
			return false;
		}
	}

	/** Check whether the history is already recorded for a metamodel. */
	public boolean isRecorded(Resource metamodel) {
		List<EPackage> rootPackages = ResourceUtils.getRootElements(metamodel,
				EPackage.class);
		List<EPackage> historyRootPackages = getHistory().getRootPackages();
		for (EPackage rootPackage : rootPackages) {
			if (historyRootPackages.contains(rootPackage)) {
				return true;
			}
		}
		return false;
	}

	/** Add a metamodel resource to the history. */
	public void addHistory(Resource metamodel) {
		List<EPackage> rootPackages = ResourceUtils.getRootElements(metamodel,
				EPackage.class);
		getExtent().addRootPackages(rootPackages);

		HistoryUtils.setHistoryURI(metamodel, historyResource.getURI());

		HistoryGenerator generator = new HistoryGenerator(rootPackages);
		List<Change> changes = generator.generate().getFirstRelease()
				.getChanges();
		getHistory().getLastRelease().getChanges().addAll(changes);
	}

	/** Create history for a certain metamodel. */
	public void createHistory(List<Resource> metamodelResources) {
		URI historyURI = HistoryUtils.getDefaultHistoryURI(metamodelResources
				.get(0));
		createHistory(metamodelResources, historyURI);
	}

	/** Create history for a certain metamodel. */
	public void createHistory(List<Resource> metamodelResources, URI historyURI) {
		historyResource = editingDomain.getResourceSet().createResource(
				historyURI);

		List<EPackage> rootPackages = new ArrayList<EPackage>();
		for (Resource resource : metamodelResources) {
			HistoryUtils.setHistoryURI(resource, historyURI);
			rootPackages.addAll(ResourceUtils.getRootElements(resource,
					EPackage.class));
		}
		History history = new HistoryGenerator(rootPackages).generate();
		historyResource.getContents().add(history);
	}

	/** Release a metamodel. */
	public void release() {
		Release currentRelease = getHistory().getLastRelease();
		if (!currentRelease.getChanges().isEmpty()) {
			currentRelease.setDate(new Date());

			HistoryFactory factory = HistoryFactory.eINSTANCE;
			Release version = factory.createRelease();
			getHistory().getReleases().add(version);
		}
	}

	/** Get the history that is listened to. */
	public History getHistory() {
		History history = (History) historyResource.getContents().get(0);
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
}
