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
package org.eclipse.emf.edapt.cdo.migration.execution;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.cdo.migration.StrategyBackwardConverter;
import org.eclipse.emf.edapt.cdo.migration.StrategyForwardConverter;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.Persistency;

/**
 * Helper class for loading and saving models.
 * 
 * @author herrmama
 * @author Christophe Bouhier
 * @version $Rev$
 * @levd.rating YELLOW Hash: 7340771F1DE173BDAA2534B8901681B1
 */
public class CDOPersistency extends Persistency {

	/**
	 * Save model to an explity URI.
	 * 
	 * @param model
	 * @param extent
	 * @param list
	 * @throws IOException
	 */
	public static void saveModel(Model model, MetamodelExtent extent,
			List<URI> list) throws IOException {

		StrategyBackwardConverter bConverter = new StrategyBackwardConverter(
				extent, list);
		ResourceSet resourceSet = bConverter.convert(model);
		ResourceUtils.saveResourceSet(resourceSet);
	}

	public static Model loadModel(List<URI> modelURIs, Metamodel metamodel,
			ResourceSet set) throws IOException {
		ResourceUtils.loadResourceSet(modelURIs, set);
		StrategyForwardConverter fConverter = new StrategyForwardConverter(
				metamodel.getEPackages());
		Model model = fConverter.convert(set);
		model.setMetamodel(metamodel);
		return model;
	}

}
