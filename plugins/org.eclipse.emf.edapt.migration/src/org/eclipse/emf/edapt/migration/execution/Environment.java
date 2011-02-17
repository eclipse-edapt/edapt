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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.migration.MigrationPlugin;


/**
 * Environment for groovy evaluation
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class Environment {

	/**
	 * Singleton instance
	 */
	private static Environment instance;

	/**
	 * Header contents
	 */
	private String header;

	/**
	 * Hidden constructor
	 */
	private Environment() {
		// hidden
	}

	/**
	 * Get the header contents
	 * 
	 * @return Contents
	 */
	public String getHeader() {
		if (header == null) {
			if (Platform.isRunning()) {
				try {
					URL url = MigrationPlugin.getPlugin().getBundle()
							.getResource("groovy/header.groovy");
					header = FileUtils.getContents(url.openStream());
				} catch (IOException e) {
					LoggingUtils.logError(MigrationPlugin.getPlugin(), e);
				}
			} else {
				header = FileUtils.getContents(new File(
						"../org.eclipse.emf.edapt.cope.migration/groovy/header.groovy"));
			}
		}
		return header;
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return Instance
	 */
	public static Environment getInstance() {
		if (instance == null) {
			instance = new Environment();
		}
		return instance;
	}
}
