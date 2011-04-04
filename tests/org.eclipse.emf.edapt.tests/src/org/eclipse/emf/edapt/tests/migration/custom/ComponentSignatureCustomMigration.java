package org.eclipse.emf.edapt.tests.migration.custom;

import java.util.List;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Model;

public class ComponentSignatureCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model) {
		for (Instance signature : model.getInstances("component.Signature")) {
			for (Instance port : signature.<List<Instance>> get("inPort")) {
				port.migrate("component.InPort");
			}
			for (Instance port : signature.<List<Instance>> get("outPort")) {
				port.migrate("component.OutPort");
			}
		}
	}
}
