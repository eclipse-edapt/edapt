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
package org.eclipse.emf.edapt.casestudy.gmfmap;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.migration.execution.MigrationException;
import org.eclipse.emf.edapt.migration.test.MigrationTestBase;

public class GMFMapModelMigrationTest extends MigrationTestBase {
	
	public void testPatch_138440() throws Exception {
		
		testMigration("testmodels/patch_138440.gmfmap", 0);		
	}
	
	public void testPatch_161380() throws Exception {
		
		testMigration("testmodels/patch_161380.gmfmap", 1);
	}
		
	public void test227505() throws Exception {
		
		testMigration("testmodels/test227505.gmfmap", 0);	
	}
		
	public void testNotChangingOrderOfLabelMappings() throws Exception {
		
		testMigration("testmodels/testNotChangingOrderOfLabelMappings.gmfmap", 1);
	}

	private void testMigration(String fileName, int differences) throws MigrationException, IOException {
				
		URI modelURI = URIUtils.getURI(fileName);
		URI expectedTargetModelURI = URIUtils.getURI(fileName.replace(".gmfmap", "_2.gmfmap"));
		URI expectedTargetMetamodelURI = URIUtils.getURI("metamodels/gmfmap_1.58.ecore");
		
		testMigration(MigratorProvider.getMigrator(), modelURI, expectedTargetModelURI, expectedTargetMetamodelURI, differences);
	}
}
