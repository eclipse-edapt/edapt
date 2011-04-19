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
package org.eclipse.emf.edapt.history.reconstruction;

import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.MigrateableChange;
import org.eclipse.emf.edapt.history.MigrationChange;

/**
 * Reconstructor for the metamodel adaptation code of a certain {@link MigrationChange}.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigrationChangeReconstructor extends ReconstructorBase {
	
	/** Source change */
	private final MigrateableChange sourceChange;
	
	/** Target change */
	private final MigrateableChange targetChange;
	
	/** Whether the metamodel is consistent before or after the changes. */
	private boolean consistent = true;

	/** The extent of the reconstructed metamodel. */
	private MetamodelExtent extent;
	
	/** Constructor */
	public MigrationChangeReconstructor(MigrateableChange sourceChange, MigrateableChange targetChange) {
		this.sourceChange = sourceChange;
		this.targetChange = targetChange;
	}
	
	/** {@inheritDoc} */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		this.extent = extent;
	}

	/** {@inheritDoc} */
	@Override
	public void startChange(Change originalChange) {
		if(originalChange == sourceChange) {
			if(!extent.isConsistent()) {
				consistent = false;
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endChange(Change originalChange) {
		if(originalChange == targetChange) {
			if(!extent.isConsistent()) {
				consistent = false;
			}
		}
	}

	/** Returns consistentBefore. */
	public boolean isConsistent() {
		return consistent;
	}
}
