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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.internal.migration.execution.internal.BundleClassLoader;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.ReleaseUtils;
import org.eclipse.emf.edapt.spi.migration.MigrationPlugin;
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
public final class MigratorRegistry {

	/** Singleton instance. */
	private static MigratorRegistry migratorRegistry;

	/** Registered migrators identified by unversioned namespace URI. */
	private final Map<String, Migrator> migrators;

	/** Private default constructor. */
	private MigratorRegistry() {
		migrators = new HashMap<String, Migrator>();
		if (Platform.isRunning()) {
			registerExtensionMigrators();
		}
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
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		final IConfigurationElement[] configurationElements = extensionRegistry
			.getConfigurationElementsFor("org.eclipse.emf.edapt.migrators"); //$NON-NLS-1$

		for (final IConfigurationElement configurationElement : configurationElements) {
			registerExtensionMigrator(configurationElement);
		}
	}

	/** Register migrator for one extension. */
	private void registerExtensionMigrator(
		IConfigurationElement configurationElement) {

		final String migrationPath = configurationElement.getAttribute("path"); //$NON-NLS-1$

		final IContributor contributor = configurationElement.getContributor();
		final String bundleName = contributor.getName();
		final Bundle bundle = Platform.getBundle(bundleName);
		final URI migratorURI = URI.createPlatformPluginURI("/" + bundleName + "/" //$NON-NLS-1$ //$NON-NLS-2$
			+ migrationPath, true);
		try {
			registerMigrator(migratorURI, new BundleClassLoader(bundle));
		} catch (final MigrationException e) {
			LoggingUtils.logError(MigrationPlugin.getPlugin(), e);
		}
	}

	/** Register a migrator by its URL. */
	public void registerMigrator(URL migratorURL, IClassLoader loader)
		throws MigrationException {
		final Migrator migrator = new Migrator(URIUtils.getURI(migratorURL), loader);
		for (final String nsURI : migrator.getNsURIs()) {
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
		final String nsURI = ReleaseUtils.getNamespaceURI(modelURI);
		return getMigrator(nsURI);
	}
}
