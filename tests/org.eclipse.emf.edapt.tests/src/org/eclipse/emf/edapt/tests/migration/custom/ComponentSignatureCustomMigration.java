package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class ComponentSignatureCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		metamodel.setDefaultPackage("component"); //$NON-NLS-1$
		for (final Instance signature : model.getInstances("Signature")) { //$NON-NLS-1$
			for (final Instance port : signature.getLinks("inPort")) { //$NON-NLS-1$
				port.migrate("InPort"); //$NON-NLS-1$
			}
			for (final Instance port : signature.getLinks("outPort")) { //$NON-NLS-1$
				port.migrate("OutPort"); //$NON-NLS-1$
			}
		}
	}
}
