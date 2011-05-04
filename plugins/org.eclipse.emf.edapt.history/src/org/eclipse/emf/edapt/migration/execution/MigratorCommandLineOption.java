package org.eclipse.emf.edapt.migration.execution;

/**
 * Possible options for the {@link MigratorCommandLine}.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public enum MigratorCommandLineOption {

	/** The history file. */
	HISTORY('h'),

	/** The source release. */
	RELEASE('r'),

	/** The validation level. */
	VALIDATION('v'),

	/** The set of operations. */
	OPERATION('o'),

	/** The set of libraries. */
	LIBRARY('l');

	/** The character representing an option. */
	private char character;

	/** Constructor. */
	private MigratorCommandLineOption(char c) {
		this.character = c;
	}

	/** Get the option with a certain character. */
	public static MigratorCommandLineOption getOption(char character) {
		for (MigratorCommandLineOption option : values()) {
			if (option.character == character) {
				return option;
			}
		}
		return null;
	}

	/** Get the command line prefix for this option. */
	public String toOptionPrefix() {
		return "-" + character + " ";
	}
}
