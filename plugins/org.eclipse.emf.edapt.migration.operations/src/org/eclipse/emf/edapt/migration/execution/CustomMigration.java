package org.eclipse.emf.edapt.migration.execution;

import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public abstract class CustomMigration {

	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {

	}

	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

	}
}
