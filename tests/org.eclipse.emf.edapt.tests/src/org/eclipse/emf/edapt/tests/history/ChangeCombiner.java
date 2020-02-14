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
package org.eclipse.emf.edapt.tests.history;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.Release;

/**
 * Helper class to detect the changes that were added to a release
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ChangeCombiner<C extends Change> extends AdapterImpl {

	/**
	 * Added changes
	 */
	private List<C> changes;

	/**
	 * Release to which we listen
	 */
	private final Release release;

	/**
	 * Constructor
	 */
	public ChangeCombiner(Release release) {
		this.release = release;
		start();
	}

	/**
	 * Start listening to release
	 */
	private void start() {
		release.eAdapters().add(this);
		changes = new ArrayList<C>();
	}

	/**
	 * Stop listening to release
	 */
	public List<C> stop() {
		release.eAdapters().remove(this);
		return changes;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void notifyChanged(Notification msg) {
		if (msg.getFeature() == HistoryPackage.eINSTANCE.getRelease_Changes()) {
			if (msg.getEventType() == Notification.ADD) {
				changes.add((C) msg.getNewValue());
			} else if (msg.getEventType() == Notification.ADD_MANY) {
				changes.addAll((List) msg.getNewValue());
			} else if (msg.getEventType() == Notification.REMOVE) {
				changes.remove(msg.getOldValue());
			} else if (msg.getEventType() == Notification.REMOVE_MANY) {
				changes.removeAll((List) msg.getOldValue());
			}
		}
	}
}
