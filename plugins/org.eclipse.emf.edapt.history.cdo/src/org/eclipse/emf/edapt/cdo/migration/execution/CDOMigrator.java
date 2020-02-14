package org.eclipse.emf.edapt.cdo.migration.execution;

import java.io.File;
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
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.cdo.common.model.CDOPackageInfo;
import org.eclipse.emf.cdo.common.model.CDOPackageRegistry;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.CDOResourceFactory;
import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOResourceLeaf;
import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOURIData;
import org.eclipse.emf.cdo.util.CDOURIUtil;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.cdo.view.CDOViewProvider;
import org.eclipse.emf.cdo.view.CDOViewProviderRegistry;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.edapt.common.IResourceSetFactory;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.internal.common.ResourceSetFactoryImpl;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.declaration.OperationRegistry;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.internal.migration.execution.ValidationLevel;
import org.eclipse.emf.edapt.internal.migration.execution.internal.ClassLoaderFacade;
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
 * @author Christophe Bouhier
 */
public class CDOMigrator {

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

	private MetamodelExtent extent;

	private EdaptCDOViewProvider edaptCDOViewProvider;

	/** Constructor. */
	public CDOMigrator(URI historyURI, IClassLoader classLoader)
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
	public CDOMigrator(History history, IClassLoader classLoader) {
		this.history = history;
		this.classLoader = classLoader;

		// Register the CDO Resource facgtory.
		registerCDOResourceFactory();

		// Register a CDO View Provider which deals with connection aware and
		// canonical URI's.
		final CDOViewProviderRegistry vpRegistry = CDOViewProviderRegistry.INSTANCE;
		edaptCDOViewProvider = new EdaptCDOViewProvider("cdo(\\.net4j\\.tcp)?://.*", CDOViewProvider.DEFAULT_PRIORITY); //$NON-NLS-1$
		vpRegistry.addViewProvider(edaptCDOViewProvider);
		init();
	}

	private void registerCDOResourceFactory() {
		// Initialize our factoy.
		final Map<String, Object> map = Resource.Factory.Registry.INSTANCE
			.getProtocolToFactoryMap();

		// cdo.net4j.tcp
		if (!map.containsKey(CDONet4jUtil.PROTOCOL_TCP)) {
			map.put(CDONet4jUtil.PROTOCOL_TCP, CDOResourceFactory.INSTANCE);
		}
		// cdo
		if (!map.containsKey(CDOURIUtil.PROTOCOL_NAME)) {
			map.put(CDOURIUtil.PROTOCOL_NAME, CDOResourceFactory.INSTANCE);
		}
	}

	/**
	 * Get the {@link CDOViewProvider}.
	 *
	 * @return the view provider
	 */
	public EdaptCDOViewProvider getEdaptCDOViewProvider() {
		return edaptCDOViewProvider;
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
	 * Migrate a model based on a set of {@link URI} and save to it's own URI
	 * location.
	 *
	 * @param modelURIs
	 * @param sourceRelease
	 * @param targetRelease
	 * @param monitor
	 * @throws MigrationException
	 */
	// public void migrateAndSave(List<URI> modelURIs, Release sourceRelease,
	// Release targetRelease, IProgressMonitor monitor)
	// throws MigrationException {
	// Model model = migrate(modelURIs, sourceRelease, targetRelease, monitor);
	// if (model == null) {
	// throw new MigrationException("Model is up-to-date", null);
	// }
	// // Get the original extend, as we need a factory...
	// try {
	// Persistency.saveModel(model);
	// } catch (IOException e) {
	// throw new MigrationException("Model could not be saved", e);
	// }
	// }

	/**
	 *
	 * @param sourceURIData the source uri data
	 * @param targetURIData the target uri data
	 * @param monitor the progress monitor
	 * @throws MigrationException
	 */
	public void migrateAndCopy(CDOURIData sourceURIData,
		CDOURIData targetURIData, IProgressMonitor monitor)
		throws MigrationException {

		// We need to force this, to let the ViewProvider store the root
		// resource ;-)
		try {
			final ResourceSetImpl set = new ResourceSetImpl();
			// Trigger an exception.
			set.getResource(sourceURIData.toURI(), true);
		} catch (final Exception e) {
		}

		// Get the modelURIs by collecting it from the repository.
		final CDOResource rootResource = getEdaptCDOViewProvider()
			.getRootResource(sourceURIData.getRepositoryName());

		final List<CDOResource> cdoResCollection = new ArrayList<CDOResource>();
		collectResources(cdoResCollection, rootResource);

		for (final CDOResource cdoRes : cdoResCollection) {
			// Get the source release

			final List<URI> sourceConnectionAwareURIS = new ArrayList<URI>();
			final URI sourceURI = cdoConnectionAwareURI(cdoRes.getURI(),
				sourceURIData);
			sourceConnectionAwareURIS.add(sourceURI);

			final List<URI> targetConnectionAwareURIS = new ArrayList<URI>();
			final URI targetURI = cdoConnectionAwareURI(cdoRes.getURI(),
				targetURIData);
			targetConnectionAwareURIS.add(targetURI);

			final Set<Release> releases = this.getRelease(cdoRes);

			final Release release = HistoryUtils.getMinimumRelease(releases);
			final Model model = migrate(sourceConnectionAwareURIS, release, null,
				monitor);
			if (model == null) {
				throw new MigrationException("Model is up-to-date", null); //$NON-NLS-1$
			}
			// Get the original extend, as we need a factory...
			try {
				CDOPersistency.saveModel(model, extent,
					targetConnectionAwareURIS);
			} catch (final IOException e) {
				throw new MigrationException("Model could not be saved", e); //$NON-NLS-1$
			}
		}

	}

	public void migrateAndCopy(List<URI> modelURIs, Release sourceRelease,
		Release targetRelease, IProgressMonitor monitor, List<URI> list)
		throws MigrationException {
		final Model model = migrate(modelURIs, sourceRelease, targetRelease, monitor);
		if (model == null) {
			throw new MigrationException("Model is up-to-date", null); //$NON-NLS-1$
		}
		// Get the original extent, as we need a factory...
		try {
			CDOPersistency.saveModel(model, extent, list);
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
	private Release getLatestRelease() {
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

			monitor.beginTask("Migrate: " + modelURIs, //$NON-NLS-1$
				numberOfSteps(sourceRelease, targetRelease));
			final EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
				URI.createFileURI("test")); //$NON-NLS-1$
			final CDOMigrationReconstructor migrationReconstructor = new CDOMigrationReconstructor(
				modelURIs, sourceRelease, targetRelease, monitor,
				classLoader, level, resourceSetFactory);
			reconstructor.addReconstructor(migrationReconstructor);

			reconstructor.reconstruct(targetRelease, false);

			extent = reconstructor.getExtent();

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

		// FIXME, get the nsURI from a CDO Resource.

		final String nsURI = ReleaseUtils.getNamespaceURI(modelURI);
		return releaseMap.containsKey(nsURI) ? releaseMap.get(nsURI)
			: Collections.<Release> emptySet();
	}

	public Set<Release> getRelease(CDOResource resource) {

		// Simply get a matching Release for the register package in the CDO
		// Registry.
		final CDOPackageRegistry packageRegistry = resource.cdoView().getSession()
			.getPackageRegistry();
		for (final CDOPackageInfo info : packageRegistry.getPackageInfos()) {
			final String packageURI = info.getPackageURI();
			if (releaseMap.containsKey(packageURI)) {
				return releaseMap.get(packageURI);
			}
		}
		// String nsURI = ReleaseUtils.getNamespaceURI(modelURI);
		return Collections.<Release> emptySet();
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

	// CB TODO, adapt for CDO handling.
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

			final CDOMigrator migrator = new CDOMigrator(commandLine.getHistoryURI(),
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

			migrator.migrateAndCopy(modelURIs, sourceRelease, targetRelease,
				new PrintStreamProgressMonitor(System.out), null);
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
	 * clear the specified CDO Repository.
	 *
	 * @param host
	 * @param port
	 * @param repo
	 */
	public void clearCDORepositories(String host, String port, String repo) {
		final ResourceSetImpl set = new ResourceSetImpl();

		final URI uri = CDOMigrator.cdoConnectionAwareURI(host, port, repo, ""); //$NON-NLS-1$

		try {
			// Trigger an exception.
			set.getResource(uri, true);
		} catch (final Exception e) {
			// this is expected to happen, as CDO doesn't support retrieving
			// the root resource with a connection aware URI.
			// However this will have triggered our view provider to store the
			// root resource :-)
			final CDOResource rootResource = getEdaptCDOViewProvider()
				.getRootResource(repo);
			if (rootResource != null) {

				// Will contain all our resources.
				final List<CDOResource> cdoResCollection = new ArrayList<CDOResource>();
				collectResources(cdoResCollection, rootResource);
				for (final CDOResource res : cdoResCollection) {
					try {
						res.delete(null);
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
				}
				// Can safely cast, as we use Transactions only in our provider.
				commitTransaction((CDOTransaction) rootResource.cdoView());
			}
		}
	}

	// CDO UTILITIES.
	/**
	 * Construct CDO {@link URI} from a given {@link ResourceSet source models},
	 * create {@link ResourceSet target models } and copy the source content to
	 * the target content. Note: this will only work with a configured {@link CDOViewProvider}.
	 *
	 * @param metamodel
	 * @param sourceModels
	 * @param resourceSetFactory
	 */
	public static void copy(Metamodel metamodel, ResourceSet sourceModels,
		List<URI> cdoURIs, IResourceSetFactory resourceSetFactory) {

		// Our CDO target Resourceset.
		final ResourceSet set = resourceSetFactory.createResourceSet();

		for (final Resource resource : sourceModels.getResources()) {

			final int index = sourceModels.getResources().indexOf(resource);
			final URI cdoResourceURI = cdoURIs.get(index);

			final CDOResource cdoLoadResource = cdoCreateResource(set,
				cdoResourceURI, metamodel);

			// Copy over the stuff to CDO.
			final EObject loadElement = resource.getContents().get(0);

			final EObject copy = EcoreUtil.copy(loadElement);
			cdoLoadResource.getContents().add(copy);

			try {
				cdoLoadResource.save(null);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		// TODO, how to get rid of the transaction?
	}

	/**
	 * Create a {@link CDOResource} from a {@link URI} the {@link Metamodel
	 * MMMeta} will be used to register the corresponding {@link EPackage} by
	 * the {@link CDOViewProvider}.
	 *
	 * @param set
	 * @param cdoResourceURI
	 * @param mmmeta
	 * @return the cdo resource
	 */
	public static CDOResource cdoCreateResource(ResourceSet set,
		URI cdoResourceURI, Metamodel mmmeta) {

		ResourceUtils.register(mmmeta.getEPackages(), set.getPackageRegistry());

		final Resource resource = set.createResource(cdoResourceURI);

		if (resource instanceof CDOResource) {
			return (CDOResource) resource;
		}
		return null;
	}

	/**
	 * Get a {@link CDOURIData connection aware URI} for specified arguments.
	 * The schema is set to TCP.
	 *
	 * @param host
	 * @param port
	 * @param repo
	 * @param resourceName
	 * @return the uri
	 */
	public static URI cdoConnectionAwareURI(String host, String port,
		String repo, String resourceName) {

		final CDOURIData cdouriData = new CDOURIData();
		cdouriData.setScheme("cdo.net4j.tcp"); //$NON-NLS-1$
		cdouriData.setAuthority(host + ":" + port); //$NON-NLS-1$
		cdouriData.setRepositoryName(repo);
		cdouriData.setResourcePath(new Path(resourceName));

		return cdouriData.toURI();
	}

	/**
	 * Get a {@link CDOURIData connection aware URI} for original canonical URI
	 * and specified arguments. The schema is set to TCP.
	 *
	 * @param canonicalURI uri for creating a {@link CDOURIData}
	 * @param authority authority for creating a {@link CDOURIData}
	 * @return the uri
	 */
	public static URI cdoConnectionAwareURI(URI canonicalURI, String authority) {

		final CDOURIData cdouriData = new CDOURIData(canonicalURI);
		cdouriData.setScheme("cdo.net4j.tcp"); //$NON-NLS-1$
		cdouriData.setAuthority(authority);

		return cdouriData.toURI();
	}

	/**
	 * Get a {@link CDOURIData connection aware URI} for original canonical URI
	 * and specified source CDOURIData. (Setting the authority, user name and
	 * password from this source). </br>
	 * The schema is set to TCP.
	 *
	 * @param canonicalURI
	 * @param data
	 * @return the uri
	 */
	public static URI cdoConnectionAwareURI(URI canonicalURI, CDOURIData data) {

		String path = CDOURIUtil.extractResourcePath(canonicalURI);
		// Remove the leading '/'.
		if (path.startsWith("/")) { //$NON-NLS-1$
			path = path.substring(1, path.length());
		}

		final CDOURIData cdouriData = new CDOURIData();
		cdouriData.setScheme("cdo.net4j.tcp"); //$NON-NLS-1$
		cdouriData.setAuthority(data.getAuthority());
		cdouriData.setResourcePath(new Path(path));
		cdouriData.setRepositoryName(data.getRepositoryName());
		cdouriData.setUserName(data.getUserName());
		cdouriData.setPassWord(data.getPassWord());

		return cdouriData.toURI();
	}

	/**
	 * Clear a {@link CDOResource} from the CDO Repository. The CDOResource
	 * should be active (Have an active {@link CDOTransaction}).
	 *
	 * @param cdoSourceURIs
	 * @param resourceSetFactoryImpl
	 */
	public static void clear(List<URI> cdoSourceURIs,
		ResourceSetFactoryImpl resourceSetFactoryImpl) {
		final ResourceSet set = resourceSetFactoryImpl.createResourceSet();

		CDOTransaction transaction;
		for (final URI uri : cdoSourceURIs) {

			try {

				final Resource resource = set.getResource(uri, true);

				if (resource instanceof CDOResource) {
					final CDOResource cdoRes = (CDOResource) resource;
					transaction = (CDOTransaction) cdoRes.cdoView();
					try {
						cdoRes.delete(null);
						transaction.commit();
					} catch (final IOException e) {
					} catch (final ConcurrentAccessException e) {
					} catch (final CommitException e) {
					}
				}
			} catch (final RuntimeException re) {
				// Guard for Exception, as the resource might not exist,
				// which will be an invalid URI when demand loading duh....
			}

		}
	}

	/**
	 * Converts a 'regular' resource URI to a CDO Resource URI.
	 *
	 * @param sourceURI
	 * @return the uri
	 */
	public static URI cdoConnectionAwareURI(URI sourceURI, String host,
		String port, String repo) {

		try {
			URI.createFileURI(sourceURI.toString());
		} catch (final Exception e) {
			// bail when we are not a file URI.
			e.printStackTrace();
		}

		final String fileName = sourceURI.lastSegment();

		// Strip the extension of the file name.
		final String resourceName = fileName.substring(0, fileName.lastIndexOf(".")); //$NON-NLS-1$
		return cdoConnectionAwareURI(host, port, repo, resourceName);
	}

	/**
	 * Converts regular CDOResource URI's to connection aware URI's.
	 *
	 * @param data
	 * @param cdoResCollection
	 * @param node
	 * @return the uris
	 */
	public static List<URI> collectConnectionAwareURIs(CDOURIData data,
		List<CDOResource> cdoResCollection, CDOResourceNode node) {

		final List<URI> connectionAwareURIs = new ArrayList<URI>();
		collectResources(cdoResCollection, node);
		for (final CDOResource res : cdoResCollection) {
			final URI connectionAware = cdoConnectionAwareURI(res.getURI(),
				data.getAuthority());
			connectionAwareURIs.add(connectionAware);
		}
		return connectionAwareURIs;
	}

	/**
	 * Collect all {@link CDOResource} for a root resource. (Works best if the
	 * initial {@link CDOResourceNode} is of type {@link CDOResource} and this
	 * is the root resource for the repository.
	 *
	 * @param cdoResCollection
	 * @param node
	 */
	public static void collectResources(List<CDOResource> cdoResCollection,
		CDOResourceNode node) {

		if (node.isRoot()) {
			// We are the root,
			final CDOResource rootResource = (CDOResource) node;
			for (final EObject eo : rootResource.getContents()) {
				if (eo instanceof CDOResourceNode) {
					collectResources(cdoResCollection, (CDOResourceNode) eo);
				}
			}
		} else if (node instanceof CDOResourceFolder) {
			// Walk the resource hierarchy and clean all.
			final CDOResourceFolder folder = (CDOResourceFolder) node;
			for (final CDOResourceNode childNode : folder.getNodes()) {
				collectResources(cdoResCollection, childNode);
			}
		} else if (node instanceof CDOResourceLeaf) {
			final CDOResourceLeaf leaf = (CDOResourceLeaf) node;
			// Clear it! (Call delete(null).
			if (leaf instanceof CDOResource) {
				final CDOResource res = (CDOResource) leaf;
				cdoResCollection.add(res);
			}
		}
	}

	/**
	 * Commit a CDO Transaction.
	 *
	 * @param t
	 * @return <code>true</code> if commit failed, <code>false</code> otherwise
	 */
	public static boolean commitTransaction(CDOTransaction t) {
		boolean commitFailed = false;
		try {
			t.commit();
		} catch (final ConcurrentAccessException e) {
			e.printStackTrace();
			commitFailed = true;
		} catch (final CommitException e) {
			e.printStackTrace();
			commitFailed = true;
		}
		return commitFailed;
	}

	public static EPackage loadEPackageFromEcore(URI expectedTargetMetamodelURI) {
		// register globally the Ecore Resource Factory to the ".ecore"
		// extension
		// weird that we need to do this, but well...
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl()); //$NON-NLS-1$

		final ResourceSet rs = new ResourceSetImpl();
		// enable extended metadata
		final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(
			rs.getPackageRegistry());
		rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA,
			extendedMetaData);

		final Resource r = rs.getResource(expectedTargetMetamodelURI, true);
		final EObject eObject = r.getContents().get(0);
		if (eObject instanceof EPackage) {
			final EPackage p = (EPackage) eObject;
			rs.getPackageRegistry().put(p.getNsURI(), p);
			return p;
		}
		return null;
	}

	/**
	 * Converts a 'regular' resource URI to a CDO Resource URI.
	 *
	 * @param sourceURI
	 * @return the uri
	 */
	public static URI cdoCanonicalURI(URI sourceURI, String repo) {

		// FIXME, check the URI is file based....
		final String fileName = sourceURI.lastSegment();

		// Strip the extension of the file name.
		final String resourceName = fileName.substring(0, fileName.lastIndexOf(".")); //$NON-NLS-1$

		final CDOURIData cdouriData = new CDOURIData();

		cdouriData.setScheme("cdo"); //$NON-NLS-1$
		cdouriData.setRepositoryName(repo);
		cdouriData.setResourcePath(new Path(resourceName));

		return cdouriData.toURI();
	}
}
