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

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.reconstruction.ModelAssert;
import org.eclipse.emf.edapt.migration.test.PrintStreamProgressMonitor;
import org.eclipse.emf.edapt.tests.history.lifecycle.TestBase;



public class GMFMapMetamodelAdaptationTest extends TestBase {

	public void testHistory() throws Exception {
		
		URI contextURI = URI.createFileURI(new File(".").getAbsolutePath());
				
		URI metamodelsURI = contextURI.appendSegment("metamodels");
		URI targetMetamodelURI = metamodelsURI.appendSegment("gmfmap.ecore");
		URI expectedMetamodelURI =  metamodelsURI.appendSegment("gmfmap_1.58.ecore");

		URI migratorURI = contextURI.appendSegment("migrator");

		importMigrator(migratorURI, targetMetamodelURI);
		
		EObject actual = ResourceUtils.loadResourceSet(targetMetamodelURI).getResources().get(0).getContents().get(0);
		EObject expected = ResourceUtils.loadResourceSet(expectedMetamodelURI).getResources().get(0).getContents().get(0);
		ModelAssert.assertDifference(expected, actual, 0);
	}
	
	public void testAdaptation() throws Exception {
		
		URI contextURI = URI.createFileURI(new File(".").getAbsolutePath());
		
		URI metamodelsURI = contextURI.appendSegment("metamodels");
		URI sourceMetamodelURI = metamodelsURI.appendSegment("gmfmap_1.43.ecore");
		URI targetMetamodelURI = metamodelsURI.appendSegment("gmfmap2.ecore");
		URI expectedMetamodelURI = metamodelsURI.appendSegment("gmfmap_1.58.ecore");
		FileUtils.copy(sourceMetamodelURI, targetMetamodelURI);
				
		MigratorProvider.getMigrator().adapt(targetMetamodelURI, 0, Integer.MAX_VALUE, new PrintStreamProgressMonitor(System.out));

		EObject actual = ResourceUtils.loadResourceSet(targetMetamodelURI).getResources().get(0).getContents().get(0);
		EObject expected = ResourceUtils.loadResourceSet(expectedMetamodelURI).getResources().get(0).getContents().get(0);
		ModelAssert.assertDifference(expected, actual, 0);
	}
}
