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
package org.eclipse.emf.edapt.history.reconstruction;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.spi.history.Add;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.HistoryPlugin;
import org.eclipse.emf.edapt.spi.history.Move;
import org.eclipse.emf.edapt.spi.history.Remove;
import org.eclipse.emf.edapt.spi.history.Set;

/**
 * Reconstructor for reconstructing a certain version of the metamodel by
 * visiting the history in forward direction.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class EcoreForwardReconstructor extends ForwardReconstructorBase {

	/** Target folder of the reproduced metamodel. */
	private final URI folder;

	/** Switch to perform reconstruction depending on change. */
	private EcoreReconstructorSwitch ecoreSwitch;

	/** Constructor. */
	public EcoreForwardReconstructor(URI folder) {
		this.folder = folder;
	}

	/** {@inheritDoc} */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		ecoreSwitch = new EcoreReconstructorSwitch();

		super.init(mapping, extent);
	}

	/** {@inheritDoc} */
	@Override
	public void startChange(Change change) {
		super.startChange(change);

		if (!(change instanceof Delete)) {
			ecoreSwitch.doSwitch(change);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endChange(Change change) {
		if (change instanceof Delete) {
			ecoreSwitch.doSwitch(change);
		}

		super.endChange(change);
	}

	/** Save all metamodel resources. */
	public void save() {
		try {
			ResourceUtils.saveResourceSet(resourceSet);
		} catch (final IOException e) {
			LoggingUtils.logError(HistoryPlugin.getPlugin(),
				e);
		}
	}

	/** Switch to perform reconstruction depending on change. */
	private class EcoreReconstructorSwitch extends EcoreReconstructorSwitchBase<Object> {

		/** {@inheritDoc} */
		@Override
		public Object caseSet(Set operation) {
			final EObject element = mapping.getTarget(operation.getElement());
			final EStructuralFeature feature = operation.getFeature();
			final Object value = mapping.resolveTarget(operation.getValue());

			set(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseCreate(Create operation) {

			final EClass type = operation.getElement().eClass();

			EObject element = null;
			if (operation.getTarget() != null) {
				final EObject target = mapping.getTarget(operation.getTarget());
				final EReference reference = operation.getReference();

				element = create(target, reference, type);
			}
			else {
				element = type.getEPackage().getEFactoryInstance().create(type);
				String filename = operation.getElement().eResource().getURI().lastSegment();
				if (filename == null) {
					filename = operation.getElement().eResource().getURI().toString();
				}
				final URI uri = folder.appendSegment(filename);
				Resource resource = resourceSet.getResource(uri, false);
				if (resource == null) {
					resource = resourceSet.createResource(uri);
				}
				resource.getContents().add(element);
			}

			extent.addToExtent(element);
			mapping.map(operation.getElement(), element);

			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseDelete(Delete operation) {
			final EObject element = mapping.getTarget(operation.getElement());

			delete(element);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseMove(Move operation) {
			final EObject element = mapping.getTarget(operation.getElement());
			final EObject target = mapping.getTarget(operation.getTarget());
			final EReference reference = operation.getReference();

			move(element, target, reference);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseAdd(Add operation) {

			final EObject element = mapping.getTarget(operation.getElement());
			final Object value = mapping.resolveTarget(operation.getValue());
			final EStructuralFeature feature = operation.getFeature();

			add(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseRemove(Remove operation) {

			final EObject element = mapping.getTarget(operation.getElement());
			final Object value = mapping.resolveTarget(operation.getValue());
			final EStructuralFeature feature = operation.getFeature();

			remove(element, feature, value);
			return operation;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseCompositeChange(CompositeChange operation) {

			return operation;
		}
	}
}
