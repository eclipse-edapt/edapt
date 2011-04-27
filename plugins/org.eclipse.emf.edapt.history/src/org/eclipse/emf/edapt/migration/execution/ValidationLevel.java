package org.eclipse.emf.edapt.migration.execution;

/**
 * Enumeration for the validation level
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
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
