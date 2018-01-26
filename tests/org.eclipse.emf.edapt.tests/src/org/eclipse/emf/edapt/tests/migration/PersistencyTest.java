/*******************************************************************************
 * Copyright (c) 2007-2017 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.tests.migration;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.IResourceSetFactory;
import org.eclipse.emf.edapt.common.IResourceSetProcessor;
import org.eclipse.emf.edapt.internal.migration.internal.Persistency;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.junit.Test;
import org.mockito.Mockito;

@SuppressWarnings("restriction")
public class PersistencyTest {

	@Test
	public void testPostLoadProcessor() throws IOException {
		/* setup */
		final List<URI> modelURIs = Collections.emptyList();
		final Metamodel metamodel = Mockito.mock(Metamodel.class);
		final ResourceSet resourceSet = Mockito.mock(ResourceSet.class);
		Mockito.doReturn(ECollections.<Resource> emptyEList()).when(resourceSet).getResources();
		final IResourceSetFactory resourceSetFactory = Mockito.mock(IResourceSetFactory.class);
		Mockito.doReturn(resourceSet).when(resourceSetFactory).createResourceSet();
		final IResourceSetProcessor postLoadProcessor = Mockito.mock(IResourceSetProcessor.class);

		/* act */
		Persistency.loadModel(modelURIs, metamodel, resourceSetFactory, postLoadProcessor);

		/* verify */
		Mockito.verify(postLoadProcessor, Mockito.times(1)).process(resourceSet);

	}

}
