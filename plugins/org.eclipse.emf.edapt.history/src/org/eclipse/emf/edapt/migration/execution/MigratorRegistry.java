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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.migration.IDebugger;
import org.eclipse.emf.edapt.migration.IOracle;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.MigrationPlugin;
import org.eclipse.emf.edapt.migration.RandomOracle;
import org.eclipse.emf.edapt.migration.ReleaseUtils;
import org.osgi.framework.Bundle;

/**
 * Registry for all migrators (singleton). A migrator is registered as an
 * Eclipse extension.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigratorRegistry {

	/** Singleton instance. */
	private static MigratorRegistry migratorRegistry;

	/** Registered migrators identified by unversioned namespace URI. */
	private final Map<String, Migrator> migrators;

	/** Registered oracle. */
	private IOracle oracle;

	/** Registered debugger. */
	private IDebugger debugger;

	/** Private default constructor. */
	private MigratorRegistry() {
		migrators = new HashMap<String, Migrator>();
		if (Platform.isRunning()) {
			registerExtensionMigrators();
		}
		oracle = new RandomOracle();
	}

	/** Getter for instance. */
	public static MigratorRegistry getInstance() {
		if (migratorRegistry == null) {
			migratorRegistry = new MigratorRegistry();
		}
		return migratorRegistry;
	}

	/** Register all migrators from extensions. */
	private void registerExtensionMigrators() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement[] configurationElements = extensionRegistry
				.getConfigurationElementsFor("org.eclipse.emf.edapt.migrators2");

		for (IConfigurationElement configurationElement : configurationElements) {
			registerExtensionMigrator(configurationElement);
		}
	}

	/** Register migrator for one extension. */
	private void registerExtensionMigrator(
			IConfigurationElement configurationElement) {

		String migrationPath = configurationElement.getAttribute("path");

		IContributor contributor = configurationElement.getContributor();
		String bundleName = contributor.getName();
		Bundle bundle = Platform.getBundle(bundleName);

		URL migratorURL = bundle.getResource(migrationPath);

		try {
			registerMigrator(migratorURL, new BundleClassLoader(bundle));
		} catch (MigrationException e) {
			LoggingUtils.logError(MigrationPlugin.getPlugin(), e);
		}
	}

	/** Register a migrator by its URL. */
	public void registerMigrator(URL migratorURL, IClassLoader loader)
			throws MigrationException {
		Migrator migrator = new Migrator(URIUtils.getURI(migratorURL), loader);
		for (String nsURI : migrator.getNsURIs()) {
			migrators.put(nsURI, migrator);
		}
	}

	/** Register a migrator by its URI. */
	public void registerMigrator(URI migratorURI, IClassLoader loader)
			throws MigrationException {
		registerMigrator(URIUtils.getURL(migratorURI), loader);
	}

	/** Get a migrator by its namespace already stripped from version. */
	public Migrator getMigrator(String nsURI) {
		return migrators.get(nsURI);
	}

	/** Get a migrator for a certain model. */
	public Migrator getMigrator(URI modelURI) {
		String nsURI = ReleaseUtils.getNamespaceURI(modelURI);
		return getMigrator(nsURI);
	}

	/** Returns the oracle. */
	public IOracle getOracle() {
		return oracle;
	}

	/** Sets oracle. */
	public void setOracle(IOracle oracle) {
		this.oracle = oracle;
	}

	/** Returns debugger. */
	public IDebugger getDebugger() {
		return debugger;
	}

	/** Sets debugger. */
	public void setDebugger(IDebugger debugger) {
		this.debugger = debugger;
	}
}
