/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BMW Car IT - Initial API and implementation
 *     Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.TODELETE;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.HistoryPlugin;
import org.eclipse.emf.edapt.history.InitializerChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.Release;


/**
 * Reconstructor that generates a migrator.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigratorCodeGenerator extends AdaptationCodeReconstructorBase {

	/** Whether generation is active. */
	private boolean enabled = false;

	/** Current step. */
	private int step;

	/** Current release. */
	private Release release;

	/** Migrator folder. */
	private final URI migratorURI;

	/** Release folder. */
	private URI releaseURI;

	/** Whether a new step has to be started. */
	private boolean newStep = false;

	/** Whether generation is started. */
	private boolean started = false;

	/** Trigger to restart generation. */
	private Change trigger = null;

	/** Constructor. */
	public MigratorCodeGenerator(URI migratorURI) {
		this.migratorURI = migratorURI;
	}

	/** {@inheritDoc} */
	@Override
	public void startRelease(Release originalRelease) {
		if (!originalRelease.isFirstRelease()) {
			enable();
		}
		if (originalRelease.isLastRelease()) {
			disable();
		}

		step = 1;
		newStep = false;
		started = true;
		init();

		release = originalRelease;
		if (isEnabled()) {
			initReleaseFolder();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void startChange(Change originalChange) {
		if (isEnabled()) {
			checkNewStep(originalChange);
			if (isStarted() && !(originalChange instanceof Create)) {
				adaptationSwitch.doSwitch(originalChange);
			}
			checkPause(originalChange);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endChange(Change originalChange) {
		if (isEnabled()) {
			checkResume(originalChange);
			if (isStarted() && (originalChange instanceof Create)) {
				adaptationSwitch.doSwitch(originalChange);
			}
			updateNewStep(originalChange);
			if (release.getChanges().get(release.getChanges().size() - 1) == originalChange) {
				newStep();
			}
		}
	}

	/** Check whether to pause migration generator. */
	private void checkResume(Change originalChange) {
		if (trigger == originalChange) {
			started = true;
			trigger = null;
		}
	}

	/** Check whether to resume migration generator. */
	private void checkPause(Change originalChange) {
		if (trigger != null) {
			return;
		}
		if (originalChange instanceof OperationChange
				|| originalChange instanceof MigrationChange
				|| originalChange instanceof InitializerChange) {
			started = false;
			trigger = originalChange;
		}
	}

	/**
	 * Whether generation is started.
	 * 
	 * @return true if generation is started, false otherwise
	 */
	private boolean isStarted() {
		return started;
	}

	/**
	 * Whether generation is active.
	 * 
	 * @return true if generation is active, false otherwise
	 */
	private boolean isEnabled() {
		return enabled;
	}

	/** De-activate generation. */
	private void disable() {
		enabled = false;
	}

	/** Activate generation. */
	private void enable() {
		enabled = true;
	}

	/** Check whether a new step has to be started. */
	private void checkNewStep(Change originalChange) {
		if (originalChange instanceof MigrationChange) {
			newStep();
		} else if (originalChange instanceof OperationChange
				&& originalChange.eContainer() instanceof Release) {
			newStep();
		} else {
			if (newStep) {
				newStep();
			}
		}
	}

	/** Update status for the start of new step. */
	private void updateNewStep(Change originalChange) {
		if (originalChange instanceof MigrationChange) {
			newStep = true;
		} else if (originalChange instanceof OperationChange
				&& originalChange.eContainer() instanceof Release) {
			newStep = true;
		}
	}

	/** Start a new step. */
	private void newStep() {
		newStep = false;
		started = true;
		if (code.length() > 0) {
			createMigration();
			init();
			step++;
		}
	}

	/** Initialize the folder for a release. */
	private void initReleaseFolder() {
		createReleaseFolder();
		createReleaseMetamodel();
	}

	/** Create the release folder. */
	private void createReleaseFolder() {
		releaseURI = migratorURI.appendSegment("release" + release.getNumber());
		if (releaseURI.isPlatform()) {
			try {
				IFolder folder = URIUtils.getFolder(releaseURI);
				if (!folder.exists()) {
					folder.create(true, false, new NullProgressMonitor());
				}
			} catch (CoreException e) {
				LoggingUtils.logError(HistoryPlugin.getPlugin(), e);
			}
		} else {
			URIUtils.getJavaFile(releaseURI).mkdir();
		}
	}

	/** Create the release metamodel. */
	private void createReleaseMetamodel() {
		ResourceSet resourceSet = new ResourceSetImpl();
		URI metamodelURI = releaseURI.appendSegment(
				"release" + (release.getNumber() - 1)).appendFileExtension(
				ResourceUtils.ECORE_FILE_EXTENSION);
		Resource resource = resourceSet.createResource(metamodelURI);

		Collection<EPackage> rootPackages = extent.getRootPackages();
		Collection<EPackage> copy = copy(rootPackages);

		resource.getContents().addAll(copy);

		try {
			resource.save(null);
		} catch (IOException e) {
			LoggingUtils.logError(HistoryPlugin.getPlugin(), e);
		}
	}

	/** Copy a collection of elements. */
	public <T> Collection<T> copy(Collection<T> original) {
		Copier copier = new Copier() {
			@Override
			protected void copyReference(EReference reference, EObject object,
					EObject copyEObject) {
				if (MetamodelUtils.getGenericReference(reference) != null) {
					object.eGet(reference);
				}
				super.copyReference(reference, object, copyEObject);
			}
		};
		Collection<T> copy = copier.copyAll(original);
		copier.copyReferences();
		return copy;
	}

	/** Create migrator code. */
	private void createMigration() {
		URI stepURI = releaseURI.appendSegment("step" + step)
				.appendFileExtension("groovy");
		if (stepURI.isPlatform()) {
			IFile file = URIUtils.getFile(stepURI);
			try {
				ByteArrayInputStream source = new ByteArrayInputStream(code
						.toString().getBytes());
				if (file.exists()) {
					file.setContents(source, true, true,
							new NullProgressMonitor());
				} else {
					file.create(source, true, new NullProgressMonitor());
				}
			} catch (CoreException e) {
				LoggingUtils.logError(HistoryPlugin.getPlugin(), e);
			}
		} else {
			try {
				File file = URIUtils.getJavaFile(stepURI);
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(code.toString());
				writer.close();
			} catch (IOException e) {
				LoggingUtils.logError(HistoryPlugin.getPlugin(), e);
			}
		}
	}
}