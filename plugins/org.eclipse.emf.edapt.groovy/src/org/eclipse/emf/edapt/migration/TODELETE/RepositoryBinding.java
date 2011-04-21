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
package org.eclipse.emf.edapt.migration.TODELETE;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.migration.Repository;

/**
 * A special binding to make metamodel packages available through variables
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class RepositoryBinding extends Binding {
	
	/**
	 * Variable for the default package
	 */
	private static final String DEFAULT_PACKAGE_VARIABLE_NAME = "defaultPackage";

	/**
	 * Repository
	 */
	private Repository repository;

	/**
	 * Constructor
	 * 
	 * @param repository
	 */
	public RepositoryBinding(Repository repository) {
		this.repository = repository;
		setVariable("repository", repository);
		
		if(repository.getMetamodel() != null &&
				repository.getMetamodel().getEPackages().size() == 1) {
			setVariable(DEFAULT_PACKAGE_VARIABLE_NAME, repository.getMetamodel().getEPackages().get(0));
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getVariable(String name) {
		try {
			Object v = super.getVariable(name);
			return v;
		} catch(MissingPropertyException e) {
			if(name.startsWith("_")) {
				name = name.substring(1);
			}
			if(repository.getMetamodel().getEPackage(name) != null) {
				return repository.getMetamodel().getEPackage(name);
			}
			if("emf".equals(name)) {
				return EcorePackage.eINSTANCE;
			}
			if(super.getVariable(DEFAULT_PACKAGE_VARIABLE_NAME) != null) {
				EPackage defaultPackage = (EPackage) super.getVariable(DEFAULT_PACKAGE_VARIABLE_NAME);
				if(defaultPackage.getEClassifier(name) != null) {
					return defaultPackage.getEClassifier(name);
				}
			}
			throw e;
		}
	}
}
