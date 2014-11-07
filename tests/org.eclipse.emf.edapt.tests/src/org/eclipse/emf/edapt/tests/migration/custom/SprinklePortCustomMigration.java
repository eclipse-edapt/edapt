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

		metamodel.setDefaultPackage("sprinkle");

		// hierarchy inputs
		for (Instance connection : model.getAllInstances("Connection")) {
			if (connection.evaluate("destination.signal.parent") == connection
					.evaluate("source.signal")) {
				connection.getLink("source").migrate("Input");
				connection.getLink("destination").migrate("Input");
			}
		}

		// hierarchy outputs
		for (Instance connection : model.getAllInstances("Connection")) {
			if (connection.evaluate("source.signal.parent") == connection
					.evaluate("destination.signal")) {
				connection.getLink("source").migrate("Output");
				connection.getLink("destination").migrate("Output");
			}
		}

		// do inputs
		for (Instance port : model.getInstances("Port")) {
			if (!port.getInverse("Connection.destination").isEmpty()) {
				port.migrate("Input");
			}
		}

		// do outputs
		for (Instance port : model.getInstances("Port")) {
			if (!port.getInverse("Connection.source").isEmpty()) {
				port.migrate("Output");
			}
		}
	}
}
