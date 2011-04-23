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
package org.eclipse.emf.edapt.migration.execution;

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
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.history.Add;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.Language;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.Set;
import org.eclipse.emf.edapt.history.reconstruction.EcoreReconstructorSwitchBase;
import org.eclipse.emf.edapt.history.reconstruction.FinishedException;
import org.eclipse.emf.edapt.history.reconstruction.Mapping;
import org.eclipse.emf.edapt.history.reconstruction.ReconstructorBase;
import org.eclipse.emf.edapt.history.reconstruction.ResolverBase;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.Persistency;

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
	private final Release sourceRelease;

	/** Target release. */
	private final Release targetRelease;

	/** URIs of the models that need to be migrated. */
	private final List<URI> modelURIs;

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
	private final IProgressMonitor monitor;

	/** Switch to perform migration depending on change. */
	private MigrationReconstructorSwitch migrationSwitch;

	/** Custom migration that is currently active. */
	private CustomMigration customMigration;

	/** Classloader to load {@link CustomMigration}s. */
	private final IClassLoader classLoader;

	/** Constructor. */
	public MigrationReconstructor(List<URI> modelURIs, Release sourceRelease,
			Release targetRelease, IProgressMonitor monitor,
			IClassLoader classLoader) {
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
			throw new FinishedException();
		}
		if (originalRelease == sourceRelease) {
			enable();
			started = true;
			model = loadModel();
			try {
				model.checkConformance();
			} catch (MigrationException e) {
				throwWrappedMigrationException(e);
			}
		}
	}

	/** Load the model before migration. */
	private Model loadModel() {
		Metamodel metamodel = loadMetamodel();
		metamodel.refreshCaches();
		try {
			Model model = Persistency.loadModel(modelURIs, metamodel);
			model.checkConformance();
			return model;
		} catch (IOException e) {
			throwWrappedMigrationException("Model could not be loaded", e);
		} catch (MigrationException e) {
			throwWrappedMigrationException(e);
		}
		return null;
	}

	/** Save the model after migration. */
	private void saveModel() {
		try {
			model.checkConformance();
			Persistency.saveModel(model);
		} catch (IOException e) {
			throwWrappedMigrationException("Model could not be saved", e);
		} catch (MigrationException e) {
			throwWrappedMigrationException(e);
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
				if(change.eContainer() instanceof Release) {
					monitor.worked(1);
					try {
						model.checkConformance();
					} catch (MigrationException e) {
						throwWrappedMigrationException(e);
					}
				}
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
					try {
						customMigration.migrateAfter(model, model
								.getMetamodel());
					} catch (MigrationException e) {
						throwWrappedMigrationException(e);
					} finally {
						customMigration = null;
					}
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
				|| (originalChange instanceof MigrationChange && ((MigrationChange) originalChange)
						.getLanguage() != Language.JAVA)
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
		MigrationException me = new MigrationException(message, e);
		throwWrappedMigrationException(me);
	}

	/** Wrap and throw a {@link MigrationException}. */
	private void throwWrappedMigrationException(MigrationException me) {
		throw new WrappedMigrationException(me);
	}

	/** Switch that performs the migration attached to a change. */
	private class MigrationReconstructorSwitch extends
			EcoreReconstructorSwitchBase<Object> {

		/**
		 * Resolver to resolve the metamodel on which the migration is performed
		 * from the original metamodel.
		 */
		private final ResolverBase resolver = new ResolverBase() {

			@Override
			protected EObject doResolve(EObject element) {
				element = mapping.resolveTarget(element);
				element = find(element);
				return element;
			}
		};

		/** {@inheritDoc} */
		@Override
		public Object caseSet(Set set) {
			EObject element = set.getElement();
			EStructuralFeature feature = set.getFeature();
			Object value = set.getValue();
			if (feature == EcorePackage.eINSTANCE.getEReference_EOpposite()) {
				model.setEOpposite((EReference) resolve(element),
						(EReference) resolve((EObject) value));
			} else if (feature instanceof EReference) {
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
			if (change.getLanguage() == Language.JAVA) {
				try {
					Class<?> c = classLoader.load(migration);
					customMigration = (CustomMigration) c.newInstance();
					customMigration.migrateBefore(model, model.getMetamodel());
				} catch (ClassNotFoundException e) {
					throwWrappedMigrationException(
							"Custom migration could not be loaded", e);
				} catch (InstantiationException e) {
					throwWrappedMigrationException(
							"Custom migration could not be instantiated", e);
				} catch (IllegalAccessException e) {
					throwWrappedMigrationException(
							"Custom migration could not be accessed", e);
				} catch (MigrationException e) {
					throwWrappedMigrationException(e);
				}
			}

			return change;
		}

		/** {@inheritDoc} */
		@Override
		public Object caseOperationChange(OperationChange change) {
			OperationInstance operationInstance = (OperationInstance) resolver
					.copyResolve(change.getOperation(), true);

			OperationImplementation operation = OperationInstanceConverter
					.convert(operationInstance, model.getMetamodel());
			try {
				operation.checkAndExecute(model.getMetamodel(), model);
			} catch (MigrationException e) {
				throwWrappedMigrationException(e);
			}

			return change;
		}

		/** Resolve an element using the resolver. */
		private EObject resolve(EObject element) {
			return resolver.resolve(element);
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
