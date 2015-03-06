package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGenPathCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {
		for (final Instance container : model
			.getAllInstances("gmfgen.GenAuditContainer")) { //$NON-NLS-1$
			Instance current = container;
			while (current.get("parentContainer") != null) { //$NON-NLS-1$
				current = current.getLink("parentContainer"); //$NON-NLS-1$
				container.add("path", 0, current); //$NON-NLS-1$
			}
		}
		model.checkConformance();
	}
}
