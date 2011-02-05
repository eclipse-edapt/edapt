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
package org.eclipse.emf.edapt.cope.tests.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * Helper methods for test cases.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class TestUtils {

	/** Constructor. */
	private TestUtils() {
		// hidden constructor
	}

	/** Create an {@link EditingDomain} based on a {@link ResourceSet}. */
	public static EditingDomain createEditingDomain(ResourceSet resourceSet) {
		AdapterFactory adapterFactory = new ReflectiveItemProviderAdapterFactory();
		CommandStack commandStack = new BasicCommandStack();
		EditingDomain editingDomain = new AdapterFactoryEditingDomain(
				adapterFactory, commandStack, resourceSet);
		return editingDomain;
	}

}
