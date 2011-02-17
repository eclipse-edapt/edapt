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
package org.eclipse.emf.edapt.cope.migration.ui;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.cope.common.LoggingUtils;
import org.eclipse.emf.edapt.cope.common.ResourceUtils;
import org.eclipse.emf.edapt.cope.common.URIUtils;
import org.eclipse.emf.edapt.cope.common.ui.HandlerUtils;


/**
 * Action to register a metamodel to the package registry.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class RegisterMetamodelHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		IFile file = HandlerUtils.getSelectedElement(event);
		URI uri = URIUtils.getURI(file);
		try {
			ResourceSet resourceSet = ResourceUtils.loadResourceSet(uri);
			List<EPackage> ePackages = ResourceUtils.getRootElements(
					resourceSet, EPackage.class);
			ResourceUtils.register(ePackages, EPackage.Registry.INSTANCE);
		} catch (IOException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(), e);
		}
		return null;
	}
}
