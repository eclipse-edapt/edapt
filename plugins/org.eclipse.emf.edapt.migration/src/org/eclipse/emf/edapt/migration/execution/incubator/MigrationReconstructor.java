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
package org.eclipse.emf.edapt.migration.execution.incubator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.history.Add;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.Set;
import org.eclipse.emf.edapt.history.reconstruction.CodeGeneratorHelper;
import org.eclipse.emf.edapt.history.reconstruction.EcoreReconstructorSwitchBase;
import org.eclipse.emf.edapt.history.reconstruction.FinishedException;
import org.eclipse.emf.edapt.history.reconstruction.Mapping;
import org.eclipse.emf.edapt.history.reconstruction.ReconstructorBase;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.DiagnosticException;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationPlugin;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.GroovyEvaluator;
import org.eclipse.emf.edapt.migration.execution.MigrationException;
import org.eclipse.emf.edapt.migration.execution.Persistency;

/**
 * A recontructor that perform the migration of models from a source release to
 * a target release.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigrationReconstructor extends ReconstructorBase {

	/** Source release. */
	private Release sourceRelease;

	/** Target release. */
	private Release targetRelease;

	/** URIs of the models that need to be migrated. */
	private List<URI> modelURIs;

	/** Extent of the reconstructed metamodel. */
	private MetamodelExtent extent;

	/** Internal representation of the model during migration. */
	private Model model;

	/** Whether migration is active. */
	private boolean enabled = false;

	/** Whether generation is started. */
	private boolean started = false;

	/** Trigger to restart migration. */
	private Change trigger = null;

	/** Mapping to the reconstructed metamodel. */
	private Mapping mapping;

	/** Monitor to show progress. */
	private IProgressMonitor monitor;

	/** Switch to perform migration depending on change. */
	private MigrationReconstructorSwitch migrationSwitch;

	private CustomMigration customMigration;

	private IClassLoader classLoader;

	/** Constructor. */
	public MigrationReconstructor(List<URI> modelURIs, Release sourceRelease,
			Release targetRelease, IProgressMonitor monitor, IClassLoader classLoader) {
		this.modelURIs = modelURIs;
		this.sourceRelease = sourceRelease;
		this.targetRelease = targetRelease;
		this.monitor = monitor;
		this.classLoader = classLoader;
	}

	/** {@inheritDoc} */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		migrationSwitch = new MigrationReconstructorSwitch();
		this.extent = extent;
		this.mapping = mapping;
	}

	/** {@inheritDoc} */
	@Override
	public void startRelease(Release originalRelease) {
		if (isEnabled()) {
			monitor.subTask("Release " + originalRelease.getNumber());
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endRelease(Release originalRelease) {
		if (originalRelease == targetRelease) {
			disable();
			saveModel();
			GroovyEvaluator.getInstance().unsetModel();
			throw new FinishedException();
		}
		if (originalRelease == sourceRelease) {
			enable();
			started = true;
			model = loadModel();
			GroovyEvaluator.getInstance().setModel(model);
			try {
				model.checkConformance();
			} catch (DiagnosticException e) {
				throwWrappedMigrationException(
						"Model not consistent before migration", e);
			}
		}
	}

	/** Load the model before migration. */
	private Model loadModel() {
		Metamodel metamodel = loadMetamodel();
		metamodel.refreshCaches();
		try {
			return Persistency.loadModel(modelURIs, metamodel);
		} catch (IOException e) {
			throwWrappedMigrationException("Model could not be loaded", e);
		}
		return null;
	}

	/** Save the model after migration. */
	private void saveModel() {
		try {
			Persistency.saveModel(model);
		} catch (IOException e) {
			throwWrappedMigrationException("Model could not be saved", e);
		}
	}

	/** Load the metamodel. */
	private Metamodel loadMetamodel() {
		final ResourceSet resourceSet = new ResourceSetImpl();
		URI metamodelURI = URI.createFileURI(new File("metamodel."
				+ ResourceUtils.ECORE_FILE_EXTENSION).getAbsolutePath());
		Resource resource = resourceSet.createResource(metamodelURI);

		Collection<EPackage> rootPackages = extent.getRootPackages();
		Copier copier = new Copier() {
			@Override
			protected void copyReference(EReference reference, EObject object,
					EObject copyEObject) {
				if (MetamodelUtils.getGenericReference(reference) != null) {
					object.eGet(reference);
				}
				super.copyReference(reference, object, copyEObject);
			}

			@Override
			public EObject get(Object key) {
				EObject value = super.get(key);
				if (value == null && key instanceof EObject) {
					EObject element = (EObject) key;
					if (element.eResource() != null) {
						URI uri = EcoreUtil.getURI(element);
						EObject loaded = resourceSet.getEObject(uri, true);
						return loaded;
					}
				}
				return value;
			}
		};
		Collection<EPackage> copy = copier.copyAll(rootPackages);
		resource.getContents().addAll(copy);
		copier.copyReferences();

		return Persistency.loadMetamodel(resourceSet);
	}

	/** {@inheritDoc} */
	@Override
	public void startChange(Change change) {
		if (isEnabled()) {
			if (isStarted()) {
				migrationSwitch.doSwitch(change);
				monitor.worked(1);
			}
			checkPause(change);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endChange(Change change) {
		if (isEnabled()) {
			checkResume(change);
			if (isStarted()) {
				if (change instanceof MigrationChange
						&& customMigration != null) {
					customMigration.migrateAfter(model);
				}
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
				|| (originalChange instanceof MigrationChange && !((MigrationChange) originalChange)
						.getMigration().startsWith("class:"))
				|| originalChange instanceof Delete) {
			started = false;
			trigger = originalChange;
		}
	}

	/** Check whether migration is started. */
	private boolean isStarted() {
		return started;
	}

	/** Check whether migration is active. */
	private boolean isEnabled() {
		return enabled;
	}

	/** De-activate migration. */
	private void disable() {
		enabled = false;
	}

	/** Activate migration. */
	private void enable() {
		enabled = true;
	}

	/** Wrap and throw a {@link MigrationException}. */
	private void throwWrappedMigrationException(String message, Throwable e) {
		throw new WrappedMigrationException(new MigrationException(message, e));
	}

	/** Switch that performs the migration attached to a change. */
	private class MigrationReconstructorSwitch extends
			EcoreReconstructorSwitchBase<Object> {

		/** {@inheritDoc} */
		@Override
		public Object caseSet(Set set) {
			EObject element = set.getElement();
			EStructuralFeature feature = set.getFeature();
			Object value = set.getValue();
			if (feature instanceof EReference) {
				set(resolve(element), feature, resolve((EObject) value));
			} else {
				set(resolve(element), feature, value);
			}

			return set;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseAdd(Add add) {
			EObject element = add.getElement();
			EStructuralFeature feature = add.getFeature();
			Object value = add.getValue();
			if (feature instanceof EReference) {
				add(resolve(element), feature, resolve((EObject) value));
			} else {
				add(resolve(element), feature, value);
			}

			return add;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseRemove(Remove remove) {
			EObject element = remove.getElement();
			EStructuralFeature feature = remove.getFeature();
			Object value = remove.getValue();
			if (feature instanceof EReference) {
				remove(resolve(element), feature, resolve((EObject) value));
			} else {
				remove(resolve(element), feature, value);
			}

			return remove;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseCreate(Create create) {
			EObject element = create.getTarget();
			EReference reference = create.getReference();
			create(resolve(element), reference, create.getElement().eClass());

			return create;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseDelete(Delete delete) {
			EObject element = delete.getElement();
			delete(resolve(element));

			return delete;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseMove(Move move) {
			move(resolve(move.getElement()), resolve(move.getTarget()), move
					.getReference());

			return move;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseMigrationChange(MigrationChange change) {
			String migration = change.getMigration();
			if (migration.startsWith("class:")) {
				try {
					migration = migration.substring(6);
					Class<?> c = classLoader.load(migration);
					customMigration = (CustomMigration) c.newInstance();
					customMigration.migrateBefore(model);
				} catch (ClassNotFoundException e) {
					throwWrappedMigrationException(
							"Custom migration could not be loaded", e);
				} catch (InstantiationException e) {
					throwWrappedMigrationException(
							"Custom migration could not be instantiated", e);
				} catch (IllegalAccessException e) {
					throwWrappedMigrationException(
							"Custom migration could not be accessed", e);
				}
			} else {
				GroovyEvaluator.getInstance().evaluate(
						new ByteArrayInputStream(migration.getBytes()));
			}

			return change;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseOperationChange(OperationChange change) {
			OperationInstance operationInstance = (OperationInstance) mapping
					.copyResolveTarget(change.getOperation());
			CodeGeneratorHelper coder = new CodeGeneratorHelper(extent);
			String assembledCode = coder.assembleCode(operationInstance);
			GroovyEvaluator.getInstance().evaluate(
					new ByteArrayInputStream(assembledCode.getBytes()));

			return change;
		}

		/** Resolve a metamodel element. */
		private EObject resolve(EObject element) {
			element = mapping.resolveTarget(element);
			element = find(element);
			return element;
		}

		/** Find an element in the metamodel created for migration. */
		@SuppressWarnings("unchecked")
		private EObject find(EObject sourceElement) {
			EObject sourceParent = sourceElement.eContainer();
			if (sourceParent == null) {
				EPackage sourcePackage = (EPackage) sourceElement;
				for (EPackage targetPackage : model.getMetamodel()
						.getEPackages()) {
					if (targetPackage.getNsURI().equals(
							sourcePackage.getNsURI())) {
						return targetPackage;
					}
				}
				return sourcePackage;
			}
			EObject targetParent = find(sourceParent);
			EReference reference = sourceElement.eContainmentFeature();
			if (reference.isMany()) {
				List<EObject> targetChildren = (List<EObject>) targetParent
						.eGet(reference);
				List<EObject> sourceChildren = (List<EObject>) sourceParent
						.eGet(reference);
				int index = sourceChildren.indexOf(sourceElement);
				EObject targetElement = targetChildren.get(index);
				return targetElement;
			}
			EObject targetElement = (EObject) targetParent.eGet(reference);
			return targetElement;
		}
	}
}
