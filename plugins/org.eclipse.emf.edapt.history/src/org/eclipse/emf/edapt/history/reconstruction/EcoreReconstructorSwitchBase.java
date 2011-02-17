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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;
import org.eclipse.emf.edapt.history.util.HistorySwitch;


/**
 * Base class for reconstructor switches
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class EcoreReconstructorSwitchBase<E> extends HistorySwitch<E> {

	/**
	 * Add a child element to the multi-valued feature of a owner element
	 * 
	 * @param element Owner element
	 * @param feature Feature 
	 * @param value Child element
	 */
	@SuppressWarnings("unchecked")
	protected void add(EObject element, EStructuralFeature feature, Object value) {
		if(feature.isMany()) {
			List list = (List) element.eGet(feature);
			list.add(value);
		}
		else {
			element.eSet(feature, value);
		}
	}
	
	/**
	 * Remove a child element from the single-valued feature of an owner element
	 * 
	 * @param element Owner element
	 * @param feature Feature
	 * @param value Child element
	 */
	@SuppressWarnings("unchecked")
	protected void remove(EObject element, EStructuralFeature feature, Object value) {
		if(feature.isMany()) {
	    	((List) element.eGet(feature)).remove(value);
	    }
	    else {
	    	element.eSet(feature, null);
	    }
	}
	
	/**
	 * Set the single-valued feature of an element with a value
	 * 
	 * @param element Element
	 * @param feature Feature
	 * @param value Value
	 */
	protected void set(EObject element, EStructuralFeature feature, Object value) {
		element.eSet(feature, value);
	}
	
	/**
	 * Create a child of a certain type within the containment reference of an owner element
	 * 
	 * @param owner Owner element
	 * @param reference Containment reference
	 * @param type Type of new child
	 * @return Created child
	 */
	protected EObject create(EObject owner, EReference reference, EClass type) {
		EObject child = type.getEPackage().getEFactoryInstance().create(type);
		add(owner, reference, child);
		return child;
	}

	/**
	 * Delete an element, all its contents and the cross references to the
	 * element and all its contents (only delete crossreferences from instances
	 * of Ecore types)
	 * 
	 * @param element
	 *            Element to delete
	 * @return Collection of all deleted elements
	 */
	@SuppressWarnings("unchecked")
	protected Collection delete(EObject element) {
		ResourceSet resourceSet = element.eResource().getResourceSet();
		Collection elements = new UniqueEList();
		elements.add(element);
		for (Iterator i = element.eAllContents(); i.hasNext();) {
			elements.add(i.next());
		}
		Map usages = UsageCrossReferencer.findAll(elements, resourceSet);
		for (Iterator i = usages.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			EObject eObject = (EObject) entry.getKey();
			Collection settings = (Collection) entry.getValue();
			for (Iterator j = settings.iterator(); j.hasNext();) {
				EStructuralFeature.Setting setting = (EStructuralFeature.Setting) j.next();
				EObject referencingEObject = setting.getEObject();
				if (!elements.contains(referencingEObject)) {
					EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
					if (eStructuralFeature.isChangeable()) {
						if (isModelElement(referencingEObject)) {
							remove(referencingEObject, eStructuralFeature, eObject);
						}
					}
				}
			}
		}
		EStructuralFeature feature = element.eContainmentFeature();
		EObject owner = element.eContainer();
		if (owner == null) {
			element.eResource().getContents().remove(element);
		} else {
			remove(owner, feature, element);
		}
		return elements;
	}

	/**
	 * Change the containment of an element
	 * 
	 * @param element Element to be moved
	 * @param target Target element
	 * @param reference 
	 */
	protected void move(EObject element, EObject target, EReference reference) {
		add(target, reference, element);
	}
	
	/**
	 * Decide whether an element is part of a metamodel
	 * 
	 * @param element Element
	 * @return true if element is part of a metamodel, false otherwise
	 */
	public static boolean isModelElement(EObject element) {
		EPackage p = element.eClass().getEPackage();
		boolean isModelElement = p == EcorePackage.eINSTANCE;
		return isModelElement;
	}
}
