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
package org.eclipse.emf.edapt.migration.execution;

import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.groovy.control.CompilationFailedException;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.OperationRegistry;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MetamodelResource;
import org.eclipse.emf.edapt.migration.MigrationFactory;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.Repository;

/**
 * Evaluator for groovy scripts
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class GroovyEvaluator {

	/**
	 * Singleton instance
	 */
	private static GroovyEvaluator instance;

	/**
	 * Groovy shell
	 */
	private GroovyShell shell;

	/**
	 * Binding
	 */
	private RepositoryBinding binding;

	/**
	 * Repository
	 */
	private Repository repository;
	
	/**
	 * Hidden constructor
	 */
	private GroovyEvaluator() {
		
		ExpandoMetaClass.enableGlobally();

		repository = MigrationFactory.eINSTANCE.createRepository();
		binding = new RepositoryBinding(repository);
		initShell();
	}

	/**
	 * Initialize Groovy shell
	 */
	private void initShell() {
		this.shell = new GroovyShell(GroovyEvaluator.class.getClassLoader(), binding);
		
		getShell().evaluate(Environment.getInstance().getHeader());
		for(Library library : OperationRegistry.getInstance().getLibraries()) {
			getShell().evaluate(library.getCode());
		}
	}
	
	/**
	 * Set the model
	 * 
	 * @param model
	 */
	public void setModel(Model model) {
		repository.setModel(model);
		repository.setMetamodel(model.getMetamodel());
	}
	
	/**
	 * Unset the model
	 */
	public void unsetModel() {
		repository.setModel(null);
		repository.setMetamodel(null);
	}
	
	/**
	 * Cache for recurring scripts
	 */
	private Map<String, Script> scriptCache = new HashMap<String, Script>();
	
	/**
	 * Evaluate a recurring script
	 * 
	 * @param script
	 * @param variables
	 * @param packages
	 * @return Result
	 */
	public Object evaluateCached(String script, Map<String, Object> variables, Collection<EPackage> packages) {
		initModel(packages);
		initVariables(variables);
		Script s = scriptCache.get(script);
		if(s == null) {
			s = getShell().parse(script);
			scriptCache.put(script, s);
		}
		Object result = s.run();
		return result;		
	}
	
	/**
	 * Evaluate a script
	 * 
	 * @param script
	 * @param variables
	 * @param packages
	 * @return Result of evaluation
	 */
	public Object evaluate(String script, Map<String, Object> variables, Collection<EPackage> packages) {
		initModel(packages);
		initVariables(variables);
		Object result = getShell().evaluate(script);
		return result;
	}

	/**
	 * Initialize the variables
	 * 
	 * @param variables
	 */
	private void initVariables(Map<String, Object> variables) {
		for(Entry<String, Object> entry : variables.entrySet()) {
			getShell().getContext().setVariable(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Initialize both model and metamodel
	 * 
	 * @param packages
	 */
	private void initModel(Collection<EPackage> packages) {
		Metamodel metamodel = MigrationFactory.eINSTANCE.createMetamodel();
		MetamodelResource resource = MigrationFactory.eINSTANCE.createMetamodelResource();
		metamodel.getResources().add(resource);
		resource.getRootPackages().addAll(packages);
		Model model = MigrationFactory.eINSTANCE.createModel();
		model.setMetamodel(metamodel);
		setModel(model);
	}
	
	/**
	 * Evaluate a script
	 * 
	 * @param stream
	 */
	public void evaluate(InputStream stream) {
		getShell().evaluate(stream);
	}

	/**
	 * Get the Groovy shell
	 */
	private GroovyShell getShell() {
		return shell;
	}
	
	/**
	 * Parse a script
	 * 
	 * @param url
	 * @return Parsed script
	 * @throws IOException 
	 * @throws CompilationFailedException 
	 */
	public Script parse(URL url) throws CompilationFailedException, IOException {
		return getShell().parse(url.openStream(), url.getPath());
	}
	
	/**
	 * Get the singleton instance
	 * 
	 * @return Instance
	 */
	public static GroovyEvaluator getInstance() {
		if(instance == null) {
			instance = new GroovyEvaluator();
		}
		return instance;
	}	
}
