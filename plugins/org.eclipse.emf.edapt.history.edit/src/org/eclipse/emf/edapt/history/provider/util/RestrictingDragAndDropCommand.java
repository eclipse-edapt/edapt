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
package org.eclipse.emf.edapt.history.provider.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.history.util.MoveChecker;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Special {@link DragAndDropCommand} that forbids moves of changes
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class RestrictingDragAndDropCommand extends DragAndDropCommand {

	/**
	 * Constructor
	 */
	public RestrictingDragAndDropCommand(EditingDomain domain, Object owner,
		float location, int operations, int operation,
		Collection<?> collection) {
		super(domain, owner, location, operations, operation, collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean prepareDropMoveOn() {
		if (!super.prepareDropMoveOn()) {
			return false;
		}
		final List<Change> changes = getChanges();
		final EObject target = (EObject) owner;
		if (changes.contains(owner)) {
			return false;
		}
		final boolean canBeMoved = MoveChecker.canBeMoved(changes, target);
		return canBeMoved;
	}

	/**
	 * Get the changes to be moved
	 */
	private List<Change> getChanges() {
		final List<Change> changes = new ArrayList<Change>();
		for (final Object element : collection) {
			if (element instanceof Change) {
				changes.add((Change) element);
			} else {
				return Collections.emptyList();
			}
		}
		return changes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean prepareDropCopyOn() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean prepareDropMoveInsert(Object parent,
		Collection<?> children, int index) {
		if (!super.prepareDropMoveInsert(parent, children, index)) {
			return false;
		}
		final List<Change> changes = getChanges();
		final EObject target = (EObject) parent;
		final boolean canBeMoved = MoveChecker.canBeMoved(changes, target, index);
		return canBeMoved;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean prepareDropCopyInsert(Object parent,
		Collection<?> children, int index) {
		return false;
	}
}
