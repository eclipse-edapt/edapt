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
package org.eclipse.emf.edapt.cope.casestudy.gmfgen;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.cope.common.URIUtils;
import org.eclipse.emf.edapt.cope.migration.execution.MigrationException;
import org.eclipse.emf.edapt.cope.migration.test.MigrationTestBase;


public class GMFGenModelMigrationTest extends MigrationTestBase {
	
	public void test228913_copier() throws Exception {
		testMigration("testmodels/228913-copier.gmfgen", 0);
	}

	public void testPatch_138440() throws Exception {
		testMigration("testmodels/patch_138440.gmfgen", 2);
	}

	public void testPatch_161380() throws Exception {
		testMigration("testmodels/patch_161380.gmfgen", 2);
	}

	public void test226149() throws Exception {
		testMigration("testmodels/test226149.gmfgen", 2);
	}

	public void testFeatureLabelModelFacet() throws Exception {
		testMigration("testmodels/testFeatureLabelModelFacet.gmfgen", 2);
	}

	public void testGenAuditRootDefaultAndNested() throws Exception {
		testMigration("testmodels/testGenAuditRootDefaultAndNested.gmfgen", 0);
	}

	public void testGenAuditRootNoDefaultButNested() throws Exception {
		testMigration("testmodels/testGenAuditRootNoDefaultButNested.gmfgen", 7);
	}

	public void testGenAudits() throws Exception {
		testMigration("testmodels/testGenAudits.gmfgen", 34);
	}

	public void testGenAuditsCorrectCategories() throws Exception {
		testMigration("testmodels/testGenAuditsCorrectCategories.gmfgen", 0);
	}

	public void testGenDiagram() throws Exception {
		testMigration("testmodels/testGenDiagram.gmfgen", 0);
	}

	public void testGenEditorAuditRootNoDefaultButNested() throws Exception {
		testMigration("testmodels/testGenEditorAuditRootNoDefaultButNested.gmfgen", 6);
	}

	public void testRequiredPluginsMoved() throws Exception {
		testMigration("testmodels/testRequiredPluginsMoved.gmfgen", 2);
	}

	private void testMigration(String fileName, int expectedNumber) throws MigrationException, IOException {
		
		URI modelURI = URIUtils.getURI(fileName);
		URI expectedTargetModelURI = URIUtils.getURI(fileName.replace(".gmfgen", "_2.gmfgen"));
		URI expectedTargetMetamodelURI = URIUtils.getURI("metamodels/gmfgen_1.248.ecore");
		
		testMigration(MigratorProvider.getMigrator(), modelURI, expectedTargetModelURI, expectedTargetMetamodelURI, expectedNumber);
	}
}
