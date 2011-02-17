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
package org.eclipse.emf.edapt.cope.history.recorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Directed graph with elements of a certain type as nodes
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class DirectedGraph<E> {

	/**
	 * Nodes
	 */
	private List<E> elements;
	
	/**
	 * Elements which are source of incoming edges
	 */
	private Map<E, Set<E>> incoming;
	
	/**
	 * Elements which are target of outgoing edges
	 */
	private Map<E, Set<E>> outgoing;
	
	/**
	 * Default constructor
	 */
	public DirectedGraph() {
		elements = new ArrayList<E>();
		incoming = new HashMap<E, Set<E>>();
		outgoing = new HashMap<E, Set<E>>();
	}
	
	/**
	 * Constructor that adds a number of elements as initial nodes
	 * 
	 * @param elements Initial nodes
	 */
	public DirectedGraph(Collection<E> elements) {
		this();
		for(E element : elements) {
			add(element);
		}
	}
	
	/**
	 * Add an element as node
	 * 
	 * @param element Element
	 */
	public void add(E element) {
		if(!elements.contains(element)) {
			elements.add(element);
			incoming.put(element, new HashSet<E>());
			outgoing.put(element, new HashSet<E>());
		}
	}
	
	/**
	 * Remove an element as node
	 * 
	 * @param element Element
	 */
	public void remove(E element) {
		if(elements.remove(element)) {
			for(E from : incoming.get(element)) {
				outgoing.get(from).remove(element);
			}
			for(E to : outgoing.get(element)) {
				incoming.get(to).remove(element);
			}
			outgoing.remove(element);
			incoming.remove(element);
		}
	}
	
	/**
	 * Add an edge between two elements
	 * 
	 * @param source Source element
	 * @param target Target element
	 */
	public void addOrder(E source, E target) {
		add(source);
		add(target);
		incoming.get(target).add(source);
		outgoing.get(source).add(target);
	}
	
	/**
	 * Remove an edge between two elements
	 * 
	 * @param source Source element
	 * @param target Target element
	 */
	public void removeOrder(E source, E target) {
		incoming.get(target).remove(source);
		outgoing.get(source).remove(target);
	}
	
	/**
	 * Get the elements which are source of incoming edges
	 * 
	 * @param element Element
	 * @return Elements
	 */
	public Set<E> getIncoming(E element) {
		return new HashSet<E>(incoming.get(element));
	}
	
	/**
	 * Get the elements which are target of outgoing edges
	 * 
	 * @param element
	 * @return Elements
	 */
	public Set<E> getOutgoing(E element) {
		return new HashSet<E>(outgoing.get(element));
	}
	
	/**
	 * Getter for elements
	 * 
	 * @return Elements
	 */
	public Set<E> getElements() {
		return new HashSet<E>(elements);
	}
	
	/**
	 * Return whether the graph is empty
	 * 
	 * @return empty or not?
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	/**
	 * Return all elements which do not have incoming edges
	 * 
	 * @return Elements
	 */
	public E getNoIncomingElement() {
		for(E element : elements) {
			if(getIncoming(element).isEmpty()) {
				return element;
			}
		}
		return null;
	}
	
	/**
	 * Determine whether an element in contained in the directed graph
	 * 
	 * @param element
	 * @return true if contained, false otherwise
	 */
	public boolean contains(E element) {
		return elements.contains(element);
	}
	
	/**
	 * Calculate a topological sorting of the elements in the directed graph.
	 * Note that the directed graph is destroyed in response to this calculation.
	 * 
	 * @return Topological sorting of element, null if the directed graph contains
	 * a cycle
	 */
	public List<E> getOrdering() {
		List<E> order = new ArrayList<E>();
		E noIncoming = getNoIncomingElement();
		while(noIncoming != null) {
			remove(noIncoming);
			order.add(noIncoming);
			noIncoming = getNoIncomingElement();
		}

		if(!isEmpty()){
			return null;
		}
		
		return order;
	}
}
