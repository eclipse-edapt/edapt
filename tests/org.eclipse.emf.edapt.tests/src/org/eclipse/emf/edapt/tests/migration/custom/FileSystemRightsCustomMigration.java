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
		final int n = Integer.toString(value).length();
		int i = n - 1 - digit;
		while (i > 0) {
			value = value / 10;
			i--;
		}
		return value % 10;
	}

	public Instance toRights(int rights, Model model) {
		final Instance instance = model.newInstance("filesystem.Rights"); //$NON-NLS-1$

		instance.set("canExecute", rights % 2 > 0); //$NON-NLS-1$
		rights = rights / 2;
		instance.set("canWrite", rights % 2 > 0); //$NON-NLS-1$
		rights = rights / 2;
		instance.set("canRead", rights % 2 > 0); //$NON-NLS-1$
		rights = rights / 2;

		return instance;
	}

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
		throws MigrationException {
		rightsAttribute = metamodel
			.getEAttribute("filesystem.FileSystemElement.rights"); //$NON-NLS-1$
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {

		for (final Instance element : model
			.getAllInstances("filesystem.FileSystemElement")) { //$NON-NLS-1$
			final Integer rights = element.get(rightsAttribute);

			element.set("userRights", toRights(getDigit(rights, 0), model)); //$NON-NLS-1$
			element.set("groupRights", toRights(getDigit(rights, 1), model)); //$NON-NLS-1$
			element.set("otherRights", toRights(getDigit(rights, 2), model)); //$NON-NLS-1$
			element.unset(rightsAttribute);
		}

	}
}
