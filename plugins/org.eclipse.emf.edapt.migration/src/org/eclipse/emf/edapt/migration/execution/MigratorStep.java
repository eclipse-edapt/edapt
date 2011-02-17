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
package org.eclipse.emf.edapt.migration.execution;

import groovy.lang.Script;

import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.migration.DiagnosticException;
import org.eclipse.emf.edapt.migration.Model;

/**
 * One migration step.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigratorStep {

	/** Parent release migrator. */
	private final ReleaseMigrator releaseMigrator;

	/** Step number. */
	private final int step;

	/** Step URI. */
	private URI stepURI;

	/** Cached Groovy script. */
	private Script script;

	/** Constructor. */
	public MigratorStep(ReleaseMigrator releaseMigrator, int step) {
		this.releaseMigrator = releaseMigrator;
		this.step = step;
	}

	/**
	 * Initialize the step.
	 * 
	 * @return true if the step exists
	 */
	boolean init() {
		stepURI = releaseMigrator.getReleaseURI().appendSegment("step" + step)
				.appendFileExtension("groovy");
		try {
			return ResourceUtils.exists(stepURI);
		} catch (IOException e) {
			return false;
		}
	}

	/** Get the Groovy script. */
	private Script getScript() throws MigrationException {
		if (script == null) {
			try {
				script = GroovyEvaluator.getInstance().parse(
						URIUtils.getURL(stepURI));
			} catch (CompilationFailedException e) {
				throwException("Compile error in migration script", e);
			} catch (IOException e) {
				throwException("Could not load migration script", e);
			}
		}
		return script;
	}

	/** Execute the step. */
	public void execute(Model model, IProgressMonitor monitor)
			throws MigrationException {
		try {
			getScript().run();
		} catch (RuntimeException e) {
			throwException("Runtime error during migration", e);
		}
		monitor.worked(1);
		try {
			model.checkConformance();
		} catch (DiagnosticException e) {
			throwException("Validation error after migration script", e);
		}
	}

	/** Throw a migration exception with the step as location. */
	private void throwException(String message, Throwable e)
			throws MigrationException {
		throw new MigrationException(stepURI, message, e);
	}
}