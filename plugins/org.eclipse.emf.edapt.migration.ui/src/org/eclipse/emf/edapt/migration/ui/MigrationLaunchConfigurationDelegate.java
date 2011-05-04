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
import org.eclipse.emf.edapt.declaration.LibraryImplementation;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.declaration.OperationRegistry;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.MigratorCommandLine;
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
	@SuppressWarnings("unchecked")
	@Override
	public String getProgramArguments(ILaunchConfiguration configuration)
			throws CoreException {

		List<URI> modelURIs = new ArrayList<URI>();
		List<IFile> modelFiles = getModelFiles(configuration);
		for (IFile modelFile : modelFiles) {
			modelURIs.add(URIUtils.getURI(modelFile.getLocation().toString()));
		}

		String historyFilename = configuration.getAttribute(HISTORY, "");
		IFile file = FileUtils.getFile(historyFilename);
		URI historyURI = URIUtils.getURI(file.getLocation().toString());

		int releaseNumber = configuration.getAttribute(RELEASE, -1);
		ValidationLevel level = ValidationLevel.valueOf(configuration
				.getAttribute(VALIDATION, ValidationLevel.CUSTOM_MIGRATION
						.toString()));

		OperationRegistry registry = OperationRegistry.getInstance();
		List<Class<? extends LibraryImplementation>> libraries = new ArrayList<Class<? extends LibraryImplementation>>();
		for (Library library : registry.getRootLibraries()) {
			libraries.add(library.getImplementation());
		}

		List<Class<? extends OperationImplementation>> operations = new ArrayList<Class<? extends OperationImplementation>>();
		for (Operation operation : registry.getRootOperations()) {
			operations.add(operation.getImplementation());
		}

		MigratorCommandLine commandLine = new MigratorCommandLine(historyURI,
				modelURIs, releaseNumber, level, libraries, operations);
		String argument = commandLine.getCommandLine();

		// VM arguments
		String vmArguments = configuration.getAttribute(ARGUMENTS, "");
		argument += " " + vmArguments;

		return argument;
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
