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
package org.eclipse.emf.edapt.history.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.ContentChange;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.InitializerChange;
import org.eclipse.emf.edapt.history.MigrateableChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.NonDelete;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.Set;
import org.eclipse.emf.edapt.history.ValueChange;


/**
 * Helper class to check whether a change depends on another change
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class DependencyChecker {

	/** Hidden constructor. */
	private DependencyChecker() {
		// no implementation
	}

	/**
	 * Check whether a list of changes (source) depends on another list of
	 * changes (target).
	 */
	public static boolean depends(List<Change> sourceChanges,
			List<Change> targetChanges) {
		for (Change sourceChange : sourceChanges) {
			if (depends(sourceChange, targetChanges)) {
				return true;
			}
		}
		return false;
	}

	/** Check whether a change (source) depends on a list of changes (target). */
	public static boolean depends(Change sourceChange,
			List<Change> targetChanges) {
		for (Change targetChange : targetChanges) {
			if (depends(sourceChange, targetChange)) {
				return true;
			}
		}
		return false;
	}

	/** Check whether a change (source) depends on another change (target). */
	public static boolean depends(Change source, Change target) {
		List<PrimitiveChange> sourceChanges = getPrimitiveChanges(source);
		List<PrimitiveChange> targetChanges = getPrimitiveChanges(target);

		for (PrimitiveChange sourceChange : sourceChanges) {
			for (PrimitiveChange targetChange : targetChanges) {
				if (depends(sourceChange, targetChange)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Check whether a primitive change depends on another primitive change. A
	 * primitive change depends on another one if the first requires the second
	 * or they conflict with each other.
	 */
	public static boolean depends(PrimitiveChange source, PrimitiveChange target) {
		return requires(source, target) || conflicts(source, target);
	}

	/** Check whether a primitive change requires another primitive change. */
	private static boolean requires(PrimitiveChange source,
			PrimitiveChange target) {
		if (source instanceof Delete) {
			return requires((Delete) source, target);
		} else if (target instanceof Create) {
			return requires(source, (Create) target);
		}
		return false;
	}

	/**
	 * Check whether a primitive change requires the creation of an element. A
	 * primitive change requires the creation of an element if it refers to that
	 * element.
	 */
	private static boolean requires(PrimitiveChange source, Create target) {
		java.util.Set<EObject> elements = getElements(source);
		for (EObject element : elements) {
			if (element == target.getElement()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the deletion of an element requires a primitive change. The
	 * deletion of an element requires a primitive change if it deletes elements
	 * to which it refers.
	 */
	private static boolean requires(Delete source, PrimitiveChange target) {
		java.util.Set<EObject> elements = getElements(target);
		for (EObject element : elements) {
			if (isDeleted(source, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether a primitive change conflicts with another primitive change.
	 */
	private static boolean conflicts(PrimitiveChange source,
			PrimitiveChange target) {
		if (source instanceof ValueChange && target instanceof ValueChange) {
			return conflicts((ValueChange) source, (ValueChange) target);
		}
		if(source instanceof Move && target instanceof Move) {
			return conflicts((Move) source, (Move) target);
		}
		return false;
	}

	/**
	 * Check whether a value change conflicts with another value change. A value
	 * change may conflict with another value change if they both change the
	 * same feature of the same element. In case the feature is single-valued,
	 * there is always a conflict. In case the feature is multi-valued, there is
	 * only a conflict if the same value is added or removed.
	 */
	private static boolean conflicts(ValueChange source, ValueChange target) {
		if (source.getElement() == target.getElement()
				&& source.getFeature() == target.getFeature()) {
			EStructuralFeature feature = source.getFeature();
			if (feature.isMany()) {
				if (source.getValue() == target.getValue()) {
					return true;
				}
			} else {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check whether a move conflicts with another move. A move conflicts with
	 * another move if they move the same element within the same reference.
	 */
	private static boolean conflicts(Move source, Move target) {
		if (source.getElement() == target.getElement()
				&& source.getReference() == target.getReference()) {
			return true;
		}
		return false;
	}

	/** Get the primitive changes contained in a change. */
	private static List<PrimitiveChange> getPrimitiveChanges(Change change) {
		List<PrimitiveChange> changes = new ArrayList<PrimitiveChange>();
		if (change instanceof PrimitiveChange) {
			changes.add((PrimitiveChange) change);
			if (change instanceof InitializerChange) {
				InitializerChange initializerChange = (InitializerChange) change;
				changes.addAll(initializerChange.getChanges());
			}
		} else if (change instanceof CompositeChange) {
			CompositeChange compositeChange = (CompositeChange) change;
			changes.addAll(compositeChange.getChanges());
		} else if (change instanceof MigrationChange) {
			MigrationChange migrationChange = (MigrationChange) change;
			for (MigrateableChange migrateableChange : migrationChange
					.getChanges()) {
				changes.addAll(getPrimitiveChanges(migrateableChange));
			}
		}
		return changes;
	}

	/** Check whether an element was deleted by a delete. */
	private static boolean isDeleted(Delete delete, EObject element) {
		if (delete.getElement() == element) {
			return true;
		}
		for (Iterator<EObject> i = delete.getElement().eAllContents(); i
				.hasNext();) {
			if (i.next() == element) {
				return true;
			}
		}
		return false;
	}

	/** Get the elements that a {@link PrimitiveChange} uses. */
	private static java.util.Set<EObject> getElements(PrimitiveChange change) {
		java.util.Set<EObject> elements = new HashSet<EObject>();
		if (change instanceof ValueChange) {
			ValueChange valueChange = (ValueChange) change;
			elements.add(valueChange.getElement());
			if (valueChange.getFeature() instanceof EReference) {
				if (valueChange.getReferenceValue() != null) {
					elements.add(valueChange.getReferenceValue());
				}
				if (valueChange instanceof Set) {
					Set set = (Set) valueChange;
					if (set.getOldReferenceValue() != null) {
						elements.add(set.getOldReferenceValue());
					}
				}
			}
		} else if (change instanceof ContentChange) {
			ContentChange contentChange = (ContentChange) change;
			if (contentChange.getTarget() != null) {
				elements.add(contentChange.getTarget());
			}
			if (contentChange instanceof NonDelete) {
				NonDelete nonDelete = (NonDelete) contentChange;
				elements.add(nonDelete.getElement());
				if (nonDelete instanceof Move) {
					Move move = (Move) nonDelete;
					elements.add(move.getSource());
				}
			} else if (contentChange instanceof Delete) {
				Delete delete = (Delete) contentChange;
				elements.add(delete.getElement());
				for (Iterator<EObject> i = delete.getElement().eAllContents(); i
						.hasNext();) {
					elements.add(i.next());
				}
			}
		}
		return elements;
	}
}