/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.reconstruction;

import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
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
		for (final EPackage p : extent.getRootPackages()) {
			final String filename = mapping.getSource(p).eResource().getURI()
				.lastSegment();
			final URI uri = folder.appendSegment(filename);
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
			final EObject element = mapping.getTarget(operation.getElement());
			final EStructuralFeature feature = operation.getFeature();
			final Object value = mapping.resolveTarget(operation.getOldValue());

			set(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseCreate(Create operation) {
			final EObject element = mapping.getTarget(operation.getElement());
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
			final EObject target = mapping.getTarget(operation.getTarget());
			final EReference reference = operation.getReference();

			final org.eclipse.emf.ecore.util.EcoreUtil.Copier copier = new org.eclipse.emf.ecore.util.EcoreUtil.Copier();
			final EObject reproducedElement = copier.copy(operation.getElement());
			copier.copyReferences();
			for (final Entry<EObject, EObject> entry : copier.entrySet()) {
				mapping.map(entry.getKey(), entry.getValue());
				extent.addToExtent(entry.getValue());
			}
			add(target, reference, reproducedElement);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseMove(Move operation) {
			final EObject element = mapping.getTarget(operation.getElement());
			final EObject source = mapping.getTarget(operation.getSource());
			final EReference reference = operation.getReference();

			move(element, source, reference);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseAdd(Add operation) {
			final EObject element = mapping.getTarget(operation.getElement());
			final Object value = mapping.resolveTarget(operation.getValue());
			final EStructuralFeature feature = operation.getFeature();

			remove(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseRemove(Remove operation) {
			final EObject element = mapping.getTarget(operation.getElement());
			final Object value = mapping.resolveTarget(operation.getValue());
			final EStructuralFeature feature = operation.getFeature();

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
