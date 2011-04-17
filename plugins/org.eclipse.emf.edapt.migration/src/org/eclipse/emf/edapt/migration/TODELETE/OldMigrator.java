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
package org.eclipse.emf.edapt.migration.TODELETE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.MigrationFactory;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.Persistency;
import org.eclipse.emf.edapt.migration.ReleaseUtil;


/**
 * Migrator to migrate a model from a previous to the current release.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OldMigrator {

	/** Path of migrator. */
	private final URI migratorURI;

	/** Mapping of namespace URIs to releases. */
	private Map<String, Set<Integer>> releaseMap;

	/** Migrators for each release. */
	private List<ReleaseMigrator> migrators;

	/** Constructor. */
	public OldMigrator(URI migratorURI) throws MigrationException {
		this.migratorURI = migratorURI;

		init();
	}

	/** Initialize release map from migrator. */
	private void init() throws MigrationException {
		releaseMap = new HashMap<String, Set<Integer>>();

		migrators = new ArrayList<ReleaseMigrator>();
		for (int release = 0; true; release++) {
			ReleaseMigrator releaseMigrator = new ReleaseMigrator(this, release);
			if (releaseMigrator.init()) {
				for (EPackage element : releaseMigrator.getMetamodel()
						.getEPackages()) {
					registerRelease(element, release);
				}
				migrators.add(releaseMigrator);

			} else {
				break;
			}
		}
	}

	/** Register a package for a certain release. */
	private void registerRelease(EPackage ePackage, int release) {
		String nsURI = ePackage.getNsURI();
		Set<Integer> releases = releaseMap.get(nsURI);
		if (releases == null) {
			releases = new HashSet<Integer>();
			releaseMap.put(nsURI, releases);
		}
		releases.add(release);
		for (EPackage subPackage : ePackage.getESubpackages()) {
			registerRelease(subPackage, release);
		}
	}

	/** Returns the length of the migration in terms of the steps. */
	private int numberOfSteps(int sourceRelease, int targetRelease) {
		int size = 0;
		for (int i = sourceRelease; i < targetRelease; i++) {
			size += migrators.get(i).size();
		}
		return size;
	}

	/** Get set of namespace URIs. */
	public Set<String> getNsURIs() {
		return releaseMap.keySet();
	}

	/**
	 * Migrate a model based on a set of {@link URI}
	 * 
	 * @param modelURIs
	 * @param sourceRelease
	 *            Release to which the model conforms (0 is the first release)
	 * @param targetRelease
	 *            Release to which the model should be migrated (use
	 *            Integer.MAX_VALUE for the newest release)
	 * @param monitor
	 *            Progress monitor
	 */
	public void migrate(List<URI> modelURIs, int sourceRelease,
			int targetRelease, IProgressMonitor monitor)
			throws MigrationException {

		targetRelease = Math.min(targetRelease, migrators.size());
		if (sourceRelease >= targetRelease) {
			return;
		}

		try {
			monitor.beginTask("Migrate", numberOfSteps(sourceRelease,
					targetRelease));

			Model model = loadModel(modelURIs, sourceRelease);
			GroovyEvaluator.getInstance().setModel(model);
			model.checkConformance();

			performMigration(model, sourceRelease, targetRelease, monitor);

			Persistency.saveModel(model);
		} catch (IOException e) {
			throw new MigrationException("Model could not be saved", e);
		} finally {
			GroovyEvaluator.getInstance().unsetModel();
			monitor.done();
		}
	}

	/** Load the model from a URI using a metamodel of a certain release. */
	private Model loadModel(List<URI> modelURIs, int sourceRelease)
			throws MigrationException {
		try {
			Metamodel metamodel = migrators.get(sourceRelease).loadMetamodel();
			return Persistency.loadModel(modelURIs, metamodel);
		} catch (IOException e) {
			throw new MigrationException("Model could not be loaded", e);
		}
	}

	/** Adapt a metamodel. */
	public void adapt(URI metamodelURI, int sourceRelease, int targetRelease,
			IProgressMonitor monitor) throws MigrationException {
		targetRelease = Math.min(targetRelease, migrators.size());
		if (sourceRelease >= targetRelease) {
			return;
		}

		try {
			monitor.beginTask("Adapt", numberOfSteps(0, targetRelease));

			Metamodel metamodel = loadMetamodel(metamodelURI);
			Model model = MigrationFactory.eINSTANCE.createModel();
			model.setMetamodel(metamodel);
			GroovyEvaluator.getInstance().setModel(model);

			performMigration(model, sourceRelease, targetRelease, monitor);

			Persistency.saveMetamodel(metamodel);
		} catch (IOException e) {
			throw new MigrationException(metamodelURI,
					"Metamodel could not be saved", e);
		} catch (MigrationException e) {
			throw e;
		} finally {
			GroovyEvaluator.getInstance().unsetModel();
		}
	}

	/** Load the metamodel from a URI. */
	private Metamodel loadMetamodel(URI metamodelURI) throws MigrationException {
		Metamodel metamodel;
		try {
			metamodel = Persistency.loadMetamodel(metamodelURI);
		} catch (IOException e) {
			throw new MigrationException(metamodelURI,
					"Metamodel could not be loaded", e);
		}
		return metamodel;
	}

	/** Apply the migrator to a model. */
	private void performMigration(Model model, int sourceRelease,
			int targetRelease, IProgressMonitor monitor)
			throws MigrationException {
		for (int i = sourceRelease; i < targetRelease; i++) {
			migrators.get(i).execute(model, monitor);
		}
	}

	/** Get the release of a model based on {@link URI}. */
	public Set<Integer> getRelease(URI modelURI) {
		String nsURI = ReleaseUtil.getNamespaceURI(modelURI);
		return releaseMap.get(nsURI);
	}

	/**
	 * Returns the metamodel for a release.
	 * 
	 * Note: This metamodel should not be changed, as it is cached.
	 */
	public Metamodel getMetamodel(int release) {
		if (release < migrators.size()) {
			Metamodel metamodel = migrators.get(release).getMetamodel();
			return metamodel;
		}
		return null;
	}

	/** Get the URI of this migrator. */
	/* package */URI getMigratorURI() {
		return migratorURI;
	}
}