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
package org.eclipse.emf.edapt.migration.execution;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.ReversableMap;
import org.eclipse.emf.edapt.common.TwoWayIdentityHashMap;
import org.eclipse.emf.edapt.migration.AttributeSlot;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ModelResource;
import org.eclipse.emf.edapt.migration.ReferenceSlot;
import org.eclipse.emf.edapt.migration.Slot;
import org.eclipse.emf.edapt.migration.Type;


/**
 * Convert a model graph to an EMF model
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class BackwardConverter {

	/**
	 * Mapping from graph nodes to EMF model elements
	 */
	private ReversableMap<Instance, EObject> mapping;
	
	/**
	 * Convert model graph to EMF elements
	 * 
	 * @param model
	 * @return EMF elements
	 */
	public ResourceSet convert(Model model) {
		hackMetamodel(model.getMetamodel());
		ResourceSet resourceSet = new ResourceSetImpl();
		ResourceUtils.register(model.getMetamodel().getEPackages(), resourceSet.getPackageRegistry());
		
		mapping = new TwoWayIdentityHashMap<Instance, EObject>();
		
		// create an EMF model element for each node
		for(Type type : model.getTypes()) {
			createObjects(type);
		}
		
		// initialize the EMF model elements based on the edges
		for(Type type : model.getTypes()) {
			for(Instance element : type.getInstances()) {
				initObject(element);
			}			
		}
		
		// determine root EMF model elements
		for(ModelResource modelResource : model.getResources()) {
			Resource resource = resourceSet.createResource(modelResource.getUri());
			for(Instance element : modelResource.getRootInstances()) {
				resource.getContents().add(resolve(element));
			}
			if(resource instanceof XMLResource) {
				XMLResource xmlResource = (XMLResource) resource;
				for(Iterator<EObject> i = resource.getAllContents(); i.hasNext(); ) {
					EObject eObject = i.next();
					Instance instance = mapping.reverseGet(eObject);
					String uuid = instance.getUuid();
					if(uuid != null) {
						xmlResource.setID(eObject, uuid);
					}
				}
			}
		}
		
		return resourceSet;
	}

	/**
	 * Initialize an EMF model element based on the edges outgoing from a node
	 * 
	 * @param element Node
	 */
	@SuppressWarnings("unchecked")
	private void initObject(Instance element) {
		EObject eObject = resolve(element);
		for(Slot slot : element.getSlots()) {
			EStructuralFeature feature = slot.getEFeature();
			if(ignore(feature)) {
				continue;
			}
			if(slot instanceof AttributeSlot) {
				AttributeSlot attributeSlot = (AttributeSlot) slot;
				EAttribute attribute = attributeSlot.getEAttribute();
				eObject.eSet(attribute, element.get(attribute));
			}
			else {
				ReferenceSlot referenceSlot = (ReferenceSlot) slot;
				EReference reference = referenceSlot.getEReference();
				if(reference.isMany()) {
					EList values = (EList) eObject.eGet(reference);
					int index = 0;
					for(Instance value : referenceSlot.getValues()) {
						EObject valueEObject = resolve(value);
						if(reference.isUnique() && values.contains(valueEObject)) {
							values.move(index, valueEObject);
						}
						else {
							values.add(index, valueEObject);
						}
						index++;
					}
				}
				else {
					if(!referenceSlot.getValues().isEmpty()) {
						eObject.eSet(reference, resolve(referenceSlot.getValues().get(0)));
					}
				}
			}
		}
	}

	/**
	 * Determines whether a certain feature should be ignored during conversion
	 */
	private boolean ignore(EStructuralFeature feature) {
		return feature.isTransient() || !feature.isChangeable() ||
		// according to
		// http://www.eclipse.org/newsportal/article.php?id=26780&group=eclipse.tools.emf
		// the following three references need to be ignored
		EcorePackage.eINSTANCE.getEClass_ESuperTypes().equals(feature) ||
		EcorePackage.eINSTANCE.getETypedElement_EType().equals(feature) ||
		EcorePackage.eINSTANCE.getEOperation_EExceptions().equals(feature);
	}

	/**
	 * Create all EMF model elements of a certain type
	 * 
	 * @param type Type
	 */
	private void createObjects(Type type) {
		EClass eClass = type.getEClass();
		for(Instance element : type.getInstances()) {
			EObject eObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
			if(element.isProxy()) {
				((InternalEObject) eObject).eSetProxyURI(element.getUri());
			}
			mapping.put(element, eObject);
		}
	}

	/**
	 * Get the EMF model element corresponding to a node
	 * 
	 * @param instance Node
	 * @return EMF model element
	 */
	private EObject resolve(Instance instance) {
		return mapping.get(instance);
	}
	
	
	/**
	 * Clear the internal caches within the metamodel elements
	 * 
	 * @param metamodel
	 */
	private void hackMetamodel(Metamodel metamodel) {
		for(EPackage ePackage : metamodel.getEPackages()) {
			for(Iterator<EObject> i = ePackage.eAllContents(); i.hasNext(); ) {
				EObject element = i.next();
				if(element instanceof EStructuralFeatureImpl) {
					EStructuralFeatureImpl feature = (EStructuralFeatureImpl) element;
					feature.setSettingDelegate(null);
				}
			}
		}
	}
}
