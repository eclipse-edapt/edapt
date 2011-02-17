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
package org.eclipse.emf.edapt.casestudy.gmfgraph;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.migration.execution.MigrationException;
import org.eclipse.emf.edapt.migration.test.MigrationTestBase;

public class GMFGraphModelMigrationTest extends MigrationTestBase {
	
	public void testBasicMigration() throws Exception {
		
		testMigration("testmodels/basic.gmfgraph", 0);		
	}
	
	public void testCustomFiguresMigration() throws Exception {
		
		testMigration("testmodels/customFigures.gmfgraph", 3);
	}
		
	public void testMultifileMigration() throws Exception {
		
		testMigration("testmodels/multifile_main.gmfgraph", 6);	
	}
		
	public void testTestMigration() throws Exception {
		
		testMigration("testmodels/test_main.gmfgraph", 2);
	}

	private void testMigration(String fileName, int differences) throws MigrationException, IOException {
		
		URI modelURI = URIUtils.getURI(fileName);
		URI expectedTargetModelURI = URIUtils.getURI(fileName.replace(".gmfgraph", "2.gmfgraph"));
		URI expectedTargetMetamodelURI = URIUtils.getURI("metamodels/gmfgraph_1.33.ecore");
		
		testMigration(MigratorProvider.getMigrator(), modelURI, expectedTargetModelURI, expectedTargetMetamodelURI, differences);
	}
}
