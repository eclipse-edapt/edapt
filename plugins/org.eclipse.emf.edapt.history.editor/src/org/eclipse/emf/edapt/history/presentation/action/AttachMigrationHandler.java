/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BMW Car IT - Initial API and implementation
 *     Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.presentation.action;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.edapt.history.MigrateableChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.presentation.AttachMigrationCommand;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.reconstruction.MigrationChangeReconstructor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Action to combine a sequence of primitives changes into a composite one.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class AttachMigrationHandler extends
		SubsequentChangesHandler<MigrateableChange> {

	/** {@inheritDoc} */
	@Override
	protected Object execute(Release release, List<MigrateableChange> changes,
			EditingDomain domain, ExecutionEvent event) {
		final MigrateableChange sourceChange = changes.get(0);
		MigrateableChange targetChange = changes.get(changes.size() - 1);

		final MigrationChangeReconstructor reconstructor = reconstruct(
				sourceChange, targetChange);
		if (isConsistent(reconstructor)) {
			IJavaElement element = JavaUIUtils.createCustomMigration(release);
			if (element != null) {
				attachMigration(changes, element.getElementName(), domain);
			}
		}
		return null;
	}

	/** Perform reconstruction to assemble code for metamodel adaptation. */
	private MigrationChangeReconstructor reconstruct(
			final MigrateableChange sourceChange, MigrateableChange targetChange) {
		EcoreForwardReconstructor ecoreReconstructor = new EcoreForwardReconstructor(
				sourceChange.eResource().getURI());
		final MigrationChangeReconstructor migrationReconstructor = new MigrationChangeReconstructor(
				sourceChange, targetChange);
		ecoreReconstructor.addReconstructor(migrationReconstructor);

		ecoreReconstructor.reconstruct(targetChange, false);
		return migrationReconstructor;
	}

	/**
	 * Check whether the metamodel is consistent before and after the changes to
	 * which a custom migration is attached.
	 */
	private boolean isConsistent(
			final MigrationChangeReconstructor migrationReconstructor) {
		if (!migrationReconstructor.isConsistent()) {
			return MessageDialog
					.openConfirm(
							Display.getDefault().getActiveShell(),
							"Metamodel inconsistent",
							"The metamodel is inconsistent before or after the changes. Do you really want to attach a migration to them?");
		}

		return true;
	}

	/** Attach a custom migration to the changes. */
	private MigrationChange attachMigration(List<MigrateableChange> changes,
			String migration, EditingDomain domain) {
		AttachMigrationCommand command = new AttachMigrationCommand(changes,
				migration);
		domain.getCommandStack().execute(command);
		return command.getMigrationChange();
	}
}
