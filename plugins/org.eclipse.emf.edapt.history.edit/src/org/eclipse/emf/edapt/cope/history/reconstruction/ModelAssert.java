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
package org.eclipse.emf.edapt.cope.history.reconstruction;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import junit.framework.AssertionFailedError;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edapt.cope.common.ResourceUtils;


/**
 * Assertions for dealing with models.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ModelAssert {

	/** Assert that two models are equal. */
	public static void assertEquals(EObject expected, EObject actual,
			String message) {
		DiffModel diff = diff(expected, actual);
		boolean empty = numberOfChanges(diff) == 0;
		if (!empty) {
			if (message == null) {
				message = "models are not equal";
			}
			saveDiffModel(expected, diff);
			throw new AssertionFailedError(message);
		}
	}

	/** Save the difference model. */
	private static void saveDiffModel(EObject expected, DiffModel diff) {
		try {
			URI expectedURI = expected.eResource().getURI();
			String name = expectedURI.trimFileExtension().lastSegment()
					+ "_diff.xmi";
			URI uri = expectedURI.trimSegments(1).appendFragment(name);
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.createResource(uri);
			resource.getContents().add(diff);
			ResourceUtils.saveResourceSet(resourceSet);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/** Assert that two models are equal. */
	public static void assertEquals(EObject expected, EObject actual) {
		assertEquals(expected, actual, null);
	}

	/**
	 * Assert that there are only a certain number of changes between two
	 * models.
	 */
	public static void assertDifference(EObject expected, EObject actual,
			int expectedNumber, String message) {
		DiffModel diff = diff(expected, actual);
		int actualNumber = numberOfChanges(diff);
		boolean same = actualNumber == expectedNumber;
		if (!same) {
			if (message == null) {
				message = expectedNumber + " differences expected, but "
						+ actualNumber + " encountered";
			}
			saveDiffModel(expected, diff);
			throw new AssertionFailedError(message);
		}
	}

	/**
	 * Assert that there are only a certain number of changes between two
	 * models.
	 */
	public static void assertDifference(EObject expected, EObject actual,
			int expectedNumber) {
		assertDifference(expected, actual, expectedNumber, null);
	}

	/** Calculate the difference between two models. */
	private static DiffModel diff(EObject expected, EObject actual) {
		try {
			MatchModel match = MatchService.doMatch(expected, actual,
					Collections.<String, Object> emptyMap());
			// Computing differences
			DiffModel diff = DiffService.doDiff(match, false);
			// Filter differences
			IDiffModelFilter filter = DiffModelFilterUtils.and(
					DiffModelOrderFilter.INSTANCE,
					DiffModelResourceFilter.INSTANCE);
			DiffModelFilterUtils.filter(diff, filter);
			return diff;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/** Determine the number of changes denoted by a difference model. */
	public static int numberOfChanges(DiffModel diff) {
		int number = 0;
		for (Iterator<EObject> i = diff.eAllContents(); i.hasNext();) {
			EObject element = i.next();
			if (element instanceof DiffElement
					&& !(element instanceof DiffGroup)) {
				number++;
			}
		}
		return number;
	}

	/** Determine the number of changes denoted by a difference model. */
	public static int numberOfChanges(DiffResourceSet diff) {
		int number = 0;
		for (Iterator<EObject> i = diff.eAllContents(); i.hasNext();) {
			EObject element = i.next();
			if (element instanceof DiffElement
					&& !(element instanceof DiffGroup)) {
				number++;
			}
		}
		return number;
	}
}
