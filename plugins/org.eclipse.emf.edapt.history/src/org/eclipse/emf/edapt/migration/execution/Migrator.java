/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.migration.execution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.IResourceSetFactory;
import org.eclipse.emf.edapt.common.IResourceSetProcessor;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.internal.common.ResourceSetFactoryImpl;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.declaration.OperationRegistry;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.internal.migration.execution.ValidationLevel;
import org.eclipse.emf.edapt.internal.migration.execution.internal.ClassLoaderFacade;
import org.eclipse.emf.edapt.internal.migration.execution.internal.MigrationReconstructor;
import org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLine;
import org.eclipse.emf.edapt.internal.migration.execution.internal.WrappedMigrationException;
import org.eclipse.emf.edapt.internal.migration.internal.BackupUtils;
import org.eclipse.emf.edapt.internal.migration.internal.MaterializingBackwardConverter;
import org.eclipse.emf.edapt.internal.migration.internal.Persistency;
import org.eclipse.emf.edapt.internal.migration.internal.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.ReleaseUtils;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

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

	/** Factory to create {@link ResourceSet}s for custom serialization. */
	private IResourceSetFactory resourceSetFactory = new ResourceSetFactoryImpl();

	/** Validation level. */
	private ValidationLevel level = ValidationLevel.CUSTOM_MIGRATION;

	private IResourceSetProcessor postLoadProcessor;

	/** Constructor. */
	public Migrator(URI historyURI, IClassLoader classLoader)
		throws MigrationException {
		HistoryPackage.eINSTANCE.getHistory();
		try {
			history = ResourceUtils.loadElement(historyURI);
		} catch (final IOException e) {
			throw new MigrationException("History could not be loaded", e); //$NON-NLS-1$
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
		final Map<EPackage, String> packageMap = new HashMap<EPackage, String>();

		for (final Release release : history.getReleases()) {
			if (!release.isLastRelease()) {
				updatePackages(release, packageMap);
				registerRelease(release, packageMap);
			}
		}
	}

	/** Register a package for a certain release. */
	private void registerRelease(Release release,
		Map<EPackage, String> packageMap) {
		for (final Entry<EPackage, String> entry : packageMap.entrySet()) {
			final String nsURI = entry.getValue();
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
		for (final Iterator<EObject> i = release.eAllContents(); i.hasNext();) {
			final EObject element = i.next();
			if (element instanceof org.eclipse.emf.edapt.spi.history.Set) {
				final org.eclipse.emf.edapt.spi.history.Set set = (org.eclipse.emf.edapt.spi.history.Set) element;
				if (set.getFeature() == EcorePackage.eINSTANCE
					.getEPackage_NsURI()) {
					final EPackage ePackage = (EPackage) set.getElement();
					final String nsURI = (String) set.getValue();
					packageMap.put(ePackage, nsURI);
				}
			} else if (element instanceof Delete) {
				final Delete delete = (Delete) element;
				packageMap.remove(delete.getElement());
			}
		}
	}

	/**
	 * Migrate a model based on a set of {@link URI}.
	 *
	 * @param modelURIs
	 * @param sourceRelease
	 *            Release to which the model conforms
	 * @param targetRelease
	 *            Release to which the model should be migrated (use null for
	 *            the newest release)
	 * @param monitor
	 *            Progress monitor
	 */
	public void migrateAndSave(List<URI> modelURIs, Release sourceRelease,
		Release targetRelease, IProgressMonitor monitor)
		throws MigrationException {
		this.migrateAndSave(modelURIs, sourceRelease, targetRelease, monitor, null);
	}

	/**
	 * Migrate a model based on a set of {@link URI}.
	 *
	 * @param modelURIs
	 * @param sourceRelease
	 *            Release to which the model conforms
	 * @param targetRelease
	 *            Release to which the model should be migrated (use null for
	 *            the newest release)
	 * @param monitor
	 *            Progress monitor
	 * @param options
	 *            Options to pass to the ResourceSet when saving
	 * @since 1.1
	 */
	public void migrateAndSave(List<URI> modelURIs, Release sourceRelease,
		Release targetRelease, IProgressMonitor monitor, Map<String, Object> options)
		throws MigrationException {
		final Model model = migrate(modelURIs, sourceRelease, targetRelease, monitor);
		if (model == null) {
			throw new MigrationException("Model is up-to-date", null); //$NON-NLS-1$
		}
		try {
			Persistency.saveModel(model, options);
		} catch (final IOException e) {
			throw new MigrationException("Model could not be saved", e); //$NON-NLS-1$
		}
	}

	/**
	 * Migrate a model based on a set of {@link URI}s and load it afterwards.
	 *
	 * @param modelURIs
	 *            The set of {@link URI}
	 * @param sourceRelease
	 *            Release to which the model conforms
	 * @param targetRelease
	 *            Release to which the model should be migrated (use null for
	 *            the newest release)
	 * @param monitor
	 *            Progress monitor
	 * @return The model in a {@link ResourceSet}
	 */
	public ResourceSet migrateAndLoad(List<URI> modelURIs,
		Release sourceRelease, Release targetRelease,
		IProgressMonitor monitor) throws MigrationException {
		final Model model = migrate(modelURIs, sourceRelease, targetRelease, monitor);
		if (model == null) {
			return null;
		}
		final MaterializingBackwardConverter converter = new MaterializingBackwardConverter();
		return converter.convert(model);
	}

	/** Get the latest release. */
	public Release getLatestRelease() {
		final List<Release> releases = history.getReleases();
		return releases.get(releases.size() - 2);
	}

	/**
	 * Migrate a model based on a set of {@link URI}s.
	 *
	 * @param modelURIs
	 *            The set of {@link URI}
	 * @param sourceRelease
	 *            Release to which the model conforms
	 * @param targetRelease
	 *            Release to which the model should be migrated (use null for
	 *            the newest release)
	 * @param monitor
	 *            Progress monitor
	 * @return The model in the generic structure
	 */
	private Model migrate(List<URI> modelURIs, Release sourceRelease,
		Release targetRelease, IProgressMonitor monitor)
		throws MigrationException {
		try {
			if (targetRelease == null) {
				targetRelease = getLatestRelease();
			}
			if (sourceRelease == targetRelease) {
				return null;
			}

			monitor.beginTask("Migrate", //$NON-NLS-1$
				numberOfSteps(sourceRelease, targetRelease));
			final EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
				URI.createFileURI("test")); //$NON-NLS-1$
			final MigrationReconstructor migrationReconstructor = new MigrationReconstructor(
				modelURIs, sourceRelease, targetRelease, monitor,
				classLoader, level, resourceSetFactory, postLoadProcessor);
			reconstructor.addReconstructor(migrationReconstructor);

			reconstructor.reconstruct(targetRelease, false);

			final Model model = migrationReconstructor.getModel();
			return model;
		} catch (final WrappedMigrationException e) {
			throw e.getCause();
		} finally {
			monitor.done();
		}
	}

	/** Returns the length of the migration in terms of the steps. */
	private int numberOfSteps(Release sourceRelease, Release targetRelease) {
		int size = 0;
		boolean inRelease = false;
		for (final Release release : history.getReleases()) {
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
		final String nsURI = ReleaseUtils.getNamespaceURI(modelURI);
		return releaseMap.containsKey(nsURI) ? releaseMap.get(nsURI)
			: Collections.<Release> emptySet();
	}

	/**
	 * Get the release of a model based on a set of namespace URIs.
	 *
	 * @since 1.2
	 */
	public Set<Release> getRelease(Set<String> nsURIs) {
		final Set<Release> releases = new LinkedHashSet<Release>();
		for (final String nsURI : nsURIs) {
			final Set<Release> set = getReleaseMap().get(nsURI);
			if (set == null || set.isEmpty()) {
				continue;
			}
			if (releases.isEmpty()) {
				releases.addAll(set);
			} else {
				releases.retainAll(set);
			}
		}
		return releases;
	}

	/** Get the release map from NS-URI to releases */
	public Map<String, Set<Release>> getReleaseMap() {
		return releaseMap;
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
		final List<Release> releases = new ArrayList<Release>();
		releases.addAll(history.getReleases());
		releases.remove(history.getLastRelease());
		return releases;
	}

	/** Get set of namespace URIs. */
	public Set<String> getNsURIs() {
		return releaseMap.keySet();
	}

	// CB TODO SHOULD BE API or Adapter, this is called from MigrateHandler.

	/** Returns the metamodel for a release. */
	public Metamodel getMetamodel(Release release) {
		final EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
			URI.createFileURI("test")); //$NON-NLS-1$
		reconstructor.reconstruct(release, false);
		final URI metamodelURI = URI.createFileURI(new File("metamodel." //$NON-NLS-1$
			+ ResourceUtils.ECORE_FILE_EXTENSION).getAbsolutePath());
		final List<EPackage> rootPackages = ResourceUtils.getRootElements(
			reconstructor.getResourceSet(), EPackage.class);
		final ResourceSet resourceSet = MetamodelUtils
			.createIndependentMetamodelCopy(rootPackages, metamodelURI);
		return Persistency.loadMetamodel(resourceSet);
	}

	/** Set the validation level. */
	public void setLevel(ValidationLevel level) {
		this.level = level;
	}

	/** Main method to perform migrations. */
	public static void main(String[] args) {

		final MigratorCommandLine commandLine = new MigratorCommandLine(args);
		final List<URI> modelURIs = commandLine.getModelURIs();
		final int sourceReleaseNumber = commandLine.getSourceReleaseNumber();
		final int targetReleaseNumber = commandLine.getTargetReleaseNumber();

		try {
			for (final Class<? extends LibraryImplementation> library : commandLine
				.getLibraries()) {
				OperationRegistry.getInstance().registerLibrary(library);
			}
			for (final Class<? extends OperationImplementation> operation : commandLine
				.getOperations()) {
				OperationRegistry.getInstance().registerOperation(operation);
			}

			final Migrator migrator = new Migrator(commandLine.getHistoryURI(),
				new ClassLoaderFacade(Thread.currentThread()
					.getContextClassLoader()));
			migrator.setLevel(commandLine.getLevel());

			final Set<Release> releases = migrator.getRelease(modelURIs.get(0));
			Release sourceRelease = null;
			if (sourceReleaseNumber != -1) {
				sourceRelease = HistoryUtils.getRelease(releases,
					sourceReleaseNumber);
			} else {
				sourceRelease = releases.iterator().next();
			}

			if (commandLine.isBackup()) {
				final Metamodel metamodel = migrator.getMetamodel(sourceRelease);
				try {
					BackupUtils.backup(modelURIs, metamodel);
				} catch (final IOException e) {
					System.err.println(e.getMessage());
				}
			}

			final Release targetRelease = migrator.getRelease(targetReleaseNumber);

			migrator.migrateAndSave(modelURIs, sourceRelease, targetRelease,
				new PrintStreamProgressMonitor(System.out));
		} catch (final MigrationException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getCause().getMessage());
		}
	}

	/** Set the factory to create {@link ResourceSet}s for custom serialization. */
	public void setResourceSetFactory(IResourceSetFactory resourceSetFactory) {
		if (resourceSetFactory != null) {
			this.resourceSetFactory = resourceSetFactory;
		}
	}

	/** Get the factory to create {@link ResourceSet}s for custom serialization. */
	public IResourceSetFactory getResourceSetFactory() {
		return resourceSetFactory;
	}

	/**
	 * The given processor will be called after the outdated model was loaded. It will be called before Edapt translates
	 * the dynamic EMF model into its internal representation required for the migration. Hence this processor may be
	 * used to modify the to be migrated model before the actual migrations starts.
	 *
	 * @since 1.3
	 */
	public void setPostLoadModelProcessor(IResourceSetProcessor postLoadProcessor) {
		this.postLoadProcessor = postLoadProcessor;
	}

	/**
	 * The post load model processor.
	 *
	 * @since 1.3
	 */
	public IResourceSetProcessor getPostLoadModelProcessor() {
		return postLoadProcessor;
	}
}
