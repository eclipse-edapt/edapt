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

import static org.eclipse.emf.common.notify.Notification.SET;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edapt.spi.history.History;


/**
 * Recorder to store and restore links from the history to the metamodel that
 * have been lost due to {@link ResourceSet}-wide deletes.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class HistoryChangeRecorder {

	/**
	 * History for which changes should be recorded
	 */
	private final History history;

	/**
	 * Adapter listening to the resource contents
	 */
	private final EContentAdapter adapter;

	/**
	 * Whether the recorder is currently recording
	 */
	private boolean recording;

	/**
	 * Links that need to restored in the history.
	 */
	private final List<Link> links = new ArrayList<Link>();

	/**
	 * Constructor
	 */
	public HistoryChangeRecorder(History history) {
		this.history = history;

		adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				switch (notification.getEventType()) {
				case SET:
					handleSet(notification);
					break;
				}
				super.notifyChanged(notification);
			}
		};

		recording = false;

		beginRecording();
	}

	/**
	 * Handle SET notification
	 */
	private void handleSet(Notification notification) {
		if (notification.getFeature() instanceof EReference) {
			EReference reference = (EReference) notification.getFeature();
			if (reference.isContainment()) {
				return;
			}
			EObject element = (EObject) notification.getNotifier();
			EObject oldValue = (EObject) notification.getOldValue();
			if (notification.getNewValue() == null && oldValue != null) {
				if (oldValue.eClass().getEPackage() == EcorePackage.eINSTANCE) {
					Link link = new Link(element, reference, oldValue);
					links.add(link);
				}
			}
		}
	}

	/**
	 * Start the recorder, unless already started
	 */
	public void beginRecording() {
		if (!isRecording()) {
			history.eAdapters().add(adapter);

			recording = true;
		} else {
			throw new IllegalStateException("Recorder is already started");
		}
	}

	/** Returns recording. */
	public boolean isRecording() {
		return recording;
	}

	/**
	 * Stop the recorder, unless already stopped
	 */
	public void endRecording() {
		if (isRecording()) {
			history.eAdapters().remove(adapter);

			for (Link link : links) {
				link.restore();
			}

			recording = false;
		} else {
			throw new IllegalStateException("Recorder is already stopped");
		}
	}

	/**
	 * Link of an element to another one.
	 */
	private class Link {

		/**
		 * Source of the link
		 */
		private final EObject element;

		/**
		 * Reference of the link
		 */
		private final EReference reference;

		/**
		 * Target of the link
		 */
		private final EObject value;

		/**
		 * Constructor
		 */
		public Link(EObject element, EReference reference, EObject value) {
			this.element = element;
			this.reference = reference;
			this.value = value;
		}

		/**
		 * Restore the link
		 */
		public void restore() {
			element.eSet(reference, value);
		}
	}
}
