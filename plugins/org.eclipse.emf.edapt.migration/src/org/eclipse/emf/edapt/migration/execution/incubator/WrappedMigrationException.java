package org.eclipse.emf.edapt.migration.execution.incubator;

import org.eclipse.emf.edapt.migration.execution.MigrationException;

public class WrappedMigrationException extends RuntimeException {

	public WrappedMigrationException(MigrationException e) {
		super(e);
	}
	
	@Override
	public MigrationException getCause() {
		return (MigrationException) super.getCause();
	}
}
