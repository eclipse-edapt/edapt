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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;


/**
 * Migrator for a release.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReleaseMigrator {

	/** Parent migrator. */
	private final OldMigrator migrator;

	/** Release number. */
	private final int release;

	/** Migration steps. */
	private final List<MigratorStep> steps;

	/** Release URL. */
	private URI releaseURI;

	/** Metamodel URI. */
	private URI metamodelURI;

	/** Cached metamodel. */
	private Metamodel metamodel;

	/** Constructor, */
	public ReleaseMigrator(OldMigrator migrator, int release) {
		this.migrator = migrator;
		this.release = release;
		steps = new ArrayList<MigratorStep>();
	}

	/**
	 * Initialize the release migrator.
	 * 
	 * @return true if the migrator exists
	 */
	boolean init() throws MigrationException {
		releaseURI = migrator.getMigratorURI().appendSegment(
				"release" + (release + 1));
		metamodelURI = releaseURI.appendSegment("release" + release + "."
				+ ResourceUtils.ECORE_FILE_EXTENSION);

		try {
			if (ResourceUtils.exists(metamodelURI)) {
				metamodel = loadMetamodel();
				for (int s = 1; true; s++) {
					MigratorStep step = new MigratorStep(this, s);
					if (step.init()) {
						steps.add(step);
					} else {
						break;
					}
				}
				return true;
			}
		} catch (IOException e) {
			// return false
		}

		return false;
	}

	/**
	 * Get the source metamodel of this release.
	 * 
	 * Note: This metamodel should not be changed, as it is cached.
	 */
	public Metamodel getMetamodel() {
		return metamodel;
	}

	/**
	 * Load the source metamodel of this release.
	 * 
	 * Note: This metamodel can be changed.
	 */
	public Metamodel loadMetamodel() throws MigrationException {
		try {
			Metamodel metamodel = Persistency.loadMetamodel(metamodelURI);
			return metamodel;
		} catch (IOException e) {
			throw new MigrationException(metamodelURI,
					"Metamodel could not be loaded", e);
		}
	}

	/** Execute the migration for this release. */
	public void execute(Model model, IProgressMonitor monitor)
			throws MigrationException {
		monitor.subTask("Release " + release);
		for (MigratorStep step : steps) {
			step.execute(model, monitor);
		}
	}

	/** Return the number of migration steps. */
	public int size() {
		return steps.size();
	}

	/** Get the URI of this release migrator. */
	/* package */URI getReleaseURI() {
		return releaseURI;
	}
}
