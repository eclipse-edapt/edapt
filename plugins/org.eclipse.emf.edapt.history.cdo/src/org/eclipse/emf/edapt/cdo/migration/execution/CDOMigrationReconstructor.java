package org.eclipse.emf.edapt.cdo.migration.execution;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.IResourceSetFactory;
import org.eclipse.emf.edapt.history.reconstruction.FinishedException;
import org.eclipse.emf.edapt.internal.migration.execution.IClassLoader;
import org.eclipse.emf.edapt.internal.migration.execution.ValidationLevel;
import org.eclipse.emf.edapt.internal.migration.execution.internal.MigrationReconstructor;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.MigrationFactory;
import org.eclipse.emf.edapt.spi.migration.Model;

public class CDOMigrationReconstructor extends MigrationReconstructor {

	public CDOMigrationReconstructor(List<URI> modelURIs,
		Release sourceRelease, Release targetRelease,
		IProgressMonitor monitor, IClassLoader classLoader,
		ValidationLevel level, IResourceSetFactory resourceSetFactory) {
		super(modelURIs, sourceRelease, targetRelease, monitor, classLoader,
			level, resourceSetFactory);
	}

	/** Load the model before migration. */
	@Override
	protected void loadRepository() {
		final Metamodel metamodel = loadMetamodel();
		metamodel.refreshCaches();
		try {
			final ResourceSet set = resourceSetFactory.createResourceSet();
			final Model model = CDOPersistency.loadModel(modelURIs, metamodel, set);
			repository = MigrationFactory.eINSTANCE.createRepository();
			repository.setMetamodel(metamodel);
			repository.setModel(model);
			checkConformanceIfMoreThan(ValidationLevel.HISTORY);
		} catch (final IOException e) {
			throwWrappedMigrationException("Model could not be loaded", e); //$NON-NLS-1$
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endRelease(Release originalRelease) {
		if (originalRelease == targetRelease) {
			disable();
			checkConformanceIfMoreThan(ValidationLevel.HISTORY);
			throw new FinishedException();
		} else if (originalRelease == sourceRelease) {
			enable();
			start();
			loadRepository();
		} else if (isEnabled()) {
			checkConformanceIfMoreThan(ValidationLevel.RELEASE);
		}
	}

}
