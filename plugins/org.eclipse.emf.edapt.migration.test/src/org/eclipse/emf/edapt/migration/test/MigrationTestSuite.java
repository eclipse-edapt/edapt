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
package org.eclipse.emf.edapt.migration.test;

import java.io.IOException;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.migration.execution.incubator.Migrator;

/**
 * Suite to test a migration.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigrationTestSuite extends TestSuite {

	/** Definition of the test suite. */
	private final TestSuiteDefinition suiteDefinition;

	/** Migrator. */
	private Migrator migrator;

	/** Constructor. */
	public MigrationTestSuite(URI definitionURI) throws IOException {
		this(loadTestSuiteDefinition(definitionURI));
	}

	/** Helper method to load a test suite definition. */
	private static TestSuiteDefinition loadTestSuiteDefinition(URI definitionURI)
			throws IOException {
		// ensure that test metamodel is initialized.
		TestPackage.eINSTANCE.getTestCaseDefinition();
		return (TestSuiteDefinition) ResourceUtils.loadElement(definitionURI);
	}

	/** Constructor. */
	public MigrationTestSuite(TestSuiteDefinition suiteDefinition) {
		super(suiteDefinition.getName());

		this.suiteDefinition = suiteDefinition;

		for (TestCaseDefinition caseDefinition : suiteDefinition.getCases()) {
			addTest(new MigrationTestCase(this, caseDefinition));
		}
	}

	@Override
	public void run(TestResult result) {
		try {
			migrator = loadMigrator();
			super.run(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Resolve the {@link URI} relative to the location of the definition. */
	private URI getURI(String uri) {
		URI definitionURI = suiteDefinition.eResource().getURI();
		return URI.createFileURI(uri).resolve(definitionURI);
	}

	/** Load the history. */
	private History loadHistory() throws IOException {
		// ensure that history metamodel is initialized.
		HistoryPackage.eINSTANCE.getHistory();
		URI historyURI = getURI(suiteDefinition.getHistory());
		History history = ResourceUtils.loadElement(historyURI);
		return history;
	}

	/** Get the migrator. */
	Migrator getMigrator() {
		return migrator;
	}

	private Migrator loadMigrator() throws IOException {
		History history = loadHistory();
		Migrator migrator = new Migrator(history);
		return migrator;
	}
}
