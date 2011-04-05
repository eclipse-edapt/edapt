package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public class ComponentSignatureCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		metamodel.setDefaultPackage("component");
		for (Instance signature : model.getInstances("Signature")) {
			for (Instance port : signature.getLinks("inPort")) {
				port.migrate("InPort");
			}
			for (Instance port : signature.getLinks("outPort")) {
				port.migrate("OutPort");
			}
		}
	}
}
