package org.eclipse.emf.edapt.migration.execution.incubator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
import org.eclipse.emf.edapt.migration.DiagnosticException;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.GroovyEvaluator;
import org.eclipse.emf.edapt.migration.execution.MigrationException;
import org.eclipse.emf.edapt.migration.execution.Persistency;

public class MigrationReconstructor extends ReconstructorBase {

	private Release sourceRelease;
	private Release targetRelease;
	private List<URI> modelURIs;
	private MetamodelExtent extent;
	private Model model;

	/** Switch to perform reconstruction depending on change. */
	private EcoreReconstructorSwitch ecoreSwitch;

	/** Whether migration is active. */
	private boolean enabled = false;

	/** Whether generation is started. */
	private boolean started = false;

	/** Trigger to restart migration. */
	private Change trigger = null;

	private Mapping mapping;
	private IProgressMonitor monitor;

	public MigrationReconstructor(List<URI> modelURIs, Release sourceRelease,
			Release targetRelease, IProgressMonitor monitor) {
		this.modelURIs = modelURIs;
		this.sourceRelease = sourceRelease;
		this.targetRelease = targetRelease;
		this.monitor = monitor;
	}

	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		ecoreSwitch = new EcoreReconstructorSwitch();
		this.extent = extent;
		this.mapping = mapping;
	}

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
			model = loadModel(originalRelease);
			GroovyEvaluator.getInstance().setModel(model);
			try {
				model.checkConformance();
			} catch (DiagnosticException e) {
				throwException("Model not consistent before migration", e);
			}
		}
	}

	private void throwException(String message, Throwable e) {
		throw new WrappedMigrationException(new MigrationException(message, e));
	}

	@Override
	public void startRelease(Release originalRelease) {
		if (isEnabled()) {
			monitor.subTask("Release " + originalRelease.getNumber());
		}
	}

	private void saveModel() {
		try {
			Persistency.saveModel(model);
		} catch (IOException e) {
			throwException("Model could not be saved", e);
		}
	}

	private Model loadModel(Release originalRelease) {
		Metamodel metamodel = loadMetamodel(originalRelease);
		metamodel.refreshCaches();
		try {
			return Persistency.loadModel(modelURIs, metamodel);
		} catch (IOException e) {
			throwException("Model could not be loaded", e);
		}
		return null;
	}

	private Metamodel loadMetamodel(Release originalRelease) {
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
				ecoreSwitch.doSwitch(change);
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
				|| originalChange instanceof Delete) {
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

	private class EcoreReconstructorSwitch extends
			EcoreReconstructorSwitchBase<Object> {

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

		@Override
		public Object caseCreate(Create create) {
			EObject element = create.getTarget();
			EReference reference = create.getReference();
			create(resolve(element), reference, create.getElement().eClass());

			return create;
		}

		@Override
		public Object caseDelete(Delete delete) {
			EObject element = delete.getElement();
			delete(resolve(element));

			return delete;
		}

		@Override
		public Object caseMove(Move move) {
			move(resolve(move.getElement()), resolve(move.getTarget()), move
					.getReference());

			return move;
		}

		@Override
		public Object caseMigrationChange(MigrationChange change) {
			String migration = change.getMigration();
			GroovyEvaluator.getInstance().evaluate(
					new ByteArrayInputStream(migration.getBytes()));

			return change;
		}

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

		private EObject resolve(EObject element) {
			element = mapping.resolveTarget(element);
			element = find(element);
			return element;
		}

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
			} else {
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
				} else {
					EObject targetElement = (EObject) targetParent
							.eGet(reference);
					return targetElement;
				}
			}
		}
	}
}
