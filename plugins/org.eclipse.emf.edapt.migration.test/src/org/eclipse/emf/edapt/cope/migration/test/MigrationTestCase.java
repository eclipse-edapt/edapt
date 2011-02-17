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
package org.eclipse.emf.edapt.cope.migration.test;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.cope.common.URIUtils;
import org.eclipse.emf.edapt.cope.migration.execution.MigrationException;

/**
 * Case to test a migration.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigrationTestCase extends MigrationTestBase {

	/** Parent test suite. */
	private final MigrationTestSuite suite;

	/** Definition of the test case. */
	private final TestCaseDefinition caseDefinition;

	/** Constructor. */
	public MigrationTestCase(MigrationTestSuite suite,
			TestCaseDefinition caseDefinition) {
		setName("testMigration");

		this.caseDefinition = caseDefinition;
		this.suite = suite;
	}

	/** Resolve the {@link URI} relative to the location of the definition. */
	private URI getURI(String uri) {
		URI definitionURI = caseDefinition.eResource().getURI();
		return URI.createFileURI(uri).resolve(definitionURI);
	}

	/** Test the migration. */
	public void testMigration() throws MigrationException, IOException {
		URI modelURI = getURI(caseDefinition.getModel());
		URI expectedURI = getURI(caseDefinition.getExpectedModel());
		URI historyURI = getURI(caseDefinition.getSuite().getHistory());
		URI metamodelURI = URIUtils.replaceExtension(historyURI, "ecore");

		testMigration(suite.getMigrator(), modelURI, expectedURI, metamodelURI,
				caseDefinition.getExpectedDifferences());
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return caseDefinition.getName();
	}
}
