package org.eclipse.emf.edapt.migration;

import org.eclipse.emf.edapt.migration.execution.MigrationException;

public abstract class CustomMigration {

	public void migrateBefore(Model model, Metamodel metamodel) throws MigrationException {
		
	}
	
	public void migrateAfter(Model model, Metamodel metamodel) throws MigrationException {
		
	}
}
