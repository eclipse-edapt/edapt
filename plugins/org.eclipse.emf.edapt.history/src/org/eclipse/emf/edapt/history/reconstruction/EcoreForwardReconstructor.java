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

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.Add;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.HistoryPlugin;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.Set;


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
		
		if(!(change instanceof Delete)) {
			ecoreSwitch.doSwitch(change);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void endChange(Change change) {
		if(change instanceof Delete) {
			ecoreSwitch.doSwitch(change);
		}

		super.endChange(change);
	}

	/** Save all metamodel resources. */
	public void save() {
		try {
			ResourceUtils.saveResourceSet(resourceSet);
		} catch (IOException e) {
			LoggingUtils.logError(HistoryPlugin.getPlugin(),
					e);
		}
	}

	/** Switch to perform reconstruction depending on change. */
	private class EcoreReconstructorSwitch extends EcoreReconstructorSwitchBase<Object> {
		
		/** {@inheritDoc} */
		@Override
		public Object caseSet(Set operation) {
			EObject element = mapping.getTarget(operation.getElement());
			EStructuralFeature feature = operation.getFeature();
			Object value = mapping.resolveTarget(operation.getValue());
			
			set(element, feature, value);
			return operation;
		}
		
		/** {@inheritDoc} */
		@Override
		public Object caseCreate(Create operation) {
			
			EClass type = operation.getElement().eClass();

			EObject element = null;
			if(operation.getTarget() != null) {
				EObject target = mapping.getTarget(operation.getTarget());
				EReference reference = operation.getReference();
				
				element = create(target, reference, type);
			}
			else {
				element = type.getEPackage().getEFactoryInstance().create(type);
				String filename = operation.getElement().eResource().getURI().lastSegment();
				URI uri = folder.appendSegment(filename);
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
			EObject element = mapping.getTarget(operation.getElement());
			
			delete(element);							
			return operation;
		}
		
		/** {@inheritDoc} */
		@Override
		public Object caseMove(Move operation) {
			EObject element = mapping.getTarget(operation.getElement());
			EObject target = mapping.getTarget(operation.getTarget());
			EReference reference = operation.getReference();
			
			move(element, target, reference);
			return operation;
		}
		
		/** {@inheritDoc} */
		@Override
		public Object caseAdd(Add operation) {
			
			EObject element = mapping.getTarget(operation.getElement());
			Object value = mapping.resolveTarget(operation.getValue());
			EStructuralFeature feature = operation.getFeature();
			
			add(element, feature, value);
			return operation;
		}
		
		/** {@inheritDoc} */
		@Override
		public Object caseRemove(Remove operation) {
			
			EObject element = mapping.getTarget(operation.getElement());
			Object value = mapping.resolveTarget(operation.getValue());
			EStructuralFeature feature = operation.getFeature();
			
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
