package org.eclipse.emf.edapt.migration.execution.incubator;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.execution.GroovyEvaluator;
import org.eclipse.emf.edapt.migration.execution.MigrationException;
import org.eclipse.emf.edapt.migration.execution.Persistency;
import org.eclipse.emf.edapt.migration.execution.ReleaseUtil;

public class Migrator {

	private History history;

	private HashMap<String, Set<Release>> releaseMap;

	public Migrator(URI historyURI) throws MigrationException {
		try {
			history = ResourceUtils.loadElement(historyURI);
		} catch (IOException e) {
			throw new MigrationException("History could not be loaded", e);
		}
		init();
	}

	public Migrator(History history) {
		this.history = history;
		init();
	}

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
			monitor.beginTask("Migrate", numberOfSteps(sourceRelease,
					targetRelease));

			performMigration(modelURIs, sourceRelease, targetRelease, monitor);

		} finally {
			GroovyEvaluator.getInstance().unsetModel();
			monitor.done();
		}
	}

	private Release getLatestRelease() {
		List<Release> releases = history.getReleases();
		return releases.get(releases.size() - 2);
	}

	/** Apply the migrator to a model. */
	private void performMigration(List<URI> modelURIs, Release sourceRelease,
			Release targetRelease, IProgressMonitor monitor)
			throws MigrationException {
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
				URI.createFileURI("test"));
		MigrationReconstructor migrationReconstructor = new MigrationReconstructor(
				modelURIs, sourceRelease, targetRelease, monitor);
		reconstructor.addReconstructor(migrationReconstructor);

		reconstructor.reconstruct(targetRelease, false);
	}

	/** Returns the length of the migration in terms of the steps. */
	private int numberOfSteps(Release sourceRelease, Release targetRelease) {
		int size = 0;
		boolean inRelease = false;
		for (Release release : history.getReleases()) {
			if (release == sourceRelease) {
				inRelease = true;
			}
			if (release == targetRelease) {
				break;
			}
			if (inRelease) {
				size += release.getChanges().size();
			}
		}
		return size;
	}

	/** Get the release of a model based on {@link URI}. */
	public Set<Release> getRelease(URI modelURI) {
		String nsURI = ReleaseUtil.getNamespaceURI(modelURI);
		return releaseMap.get(nsURI);
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

}
