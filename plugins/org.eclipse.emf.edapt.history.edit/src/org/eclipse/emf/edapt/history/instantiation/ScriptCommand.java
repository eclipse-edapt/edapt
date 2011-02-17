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
package org.eclipse.emf.edapt.history.instantiation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.recorder.IChangeProvider;
import org.eclipse.emf.edapt.migration.execution.GroovyEvaluator;
import org.eclipse.emf.edit.command.ChangeCommand;


/**
 * A command to execute a Groovy script that provides a {@link MigrationChange}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ScriptCommand extends ChangeCommand implements IChangeProvider {
	
	/**
	 * Groovy script
	 */
	private String script;
	
	/**
	 * Metamodel extent
	 */
	private MetamodelExtent extent;

	/**
	 * Constructor
	 * 
	 * @param script
	 * @param extent
	 */
	@SuppressWarnings("unchecked")
	public ScriptCommand(String script, MetamodelExtent extent) {
		super((Collection) extent.getRootPackages());
		
		this.script = script;
		this.extent = extent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		GroovyEvaluator.getInstance().evaluate(script, new HashMap<String, Object>(), extent.getRootPackages());
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public List<Change> getChanges(List<PrimitiveChange> changes) {
		MigrationChange change = HistoryFactory.eINSTANCE.createMigrationChange();
		
		change.setMigration(script);
		change.getChanges().addAll(changes);
		
		return Collections.singletonList((Change) change);
	}

}
