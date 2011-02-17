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
package org.eclipse.emf.edapt.history.provider.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.util.MoveChecker;
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
		List<Change> changes = getChanges();
		EObject target = (EObject) owner;
		if (changes.contains(owner)) {
			return false;
		}
		boolean canBeMoved = MoveChecker.canBeMoved(changes, target);
		return canBeMoved;
	}

	/**
	 * Get the changes to be moved
	 */
	private List<Change> getChanges() {
		List<Change> changes = new ArrayList<Change>();
		for (Object element : collection) {
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
		List<Change> changes = getChanges();
		EObject target = (EObject) parent;
		boolean canBeMoved = MoveChecker.canBeMoved(changes, target, index);
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
