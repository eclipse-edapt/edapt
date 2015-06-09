package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGenGeneratorCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {
		for (final Instance container : model
			.getInstances("gmfgen.GenAuditContainer")) { //$NON-NLS-1$
			if (container.get("parentContainer") == null) { //$NON-NLS-1$
				final Instance generator = container.getContainer();

				if (generator == null) {
					final Instance root = model.newInstance("gmfgen.GenAuditRoot"); //$NON-NLS-1$
					container.getResource().getRootInstances().add(root);
					container.getResource().getRootInstances()
						.remove(container);
					root.set("audits", container); //$NON-NLS-1$
				}
			}
		}
		model.checkConformance();
	}
}
