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
package org.eclipse.emf.edapt.history.presentation;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;


/**
 * Extended property source to allow extended property descriptors
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ExtendedPropertySource extends PropertySource {

	/**
	 * Constructor
	 * 
	 * @param object Object
	 * @param itemPropertySource Item property source
	 */
	public ExtendedPropertySource(Object object, IItemPropertySource itemPropertySource) {
		super(object, itemPropertySource);
	}

	/** {@inheritDoc} */
	@Override
	protected IPropertyDescriptor createPropertyDescriptor(IItemPropertyDescriptor itemPropertyDescriptor) {
		
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(object);
		
		if(object instanceof MigrationChange) {
			MigrationChange change = (MigrationChange) object;
			if(feature == HistoryPackage.eINSTANCE.getMigrationChange_Migration()) {
				return new MigrationChangeMigrationPropertyDescriptor(change, itemPropertyDescriptor);
			}
		}
		
		return super.createPropertyDescriptor(itemPropertyDescriptor);
	}
}
