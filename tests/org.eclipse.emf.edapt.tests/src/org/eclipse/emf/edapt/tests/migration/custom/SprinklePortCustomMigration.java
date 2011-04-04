package org.eclipse.emf.edapt.tests.migration.custom;

import java.util.List;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public class SprinklePortCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model) {

		Metamodel metamodel = model.getMetamodel();
		metamodel.setDefaultPackage(metamodel.getEPackage("sprinkle"));

		// model migration
		// hierarchy inputs
		for (Instance connection : model.getAllInstances("Connection")) {
			if (connection.getReference("destination").getReference("signal")
					.getReference("parent") == connection
					.getReference("source").getReference("signal")) {
				connection.getReference("source").migrate("Input");
				connection.getReference("destination").migrate("Input");
			}
		}

		// hierarchy outputs
		for (Instance connection : model.getAllInstances("Connection")) {
			if (connection.getReference("source").getReference("signal")
					.getReference("parent") == connection.getReference(
					"destination").getReference("signal")) {
				connection.getReference("source").migrate("Output");
				connection.getReference("destination").migrate("Output");
			}
		}

		// do inputs
		for (Instance port : model.getInstances("Port")) {
			if (!port.<List<Instance>> getInverse("Connection.destination")
					.isEmpty()) {
				port.migrate("Input");
			}
		}

		// do outputs
		for (Instance port : model.getInstances("Port")) {
			if (!port.<List<Instance>> getInverse("Connection.source")
					.isEmpty()) {
				port.migrate("Output");
			}
		}
	}
}
