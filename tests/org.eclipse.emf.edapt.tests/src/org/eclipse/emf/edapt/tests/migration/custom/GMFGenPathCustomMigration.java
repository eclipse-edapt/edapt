package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.CustomMigration;
import org.eclipse.emf.edapt.migration.execution.MigrationException;

public class GMFGenPathCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		for (Instance container : model
				.getAllInstances("gmfgen.GenAuditContainer")) {
			Instance current = container;
			while (current.get("parentContainer") != null) {
				current = current.getLink("parentContainer");
				container.add("path", 0, current);
			}
		}
		model.checkConformance();
	}
}
