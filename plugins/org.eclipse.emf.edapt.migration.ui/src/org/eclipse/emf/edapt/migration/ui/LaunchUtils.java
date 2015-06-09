package org.eclipse.emf.edapt.migration.ui;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.edapt.common.ui.DialogUtils;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;

/**
 * Helper methods to deal with launch configurations.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 66523B3EA6950FA609F59EE80B414457
 */
public final class LaunchUtils {

	/** Constructor. */
	private LaunchUtils() {
		// not supposed to be instantiated
	}

	/**
	 * Get the value of an attribute of type String from a launch configuration.
	 */
	public static String getAttribute(ILaunchConfiguration configuration,
		String attributeName, String defaultValue) {
		try {
			return configuration.getAttribute(attributeName, defaultValue);
		} catch (final CoreException e) {
			showAttributeAccessError(attributeName, e);
			return null;
		}
	}

	/**
	 * Get the value of an attribute of type List of Strings from a launch
	 * configuration.
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAttribute(ILaunchConfiguration configuration,
		String attributeName, List<String> defaultValue) {
		try {
			return configuration.getAttribute(attributeName, defaultValue);
		} catch (final CoreException e) {
			showAttributeAccessError(attributeName, e);
			return null;
		}
	}

	/**
	 * Get the value of an attribute of type boolean from a launch
	 * configuration.
	 */
	public static boolean getAttribute(ILaunchConfiguration configuration,
		String attributeName, boolean defaultValue) {
		try {
			return configuration.getAttribute(attributeName, defaultValue);
		} catch (final CoreException e) {
			showAttributeAccessError(attributeName, e);
			return defaultValue;
		}
	}

	/**
	 * Get the value of an attribute of type integer from a launch
	 * configuration.
	 */
	public static int getAttribute(ILaunchConfiguration configuration,
		String attributeName, int defaultValue) {
		try {
			return configuration.getAttribute(attributeName, defaultValue);
		} catch (final CoreException e) {
			showAttributeAccessError(attributeName, e);
			return defaultValue;
		}
	}

	/** Show an error that a certain attribute could not be read. */
	private static void showAttributeAccessError(String attributeName,
		CoreException e) {
		final String message = "Could read attribute " + attributeName //$NON-NLS-1$
			+ " from the launch configuration: " + e.getMessage(); //$NON-NLS-1$
		DialogUtils.openErrorDialogAsync("Error reading launch configuration", //$NON-NLS-1$
			message);
		LoggingUtils.logError(MigrationUIActivator.getDefault(), message, e);
	}
}
