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

	public static final String REPO_SOURCE = "repo_source";
	public static final String REPO_TARGET = "repo_target";

	public static final String SOURCE_PORT = "2036";
	public static final String TARGET_PORT = "2037";

	public static final String CONNECTOR_TYPE = "tcp";

	@SuppressWarnings("unused")
	private IJVMAcceptor acceptor;
	private IConnector connector;

	protected static final String HOST = "127.0.0.1";

	private final ExceptionHandler exceptionHandler = new ExceptionHandler() {

		@Override
		public void handleException(CDOSession session, int attempt,
			Exception exception) throws Exception {
			System.out.println("CDO Exception: " + exception);
		}
	};

	public CDONet4jSession openSession() {
		final CDONet4jSessionConfiguration sessionConfig = getSessionConfig();
		final CDONet4jSession openNet4jSession = sessionConfig.openNet4jSession();
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

		// Use a factory to produce a Connect and parse the connector description (See TCPCOnnectorFactory).

		connector = TCPUtil.getConnector(container, HOST + ":" + SOURCE_PORT);

		sessionConfiguration.setConnector(connector);
		sessionConfiguration.setRepositoryName(REPO_SOURCE);
		sessionConfiguration.setExceptionHandler(exceptionHandler);

		return sessionConfiguration;

	}

}
