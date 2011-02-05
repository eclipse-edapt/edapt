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
package org.eclipse.emf.edapt.cope.tests.history.recorder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.Assert;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.cope.history.instantiation.MigratorImporter;
import org.eclipse.emf.edapt.cope.history.reconstruction.IntegrityChecker;
import org.eclipse.emf.edapt.cope.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.cope.tests.history.lifecycle.TestBase;
import org.eclipse.emf.edit.domain.EditingDomain;


public class RecorderTest extends TestBase {

	public void testGMFGenRecorder() throws IOException {
		testRecorder("org.eclipse.emf.edapt.cope.casestudy.gmfgen");
	}

	public void testGMFMapRecorder() throws IOException {
		testRecorder("org.eclipse.emf.edapt.cope.casestudy.gmfmap");
	}

	public void testGMFGraphRecorder() throws IOException {
		testRecorder("org.eclipse.emf.edapt.cope.casestudy.gmfgraph");
	}

	public void testGMFToolRecorder() throws IOException {
		testRecorder("org.eclipse.emf.edapt.cope.casestudy.gmftool");
	}

	private void testRecorder(String project) throws IOException, MalformedURLException {
		String migratorDir = "../" + project + "/migrator"; 
		URI migratorURI = URI.createFileURI(new File(migratorDir).getAbsolutePath());
		
		URI metamodelURI = migratorURI.appendSegment("release1").appendSegment("release0.ecore");
		
		EditingDomainListener listener = initRecorder(metamodelURI);		
		EditingDomain domain = listener.getEditingDomain();
		listener.release();
		
		MigratorImporter importer = new MigratorImporter(listener);
		importer.importMigrator(migratorURI);
		
		IntegrityChecker checker = new IntegrityChecker(listener.getHistory());
		Assert.assertTrue(checker.check());
	}
}
