package org.eclipse.emf.edapt.migration.execution;

import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.HISTORY;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.LIBRARY;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.OPERATION;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.RELEASE;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.VALIDATION;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;

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
	private int releaseNumber = -1;

	/** The validation level. */
	private ValidationLevel level = ValidationLevel.CUSTOM_MIGRATION;

	/** The implementations of the operations. */
	private final List<Class<? extends OperationImplementation>> operations = new ArrayList<Class<? extends OperationImplementation>>();

	/** The implementations of the libraries. */
	private final List<Class<? extends LibraryImplementation>> libraries = new ArrayList<Class<? extends LibraryImplementation>>();

	/** Constructor that sets all the arguments. */
	public MigratorCommandLine(URI historyURI, List<URI> modelURIs,
			int releaseNumber, ValidationLevel level,
			List<Class<? extends LibraryImplementation>> libraries,
			List<Class<? extends OperationImplementation>> operations) {
		this.historyURI = historyURI;
		this.modelURIs.addAll(modelURIs);
		this.releaseNumber = releaseNumber;
		this.level = level;
		this.libraries.addAll(libraries);
		this.operations.addAll(operations);
	}

	/** Constructor that parses the command line. */
	@SuppressWarnings("unchecked")
	public MigratorCommandLine(String[] args) {

		MigratorCommandLineOption option = null;

		for (String arg : args) {
			if (arg.startsWith("-")) {
				option = MigratorCommandLineOption.getOption(arg.charAt(1));
			} else {
				if (option == null) {
					URI modelURI = URIUtils.getURI(unquote(arg));
					modelURIs.add(modelURI);
				} else {
					switch (option) {
					case HISTORY:
						historyURI = URIUtils.getURI(unquote(arg));
						break;
					case RELEASE:
						releaseNumber = Integer.parseInt(arg);
						break;
					case VALIDATION:
						level = ValidationLevel.valueOf(arg);
						break;
					case LIBRARY:
						try {
							Class cl = Class.forName(arg);
							libraries.add(cl);
						} catch (ClassNotFoundException e) {
							System.err.println("Library not found: " + arg);
						}
						break;
					case OPERATION:
						try {
							Class cl = Class.forName(arg);
							operations.add(cl);
						} catch (ClassNotFoundException e) {
							System.err.println("Operation not found: " + arg);
						}
						break;
					}
				}
			}
		}
	}

	/** Generate the command line for the arguments. */
	@SuppressWarnings("unchecked")
	public String getCommandLine() {
		// models
		String argument = "";
		for (URI modelURI : modelURIs) {
			String modelFilename = modelURI.toFileString();
			modelFilename = quoteSpace(modelFilename);
			argument += modelFilename + " ";
		}

		// history
		String historyFilename = historyURI.toFileString();
		historyFilename = quoteSpace(historyFilename);
		argument += HISTORY.toOptionPrefix() + historyFilename + " ";

		// release
		if (releaseNumber != -1) {
			argument += RELEASE.toOptionPrefix() + releaseNumber + " ";
		}

		// validation
		argument += VALIDATION.toOptionPrefix() + level.toString() + " ";

		// libraries
		if (!libraries.isEmpty()) {
			argument += LIBRARY.toOptionPrefix();
			for (Class library : libraries) {
				argument += library.getName() + " ";
			}
		}

		// operations
		if (!operations.isEmpty()) {
			argument += OPERATION.toOptionPrefix();
			for (Class operation : operations) {
				argument += operation.getName() + " ";
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
	public int getReleaseNumber() {
		return releaseNumber;
	}

	/** Get the validation level. */
	public ValidationLevel getLevel() {
		return level;
	}

	/** Unquote a string that starts and ends with a quote. */
	private String unquote(String string) {
		if (string.startsWith("\"") && string.endsWith("\"")) {
			return string.substring(1, string.length() - 1);
		}
		return string;
	}

	/** Quote a string that contains a space. */
	private String quoteSpace(String string) {
		if (string.contains(" ")) {
			string = "\"" + string + "\"";
		}
		return string;
	}
}
