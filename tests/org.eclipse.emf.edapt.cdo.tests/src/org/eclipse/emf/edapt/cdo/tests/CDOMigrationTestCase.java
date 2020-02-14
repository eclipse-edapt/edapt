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
package org.eclipse.emf.edapt.cdo.tests;

import java.io.IOException;

import org.eclipse.emf.cdo.util.CDOURIData;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.test.TestCaseDefinition;

/**
 * Case to test a migration.
 *
 */
@SuppressWarnings("restriction")
public class CDOMigrationTestCase extends CDOMigrationTestBase {

	/** Parent test suite. */
	private final CDOMigrationTestSuite suite;

	/** Definition of the test case. */
	private final TestCaseDefinition caseDefinition;

	/** Constructor. */
	public CDOMigrationTestCase(CDOMigrationTestSuite suite,
		TestCaseDefinition caseDefinition) {
		setName("testMigration");

		this.caseDefinition = caseDefinition;
		this.suite = suite;
	}

	/** Resolve the {@link URI} relative to the location of the definition. */
	private URI getURI(String uri) {
		final URI definitionURI = caseDefinition.eResource().getURI();
		return URI.createFileURI(uri).resolve(definitionURI);
	}

	/**
	 * Test the migration. TODO, A hack for now, but if the modelURI's is empty
	 * and the expected URI is also empty, we call the migration for the whole
	 * repository. We could elaborate by analyzing the URI with {@link CDOURIData} and get information.
	 *
	 */
	public void testMigration() throws MigrationException, IOException {

		final URI modelURI = getURI(caseDefinition.getModel());
		final CDOURIData sourceCDOURIData = new CDOURIData(caseDefinition.getModel());

		final URI expectedURI = getURI(caseDefinition.getExpectedModel());
		final CDOURIData targetCDOURIData = new CDOURIData(caseDefinition.getExpectedModel());

		final URI historyURI = getURI(caseDefinition.getSuite().getHistory());
		final URI metamodelURI = URIUtils.replaceExtension(historyURI, "ecore");

		if (!sourceCDOURIData.getResourcePath().isEmpty()) {

			testMigration(suite.getMigrator(), modelURI, expectedURI,
				metamodelURI, caseDefinition.getExpectedDifferences());
		} else {
			testMigration(sourceCDOURIData, targetCDOURIData,
				suite.getMigrator(),
				caseDefinition.getExpectedDifferences());
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return caseDefinition.getName();
	}

}
