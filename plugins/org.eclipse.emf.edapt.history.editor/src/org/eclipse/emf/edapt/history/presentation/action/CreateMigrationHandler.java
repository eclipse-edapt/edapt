package org.eclipse.emf.edapt.history.presentation.action;

import java.util.Collections;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.ui.EditingDomainHandlerBase;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IType;

/**
 * Handler to create an empty migration.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CreateMigrationHandler extends EditingDomainHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomain domain, ExecutionEvent event) {
		final EObject element = HandlerUtils.getSelectedElement(event, EObject.class);
		final IType javaType = JavaUIUtils.createCustomMigration(element);
		if (javaType != null) {
			createMigration(element, javaType.getFullyQualifiedName(), domain);
		}
		return null;
	}

	/** Create an empty migration before a change or at the end of a release. */
	private void createMigration(EObject element, String elementName,
		EditingDomain domain) {
		Release release = null;
		int index = CommandParameter.NO_INDEX;
		if (element instanceof Release) {
			release = (Release) element;
		} else if (element instanceof Change) {
			final Change change = (Change) element;
			release = change.getRelease();
			index = release.getChanges().indexOf(change);
		}

		final MigrationChange change = HistoryFactory.eINSTANCE
			.createMigrationChange();
		change.setMigration(elementName);
		final Command command = new CreateChildCommand(domain, release,
			HistoryPackage.eINSTANCE.getRelease_Changes(), change, index,
			Collections.emptyList());
		domain.getCommandStack().execute(command);
	}
}
