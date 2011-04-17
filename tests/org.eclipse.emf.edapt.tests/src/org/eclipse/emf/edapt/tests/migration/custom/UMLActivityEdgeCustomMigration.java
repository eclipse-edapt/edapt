package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

public class UMLActivityEdgeCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		for (Instance edge : model.getAllInstances("minuml1.ActivityEdge")) {
			if (edge.getLink("source").instanceOf("minuml1.ObjectNode")
					|| edge.getLink("target").instanceOf("minuml1.ObjectNode")) {
				edge.migrate("minuml1.ObjectFlow");
			} else {
				edge.migrate("minuml1.ControlFlow");
			}
		}
	}
}
