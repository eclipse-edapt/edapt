/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.migration.ui;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;

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
	@Override
	public Object execute(ExecutionEvent event) {
		final IFile file = HandlerUtils.getSelectedElement(event, IFile.class);
		final URI uri = URIUtils.getURI(file);
		try {
			final ResourceSet resourceSet = ResourceUtils.loadResourceSet(uri);
			final List<EPackage> ePackages = ResourceUtils.getRootElements(
				resourceSet, EPackage.class);
			ResourceUtils.register(ePackages, EPackage.Registry.INSTANCE);
		} catch (final IOException e) {
			LoggingUtils.logError(MigrationUIActivator.getDefault(), e);
		}
		return null;
	}
}
