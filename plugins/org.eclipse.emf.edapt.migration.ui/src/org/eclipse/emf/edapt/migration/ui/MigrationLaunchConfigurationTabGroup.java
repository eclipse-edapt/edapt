package org.eclipse.emf.edapt.migration.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.RefreshTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;

/**
 * Tab group for editing a migration launch configuration. Specifies the
 * different tabs that are necessary to edit the migration configuration.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: D4994C7719CB1540B6E598C74CBC3920
 */
public class MigrationLaunchConfigurationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	/** {@inheritDoc} */
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(new ILaunchConfigurationTab[] {
				new MigrationLaunchConfigurationMainTab(), new JavaJRETab(),
				new JavaClasspathTab(), new RefreshTab(), new CommonTab() });
	}
}
