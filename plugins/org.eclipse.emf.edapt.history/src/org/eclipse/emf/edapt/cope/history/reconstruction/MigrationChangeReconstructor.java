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

import org.eclipse.emf.edapt.cope.history.Change;
import org.eclipse.emf.edapt.cope.history.Create;
import org.eclipse.emf.edapt.cope.history.InitializerChange;
import org.eclipse.emf.edapt.cope.history.MigrateableChange;
import org.eclipse.emf.edapt.cope.history.MigrationChange;
import org.eclipse.emf.edapt.cope.history.OperationChange;

/**
 * Reconstructor for the metamodel adaptation code of a certain {@link MigrationChange}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigrationChangeReconstructor extends AdaptationCodeReconstructorBase {
	
	/**
	 * Source change
	 */
	private MigrateableChange sourceChange;
	
	/**
	 * Target change
	 */
	private MigrateableChange targetChange;
	
	/**
	 * Whether generation is enabled
	 */
	private boolean enabled = false;
	
	/**
	 * Whether generation is started
	 */
	private boolean started = false;
	
	/**
	 * Trigger to restart generation
	 */
	private Change trigger = null;

	/**
	 * Whether the metamodel is consistent before or after the changes
	 */
	private boolean consistent = true;
	
	/**
	 * Constructor
	 * 
	 * @param sourceChange
	 * @param targetChange
	 */
	public MigrationChangeReconstructor(MigrateableChange sourceChange, MigrateableChange targetChange) {
		this.sourceChange = sourceChange;
		this.targetChange = targetChange;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startChange(Change originalChange) {
		checkEnabled(originalChange);
		if(isEnabled()) {
			if(isStarted() && !(originalChange instanceof Create)) {
				adaptationSwitch.doSwitch(originalChange);
			}
			checkPause(originalChange);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endChange(Change originalChange) {
		if(isEnabled()) {
			checkResume(originalChange);
			if(isStarted() && (originalChange instanceof Create)) {
				adaptationSwitch.doSwitch(originalChange);
			}
		}
		checkDisabled(originalChange);
	}

	/**
	 * Check whether to pause migration generator
	 * 
	 * @param originalChange
	 */
	private void checkResume(Change originalChange) {
		if(originalChange == trigger) {
			started = true;
			trigger = null;
		}
	}

	/**
	 * Check whether to resume migration generator
	 * 
	 * @param originalChange
	 */
	private void checkPause(Change originalChange) {
		if(trigger != null) {
			return;
		}
		if(originalChange instanceof OperationChange || originalChange instanceof InitializerChange) {
			started = false;
			trigger = originalChange;
		}
	}

	/**
	 * Whether generation is started
	 * 
	 * @return true if generation is started, false otherwise
	 */
	private boolean isStarted() {
		return started;
	}

	/**
	 * Check whether to activate migration generator
	 * 
	 * @param originalChange
	 */
	private void checkEnabled(Change originalChange) {
		if(originalChange == sourceChange) {
			enabled = true;
			if(!extent.isConsistent()) {
				consistent = false;
			}
			started = true;
		}
	}
	
	/**
	 * Check whether to deactivate migration generator
	 * 
	 * @param originalChange
	 */
	private void checkDisabled(Change originalChange) {
		if(originalChange == targetChange) {
			enabled = false;
			if(!extent.isConsistent()) {
				consistent = false;
			}
			started = false;
		}
	}

	/**
	 * Whether generation is enabled
	 * 
	 * @return true if generation is enabled, false otherwise
	 */
	private boolean isEnabled() {
		return enabled;
	}

	/**
	 * Get code
	 * 
	 * @return Code
	 */
	public String getCode() {
		return code.toString();
	}

	/** Returns consistentBefore. */
	public boolean isConsistent() {
		return consistent;
	}
}
