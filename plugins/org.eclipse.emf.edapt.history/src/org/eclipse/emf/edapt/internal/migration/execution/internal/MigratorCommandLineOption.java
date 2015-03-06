package org.eclipse.emf.edapt.internal.migration.execution.internal;

import org.eclipse.emf.edapt.internal.common.StringUtils;

/**
 * Possible options for the {@link MigratorCommandLine}.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public enum MigratorCommandLineOption {

	/** The models that need to be migrated. */
	MODELS(' '),

	/** The history file. */
	HISTORY('h'),

	/** The source release. */
	SOURCE_RELEASE('s'),

	/** The target release. */
	TARGET_RELEASE('t'),

	/** The validation level. */
	VALIDATION_LEVEL('v'),

	/** Whether a backup should be created. */
	BACKUP('b'),

	/** The set of operations. */
	OPERATION('o'),

	/** The set of libraries. */
	LIBRARY('l'),

	/** JVM arguments. */
	VM_ARGUMENTS(' ');

	/** The character representing an option. */
	private char character;

	/** The identifier of the option in the launch configuration. */
	private String id;

	/** Constructor. */
	private MigratorCommandLineOption(char c) {
		character = c;
		init();
	}

	/** Initialize the identifier of the option. */
	private void init() {
		String string = name();
		string = StringUtils.upperToLowerCamelCase(string);
		id = "org.eclipse.emf.edapt.migration." + string; //$NON-NLS-1$
	}

	/** Get the option with a certain character. */
	public static MigratorCommandLineOption getOption(char character) {
		for (final MigratorCommandLineOption option : values()) {
			if (option.character == character) {
				return option;
			}
		}
		return null;
	}

	/** Get the command line prefix for this option. */
	public String toOptionPrefix() {
		return "-" + character + " "; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Get the identifier of the option. */
	public String id() {
		return id;
	}
}
