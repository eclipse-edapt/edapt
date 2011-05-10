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
package org.eclipse.emf.edapt.migration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edapt.common.EcoreUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.ReversableMap;
import org.eclipse.emf.edapt.common.TwoWayIdentityHashMap;

/**
 * Convert a model graph to an EMF model.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 505F0D3E0BF01E7B21A8F8CAE80B57F6
 */
public class BackwardConverter {

	/** Mapping from graph nodes to EMF model elements. */
	private ReversableMap<Instance, EObject> mapping;

	/** Convert model graph to EMF elements. */
	public ResourceSet convert(Model model) {
		model.getMetamodel().refreshCaches();

		mapping = new TwoWayIdentityHashMap<Instance, EObject>();

		initObjects(model);
		ResourceSet resourceSet = initResources(model);
		initProperties(model);

		return resourceSet;
	}

	/** Create an EMF model element for each node. */
	private void initObjects(Model model) {
		for (Type type : model.getTypes()) {
			createObjects(type);
		}
	}

	/** Determine root EMF model elements. */
	private ResourceSet initResources(Model model) {
		ResourceSet resourceSet = new ResourceSetImpl();
		ResourceUtils.register(model.getMetamodel().getEPackages(), resourceSet
				.getPackageRegistry());
		for (ModelResource modelResource : model.getResources()) {
			Resource resource = resourceSet.createResource(modelResource
					.getUri());
			for (Instance element : modelResource.getRootInstances()) {
				resource.getContents().add(resolve(element));
			}
		}
		return resourceSet;
	}

	/** Initialize the EMF model elements based on the edges. */
	private void initProperties(Model model) {
		for (Type type : model.getTypes()) {
			for (Instance instance : type.getInstances()) {
				initObject(instance);
				String uuid = instance.getUuid();
				if (uuid != null) {
					EObject eObject = resolve(instance);
					EcoreUtils.setUUID(eObject, uuid);
				}
			}
		}
	}

	/** Initialize an EMF model element based on the edges outgoing from a node. */
	@SuppressWarnings("unchecked")
	private void initObject(Instance element) {
		EObject eObject = resolve(element);
		for (Slot slot : element.getSlots()) {
			EStructuralFeature feature = slot.getEFeature();
			if (ignore(feature)) {
				continue;
			}
			if (slot instanceof AttributeSlot) {
				AttributeSlot attributeSlot = (AttributeSlot) slot;
				EAttribute attribute = attributeSlot.getEAttribute();
				eObject.eSet(attribute, element.get(attribute));
			} else {
				ReferenceSlot referenceSlot = (ReferenceSlot) slot;
				EReference reference = referenceSlot.getEReference();
				if (reference.isMany()) {
					EList values = (EList) eObject.eGet(reference);
					int index = 0;
					for (Instance value : referenceSlot.getValues()) {
						EObject valueEObject = resolve(value);
						if (reference.isUnique()
								&& values.contains(valueEObject)) {
							values.move(index, valueEObject);
						} else {
							values.add(index, valueEObject);
						}
						index++;
					}
				} else {
					if (!referenceSlot.getValues().isEmpty()) {
						eObject.eSet(reference, resolve(referenceSlot
								.getValues().get(0)));
					}
				}
			}
		}
	}

	/**
	 * Determines whether a certain feature should be ignored during conversion.
	 */
	private boolean ignore(EStructuralFeature feature) {
		return feature.isTransient()
				|| !feature.isChangeable()
				||
				// according to
				// http://www.eclipse.org/newsportal/article.php?id=26780&group=eclipse.tools.emf
				// the following three references need to be ignored
				EcorePackage.eINSTANCE.getEClass_ESuperTypes().equals(feature)
				|| EcorePackage.eINSTANCE.getETypedElement_EType().equals(
						feature)
				|| EcorePackage.eINSTANCE.getEOperation_EExceptions().equals(
						feature);
	}

	/** Create all EMF model elements of a certain type. */
	private void createObjects(Type type) {
		EClass eClass = type.getEClass();
		for (Instance element : type.getInstances()) {
			EObject eObject = eClass.getEPackage().getEFactoryInstance()
					.create(eClass);
			if (element.isProxy()) {
				((InternalEObject) eObject).eSetProxyURI(element.getUri());
			}
			mapping.put(element, eObject);
		}
	}

	/** Get the EMF model element corresponding to a node. */
	private EObject resolve(Instance instance) {
		return mapping.get(instance);
	}
}
