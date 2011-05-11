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
package org.eclipse.emf.edapt.history.presentation.util;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;


/**
 * Special editor input for {@link MigrationChange}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class SpecialEditorInput implements IStorageEditorInput {
	
	/**
	 * Migration change
	 */
	private MigrationChange change;
	
	/**
	 * Item property descriptor
	 */
	private IItemPropertyDescriptor descriptor;
	
	/**
	 * Editing domain
	 */
	private EditingDomain domain;

	/**
	 * Constructor
	 * 
	 * @param change
	 * @param descriptor
	 */
	public SpecialEditorInput(MigrationChange change, IItemPropertyDescriptor descriptor) {
		this.change = change;
		this.descriptor = descriptor;
	}
	
	/**
	 * Constructor
	 * 
	 * @param change
	 * @param domain
	 */
	public SpecialEditorInput(MigrationChange change, EditingDomain domain) {
		this.change = change;
		this.domain = domain;
	}
	
	/**
	 * Set the migration
	 * 
	 * @param migration
	 */
	public void setMigration(String migration) {
		if(descriptor != null) {
			descriptor.setPropertyValue(change, migration);
		}
		else if(domain != null) {
			Command command = SetCommand.create(domain, change, HistoryPackage.eINSTANCE.getMigrationChange_Migration(), migration);
			domain.getCommandStack().execute(command);
		}
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public IStorage getStorage() {
		return new SpecialStorage(change.getMigration());
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public boolean exists() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public String getName() {
		return "Migration Change";
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public String getToolTipText() {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if(object instanceof SpecialEditorInput) {
			return ((SpecialEditorInput) object).change == change;
		}
		return false;
	}
}
