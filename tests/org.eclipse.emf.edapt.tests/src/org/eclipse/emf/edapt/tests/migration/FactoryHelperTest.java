/*******************************************************************************
 * Copyright (c) 2007-2016 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.tests.migration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.edapt.internal.migration.internal.FactoryHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the FactoryHelper.
 *
 * @author Alexandra Buzila
 */
@SuppressWarnings({ "restriction", "nls" })
public class FactoryHelperTest {
	private final String NSURI_NO_WILDCARDS = "org.eclipse.emf.edapt.tests.factory1";
	private final String NSURI_WITH_WILDCARDS = "org.eclipse.emf.edapt.tests.factory1*";
	private final String NSURI_NO_FACTORY = "org.eclipse.emf.edapt.tests.nofactory";
	private static EPackage ePackage;
	private EFactory initialFactory;

	@Before
	public void setup() {
		ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("testPackage");
		ePackage.setNsPrefix("test");
		initialFactory = EcoreFactory.eINSTANCE.createEFactory();
		ePackage.setEFactoryInstance(initialFactory);
	}

	@Test
	public void testOverrideFactoryNoRegistration() {
		ePackage.setNsURI(NSURI_NO_FACTORY);

		FactoryHelper.INSTANCE.overrideFactory(ePackage);
		final EFactory eFactoryInstance = ePackage.getEFactoryInstance();
		// no factory should be registered for the given URI, so the package must have the initial factory
		assertTrue(eFactoryInstance == initialFactory);
	}

	@Test
	public void testRegisterFactoryWithWildcards() {
		ePackage.setNsURI(NSURI_WITH_WILDCARDS + "1234");

		FactoryHelper.INSTANCE.overrideFactory(ePackage);
		final EFactory eFactoryInstance = ePackage.getEFactoryInstance();
		assertFalse(eFactoryInstance == initialFactory);
		assertTrue(eFactoryInstance instanceof FactoryHelperTestEFactory1);
	}

	@Test
	public void testRegisterFactoryWithoutWildcards() {
		ePackage.setNsURI(NSURI_NO_WILDCARDS);

		FactoryHelper.INSTANCE.overrideFactory(ePackage);
		final EFactory eFactoryInstance = ePackage.getEFactoryInstance();
		assertFalse(eFactoryInstance == initialFactory);
		assertTrue(eFactoryInstance instanceof FactoryHelperTestEFactory2);
	}

}
