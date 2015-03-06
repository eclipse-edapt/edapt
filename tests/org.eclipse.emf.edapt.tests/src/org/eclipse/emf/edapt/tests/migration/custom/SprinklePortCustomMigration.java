package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class SprinklePortCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {

		metamodel.setDefaultPackage("sprinkle"); //$NON-NLS-1$

		// hierarchy inputs
		for (final Instance connection : model.getAllInstances("Connection")) { //$NON-NLS-1$
			if (connection.evaluate("destination.signal.parent") == connection //$NON-NLS-1$
				.evaluate("source.signal")) { //$NON-NLS-1$
				connection.getLink("source").migrate("Input"); //$NON-NLS-1$ //$NON-NLS-2$
				connection.getLink("destination").migrate("Input"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		// hierarchy outputs
		for (final Instance connection : model.getAllInstances("Connection")) { //$NON-NLS-1$
			if (connection.evaluate("source.signal.parent") == connection //$NON-NLS-1$
				.evaluate("destination.signal")) { //$NON-NLS-1$
				connection.getLink("source").migrate("Output"); //$NON-NLS-1$ //$NON-NLS-2$
				connection.getLink("destination").migrate("Output"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		// do inputs
		for (final Instance port : model.getInstances("Port")) { //$NON-NLS-1$
			if (!port.getInverse("Connection.destination").isEmpty()) { //$NON-NLS-1$
				port.migrate("Input"); //$NON-NLS-1$
			}
		}

		// do outputs
		for (final Instance port : model.getInstances("Port")) { //$NON-NLS-1$
			if (!port.getInverse("Connection.source").isEmpty()) { //$NON-NLS-1$
				port.migrate("Output"); //$NON-NLS-1$
			}
		}
	}
}
