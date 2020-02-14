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
package org.eclipse.emf.edapt.history.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.Release;

/**
 * Helper class that checks whether moving a change preserves integrity
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MoveChecker {

	/**
	 * Hidden constructor
	 */
	private MoveChecker() {
		// no implementation
	}

	/**
	 * Checks whether a set of changes can be moved to the end of a target
	 * change or release
	 */
	public static boolean canBeMoved(List<Change> changes, EObject target) {
		try {
			final List<Change> children = getChanges(getChildren(target));
			return canBeMoved(changes, target, children.size());
		} catch (final IllegalStateException ex) {
			return false;
		}
	}

	/**
	 * Checks whether a set of changes can be moved to a certain index within a
	 * target change or release
	 */
	public static boolean canBeMoved(List<Change> changes, EObject target,
		int targetIndex) {
		try {
			if (!allowedTarget(changes, target)) {
				return false;
			}
			for (final Change change : changes) {
				final EObject source = change.eContainer();
				final int sourceIndex = getIndex(change);

				if (source == target && sourceIndex == targetIndex) {
					// nothing has to be done
				} else if (before(source, sourceIndex, target, targetIndex)) {
					final List<Change> difference = getDifference(source,
						sourceIndex + 1, target, targetIndex);
					difference.removeAll(changes);
					if (DependencyChecker.depends(difference, Collections
						.singletonList(change))) {
						return false;
					}
				} else {
					final List<Change> difference = getDifference(target, targetIndex,
						source, sourceIndex - 1);
					difference.removeAll(changes);
					if (DependencyChecker.depends(change, difference)) {
						return false;
					}
				}
			}
			return true;
		} catch (final IllegalStateException ex) {
			return false;
		}
	}

	/**
	 * Checks whether a change or release is a valid target for a set of changes
	 */
	private static boolean allowedTarget(List<Change> changes, EObject target) {
		final EObject box = getBox(target);
		if (box != null) {
			for (final Change change : changes) {
				if (box != getBox(change.eContainer())) {
					return false;
				}
			}
			return true;
		}
		for (final Change change : changes) {
			if (getBox(change.eContainer()) != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the box for a change
	 */
	private static EObject getBox(EObject element) {
		while (element != null) {
			if (element instanceof OperationChange
				|| element instanceof MigrationChange) {
				return element;
			}
			element = element.eContainer();
		}
		return null;
	}

	/**
	 * Get the set of changes that are executed between a source and target
	 * change. The source is denoted by a parent and an index, and the target is
	 * denoted by a parent and an index.
	 */
	private static List<Change> getDifference(EObject source, int sourceIndex,
		EObject target, int targetIndex) {
		final List<Change> difference = new ArrayList<Change>();
		if (source == target) {
			final List elements = getChildren(source);
			targetIndex = Math.min(targetIndex, elements.size() - 1);

			final List<Change> changes = getChanges(elements.subList(sourceIndex,
				targetIndex + 1));
			difference.addAll(changes);
		} else if (contains(source, target)) {
			final List elements = getChildren(target);
			targetIndex = Math.min(targetIndex, elements.size() - 1);

			final List<Change> changes = getChanges(elements.subList(0, targetIndex + 1));
			difference.addAll(changes);
			difference.addAll(getDifference(source, sourceIndex, target
				.eContainer(), getIndex(target)));
		} else {
			final List elements = getChildren(source);

			final List<Change> changes = getChanges(elements.subList(sourceIndex,
				elements.size()));
			difference.addAll(changes);
			difference.addAll(getDifference(source.eContainer(),
				source instanceof Delete ? getIndex(source)
					: getIndex(source) + 1,
				target, targetIndex));
		}
		return difference;
	}

	/**
	 * Extracts the changes from a list of elements
	 */
	private static List<Change> getChanges(List elements) {
		final List<Change> changes = new ArrayList<Change>();
		for (final Object element : elements) {
			if (element instanceof Change) {
				changes.add((Change) element);
			} else if (element instanceof Release) {
				final Release release = (Release) element;
				changes.addAll(release.getChanges());
			}
		}
		return changes;
	}

	/**
	 * Get the children of an element
	 */
	private static List getChildren(EObject element) {
		final EReference reference = getChangeReference(element);
		final List elements = (List) element.eGet(reference);
		return elements;
	}

	/**
	 * Get the index of an element within its container element
	 */
	private static int getIndex(EObject element) {
		if (element == null || element.eContainer() == null || element.eContainmentFeature() == null) {
			throw new IllegalStateException();
		}
		return ((List) element.eContainer().eGet(element.eContainmentFeature()))
			.indexOf(element);
	}

	/**
	 * Get the reference to obtain changes from an element
	 */
	private static EReference getChangeReference(EObject element) {
		if (element instanceof History) {
			return HistoryPackage.eINSTANCE.getHistory_Releases();
		}
		for (final EReference reference : element.eClass().getEAllContainments()) {
			if (HistoryPackage.eINSTANCE.getChange().isSuperTypeOf(
				reference.getEReferenceType())) {
				return reference;
			}
		}
		return null;
	}

	/**
	 * Checks whether a source change happened before a target change. The
	 * source is denoted by a parent and an index, and the target is denoted by
	 * a parent and an index.
	 */
	private static boolean before(EObject source, int sourceIndex,
		EObject target, int targetIndex) {
		if (source == target) {
			return sourceIndex < targetIndex;
		} else if (contains(source, target)) {
			return before(source, sourceIndex, target.eContainer(),
				getIndex(target));
		} else {
			return before(source.eContainer(), getIndex(source), target,
				targetIndex);
		}
	}

	/**
	 * Checks whether an element (transitively) contains another element
	 */
	private static boolean contains(EObject parent, EObject child) {
		while (child != null) {
			if (child.eContainer() == parent) {
				return true;
			}
			child = child.eContainer();
		}
		return false;
	}
}