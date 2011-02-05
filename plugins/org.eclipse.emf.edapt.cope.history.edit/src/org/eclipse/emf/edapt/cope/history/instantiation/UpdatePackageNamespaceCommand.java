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
package org.eclipse.emf.edapt.cope.history.instantiation;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.command.ChangeCommand;

/**
 * Command to update package namespaces
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class UpdatePackageNamespaceCommand extends ChangeCommand {
	
	/**
	 * Root packages
	 */
	private List<EPackage> ePackages;
	
	/**
	 * Label to be replaced
	 */
	private String toReplace;
	
	/**
	 * Label by which it is replaced
	 */
	private String replaceBy;

	/**
	 * Constructor
	 * 
	 * @param ePackages
	 * @param toReplace
	 * @param replaceBy
	 */
	@SuppressWarnings("unchecked")
	public UpdatePackageNamespaceCommand(List<EPackage> ePackages, String toReplace, String replaceBy) {
		super((Collection) ePackages);
		
		this.ePackages = ePackages;
		this.toReplace = toReplace;
		this.replaceBy = replaceBy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		updatePackageNamespace(ePackages);
	}

	/**
	 * Helper method to update a number of packages which also recurses over their children
	 * 
	 * @param ePackages
	 */
	private void updatePackageNamespace(List<EPackage> ePackages) {
		for(EPackage ePackage : ePackages) {
			String nsURI = ePackage.getNsURI();
			String updatedNsURI = nsURI.replace(toReplace, replaceBy);
			if(!updatedNsURI.equals(nsURI)) {
				ePackage.setNsURI(updatedNsURI);
			}
			updatePackageNamespace(ePackage.getESubpackages());
		}
	}

}
