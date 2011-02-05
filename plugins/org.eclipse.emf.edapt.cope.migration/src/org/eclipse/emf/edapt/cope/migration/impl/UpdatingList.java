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
package org.eclipse.emf.edapt.cope.migration.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.cope.migration.Instance;


/**
 * A list automatically updating the model
 * 
 * @author herrmama
 * 
 * @param <E> 
 */
public class UpdatingList<E> extends BasicEList<E> {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4463041349924185459L;

	/**
	 * Feature
	 */
	private EStructuralFeature feature;
	
	/**
	 * Instance
	 */
	private Instance instance;

	/**
	 * Constructor
	 * 
	 * @param instance
	 * @param feature
	 */
	public UpdatingList(Instance instance, EStructuralFeature feature) {
		this(instance, feature, new ArrayList<E>());
	}
	
	/**
	 * Constructor
	 * 
	 * @param instance
	 * @param feature
	 * @param values
	 */
	public UpdatingList(Instance instance, EStructuralFeature feature, Collection<E> values) {
		super(values);
		this.instance = instance;
		this.feature = feature;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void didAdd(int index, Object object) {
		instance.add(feature, index, object);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void didRemove(int index, Object object) {
		instance.remove(feature, index);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isUnique() {
		return feature.isUnique();
	}
}
