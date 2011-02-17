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

import java.util.HashMap;

import org.eclipse.emf.edapt.cope.common.MetamodelExtent;
import org.eclipse.emf.edapt.cope.history.OperationInstance;
import org.eclipse.emf.edapt.cope.history.reconstruction.CodeGeneratorHelper;
import org.eclipse.emf.edapt.cope.migration.execution.GroovyEvaluator;


/**
 * Interpreter for an operation
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationInterpreter {

	/**
	 * Instance of the operation
	 */
	private final OperationInstance operationInstance;

	/**
	 * Metamodel extent
	 */
	private final MetamodelExtent extent;

	/**
	 * Constructor
	 * 
	 * @param operationInstance
	 * @param extent
	 */
	public OperationInterpreter(OperationInstance operationInstance,
			MetamodelExtent extent) {
		this.operationInstance = operationInstance;
		this.extent = extent;
	}

	/**
	 * Execute the operation
	 */
	public void execute() {
		String script = new CodeGeneratorHelper(extent)
				.assembleCode(operationInstance);
		GroovyEvaluator.getInstance().evaluate(script,
				new HashMap<String, Object>(), extent.getRootPackages());
	}
}
