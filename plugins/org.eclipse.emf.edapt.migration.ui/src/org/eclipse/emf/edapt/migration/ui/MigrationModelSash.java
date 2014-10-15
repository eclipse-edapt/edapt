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
package org.eclipse.emf.edapt.migration.ui;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.common.ui.ModelSash;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.MetamodelResource;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.ModelResource;
import org.eclipse.emf.edapt.spi.migration.Repository;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Model sash to show the generic instance model during migration
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigrationModelSash extends ModelSash {

	/**
	 * Constructor
	 */
	public MigrationModelSash(Composite parent, int style,
			AdapterFactory adapterFactory) {
		super(parent, style, adapterFactory);
		
		StructuredViewer modelViewer = getStructureViewer();
		modelViewer.setContentProvider(new MigrationContentProvider(adapterFactory));
		modelViewer.setSorter(null);
	}

	/**
	 * Content provider to display the generic instance model during migration
	 * 
	 * @author herrmama
	 * @author $Author$
	 * @version $Rev$
	 * @levd.rating RED Rev:
	 */
	private class MigrationContentProvider extends AdapterFactoryContentProvider {

		/**
		 * Repository
		 */
		private Repository repository;

		/**
		 * Constructor
		 */
		public MigrationContentProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object[] getChildren(Object object) {
			if(object instanceof Model) {
				Model model = (Model) object;
				return model.getResources().toArray();
			}
			if(object instanceof ModelResource) {
				ModelResource resource = (ModelResource) object;
				return resource.getRootInstances().toArray();
			}
			if(object instanceof MetamodelResource) {
				MetamodelResource resource = (MetamodelResource) object;
				return resource.getRootPackages().toArray();
			}
			return super.getChildren(object);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasChildren(Object object) {
			if(object instanceof Model) {
				Model model = (Model) object;
				return !model.getResources().isEmpty();
			}
			if(object instanceof ModelResource) {
				ModelResource resource = (ModelResource) object;
				return !resource.getRootInstances().isEmpty();
			}
			if(object instanceof MetamodelResource) {
				MetamodelResource resource = (MetamodelResource) object;
				return !resource.getRootPackages().isEmpty();
			}
			return super.hasChildren(object);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getParent(Object object) {
			if(object instanceof Instance) {
				Instance instance = (Instance) object;
				if(instance.getContainer() == null) {
					return instance.getResource();
				}
				return instance.getContainer();
			}
			if(object instanceof EPackage) {
				EPackage ePackage = (EPackage) object;
				if(ePackage.getESuperPackage() == null) {
					for(MetamodelResource resource : repository.getMetamodel().getResources()) {
						if(resource.getRootPackages().contains(ePackage)) {
							return resource;
						}
					}
				}
			}
			return super.getParent(object);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			repository = (Repository) newInput;
			super.inputChanged(viewer, oldInput, newInput);
		}
	}
}
