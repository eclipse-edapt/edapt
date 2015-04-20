/*******************************************************************************
 * Copyright (c) 2007-2015 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.tests.util;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.test.MigrationTestBase;
import org.eclipse.emf.edapt.migration.test.MigrationTestSuite;
import org.eclipse.emf.edapt.migration.test.TestCaseDefinition;

/**
 * Tests the migrations of a model contained in a zip archive.
 *
 * @author jfaltermeier
 *
 */
@SuppressWarnings("restriction")
public class ArchiveTC extends MigrationTestBase {

	/** Parent test suite. */
	private final MigrationTestSuite suite;

	/** Definition of the test case. */
	private final TestCaseDefinition caseDefinition;

	/** Constructor. */
	public ArchiveTC(MigrationTestSuite suite,
		TestCaseDefinition caseDefinition) {
		setName("testMigration"); //$NON-NLS-1$

		this.caseDefinition = caseDefinition;
		this.suite = suite;
	}

	/** Resolve the {@link URI} relative to the location of the definition. */
	private URI getURI(String uri) {
		final URI definitionURI = caseDefinition.eResource().getURI();
		return URI.createFileURI(uri).resolve(definitionURI);
	}

	private URI getArchiveURI(String uri) {
		final String string = getURI(uri + ".zip").toString(); //$NON-NLS-1$
		return URI.createURI("archive:" + string + "!/" + uri + ".xmi"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}

	/** Test the migration. */
	public void testMigration() throws MigrationException, IOException {
		final URI modelURI = getArchiveURI(caseDefinition.getModel());
		final URI expectedURI = getURI(caseDefinition.getExpectedModel());
		final URI historyURI = getURI(caseDefinition.getSuite().getHistory());
		final URI metamodelURI = URIUtils.replaceExtension(historyURI, "ecore"); //$NON-NLS-1$

		testMigration(suite.getMigrator(), modelURI, expectedURI, metamodelURI,
			caseDefinition.getExpectedDifferences());
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return caseDefinition.getName();
	}
}
