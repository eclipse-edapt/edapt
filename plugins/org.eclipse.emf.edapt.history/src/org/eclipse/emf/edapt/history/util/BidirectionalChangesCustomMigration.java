package org.eclipse.emf.edapt.history.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.ReferenceSlot;

/**
 * Custom migration to add bidirectionality to changes.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class BidirectionalChangesCustomMigration extends CustomMigration {

	/** List of root elements of the reconstructed metamodel. */
	private final List<Instance> root = new ArrayList<Instance>();

	/** Mapping from the current to the reconstructed metamodel. */
	private final Map<Instance, Instance> forwardMapping = new HashMap<Instance, Instance>();

	/** Mapping from the reconstructed to the current metamodel. */
	private final Map<Instance, Instance> backwardMapping = new HashMap<Instance, Instance>();

	/** Access to the model. */
	private Model model;

	/** Visit a change during reconstruction. */
	private void visitChange(Instance change) throws MigrationException {
		// CompositeChange
		if (change.instanceOf("CompositeChange")) { //$NON-NLS-1$
			final List<Instance> children = change.getLinks("changes"); //$NON-NLS-1$
			for (final Instance child : children) {
				visitChange(child);
			}
		}
		// MigrationChange
		else if (change.instanceOf("MigrationChange")) { //$NON-NLS-1$
			final List<Instance> children = change.getLinks("changes"); //$NON-NLS-1$
			for (final Instance child : children) {
				visitChange(child);
			}
		}
		// ContentChange
		else if (change.instanceOf("ContentChange")) { //$NON-NLS-1$
			visitContentChange(change);
		}
		// ValueChange
		else if (change.instanceOf("ValueChange")) { //$NON-NLS-1$
			visitValueChange(change);
		}
		// NoChange
		else if (change.instanceOf("NoChange")) { //$NON-NLS-1$
			// ignore
		} else {
			throw new MigrationException("Unknown change: " //$NON-NLS-1$
				+ change.getEClass().getName(), null);
		}
	}

	/** Visit a value change during reconstruction. */
	private void visitValueChange(Instance change) {
		if (change.instanceOf("Set")) { //$NON-NLS-1$
			visitSet(change);
		} else if (change.instanceOf("Add")) { //$NON-NLS-1$
			visitAdd(change);
		} else if (change.instanceOf("Remove")) { //$NON-NLS-1$
			visitRemove(change);
		}
	}

	/** Visit a content change during reconstruction. */
	private void visitContentChange(Instance change) {
		if (change.instanceOf("Create")) { //$NON-NLS-1$
			visitCreate(change);
		} else if (change.instanceOf("Move")) { //$NON-NLS-1$
			visitMove(change);
		} else if (change.instanceOf("Delete")) { //$NON-NLS-1$
			visitDelete(change);
		}
	}

	/** Visit a create during reconstruction. */
	private void visitCreate(Instance change) {
		final Model model = change.getType().getModel();
		final Instance element = change.getLink("element"); //$NON-NLS-1$
		final Instance reconstructed = model.newInstance(element.getEClass());
		map(element, reconstructed);
		final Instance target = change.get("target"); //$NON-NLS-1$
		if (target != null) {
			final String referenceName = change.<String> get("referenceName"); //$NON-NLS-1$
			final EStructuralFeature reference = target.getEClass()
				.getEStructuralFeature(referenceName);
			getForward(target).add(reference, reconstructed);
		} else {
			root.add(reconstructed);
		}
		final List<Instance> children = change.getLinks("changes"); //$NON-NLS-1$
		for (final Instance child : children) {
			visitValueChange(child);
		}
	}

	/** Visit a move during reconstruction. */
	private void visitMove(Instance change) {
		// make bidirectional
		final Instance element = change.get("element"); //$NON-NLS-1$
		change.set("source", getBackward(getForward(element).getContainer())); //$NON-NLS-1$
		// apply change
		final Instance target = change.get("target"); //$NON-NLS-1$
		final String referenceName = change.<String> get("referenceName"); //$NON-NLS-1$
		final EStructuralFeature reference = target.getEClass()
			.getEStructuralFeature(referenceName);
		getForward(element).getContainer().remove(reference,
			getForward(element));
		getForward(target).add(reference, getForward(element));
	}

	/** Check whether an instance is contained by another one. */
	private boolean contains(Instance containee, Instance container) {
		if (containee == null) {
			return false;
		}
		if (containee == container) {
			return true;
		}
		return contains(containee.getContainer(), container);
	}

	/** Visit a delete during reconstruction. */
	private void visitDelete(Instance change) {
		// make bidirectional
		final Instance element = getForward(change.getLink("element")); //$NON-NLS-1$
		addRemoves(element, change);
		change.set("target", getBackward(element.getContainer())); //$NON-NLS-1$
		change.set("referenceName", element.getContainerReference().getName()); //$NON-NLS-1$
		// apply change
		model.delete(element);
	}

	/** Add removes to a delete. */
	private void addRemoves(Instance element, Instance delete) {
		for (final ReferenceSlot referenceSlot : element.getReferences()) {
			final Instance source = referenceSlot.getInstance();
			if (contains(source, element) || contains(element, source)) {
				continue;
			}
			final Model model = element.getType().getModel();
			final Instance remove = model.newInstance("Remove"); //$NON-NLS-1$
			remove.set("element", getBackward(source)); //$NON-NLS-1$
			remove.set("featureName", referenceSlot.getEReference().getName()); //$NON-NLS-1$
			remove.set("referenceValue", getBackward(element)); //$NON-NLS-1$
			delete.getLinks("changes").add(remove); //$NON-NLS-1$
		}
		for (final Instance child : element.getContents()) {
			addRemoves(child, delete);
		}
	}

	/** Visit a set during reconstruction. */
	private void visitSet(Instance change) {
		final Instance element = change.get("element"); //$NON-NLS-1$
		final String featureName = change.get("featureName"); //$NON-NLS-1$
		final EStructuralFeature feature = element.getEClass().getEStructuralFeature(
			featureName);
		if (feature instanceof EReference) {
			// make bidirectional
			change.set("oldReferenceValue", getBackward(getForward(element) //$NON-NLS-1$
				.<Instance> get(feature)));
			// apply change
			final Instance referenceValue = change.getLink("referenceValue"); //$NON-NLS-1$
			getForward(element).set(feature, resolveForward(referenceValue));
		} else {
			final EDataType type = ((EAttribute) feature).getEAttributeType();
			// make bidirectional
			change.set("oldDataValue", EcoreUtil.convertToString(type, //$NON-NLS-1$
				getForward(element).get(feature)));
			// apply change
			final String dataValue = change.<String> get("dataValue"); //$NON-NLS-1$
			getForward(element).set(feature,
				EcoreUtil.createFromString(type, dataValue));
		}
	}

	/** Visit an add during reconstruction. */
	private void visitAdd(Instance change) {
		final Instance element = change.get("element"); //$NON-NLS-1$
		final String featureName = change.<String> get("featureName"); //$NON-NLS-1$
		final EStructuralFeature feature = element.getEClass().getEStructuralFeature(
			featureName);
		if (feature instanceof EReference) {
			// apply change
			final Instance referenceValue = change.getLink("referenceValue"); //$NON-NLS-1$
			getForward(element).add(feature, resolveForward(referenceValue));
		} else {
			// apply change
			final EDataType type = ((EAttribute) feature).getEAttributeType();
			final String dataValue = change.<String> get("dataValue"); //$NON-NLS-1$
			getForward(element).add(feature,
				EcoreUtil.createFromString(type, dataValue));
		}
	}

	/** Visit a remove during reconstruction. */
	private void visitRemove(Instance change) {
		final Instance element = change.get("element"); //$NON-NLS-1$
		final String featureName = change.<String> get("featureName"); //$NON-NLS-1$
		final EStructuralFeature feature = element.getEClass().getEStructuralFeature(
			featureName);
		if (feature instanceof EReference) {
			// apply change
			final Instance referenceValue = change.getLink("referenceValue"); //$NON-NLS-1$
			getForward(element).remove(feature, resolveForward(referenceValue));
		} else {
			// apply change
			final EDataType type = ((EAttribute) feature).getEAttributeType();
			final String dataValue = change.<String> get("dataValue"); //$NON-NLS-1$
			getForward(element).remove(feature,
				EcoreUtil.createFromString(type, dataValue));
		}
	}

	/**
	 * Get an instance from the reconstructed metamodel for an instance of the
	 * original metamodel.
	 */
	private Instance getForward(Instance instance) {
		return forwardMapping.get(instance);
	}

	/**
	 * Resolve an instance from the original metamodel to an instance of the
	 * reconstructed metamodel.
	 */
	private Instance resolveForward(Instance instance) {
		final Instance forward = getForward(instance);
		if (forward == null) {
			return instance;
		}
		return forward;
	}

	/**
	 * Get an instance from the original metamodel for an instance of the
	 * reconstructed metamodel.
	 */
	private Instance getBackward(Instance instance) {
		return backwardMapping.get(instance);
	}

	/**
	 * Map an instance of the original metamodel to an instance of the
	 * reconstructed metamodel.
	 */
	private void map(Instance source, Instance target) {
		forwardMapping.put(source, target);
		backwardMapping.put(target, source);
	}

	/** {@inheritDoc} */
	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {

		metamodel.setDefaultPackage("history"); //$NON-NLS-1$

		final Instance history = model.getInstances("History").get(0); //$NON-NLS-1$
		for (final Instance release : history.getLinks("releases")) { //$NON-NLS-1$
			for (final Instance change : release.getLinks("changes")) { //$NON-NLS-1$
				visitChange(change);
			}
		}

		// cleanup
		for (final Instance r : root) {
			model.delete(r);
		}
	}
}
