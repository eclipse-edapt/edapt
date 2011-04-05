package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.MigrationException;

public class GMFGenUniquenessCustomMigration extends CustomMigration {

	public String generateUnique(Instance context) {
		String id = context.get("className");
		int i = 0;
		boolean haveSuchId = true;
		while (haveSuchId) {
			haveSuchId = false;
			for (Instance next : context.getLink("root").getLinks(
					"clientContexts")) {
				if (id.equals(next.get("id"))) {
					haveSuchId = true;
					id = "" + (++i);
					break;
				}
			}
		}
		return id;
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

		for (Instance context : model.getAllInstances("gmfgen.GenAuditContext")) {
			context.set("id", generateUnique(context));
		}

		model.checkConformance();
	}
}
