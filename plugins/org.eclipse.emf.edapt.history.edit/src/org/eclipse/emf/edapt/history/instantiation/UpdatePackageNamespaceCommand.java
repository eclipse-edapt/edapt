/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation;

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
	private final List<EPackage> ePackages;

	/**
	 * Label to be replaced
	 */
	private final String toReplace;

	/**
	 * Label by which it is replaced
	 */
	private final String replaceBy;

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
		for (final EPackage ePackage : ePackages) {
			final String nsURI = ePackage.getNsURI();
			final String updatedNsURI = nsURI.replace(toReplace, replaceBy);
			if (!updatedNsURI.equals(nsURI)) {
				ePackage.setNsURI(updatedNsURI);
			}
			updatePackageNamespace(ePackage.getESubpackages());
		}
	}

}
