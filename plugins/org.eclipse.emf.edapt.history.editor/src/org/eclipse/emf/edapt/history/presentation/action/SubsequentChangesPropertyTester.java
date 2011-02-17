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
package org.eclipse.emf.edapt.history.presentation.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.jface.viewers.ISelection;


/**
 * Property tester to check whether selected changes are subsequent.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class SubsequentChangesPropertyTester extends PropertyTester {

	/** {@inheritDoc} */
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		ISelection selection = (ISelection) receiver;
		List<Change> changes = SelectionUtils.getSelectedElements(selection);

		return isValid(changes);
	}

	/**
	 * Determine whether the selection is valid, i.e. the changes are subsequent
	 * and contained by a single release
	 * 
	 * @return true if the changes are valid
	 */
	private boolean isValid(List<Change> changes) {
		if (changes.isEmpty()) {
			return false;
		}
		final Release release = sort(changes);
		if (release == null) {
			return false;
		}
		int firstIndex = release.getChanges().indexOf(changes.get(0));
		int lastIndex = release.getChanges().indexOf(
				changes.get(changes.size() - 1));
		boolean valid = (lastIndex - firstIndex + 1) == changes.size();
		return valid;
	}

	/** Sort the changes chronologically and return their common parent release. */
	public static Release sort(List<? extends Change> changes) {
		final Release release = getRelease(changes);
		if (release == null) {
			return null;
		}
		Collections.sort(changes, new Comparator<Change>() {

			public int compare(Change c1, Change c2) {
				int i1 = release.getChanges().indexOf(c1);
				int i2 = release.getChanges().indexOf(c2);
				return new Integer(i1).compareTo(i2);
			}

		});
		return release;
	}

	/** Get the release by which the changes are contained. */
	public static Release getRelease(List<? extends Change> changes) {
		Change first = changes.get(0);
		if (!(first.eContainer() instanceof Release)) {
			return null;
		}
		Release release = (Release) first.eContainer();
		for (Change change : changes) {
			if (!(change.eContainer() == release)) {
				return null;
			}
		}
		return release;
	}
}
