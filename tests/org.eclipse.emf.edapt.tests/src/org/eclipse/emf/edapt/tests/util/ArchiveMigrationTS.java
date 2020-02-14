/*******************************************************************************
 * Copyright (c) 2007-2015 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.tests.util;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.migration.test.MigrationTestSuite;
import org.eclipse.emf.edapt.migration.test.TestCaseDefinition;

import junit.framework.Test;

/**
 *
 * {@link MigrationTestSuite} extensions for create archive test cases.
 *
 * @author jfaltermeier
 *
 */
@SuppressWarnings("restriction")
public class ArchiveMigrationTS extends MigrationTestSuite {

	public ArchiveMigrationTS(URI definitionURI, IClassLoader loader) throws IOException {
		super(definitionURI, loader);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.edapt.migration.test.MigrationTestSuite#createTest(org.eclipse.emf.edapt.migration.test.TestCaseDefinition)
	 */
	@Override
	protected Test createTest(TestCaseDefinition caseDefinition) {
		return new ArchiveTC(this, caseDefinition);
	}

}
