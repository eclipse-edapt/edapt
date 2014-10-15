package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class FileSystemRightsCustomMigration extends CustomMigration {

	private EAttribute rightsAttribute;

	public int getDigit(int value, int digit) {
		int n = Integer.toString(value).length();
		int i = n - 1 - digit;
		while (i > 0) {
			value = (value / 10);
			i--;
		}
		return value % 10;
	}

	public Instance toRights(int rights, Model model) {
		Instance instance = model.newInstance("filesystem.Rights");

		instance.set("canExecute", rights % 2 > 0);
		rights = (rights / 2);
		instance.set("canWrite", rights % 2 > 0);
		rights = (rights / 2);
		instance.set("canRead", rights % 2 > 0);
		rights = (rights / 2);

		return instance;
	}

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		rightsAttribute = metamodel
				.getEAttribute("filesystem.FileSystemElement.rights");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

		for (Instance element : model
				.getAllInstances("filesystem.FileSystemElement")) {
			Integer rights = element.get(rightsAttribute);

			element.set("userRights", toRights(getDigit(rights, 0), model));
			element.set("groupRights", toRights(getDigit(rights, 1), model));
			element.set("otherRights", toRights(getDigit(rights, 2), model));
			element.unset(rightsAttribute);
		}

	}
}
