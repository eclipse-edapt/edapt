package org.eclipse.emf.edapt.cdo.tests;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.cdo.common.model.CDOPackageRegistry;
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

public class EdaptCDOViewProvider extends AbstractCDOViewProvider implements
		CDOViewProvider {

	// A Map holding containers specific to a repository.
	private static Map<String, IManagedContainer> repoContainers = new HashMap<String, IManagedContainer>();

	public EdaptCDOViewProvider() {
	}

	public EdaptCDOViewProvider(String regex) {
		super(regex);
	}

	public EdaptCDOViewProvider(String regex, int priority) {
		super(regex, priority);
	}

	public CDOView getView(URI uri, ResourceSet resourceSet) {

		
		CDOURIData cdouriData = new CDOURIData(uri);
		
		String repoName = cdouriData.getRepositoryName();
		
		IManagedContainer container = repoContainers.get(repoName);
		
		if (container == null) {
			container = new ManagedContainer();
			Net4jUtil.prepareContainer(container);
			TCPUtil.prepareContainer(container);
			container.activate();
			repoContainers.put(repoName, container);
		}else{
			for(Object e : container.getElements()){
				System.out.println(e);
			}
		}

		// Produce a TCPConnector which respects all params like UserID, Port,
		// and Host.
		IConnector connector = (IConnector) container.getElement(
				"org.eclipse.net4j.connectors", "tcp", cdouriData.getAuthority());

		CDONet4jSessionConfiguration config = CDONet4jUtil
				.createNet4jSessionConfiguration();
		config.setConnector(connector);
		config.setRepositoryName(repoName);

		CDOSession session = config.openNet4jSession();
		
		// Get the package registry for this resource set and make sure
		// it is known in the repository.
		Registry packageRegistry = resourceSet.getPackageRegistry();

		CDOPackageRegistry cdoPackageRegistry = session.getPackageRegistry();
		
		for (String nsURI : packageRegistry.keySet()) {
			EPackage packageToAdd = packageRegistry.getEPackage(nsURI);
			EPackage ePackage = cdoPackageRegistry.getEPackage(nsURI);
			if(ePackage == null){
				cdoPackageRegistry.putEPackage(packageToAdd);
			}
		}

		CDOTransaction openTransaction = session.openTransaction(resourceSet);
		try {
			openTransaction.commit();
		} catch (ConcurrentAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return openTransaction;
	}
}
