package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class UMLActivityEdgeCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {
		for (final Instance edge : model.getAllInstances("minuml1.ActivityEdge")) { //$NON-NLS-1$
			if (edge.getLink("source").instanceOf("minuml1.ObjectNode") //$NON-NLS-1$ //$NON-NLS-2$
				|| edge.getLink("target").instanceOf("minuml1.ObjectNode")) { //$NON-NLS-1$ //$NON-NLS-2$
				edge.migrate("minuml1.ObjectFlow"); //$NON-NLS-1$
			} else {
				edge.migrate("minuml1.ControlFlow"); //$NON-NLS-1$
			}
		}
	}
}
