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

import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.spi.history.Add;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.Move;
import org.eclipse.emf.edapt.spi.history.Remove;
import org.eclipse.emf.edapt.spi.history.Set;


/**
 * Reconstructor for reconstructing a certain version of the metamodel by
 * visiting the history in backward direction.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class EcoreBackwardReconstructor extends BackwardReconstructorBase {

	/** Target folder of the reproduced metamodel. */
	private final URI folder;

	/** Switch to perform reconstruction depending on change. */
	private EcoreReconstructorSwitch ecoreSwitch;

	/** Constructor. */
	public EcoreBackwardReconstructor(URI folder) {
		this.folder = folder;
	}

	/** {@inheritDoc} */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		ecoreSwitch = new EcoreReconstructorSwitch();
		for (EPackage p : extent.getRootPackages()) {
			String filename = mapping.getSource(p).eResource().getURI()
					.lastSegment();
			URI uri = folder.appendSegment(filename);
			Resource resource = resourceSet.getResource(uri, false);
			if (resource == null) {
				resource = resourceSet.createResource(uri);
			}
			resource.getContents().add(p);
		}

		super.init(mapping, extent);
	}

	/** {@inheritDoc} */
	@Override
	public void startChange(Change change) {
		super.startChange(change);

		if (change instanceof Create) {
			ecoreSwitch.doSwitch(change);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endChange(Change change) {
		super.endChange(change);

		if (!(change instanceof Create)) {
			ecoreSwitch.doSwitch(change);
		}
	}

	/** Switch to perform reconstruction depending on change. */
	private class EcoreReconstructorSwitch extends
			EcoreReconstructorSwitchBase<Object> {

		/** {@inheritDoc} */
		@Override
		public Object caseSet(Set operation) {
			EObject element = mapping.getTarget(operation.getElement());
			EStructuralFeature feature = operation.getFeature();
			Object value = mapping.resolveTarget(operation.getOldValue());

			set(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseCreate(Create operation) {
			EObject element = mapping.getTarget(operation.getElement());
			Resource toRemove = null;
			if (operation.getTarget() == null) {
				if (element.eResource().getContents().size() == 1) {
					toRemove = element.eResource();
				}
			}

			delete(element);
			if (toRemove != null) {
				resourceSet.getResources().remove(toRemove);
			}
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseDelete(Delete operation) {
			EObject target = mapping.getTarget(operation.getTarget());
			EReference reference = operation.getReference();

			org.eclipse.emf.ecore.util.EcoreUtil.Copier copier = new org.eclipse.emf.ecore.util.EcoreUtil.Copier();
			EObject reproducedElement = copier.copy(operation.getElement());
			copier.copyReferences();
			for (Entry<EObject, EObject> entry : copier.entrySet()) {
				mapping.map(entry.getKey(), entry.getValue());
				extent.addToExtent(entry.getValue());
			}
			add(target, reference, reproducedElement);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseMove(Move operation) {
			EObject element = mapping.getTarget(operation.getElement());
			EObject source = mapping.getTarget(operation.getSource());
			EReference reference = operation.getReference();

			move(element, source, reference);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseAdd(Add operation) {
			EObject element = mapping.getTarget(operation.getElement());
			Object value = mapping.resolveTarget(operation.getValue());
			EStructuralFeature feature = operation.getFeature();

			remove(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseRemove(Remove operation) {
			EObject element = mapping.getTarget(operation.getElement());
			Object value = mapping.resolveTarget(operation.getValue());
			EStructuralFeature feature = operation.getFeature();

			add(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseCompositeChange(CompositeChange operation) {
			return operation;
		}
	}
}
