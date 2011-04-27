package org.eclipse.emf.edapt.migration.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.common.ui.DialogUtils;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationRegistry;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.ValidationLevel;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

/**
 * Delegate for launching migrations.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 63F32051C0277326DE8204F8B83D36DD
 */
public class MigrationLaunchConfigurationDelegate extends JavaLaunchDelegate {

	/** Prefix for launch configuration attributes. */
	public static final String ID = "org.eclipse.emf.edapt.migration";

	/**
	 * Key for the launch configuration attribute to specify the path to the
	 * history file.
	 */
	public static final String HISTORY = ID + ".history";

	/**
	 * Key for the launch configuration attribute to specify the paths for the
	 * model files.
	 */
	public static final String MODELS = ID + ".models";

	/**
	 * Key for the launch configuration attribute to specify whether the source
	 * release should be determined automatically.
	 */
	public static final String AUTOMATIC = ID + ".automatic";

	/**
	 * Key for the launch configuration attribute to specify the source release
	 * of the migration.
	 */
	public static final String RELEASE = ID + ".release";

	/**
	 * Key for the launch configuration attribute to specify the validation
	 * level of the migration.
	 */
	public static final String VALIDATION = ID + ".validation";

	/** Key for the launch configuration attribute to specify JVM arguments. */
	public static final String ARGUMENTS = ID + ".arguments";

	/** {@inheritDoc} */
	@Override
	public boolean preLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {

		String historyFilename = configuration.getAttribute(HISTORY,
				(String) null);
		if (historyFilename == null) {
			DialogUtils.openErrorDialogAsync("Error",
					"Could not determine history.");
			return false;
		}

		IFile historyFile = FileUtils.getFile(historyFilename);
		if (!historyFile.exists()) {
			DialogUtils.openErrorDialogAsync("Error", "History "
					+ historyFilename + " does not exist.");
			return false;
		}

		URI uri = URIUtils.getURI(historyFile);
		try {
			ResourceUtils.loadElement(uri);
		} catch (IOException e) {
			DialogUtils.openErrorDialogAsync("Error", "Could not load history "
					+ historyFilename + ": " + e.getMessage());
			return false;
		}

		List<IFile> modelFiles = getModelFiles(configuration);
		for (IFile modelFile : modelFiles) {
			if (!modelFile.exists()) {
				DialogUtils.openErrorDialogAsync("Error", "History "
						+ modelFile.getLocation().toString()
						+ " does not exist.");
				return false;
			}
		}

		return super.preLaunchCheck(configuration, mode, monitor);
	}

	/** {@inheritDoc} */
	@Override
	public void launch(final ILaunchConfiguration configuration, String mode,
			final ILaunch launch, final IProgressMonitor monitor)
			throws CoreException {
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(
				new LaunchTerminationListener(configuration, launch, monitor));
		super.launch(configuration, mode, launch, monitor);
	}

	/** {@inheritDoc} */
	@Override
	public String getMainTypeName(ILaunchConfiguration configuration) {
		return Migrator.class.getName();
	}

	/** {@inheritDoc} */
	@Override
	public String getProgramArguments(ILaunchConfiguration configuration)
			throws CoreException {

		// models
		String argument = "";
		List<IFile> modelFiles = getModelFiles(configuration);
		for (IFile modelFile : modelFiles) {
			String modelFilename = modelFile.getLocation().toString();
			modelFilename = quoteSpace(modelFilename);
			argument += modelFilename + " ";
		}

		// history
		String historyFilename = configuration.getAttribute(HISTORY, "");
		IFile file = FileUtils.getFile(historyFilename);
		historyFilename = file.getLocation().toString();
		historyFilename = quoteSpace(historyFilename);
		argument += "-h " + historyFilename + " ";

		// release
		boolean automatic = configuration.getAttribute(AUTOMATIC, true);
		if (!automatic) {
			int release = configuration.getAttribute(RELEASE, 0);
			argument += "-r " + release + " ";
		}

		// validation
		String level = configuration.getAttribute(VALIDATION,
				ValidationLevel.CUSTOM_MIGRATION.toString());
		argument += "-v " + level + " ";

		// libraries
		OperationRegistry registry = OperationRegistry.getInstance();
		List<Library> libraries = registry.getRootLibraries();
		if (!libraries.isEmpty()) {
			argument += "-l ";
			for (Library library : libraries) {
				argument += library.getName() + " ";
			}
		}

		// operations
		List<Operation> operations = registry.getRootOperations();
		if (!operations.isEmpty()) {
			argument += "-o ";
			for (Operation operation : operations) {
				argument += operation.getImplementation().getName() + " ";
			}
		}

		// VM arguments
		String vmArguments = configuration.getAttribute(ARGUMENTS, "");
		argument += " " + vmArguments;

		return argument;
	}

	/** Quote a string that contains a space. */
	private String quoteSpace(String string) {
		if (string.contains(" ")) {
			string = "\"" + string + "\"";
		}
		return string;
	}

	/** Get the model files from the launch configuration. */
	@SuppressWarnings("unchecked")
	private List<IFile> getModelFiles(ILaunchConfiguration configuration)
			throws CoreException {
		List<IFile> files = new ArrayList<IFile>();
		List<String> modelURIs = configuration.getAttribute(MODELS, Collections
				.emptyList());
		for (String modelURI : modelURIs) {
			IFile file = FileUtils.getFile(modelURI);
			files.add(file);
		}
		return files;
	}

	/**
	 * A listener that waits until a certain launch is terminated and refreshes
	 * the migrated models afterwards.
	 */
	private class LaunchTerminationListener implements ILaunchesListener2 {

		/** The launch configuration that is executed. */
		private final ILaunchConfiguration configuration;

		/** The launch. */
		private final ILaunch launch;

		/** The monitor to show progress. */
		private final IProgressMonitor monitor;

		/** Constructor. */
		public LaunchTerminationListener(ILaunchConfiguration configuration,
				ILaunch launch, IProgressMonitor monitor) {
			this.configuration = configuration;
			this.launch = launch;
			this.monitor = monitor;
		}

		/** {@inheritDoc} */
		@Override
		public void launchesRemoved(ILaunch[] launches) {
			refreshModelsWhenTerminated();
		}

		/** {@inheritDoc} */
		@Override
		public void launchesChanged(ILaunch[] launches) {
			refreshModelsWhenTerminated();
		}

		/** {@inheritDoc} */
		@Override
		public void launchesAdded(ILaunch[] launches) {
			refreshModelsWhenTerminated();
		}

		/** Refresh the models when the launch is terminated. */
		private void refreshModelsWhenTerminated() {
			if (launch.isTerminated()) {
				refreshModels();
			}
		}

		/** {@inheritDoc} */
		@Override
		public void launchesTerminated(ILaunch[] launches) {
			for (ILaunch l : launches) {
				if (l == launch) {
					refreshModels();
				}
			}
		}

		/** Refresh the migrated models when the migration is terminated. */
		private void refreshModels() {
			DebugPlugin.getDefault().getLaunchManager().removeLaunchListener(
					this);
			IWorkspaceRunnable operation = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						while (!launch.isTerminated()) {
							Thread.sleep(100);
						}
					} catch (InterruptedException e) {
						LoggingUtils.logError(
								MigrationUIActivator.getDefault(), e);
					}
					List<IFile> modelFiles = getModelFiles(configuration);
					for (IFile modelFile : modelFiles) {
						modelFile.getParent().refreshLocal(
								IResource.DEPTH_INFINITE, monitor);
					}
				}
			};
			try {
				ResourcesPlugin.getWorkspace().run(operation, monitor);
			} catch (CoreException e) {
				LoggingUtils.logError(MigrationUIActivator.getDefault(), e);
			}
		}
	}
}
