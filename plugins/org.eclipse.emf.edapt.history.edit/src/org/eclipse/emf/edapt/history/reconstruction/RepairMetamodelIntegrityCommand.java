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
package org.eclipse.emf.edapt.history.reconstruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


// CB Migrate
//import org.eclipse.emf.compare.diff.merge.service.MergeService;
//import org.eclipse.emf.compare.diff.metamodel.DiffElement;
//import org.eclipse.emf.compare.diff.metamodel.DiffModel;
//import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.recorder.IChangeProvider;
import org.eclipse.emf.edit.command.ChangeCommand;


/**
 * Command to adapt the current metamodel version to the version reconstructed
 * from the history.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class RepairMetamodelIntegrityCommand extends ChangeCommand implements
		IChangeProvider {

	/** Differences between metamodel and history. */
	
	
	// CB Migrate
//	private final DiffResourceSet diffResourceSet;

	/** Constructor. */
	@SuppressWarnings("unchecked")
	
	public RepairMetamodelIntegrityCommand(Collection<EPackage> rootPackages){
		super((Collection) rootPackages);
	}
	
	
	// CB Migrate
//	public RepairMetamodelIntegrityCommand(Collection<EPackage> rootPackages,
//			DiffResourceSet diffResourceSet) {
//		super((Collection) rootPackages);
//		this.diffResourceSet = diffResourceSet;
//	}

	/** {@inheritDoc} */
	@Override
	protected void doExecute() {
//		List<DiffElement> elements = new ArrayList<DiffElement>();
//		for (DiffModel diffModel : diffResourceSet.getDiffModels()) {
//			elements.addAll(diffModel.getOwnedElements());
//		}
//		MergeService.merge(elements, false);
	}

	/** {@inheritDoc} */
	public List<Change> getChanges(List<PrimitiveChange> changes) {
		return Collections.emptyList();
	}

	/** {@inheritDoc} */
	@Override
	public ChangeDescription getChangeDescription() {
		return super.getChangeDescription();
	}
}