package org.eclipse.emf.edapt.cdo.migration.execution;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.cdo.common.model.CDOPackageRegistry;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOURIData;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.cdo.view.AbstractCDOViewProvider;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.cdo.view.CDOViewProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.ManagedContainer;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.security.IPasswordCredentialsProvider;
import org.eclipse.net4j.util.security.PasswordCredentials;
import org.eclipse.net4j.util.security.PasswordCredentialsProvider;

/**
 * A CDO View provider for Edapt. It deals with {@link CDOURIData connection
 * aware} and canonical URI. It also registers in CDO the {@link EPackage
 * packages} from the {@link ResourceSet resourceset's} package registry. </br>
 * Finally it stores the root resource. {@link CDOView#getRootResource} as there
 * is no way to obtain it with a connection aware URI.
 *
 * @author Christophe Bouhier
 */
public class EdaptCDOViewProvider extends AbstractCDOViewProvider implements
	CDOViewProvider {

	// A Map holding containers specific to a repository.
	private static Map<String, IManagedContainer> repoContainers = new HashMap<String, IManagedContainer>();

	// A Map holding root resources
	// CB Fixme, consider storing the canonical URI?
	private static Map<String, CDOResource> rootResourceRegistry = new HashMap<String, CDOResource>();

	public EdaptCDOViewProvider() {
	}

	public EdaptCDOViewProvider(String regex) {
		super(regex);
	}

	public EdaptCDOViewProvider(String regex, int priority) {
		super(regex, priority);
	}

	@Override
	public CDOView getView(URI uri, ResourceSet resourceSet) {

		final CDOURIData cdouriData = new CDOURIData(uri);

		final String repoName = cdouriData.getRepositoryName();

		IManagedContainer container = repoContainers.get(repoName);

		if (container == null) {
			container = new ManagedContainer();
			Net4jUtil.prepareContainer(container);
			TCPUtil.prepareContainer(container);
			container.activate();
			repoContainers.put(repoName, container);
		} else {
			for (final Object e : container.getElements()) {
				System.out.println(e);
			}
		}

		// TODO Base the connector on the schema.
		final String uriSchema = cdouriData.getScheme();
		System.out.println("EDAPT TODO: Schema requested is " + uriSchema //$NON-NLS-1$
			+ " base connector on this requested schema. "); //$NON-NLS-1$
		if (uriSchema.equals("cdo.net4j.tcp")) { //$NON-NLS-1$
			// Base connector on TCP.
		} else if (uriSchema.equals("cdo.net4j.jvm")) { //$NON-NLS-1$
			// Base connector on JVM.
			// Note, condition is a bundled which is server side.
			// consider checks for this.
		}

		// Produce a TCPConnector which respects all params like UserID, Port,
		// and Host.
		final IConnector connector = (IConnector) container.getElement("org.eclipse.net4j.connectors", "tcp", //$NON-NLS-1$ //$NON-NLS-2$
			cdouriData.getAuthority());
		final CDONet4jSessionConfiguration config = CDONet4jUtil
			.createNet4jSessionConfiguration();
		config.setConnector(connector);
		config.setRepositoryName(repoName);

		if (cdouriData.getUserName() != null
			&& cdouriData.getPassWord() != null) {
			final PasswordCredentials passwordCredentials = new PasswordCredentials(
				cdouriData.getUserName(), cdouriData.getPassWord()
					.toCharArray());
			final IPasswordCredentialsProvider credentialsProvider = new PasswordCredentialsProvider(
				passwordCredentials);

			config.setCredentialsProvider(credentialsProvider);
		}

		final CDOSession session = config.openNet4jSession();

		// Make sure the packages are emulated.
		session.options().setGeneratedPackageEmulationEnabled(true);

		// Get the package registry for this resource set and make sure
		// it is known in the repository. Note:
		// CDO will also populate from the global registry when?
		//
		final Registry packageRegistry = resourceSet.getPackageRegistry();

		boolean packagesAdded = false;
		if (!packageRegistry.isEmpty()) {
			final CDOPackageRegistry cdoPackageRegistry = session
				.getPackageRegistry();

			for (final String nsURI : packageRegistry.keySet()) {
				final EPackage packageToAdd = packageRegistry.getEPackage(nsURI);

				if (!cdoPackageRegistry.containsKey(nsURI)) {
					cdoPackageRegistry.putEPackage(packageToAdd);
					packagesAdded = true;
				}
			}
		}

		final CDOTransaction openTransaction = session.openTransaction(resourceSet);

		// Commit the added EPackages to the registry.
		if (packagesAdded) {
			try {
				openTransaction.commit();
			} catch (final ConcurrentAccessException e) {
				e.printStackTrace();
			} catch (final CommitException e) {
				e.printStackTrace();
			}
		}

		if (rootResourceRegistry.get(repoName) == null) {
			final CDOResource rootResource = openTransaction.getRootResource();
			@SuppressWarnings("unused")
			final URI uri2 = rootResource.getURI();
			rootResourceRegistry.put(repoName, rootResource);
		}

		return openTransaction;
	}

	/**
	 * Get the root resource for a repository. This will only result if {@link EdaptCDOViewProvider this} provider was
	 * previously consulted.
	 *
	 * @param repoName
	 * @return the uri
	 */
	public CDOResource getRootResource(String repoName) {
		final CDOResource cdoResource = rootResourceRegistry.get(repoName);
		if (cdoResource != null) {
			LifecycleUtil.checkActive(cdoResource);
			return cdoResource;
		}
		return null;
	}

}
