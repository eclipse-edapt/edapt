package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.CustomMigration;
import org.eclipse.emf.edapt.migration.execution.MigrationException;

public class GMFGenGeneratorCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		for (Instance container : model
				.getInstances("gmfgen.GenAuditContainer")) {
			if (container.get("parentContainer") == null) {
				Instance generator = container.getContainer();

				if (generator == null) {
					Instance root = model.newInstance("gmfgen.GenAuditRoot");
					container.getResource().getRootInstances().add(root);
					container.getResource().getRootInstances()
							.remove(container);
					root.set("audits", container);
				}
			}
		}
		model.checkConformance();
	}
}
