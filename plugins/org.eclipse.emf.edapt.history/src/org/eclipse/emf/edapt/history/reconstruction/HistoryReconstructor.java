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

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.util.HistoryUtils;

/**
 * Reconstructor to reproduce the history.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class HistoryReconstructor extends ReconstructorBase {

	/** Mapping from old to new elements and vice versa. */
	private Mapping mapping;

	/** Resource for reproduced history. */
	private Resource historyResource;

	/** Reproduced history. */
	private History reproducedHistory;

	/** {@inheritDoc} */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		this.mapping = mapping;
	}

	/** {@inheritDoc} */
	@Override
	public void startHistory(History originalHistory) {
		reproducedHistory = (History) mapping.copyResolveTarget(
				originalHistory, false);
		mapping.map(originalHistory, reproducedHistory);
	}

	/** {@inheritDoc} */
	@Override
	public void startRelease(Release originalRelease) {
		copy(originalRelease);
	}

	/** {@inheritDoc} */
	@Override
	public void startChange(Change originalChange) {
		if (isCompositeChange(originalChange)) {
			copy(originalChange);
		} else if (originalChange.eContainer() instanceof Create) {
			originalChange = (Create) originalChange.eContainer();
			if (mapping.getTarget(originalChange) != null) {
				return;
			}
			Change reproducedChange = (Change) copy(originalChange);

			createHistoryResource(reproducedChange);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endChange(Change originalChange) {
		if (isCompositeChange(originalChange)) {
			// do nothing
		} else if (!(originalChange instanceof Create)
				|| ((Create) originalChange).getChanges().isEmpty()) {
			Change reproducedChange = (Change) copy(originalChange);

			createHistoryResource(reproducedChange);
		}
	}

	/** Determine whether a change is composite. */
	private boolean isCompositeChange(Change change) {
		return change instanceof CompositeChange
				|| change instanceof MigrationChange;
	}

	/** Reproduce a history element. */
	private EObject copy(EObject original) {
		EObject reproduced = mapping.copyResolveTarget(original, false);
		if (original instanceof Delete) {
			Delete delete = (Delete) original;
			EObject target = mapping.getTarget(delete.getElement());
			((Delete) reproduced).setElement(target);
		} else if (original instanceof OperationChange) {
			OperationChange operationChange = (OperationChange) original;
			OperationInstance target = (OperationInstance) mapping
					.copyResolveTarget(operationChange.getOperation());
			((OperationChange) reproduced).setOperation(target);
		}
		mapping.map(original, reproduced);
		attach(original, reproduced);
		return reproduced;
	}

	/** Attach a reproduced history element to its parent. */
	@SuppressWarnings("unchecked")
	private void attach(EObject original, EObject reproduced) {
		EReference reference = original.eContainmentFeature();
		EObject reproducedContainer = mapping.resolveTarget(original
				.eContainer());
		if (reference.isMany()) {
			((List) reproducedContainer.eGet(reference)).add(reproduced);
		} else {
			reproducedContainer.eSet(reference, reproduced);
		}
	}

	/** Create the resource containing the reproduced history. */
	private void createHistoryResource(Change reproducedChange) {
		if (reproducedChange instanceof Create) {
			Create createChild = (Create) reproducedChange;
			if (createChild.getElement() instanceof EPackage
					&& createChild.getTarget() == null) {
				if (historyResource == null) {
					EPackage reproducedPackage = (EPackage) createChild
							.getElement();
					Resource modelResource = reproducedPackage.eResource();
					historyResource = createHistoryResource(modelResource);
					historyResource.getContents().add(reproducedHistory);
				}
			}
		}
	}

	/** Create the resource containing the reproduced history. */
	private Resource createHistoryResource(Resource modelResource) {
		URI modelURI = modelResource.getURI();
		String name = modelURI.trimFileExtension().lastSegment();
		URI folder = modelURI.trimSegments(1);
		ResourceSet resourceSet = modelResource.getResourceSet();
		Resource historyResource = resourceSet.createResource(folder
				.appendSegment(name).appendFileExtension(
						HistoryUtils.HISTORY_FILE_EXTENSION));
		resourceSet.getResources().add(historyResource);
		return historyResource;
	}
}
