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
package org.eclipse.emf.edapt.cope.history.presentation;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Extended content provider to allow extended property sources
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ExtendedAdapterFactoryContentProvider extends
		AdapterFactoryContentProvider {

	/**
	 * Constructor
	 * 
	 * @param adapterFactory Adapter factory
	 */
	public ExtendedAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPropertySource getPropertySource(Object object) {
		if (object instanceof IPropertySource) {
	      return (IPropertySource)object;
	    }
		IItemPropertySource itemPropertySource =
	        (IItemPropertySource)
	          (object instanceof EObject && ((EObject)object).eClass() == null ?
	            null :
	            adapterFactory.adapt(object, IItemPropertySource.class));
	  
	      return 
	        itemPropertySource != null ?  createPropertySource(object, itemPropertySource) : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IPropertySource createPropertySource(Object object, IItemPropertySource itemPropertySource) {
		return new ExtendedPropertySource(object, itemPropertySource);
	}
}
