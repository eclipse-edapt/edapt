package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGenUniquenessCustomMigration extends CustomMigration {

	public String generateUnique(Instance context) {
		String id = context.get("className"); //$NON-NLS-1$
		int i = 0;
		boolean haveSuchId = true;
		while (haveSuchId) {
			haveSuchId = false;
			for (final Instance next : context.getLink("root").getLinks( //$NON-NLS-1$
				"clientContexts")) { //$NON-NLS-1$
				if (id.equals(next.get("id"))) { //$NON-NLS-1$
					haveSuchId = true;
					id = "" + (++i); //$NON-NLS-1$
					break;
				}
			}
		}
		return id;
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {

		for (final Instance context : model.getAllInstances("gmfgen.GenAuditContext")) { //$NON-NLS-1$
			context.set("id", generateUnique(context)); //$NON-NLS-1$
		}

		model.checkConformance();
	}
}
