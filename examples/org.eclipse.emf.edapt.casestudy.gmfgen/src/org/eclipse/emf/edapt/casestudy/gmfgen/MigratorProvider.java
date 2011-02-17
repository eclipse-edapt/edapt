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
package org.eclipse.emf.edapt.casestudy.gmfgen;

import java.io.IOException;

import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.migration.execution.MigrationException;
import org.eclipse.emf.edapt.migration.execution.Migrator;


public class MigratorProvider {

	private static Migrator migrator;
	
	public static Migrator getMigrator() throws IOException, MigrationException {
		if(migrator == null) {
			migrator = new Migrator(URIUtils.getURI("migrator/."));
		}
		return migrator;
	}
}
