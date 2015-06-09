package org.eclipse.emf.edapt.history.presentation.action;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edapt.common.ui.EditingDomainHandlerBase;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IType;

/**
 * Action to set the custom migration for a migration change.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class SetMigrationHandler extends EditingDomainHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomain domain, ExecutionEvent event) {
		final MigrationChange change = HandlerUtils.getSelectedElement(event);
		final IType javaType = JavaUIUtils.selectCustomMigration(change);
		if (javaType != null) {
			final Command command = SetCommand.create(domain, change,
				HistoryPackage.eINSTANCE.getMigrationChange_Migration(),
				javaType.getFullyQualifiedName());
			domain.getCommandStack().execute(command);
		}
		return null;
	}

}
