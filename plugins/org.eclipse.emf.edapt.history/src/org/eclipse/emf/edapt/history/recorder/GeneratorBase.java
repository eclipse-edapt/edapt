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
package org.eclipse.emf.edapt.history.recorder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.history.Add;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.Set;


/**
 * Base class to generate the history for created elements
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public abstract class GeneratorBase {

	/**
	 * History factory for easier creation of History elements
	 */
	protected HistoryFactory factory;
	
	/**
	 * Container for changes
	 */
	protected CompositeChange changeContainer;
	
	/**
	 * References whose opposite has already been created
	 */
	private HashSet<EReference> opposite;
	
	/**
	 * Constructor
	 */
	public GeneratorBase() {
		factory = HistoryFactory.eINSTANCE;
		changeContainer = factory.createCompositeChange();
	}

	/**
	 * Generate history for a number of root elements
	 * 
	 * @param rootElements
	 */
	protected void generateElements(Collection<EObject> rootElements) {
		DirectedGraph<EObject> digraph = new DirectedGraph<EObject>();
		
		opposite = new HashSet<EReference>();
		
		for(EObject root : rootElements) {
			addContents(root, digraph);
		}
		
		addDependencies(digraph);
		
		List<EObject> order = digraph.getOrdering();
		for(EObject element : order) {
			generateElement(element);
		}
	}

	/**
	 * Add all elements to be created to a directed graph
	 * 
	 * @param parent
	 * @param digraph
	 */
	@SuppressWarnings("unchecked")
	protected void addContents(EObject parent, DirectedGraph<EObject> digraph) {
		digraph.add(parent);
		for(EReference reference : parent.eClass().getEAllReferences()) {
			if(skipFeature(reference)) {
				continue;
			}
			if(reference.isContainment()) {
				if(reference.isMany()) {
					for(EObject child : (List<EObject>) parent.eGet(reference)) {
						addContents(child, digraph);
						if(digraph.contains(child)) {
							digraph.addOrder(parent, child);
						}
					}
				}
				else {
					EObject child = (EObject) parent.eGet(reference);
					if(child != null) {
						addContents(child, digraph);
						if(digraph.contains(child)) {
							digraph.addOrder(parent, child);
						}
					}
				}
			}
		}
	}

	/**
	 * Add all dependencies between the elements to be created to the directed graph
	 * 
	 * @param digraph
	 */
	@SuppressWarnings("unchecked")
	private void addDependencies(DirectedGraph<EObject> digraph) {
		for(EObject element : digraph.getElements()) {
			for(EReference reference : element.eClass().getEAllReferences()) {
				if(skipFeature(reference)) {
					continue;
				}
				if(!reference.isContainment()) {
					if(reference.isMany()) {
						List<EObject> targets = (List<EObject>) element.eGet(reference);
						for(EObject target : targets) {
							if(digraph.contains(target)) {
								digraph.addOrder(target, element);
							}
						}
					}
					else {
						EObject target = (EObject) element.eGet(reference);
						if(target != null) {
							if(digraph.contains(target)) {
								if(reference == EcorePackage.eINSTANCE.getEReference_EOpposite()) {
									if(!opposite.contains(element)) {
										digraph.addOrder(target, element);
										opposite.add((EReference) target);
									}
								}
								else {
									digraph.addOrder(target, element);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Generate the history for an element
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	private void generateElement(EObject element) {
		Create change = create(element);
		append(change);
		for(EAttribute attribute : element.eClass().getEAllAttributes()) {
			if(skipFeature(attribute)) {
				continue;
			}
			if(element.eGet(attribute) == null) {
				if(attribute.getDefaultValue() != null) {
					change.getChanges().add(set(element, attribute.getDefaultValue(), attribute, element.eGet(attribute)));
				}
			}
			else if(!element.eGet(attribute).equals(attribute.getDefaultValue())) {
				change.getChanges().add(set(element, attribute.getDefaultValue(), attribute, element.eGet(attribute)));
			}
		}
		for(EReference reference : element.eClass().getEAllReferences()) {
			if(skipFeature(reference)) {
				continue;
			}
			if(!reference.isContainment()) {
				if(reference.isMany()) {
					for(EObject value : (List<EObject>) element.eGet(reference)) {
						change.getChanges().add(add(element, reference, value));
					}
				}
				else {
					EObject value = (EObject) element.eGet(reference);
					if(value != null) {
						if(reference == EcorePackage.eINSTANCE.getEReference_EOpposite()) {
							if(!opposite.contains(element)) {
								change.getChanges().add(set(element, null, reference, value));
								change.getChanges().add(set(value, null, reference, element));								
							}
						}
						else {
							change.getChanges().add(set(element, null, reference, value));
						}
					}
				}
			}
		}
	}

	/**
	 * Skip certain features
	 * 
	 * @param feature
	 * @return true if feature has to be skipped, false otherwise
	 */
	protected boolean skipFeature(EStructuralFeature feature) {
		if(feature instanceof EReference) {
			EReference reference = (EReference) feature;
			if(reference.getEOpposite() != null && reference.getEOpposite().isContainment() || isGenericReference(reference)) {
				return true;
			}
		}
		return feature.isDerived() || feature.isTransient() || !feature.isChangeable();
	}

	/**
	 * Determine whether a reference is a generic one
	 * 
	 * @param reference
	 * @return true if reference is a generic one, false otherwise
	 */
	private boolean isGenericReference(EReference reference) {
		return
			reference == EcorePackage.eINSTANCE.getETypedElement_EGenericType() ||
			reference == EcorePackage.eINSTANCE.getEClass_EGenericSuperTypes();
	}

	/**
	 * Add a create change
	 * (target and containment feature will be inferred with the help of reflective API)
	 * 
	 * @param created Element to be created
	 * @return Change
	 */
	protected Create create(EObject created) {
		Create createChild = factory.createCreate();
		createChild.setElement(created);
		createChild.setTarget(created.eContainer());
		createChild.setReference(created.eContainmentFeature());
		return createChild;
	}

	/**
	 * Add a delete change
	 * 
	 * @param element Element to be deleted
	 * @return Change
	 */
	protected Delete delete(EObject element, EReference reference, EObject target) {
		Delete delete = factory.createDelete();
		delete.setElement(element);
		delete.setReference(reference);
		delete.setTarget(target);
		return delete;
	}

	/**
	 * Add a move change of an element to a new container
	 * 
	 * @param element Element to be moved
	 * @param target Target
	 * @param reference
	 * @return Change
	 */
	protected Move move(EObject element, EObject source, EReference reference, EObject target) {
		Move move = factory.createMove();
		move.setElement(element);
		move.setSource(source);
		move.setReference(reference);
		move.setTarget(target);
		return move;
	}

	/**
	 * Add a set change
	 * 
	 * @param element Changed element
	 * @param feature Attribute to be set
	 * @param value Value
	 * @return Change
	 */
	protected Set set(EObject element, Object oldValue, EStructuralFeature feature, Object value) {
		Set set = factory.createSet();
		set.setElement(element);
		set.setFeature(feature);
		set.setOldValue(oldValue);
		set.setValue(value);
		return set;
	}

	/**
	 * Add a remove change
	 * 
	 * @param element Changed element
	 * @param feature Reference from which the element should be removed
	 * @param value Element to be removed
	 * @return Change
	 */
	protected Remove remove(EObject element, EStructuralFeature feature, Object value) {
		Remove remove = factory.createRemove();
		remove.setElement(element);
		remove.setFeature(feature);
		remove.setValue(value);
		return remove;
	}

	/**
	 * Add an add change
	 * 
	 * @param element Changed element
	 * @param feature Reference to which the element should be added
	 * @param value Element to be added
	 * @return Change
	 */
	protected Add add(EObject element, EStructuralFeature feature, Object value) {
		Add add = factory.createAdd();
		add.setElement(element);
		add.setFeature(feature);
		add.setValue(value);
		return add;
	}

	/**
	 * Append a primitive change
	 */
	protected void append(PrimitiveChange change) {
		changeContainer.getChanges().add(change);
	}
}
