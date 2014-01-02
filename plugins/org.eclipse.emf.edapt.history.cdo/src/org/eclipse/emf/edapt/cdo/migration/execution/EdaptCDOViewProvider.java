// TODO HEADER FOR EDAPT
package org.eclipse.emf.edapt.cdo.migration.execution;

import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.view.AbstractCDOViewProvider;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.ManagedContainer;

public class EdaptCDOViewProvider extends AbstractCDOViewProvider {
	private IManagedContainer container;
	
	
	public EdaptCDOViewProvider(String regex, int priority) {
		super("cdo\\.local:.*", priority);
	}


	public CDOView getView(URI uri, ResourceSet resourceSet) {
		if (container == null) {
			container = new ManagedContainer();
			Net4jUtil.prepareContainer(container);
			TCPUtil.prepareContainer(container);
			container.activate();
		}

		int startIndex = uri.toString().indexOf(':');
		String repoName = uri.toString().substring(startIndex);

		IConnector connector = (IConnector) container.getElement(
				"org.eclipse.net4j.connectors", "tcp", "localhost");

		CDONet4jSessionConfiguration config = CDONet4jUtil
				.createNet4jSessionConfiguration();
		config.setConnector(connector);
		config.setRepositoryName(repoName);

		CDOSession session = config.openNet4jSession();
		return session.openTransaction();
	}

}
