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
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ReferenceSlot;

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
	private List<Instance> root = new ArrayList<Instance>();

	/** Mapping from the current to the reconstructed metamodel. */
	private Map<Instance, Instance> forwardMapping = new HashMap<Instance, Instance>();

	/** Mapping from the reconstructed to the current metamodel. */
	private Map<Instance, Instance> backwardMapping = new HashMap<Instance, Instance>();

	/** Access to the model. */
	private Model model;

	/** Visit a change during reconstruction. */
	private void visitChange(Instance change) throws MigrationException {
		// CompositeChange
		if (change.instanceOf("CompositeChange")) {
			List<Instance> children = change.getLinks("changes");
			for (Instance child : children) {
				visitChange(child);
			}
		}
		// MigrationChange
		else if (change.instanceOf("MigrationChange")) {
			List<Instance> children = change.getLinks("changes");
			for (Instance child : children) {
				visitChange(child);
			}
		}
		// ContentChange
		else if (change.instanceOf("ContentChange")) {
			visitContentChange(change);
		}
		// ValueChange
		else if (change.instanceOf("ValueChange")) {
			visitValueChange(change);
		}
		// NoChange
		else if (change.instanceOf("NoChange")) {
			// ignore
		} else {
			throw new MigrationException("Unknown change: "
					+ change.getEClass().getName(), null);
		}
	}

	/** Visit a value change during reconstruction. */
	private void visitValueChange(Instance change) {
		if (change.instanceOf("Set")) {
			visitSet(change);
		} else if (change.instanceOf("Add")) {
			visitAdd(change);
		} else if (change.instanceOf("Remove")) {
			visitRemove(change);
		}
	}

	/** Visit a content change during reconstruction. */
	private void visitContentChange(Instance change) {
		if (change.instanceOf("Create")) {
			visitCreate(change);
		} else if (change.instanceOf("Move")) {
			visitMove(change);
		} else if (change.instanceOf("Delete")) {
			visitDelete(change);
		}
	}

	/** Visit a create during reconstruction. */
	private void visitCreate(Instance change) {
		Model model = change.getType().getModel();
		Instance element = change.getLink("element");
		Instance reconstructed = model.newInstance(element.getEClass());
		map(element, reconstructed);
		Instance target = change.get("target");
		if (target != null) {
			String referenceName = change.<String> get("referenceName");
			EStructuralFeature reference = target.getEClass()
					.getEStructuralFeature(referenceName);
			getForward(target).add(reference, reconstructed);
		} else {
			root.add(reconstructed);
		}
		List<Instance> children = change.getLinks("changes");
		for (Instance child : children) {
			visitValueChange(child);
		}
	}

	/** Visit a move during reconstruction. */
	private void visitMove(Instance change) {
		// make bidirectional
		Instance element = change.get("element");
		change.set("source", getBackward(getForward(element).getContainer()));
		// apply change
		Instance target = change.get("target");
		String referenceName = change.<String> get("referenceName");
		EStructuralFeature reference = target.getEClass()
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
		Instance element = getForward(change.getLink("element"));
		addRemoves(element, change);
		change.set("target", getBackward(element.getContainer()));
		change.set("referenceName", element.getContainerReference().getName());
		// apply change
		model.delete(element);
	}

	/** Add removes to a delete. */
	private void addRemoves(Instance element, Instance delete) {
		for (ReferenceSlot referenceSlot : element.getReferences()) {
			Instance source = referenceSlot.getInstance();
			if (contains(source, element) || contains(element, source)) {
				continue;
			}
			Model model = element.getType().getModel();
			Instance remove = model.newInstance("Remove");
			remove.set("element", getBackward(source));
			remove.set("featureName", referenceSlot.getEReference().getName());
			remove.set("referenceValue", getBackward(element));
			delete.getLinks("changes").add(remove);
		}
		for (Instance child : element.getContents()) {
			addRemoves(child, delete);
		}
	}

	/** Visit a set during reconstruction. */
	private void visitSet(Instance change) {
		Instance element = change.get("element");
		String featureName = change.get("featureName");
		EStructuralFeature feature = element.getEClass().getEStructuralFeature(
				featureName);
		if (feature instanceof EReference) {
			// make bidirectional
			change.set("oldReferenceValue", getBackward(getForward(element)
					.<Instance> get(feature)));
			// apply change
			Instance referenceValue = change.getLink("referenceValue");
			getForward(element).set(feature, resolveForward(referenceValue));
		} else {
			EDataType type = ((EAttribute) feature).getEAttributeType();
			// make bidirectional
			change.set("oldDataValue", EcoreUtil.convertToString(type,
					getForward(element).get(feature)));
			// apply change
			String dataValue = change.<String> get("dataValue");
			getForward(element).set(feature,
					EcoreUtil.createFromString(type, dataValue));
		}
	}

	/** Visit an add during reconstruction. */
	private void visitAdd(Instance change) {
		Instance element = change.get("element");
		String featureName = change.<String> get("featureName");
		EStructuralFeature feature = element.getEClass().getEStructuralFeature(
				featureName);
		if (feature instanceof EReference) {
			// apply change
			Instance referenceValue = change.getLink("referenceValue");
			getForward(element).add(feature, resolveForward(referenceValue));
		} else {
			// apply change
			EDataType type = ((EAttribute) feature).getEAttributeType();
			String dataValue = change.<String> get("dataValue");
			getForward(element).add(feature,
					EcoreUtil.createFromString(type, dataValue));
		}
	}

	/** Visit a remove during reconstruction. */
	private void visitRemove(Instance change) {
		Instance element = change.get("element");
		String featureName = change.<String> get("featureName");
		EStructuralFeature feature = element.getEClass().getEStructuralFeature(
				featureName);
		if (feature instanceof EReference) {
			// apply change
			Instance referenceValue = change.getLink("referenceValue");
			getForward(element).remove(feature, resolveForward(referenceValue));
		} else {
			// apply change
			EDataType type = ((EAttribute) feature).getEAttributeType();
			String dataValue = change.<String> get("dataValue");
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
		Instance forward = getForward(instance);
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

		metamodel.setDefaultPackage("history");

		Instance history = model.getInstances("History").get(0);
		for (Instance release : history.getLinks("releases")) {
			for (Instance change : release.getLinks("changes")) {
				visitChange(change);
			}
		}

		// cleanup
		for (Instance r : root) {
			model.delete(r);
		}
	}
}
