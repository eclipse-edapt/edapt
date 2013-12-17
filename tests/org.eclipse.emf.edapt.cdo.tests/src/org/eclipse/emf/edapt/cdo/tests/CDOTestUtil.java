package org.eclipse.emf.edapt.cdo.tests;

import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.session.CDOSession.ExceptionHandler;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.jvm.IJVMAcceptor;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;

public class CDOTestUtil {

	public static CDOTestUtil self = new CDOTestUtil();

	private static final String REPO_NAME = "repo1";

	@SuppressWarnings("unused")
	private IJVMAcceptor acceptor;
	private IConnector connector;

	protected static final String CONNECTION_ADDRESS = "localhost:2036";

	private ExceptionHandler exceptionHandler = new ExceptionHandler() {

		public void handleException(CDOSession session, int attempt,
				Exception exception) throws Exception {
			System.out.println("CDO Exception: " + exception);
		}
	};

	public CDONet4jSession openSession() {
		CDONet4jSessionConfiguration sessionConfig = getSessionConfig();
		CDONet4jSession openNet4jSession = sessionConfig.openNet4jSession();
		return openNet4jSession;
	}

	private CDONet4jSessionConfiguration getSessionConfig() {

		OMPlatform.INSTANCE.setDebugging(true);
		OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
		OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);

		final IManagedContainer container = ContainerUtil.createContainer();
		// JVMUtil.prepareContainer(container);
		Net4jUtil.prepareContainer(container); // Register Net4j factories
		TCPUtil.prepareContainer(container); // Register TCP factories
		CDONet4jUtil.prepareContainer(container); // Register CDO factories

		container.activate();

		// Create configuration
		final CDONet4jSessionConfiguration sessionConfiguration = CDONet4jUtil
				.createNet4jSessionConfiguration();

		// acceptor = JVMUtil.getAcceptor(container, "default");
		connector = TCPUtil.getConnector(container, CONNECTION_ADDRESS);

		sessionConfiguration.setConnector(connector);
		sessionConfiguration.setRepositoryName(REPO_NAME);
		sessionConfiguration.setExceptionHandler(exceptionHandler);

		return sessionConfiguration;

	}

}
