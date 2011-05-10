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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.declaration.OperationRegistry;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.migration.BackupUtils;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Persistency;
import org.eclipse.emf.edapt.migration.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.migration.ReleaseUtils;

/**
 * Migrator to migrate a model from a previous to the current release.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: B6F49196D15E37A963EB83E9543D5770
 */
public class Migrator {

	/** Metamodel history no which this migrator is based. */
	private History history;

	/** Mapping of namespace URIs to releases. */
	private HashMap<String, Set<Release>> releaseMap;

	/** Classloader to load {@link CustomMigration}s. */
	private final IClassLoader classLoader;

	/** Validation level. */
	private ValidationLevel level = ValidationLevel.CUSTOM_MIGRATION;

	/** Constructor. */
	public Migrator(URI historyURI, IClassLoader classLoader)
			throws MigrationException {
		HistoryPackage.eINSTANCE.getHistory();
		try {
			history = ResourceUtils.loadElement(historyURI);
		} catch (IOException e) {
			throw new MigrationException("History could not be loaded", e);
		}
		this.classLoader = classLoader;
		init();
	}

	/** Constructor. */
	public Migrator(History history, IClassLoader classLoader) {
		this.history = history;
		this.classLoader = classLoader;
		init();
	}

	/** Initialize release map for the migrator. */
	private void init() {
		releaseMap = new HashMap<String, Set<Release>>();
		Map<EPackage, String> packageMap = new HashMap<EPackage, String>();

		for (Release release : history.getReleases()) {
			if (!release.isLastRelease()) {
				updatePackages(release, packageMap);
				registerRelease(release, packageMap);
			}
		}
	}

	/** Register a package for a certain release. */
	private void registerRelease(Release release,
			Map<EPackage, String> packageMap) {
		for (Entry<EPackage, String> entry : packageMap.entrySet()) {
			String nsURI = entry.getValue();
			Set<Release> releases = releaseMap.get(nsURI);
			if (releases == null) {
				releases = new HashSet<Release>();
				releaseMap.put(nsURI, releases);
			}
			releases.add(release);
		}
	}

	/** Update the namespace URIs based on the changes during a release. */
	private void updatePackages(Release release,
			Map<EPackage, String> packageMap) {
		for (Iterator<EObject> i = release.eAllContents(); i.hasNext();) {
			EObject element = i.next();
			if (element instanceof org.eclipse.emf.edapt.history.Set) {
				org.eclipse.emf.edapt.history.Set set = (org.eclipse.emf.edapt.history.Set) element;
				if (set.getFeature() == EcorePackage.eINSTANCE
						.getEPackage_NsURI()) {
					EPackage ePackage = (EPackage) set.getElement();
					String nsURI = (String) set.getValue();
					packageMap.put(ePackage, nsURI);
				}
			} else if (element instanceof Delete) {
				Delete delete = (Delete) element;
				packageMap.remove(delete.getElement());
			}
		}
	}

	/**
	 * Migrate a model based on a set of {@link URI}.
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
	public void migrate(List<URI> modelURIs, Release sourceRelease,
			Release targetRelease, IProgressMonitor monitor)
			throws MigrationException {

		if (targetRelease == null) {
			targetRelease = getLatestRelease();
		}
		if (sourceRelease == targetRelease) {
			return;
		}

		try {
			monitor.beginTask("Migrate",
					numberOfSteps(sourceRelease, targetRelease));

			performMigration(modelURIs, sourceRelease, targetRelease, monitor);

		} finally {
			monitor.done();
		}
	}

	/** Get the latest release. */
	private Release getLatestRelease() {
		List<Release> releases = history.getReleases();
		return releases.get(releases.size() - 2);
	}

	/** Apply the migrator to a model. */
	private void performMigration(List<URI> modelURIs, Release sourceRelease,
			Release targetRelease, IProgressMonitor monitor)
			throws MigrationException {
		try {
			EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
					URI.createFileURI("test"));
			MigrationReconstructor migrationReconstructor = new MigrationReconstructor(
					modelURIs, sourceRelease, targetRelease, monitor,
					classLoader, level);
			reconstructor.addReconstructor(migrationReconstructor);

			reconstructor.reconstruct(targetRelease, false);
		} catch (WrappedMigrationException e) {
			throw e.getCause();
		}
	}

	/** Returns the length of the migration in terms of the steps. */
	private int numberOfSteps(Release sourceRelease, Release targetRelease) {
		int size = 0;
		boolean inRelease = false;
		for (Release release : history.getReleases()) {
			if (inRelease) {
				size += release.getChanges().size();
			}
			if (release == sourceRelease) {
				inRelease = true;
			}
			if (release == targetRelease) {
				break;
			}
		}
		return size;
	}

	/** Get the release of a model based on {@link URI}. */
	public Set<Release> getRelease(URI modelURI) {
		String nsURI = ReleaseUtils.getNamespaceURI(modelURI);
		return releaseMap.containsKey(nsURI) ? releaseMap.get(nsURI)
				: Collections.<Release> emptySet();
	}

	/** Get the release with a certain number. */
	public Release getRelease(int number) {
		if (number < 0 || number >= history.getReleases().size()) {
			return null;
		}
		return history.getReleases().get(number);
	}

	/** Get all releases. */
	public List<Release> getReleases() {
		List<Release> releases = new ArrayList<Release>();
		releases.addAll(history.getReleases());
		releases.remove(history.getLastRelease());
		return releases;
	}

	/** Get set of namespace URIs. */
	public Set<String> getNsURIs() {
		return releaseMap.keySet();
	}

	/**
	 * Returns the metamodel for a release.
	 * 
	 * Note: This metamodel should not be changed, as it is cached.
	 */
	public Metamodel getMetamodel(Release release) {
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
				URI.createFileURI("test"));
		reconstructor.reconstruct(release, false);
		return Persistency.loadMetamodel(reconstructor.getResourceSet());
	}

	/** Set the validation level. */
	public void setLevel(ValidationLevel level) {
		this.level = level;
	}

	/** Main method to perform migrations. */
	public static void main(String[] args) {

		MigratorCommandLine commandLine = new MigratorCommandLine(args);
		List<URI> modelURIs = commandLine.getModelURIs();
		int sourceReleaseNumber = commandLine.getSourceReleaseNumber();
		int targetReleaseNumber = commandLine.getTargetReleaseNumber();

		try {
			for (Class<? extends LibraryImplementation> library : commandLine
					.getLibraries()) {
				OperationRegistry.getInstance().registerLibrary(library);
			}
			for (Class<? extends OperationImplementation> operation : commandLine
					.getOperations()) {
				OperationRegistry.getInstance().registerOperation(operation);
			}

			Migrator migrator = new Migrator(commandLine.getHistoryURI(),
					new ClassLoaderFacade(Thread.currentThread()
							.getContextClassLoader()));
			migrator.setLevel(commandLine.getLevel());

			Set<Release> releases = migrator.getRelease(modelURIs.get(0));
			Release sourceRelease = null;
			if (sourceReleaseNumber != -1) {
				sourceRelease = HistoryUtils.getRelease(releases,
						sourceReleaseNumber);
			} else {
				sourceRelease = releases.iterator().next();
			}

			if (commandLine.isBackup()) {
				Metamodel metamodel = migrator.getMetamodel(sourceRelease);
				try {
					BackupUtils.backup(modelURIs, metamodel);
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}

			Release targetRelease = migrator.getRelease(targetReleaseNumber);

			migrator.migrate(modelURIs, sourceRelease, targetRelease,
					new PrintStreamProgressMonitor(System.out));
		} catch (MigrationException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getCause().getMessage());
		}
	}
}
