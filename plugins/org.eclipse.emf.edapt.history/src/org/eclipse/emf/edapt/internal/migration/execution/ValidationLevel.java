package org.eclipse.emf.edapt.internal.migration.execution;

/**
 * Enumeration for the validation level
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 * @since 1.1
 */
public enum ValidationLevel {

	/** No validation at all. */
	NONE,

	/** The model is validated before and after migration. */
	HISTORY,

	/** The model is validated after each release. */
	RELEASE,

	/** The model is validated after each custom migration. */
	CUSTOM_MIGRATION,

	/** The model is validated after each change. */
	CHANGE
}
