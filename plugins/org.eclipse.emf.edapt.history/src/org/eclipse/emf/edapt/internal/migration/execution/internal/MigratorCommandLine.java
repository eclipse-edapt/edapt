package org.eclipse.emf.edapt.internal.migration.execution.internal;

import static org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLineOption.BACKUP;
import static org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLineOption.HISTORY;
import static org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLineOption.LIBRARY;
import static org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLineOption.OPERATION;
import static org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLineOption.SOURCE_RELEASE;
import static org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLineOption.TARGET_RELEASE;
import static org.eclipse.emf.edapt.internal.migration.execution.internal.MigratorCommandLineOption.VALIDATION_LEVEL;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edapt.internal.migration.execution.ValidationLevel;
import org.eclipse.emf.edapt.migration.execution.Migrator;

/**
 * Helper class to generate and parse the command line of the {@link Migrator}.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigratorCommandLine {

	/** The URIs of the models. */
	private final List<URI> modelURIs = new ArrayList<URI>();

	/** The URI of the history. */
	private URI historyURI = null;

	/** The source release number. */
	private int sourceReleaseNumber = -1;

	/** The target release number. */
	private int targetReleaseNumber = -1;

	/** The validation level. */
	private ValidationLevel level = ValidationLevel.CUSTOM_MIGRATION;

	/** Whether a backup should be created before migration. */
	private boolean backup = false;

	/** The implementations of the operations. */
	private final List<Class<? extends OperationImplementation>> operations = new ArrayList<Class<? extends OperationImplementation>>();

	/** The implementations of the libraries. */
	private final List<Class<? extends LibraryImplementation>> libraries = new ArrayList<Class<? extends LibraryImplementation>>();

	/** Constructor that sets all the arguments. */
	public MigratorCommandLine(URI historyURI, List<URI> modelURIs,
		int sourceReleaseNumber, int targetReleaseNumber,
		ValidationLevel level, boolean backup,
		List<Class<? extends LibraryImplementation>> libraries,
		List<Class<? extends OperationImplementation>> operations) {
		this.historyURI = historyURI;
		this.modelURIs.addAll(modelURIs);
		this.sourceReleaseNumber = sourceReleaseNumber;
		this.targetReleaseNumber = targetReleaseNumber;
		this.level = level;
		this.backup = backup;
		this.libraries.addAll(libraries);
		this.operations.addAll(operations);
	}

	/** Constructor that parses the command line. */
	@SuppressWarnings("unchecked")
	public MigratorCommandLine(String[] args) {

		MigratorCommandLineOption option = null;

		for (final String arg : args) {
			if (arg.startsWith("-")) { //$NON-NLS-1$
				option = MigratorCommandLineOption.getOption(arg.charAt(1));
				if (option == MigratorCommandLineOption.BACKUP) {
					backup = true;
				}
			} else {
				if (option == null) {
					final URI modelURI = URIUtils.getURI(unquote(arg));
					modelURIs.add(modelURI);
				} else {
					switch (option) {
					case HISTORY:
						historyURI = URIUtils.getURI(unquote(arg));
						break;
					case SOURCE_RELEASE:
						sourceReleaseNumber = Integer.parseInt(arg);
						break;
					case TARGET_RELEASE:
						targetReleaseNumber = Integer.parseInt(arg);
						break;
					case VALIDATION_LEVEL:
						level = ValidationLevel.valueOf(arg);
						break;
					case LIBRARY:
						try {
							final Class cl = Class.forName(arg);
							libraries.add(cl);
						} catch (final ClassNotFoundException e) {
							System.err.println("Library not found: " + arg); //$NON-NLS-1$
						}
						break;
					case OPERATION:
						try {
							final Class cl = Class.forName(arg);
							operations.add(cl);
						} catch (final ClassNotFoundException e) {
							System.err.println("Operation not found: " + arg); //$NON-NLS-1$
						}
						break;
					}
				}
			}
		}
	}

	/** Generate the command line for the arguments. */
	public String getCommandLine() {
		// models
		String argument = ""; //$NON-NLS-1$
		for (final URI modelURI : modelURIs) {
			String modelFilename = modelURI.toFileString();
			modelFilename = quoteSpace(modelFilename);
			argument += modelFilename + " "; //$NON-NLS-1$
		}

		// history
		String historyFilename = historyURI.toFileString();
		historyFilename = quoteSpace(historyFilename);
		argument += HISTORY.toOptionPrefix() + historyFilename + " "; //$NON-NLS-1$

		// release
		if (sourceReleaseNumber != -1) {
			argument += SOURCE_RELEASE.toOptionPrefix() + sourceReleaseNumber
				+ " "; //$NON-NLS-1$
		}
		if (targetReleaseNumber != -1) {
			argument += TARGET_RELEASE.toOptionPrefix() + targetReleaseNumber
				+ " "; //$NON-NLS-1$
		}

		// validation
		argument += VALIDATION_LEVEL.toOptionPrefix() + level.toString() + " "; //$NON-NLS-1$

		// backup
		if (backup) {
			argument += BACKUP.toOptionPrefix() + " "; //$NON-NLS-1$
		}

		// libraries
		if (!libraries.isEmpty()) {
			argument += LIBRARY.toOptionPrefix();
			for (final Class library : libraries) {
				argument += library.getName() + " "; //$NON-NLS-1$
			}
		}

		// operations
		if (!operations.isEmpty()) {
			argument += OPERATION.toOptionPrefix();
			for (final Class operation : operations) {
				argument += operation.getName() + " "; //$NON-NLS-1$
			}
		}

		return argument;
	}

	/** Get the URIs of the models. */
	public List<URI> getModelURIs() {
		return modelURIs;
	}

	/** Get the URI of the history. */
	public URI getHistoryURI() {
		return historyURI;
	}

	/** Get the source release number. */
	public int getSourceReleaseNumber() {
		return sourceReleaseNumber;
	}

	/** Get the target release number. */
	public int getTargetReleaseNumber() {
		return targetReleaseNumber;
	}

	/** Get the validation level. */
	public ValidationLevel getLevel() {
		return level;
	}

	/** Check whether a backup should be created before migration. */
	public boolean isBackup() {
		return backup;
	}

	/** Get the operation implementations. */
	public List<Class<? extends OperationImplementation>> getOperations() {
		return operations;
	}

	/** Get the library implementations. */
	public List<Class<? extends LibraryImplementation>> getLibraries() {
		return libraries;
	}

	/** Unquote a string that starts and ends with a quote. */
	private String unquote(String string) {
		if (string.startsWith("\"") && string.endsWith("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
			return string.substring(1, string.length() - 1);
		}
		return string;
	}

	/** Quote a string that contains a space. */
	private String quoteSpace(String string) {
		if (string.contains(" ")) { //$NON-NLS-1$
			string = "\"" + string + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return string;
	}
}
