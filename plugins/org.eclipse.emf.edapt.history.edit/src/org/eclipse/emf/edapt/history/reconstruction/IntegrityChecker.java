/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.reconstruction;

import org.eclipse.emf.common.util.URI;
// CB Migrate
// import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
// import org.eclipse.emf.compare.diff.service.DiffService;
// import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
// import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.spi.history.History;

/**
 * Facility to check the integrity of a history. This is performed by
 * reconstructing the metamodel from the history and comparing it with the
 * current version of the metamodel.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class IntegrityChecker {

	/** History to be check. */
	private final History history;

	/**
	 * Difference model between the reconstructed and current version of the
	 * metamodel.
	 */

	// CB Migrate
	// private DiffResourceSet diffResourceSet;

	/** Constructor. */
	public IntegrityChecker(History history) {
		this.history = history;
	}

	/**
	 * Perform integrity check.
	 *
	 * @return true if check succeeds
	 */
	public boolean check() {

		// try {
		final EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
			URI.createFileURI("reconstructed")); //$NON-NLS-1$
		reconstructor.reconstruct(history.getLastRelease(), false);

		@SuppressWarnings("unused")
		final Resource originalResource = HistoryUtils.getRootResource(history
			.eResource().getResourceSet());

		// Migrate, this
		// MatchResourceSet matchResourceSet = MatchService
		// .doResourceSetMatch(originalResource.getResourceSet(),
		// reconstructor.getResourceSet(), Collections
		// .<String, Object> emptyMap());
		// diffResourceSet = DiffService.doDiff(matchResourceSet);
		//
		// IDiffModelFilter filter = DiffModelFilterUtils.and(
		// DiffModelOrderFilter.INSTANCE,
		// DiffModelResourceFilter.INSTANCE);
		// DiffModelFilterUtils.filter(diffResourceSet, filter);
		//
		// return ModelAssert.numberOfChanges(diffResourceSet) == 0;
		// } catch (InterruptedException e) {
		// LoggingUtils.logError(HistoryEditPlugin.getPlugin(),
		// e);
		// return false;
		// }

		// CB Remove later.
		return true;
	}

	/** Returns diffModel. */
	// public DiffResourceSet getDiffModel() {
	// return diffResourceSet;
	// }
}
