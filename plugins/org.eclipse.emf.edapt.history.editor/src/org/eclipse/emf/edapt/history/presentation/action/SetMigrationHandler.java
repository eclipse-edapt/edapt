package org.eclipse.emf.edapt.history.presentation.action;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edapt.common.ui.EditingDomainHandlerBase;
import org.eclipse.emf.edapt.common.ui.HandlerUtils;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaElement;

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
		MigrationChange change = HandlerUtils.getSelectedElement(event);
		IJavaElement javaElement = JavaUIUtils.selectCustomMigration(change);
		if (javaElement != null) {
			Command command = SetCommand.create(domain, change, HistoryPackage.eINSTANCE
					.getMigrationChange_Migration(), javaElement
					.getElementName());
			domain.getCommandStack().execute(command);
		}
		return null;
	}

}
