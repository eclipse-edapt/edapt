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

import java.util.List;

import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.MigrateableChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edit.command.ChangeCommand;


/**
 * Command to a attach a migration to a number of changes
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class AttachMigrationCommand extends ChangeCommand {
	
	/**
	 * Release
	 */
	private Release release;
	
	/**
	 * List of changes
	 */
	private List<MigrateableChange> changes;

	/**
	 * Initial migration code
	 */
	private String code;

	/**
	 * Resulting migration change
	 */
	private MigrationChange migrationChange;

	/**
	 * Constructor
	 * 
	 * @param changes
	 * @param code
	 */
	public AttachMigrationCommand(List<MigrateableChange> changes, String code) {
		super(changes.get(0).eContainer());
		this.release = (Release) changes.get(0).eContainer();
		this.changes = changes;
		this.code = code;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		migrationChange = HistoryFactory.eINSTANCE.createMigrationChange();
		migrationChange.setMigration(code);
		release.getChanges().add(release.getChanges().indexOf(changes.get(0)), migrationChange);
		migrationChange.getChanges().addAll(changes);
	}

	/** Returns migrationChange. */
	public MigrationChange getMigrationChange() {
		return migrationChange;
	}
}
