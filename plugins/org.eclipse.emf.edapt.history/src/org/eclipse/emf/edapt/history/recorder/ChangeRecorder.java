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
package org.eclipse.emf.edapt.history.recorder;

import static org.eclipse.emf.common.notify.Notification.ADD;
import static org.eclipse.emf.common.notify.Notification.ADD_MANY;
import static org.eclipse.emf.common.notify.Notification.MOVE;
import static org.eclipse.emf.common.notify.Notification.REMOVE;
import static org.eclipse.emf.common.notify.Notification.REMOVE_MANY;
import static org.eclipse.emf.common.notify.Notification.SET;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edapt.spi.history.Add;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.Move;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Remove;
import org.eclipse.emf.edapt.spi.history.Set;
import org.eclipse.emf.edapt.spi.history.ValueChange;


/**
 * Facility to record changes on a model
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ChangeRecorder extends GeneratorBase {

	/**
	 * Resource for which changes should be recorded
	 */
	private final List<? extends EObject> elements;

	/**
	 * Cache for candidates of deleted elements, maps candidates to related
	 * changes
	 */
	private Map<EObject, List<PrimitiveChange>> deleteCache;

	/**
	 * Adapter listening to the resource contents
	 */
	private final EContentAdapter adapter;

	/**
	 * Whether the recorder is currently recording
	 */
	private boolean recording;

	/**
	 * Whether the changes have been consolidated
	 */
	private boolean consolidated;

	/**
	 * Constructor
	 */
	public ChangeRecorder(List<? extends EObject> elements) {
		this.elements = elements;

		adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				switch (notification.getEventType()) {
				case SET:
					handleSet(notification);
					break;
				case ADD:
					handleAdd(notification);
					break;
				case ADD_MANY:
					handleAddMany(notification);
					break;
				case REMOVE:
					handleRemove(notification);
					break;
				case REMOVE_MANY:
					handleRemoveMany(notification);
					break;
				case MOVE:
					handleMove(notification);
					break;
				}
				super.notifyChanged(notification);
			}
		};

		beginRecording();
	}

	/**
	 * Start the recorder, unless already started
	 */
	public void beginRecording() {
		if (!isRecording()) {
			doBeginRecording();
		} else {
			throw new IllegalStateException("Recorder is already started");
		}
	}

	/**
	 * Start the recorder (can be overwritten by subclasses)
	 */
	protected void doBeginRecording() {
		changeContainer = HistoryFactory.eINSTANCE.createCompositeChange();
		deleteCache = new IdentityHashMap<EObject, List<PrimitiveChange>>();

		for (EObject element : elements) {
			element.eAdapters().add(adapter);
		}

		recording = true;
		consolidated = false;
	}

	/**
	 * Stop the recorder, unless already stopped
	 */
	public void endRecording() {
		if (isRecording()) {
			doEndRecording();
		} else {
			throw new IllegalStateException("Recorder is already stopped");
		}
	}

	/**
	 * Get the recorded changes.
	 */
	public CompositeChange getChanges() {

		if (!consolidated) {
			inferDeletes();
			inferCreateInitializers();

			consolidated = true;
		}

		return changeContainer;
	}

	/**
	 * Stop the recorder (can be overwritten by subclasses)
	 */
	protected void doEndRecording() {
		for (EObject element : elements) {
			element.eAdapters().remove(adapter);
		}

		recording = false;
	}

	/** Returns recording. */
	public boolean isRecording() {
		return recording;
	}

	/**
	 * Infer delete changes
	 */
	@SuppressWarnings("unchecked")
	private void inferDeletes() {
		for (Entry<EObject, List<PrimitiveChange>> entry : deleteCache
				.entrySet()) {
			List<PrimitiveChange> list = entry.getValue();
			Delete delete = (Delete) list.remove(0);
			delete.setElement(entry.getKey());
			delete.getChanges().addAll((List) list);
		}
	}

	/**
	 * Infer initializers of create changes
	 */
	private void inferCreateInitializers() {
		Create create = null;
		for (PrimitiveChange change : new ArrayList<PrimitiveChange>(
				changeContainer.getChanges())) {
			if (change instanceof Create) {
				create = (Create) change;
			} else if (change instanceof ValueChange) {
				if (create == null) {
					continue;
				}
				ValueChange valueChange = (ValueChange) change;
				if (create.getElement() == valueChange.getElement()) {
					create.getChanges().add(valueChange);
				} else {
					create = null;
				}
			} else {
				create = null;
			}
		}
	}

	/**
	 * Handle MOVE notification
	 */
	private void handleMove(
			@SuppressWarnings("unused") Notification notification) {
		// ignore
	}

	/**
	 * Handle REMOVE_MANY notification
	 */
	@SuppressWarnings("unchecked")
	private void handleRemoveMany(Notification notification) {
		EStructuralFeature feature = (EStructuralFeature) notification
				.getFeature();
		if (skipFeature(feature)) {
			return;
		}

		EObject element = (EObject) notification.getNotifier();

		// containment
		if (feature instanceof EReference
				&& ((EReference) feature).isContainment()) {
			for (EObject oldValue : (List<EObject>) notification.getOldValue()) {
				handleSingleDelete(element, (EReference) feature, oldValue);
			}
		}
		// cross reference and attribute
		else {
			for (Object oldValue : (List) notification.getOldValue()) {
				handleSingleRemove(element, feature, oldValue);
			}
		}
	}

	/**
	 * Handle REMOVE notification
	 */
	private void handleRemove(Notification notification) {
		EStructuralFeature feature = (EStructuralFeature) notification
				.getFeature();
		if (skipFeature(feature)) {
			return;
		}

		EObject element = (EObject) notification.getNotifier();

		// containment
		if (feature instanceof EReference
				&& ((EReference) feature).isContainment()) {
			EObject oldValue = (EObject) notification.getOldValue();
			handleSingleDelete(element, (EReference) feature, oldValue);
		}
		// cross reference and attribute
		else {
			Object oldValue = notification.getOldValue();
			handleSingleRemove(element, feature, oldValue);
		}
	}

	/**
	 * Handle a remove of a single element from a containment reference
	 */
	private void handleSingleDelete(EObject element, EReference reference,
			EObject value) {
		Delete delete = delete(null, reference, element);
		append(delete);
		ArrayList<PrimitiveChange> list = new ArrayList<PrimitiveChange>();
		list.add(delete);
		deleteCache.put(value, list);
	}

	/**
	 * Handle a remove of a single element from a cross reference or attribute
	 */
	private void handleSingleRemove(EObject element,
			EStructuralFeature feature, Object value) {
		Remove remove = remove(element, feature, value);
		append(remove);

		if (feature instanceof EReference) {
			EObject deleted = getDeleted((EObject) value);
			if (deleted != null) {
				deleteCache.get(deleted).add(remove);
			}
		}
	}

	/**
	 * Handle ADD_MANY notification
	 */
	@SuppressWarnings("unchecked")
	private void handleAddMany(Notification notification) {
		EStructuralFeature feature = (EStructuralFeature) notification
				.getFeature();
		if (skipFeature(feature)) {
			return;
		}

		EObject element = (EObject) notification.getNotifier();

		// containment
		if (feature instanceof EReference
				&& ((EReference) feature).isContainment()) {
			for (EObject value : (List<EObject>) notification.getNewValue()) {
				handleSingleCreate(feature, element, value);
			}
		}
		// cross reference and attribute
		else {
			for (Object value : (List) notification.getNewValue()) {
				handleSingleAdd(feature, element, value);
			}
		}
	}

	/**
	 * Handle ADD notification
	 */
	private void handleAdd(Notification notification) {
		EStructuralFeature feature = (EStructuralFeature) notification
				.getFeature();
		if (skipFeature(feature)) {
			return;
		}

		EObject element = (EObject) notification.getNotifier();

		// containment
		if (feature instanceof EReference
				&& ((EReference) feature).isContainment()) {
			EObject newValue = (EObject) notification.getNewValue();
			handleSingleCreate(feature, element, newValue);
		}
		// cross reference and attribute
		else {
			Object newValue = notification.getNewValue();
			handleSingleAdd(feature, element, newValue);
		}
	}

	/**
	 * Handle an addition of a single element to a cross reference or attribute
	 */
	private void handleSingleAdd(EStructuralFeature feature, EObject element,
			Object value) {
		Add add = add(element, feature, value);
		append(add);
	}

	/**
	 * Handle an addition of a single element to a containment reference
	 */
	private void handleSingleCreate(EStructuralFeature feature,
			EObject element, EObject value) {
		if (deleteCache.containsKey(value)) {
			Delete delete = (Delete) deleteCache.remove(value).get(0);
			Move move = move(value, delete.getTarget(), (EReference) feature,
					element);

			replace(delete, move);
		} else {
			generateElements(Collections.singletonList(value));
		}
	}

	/**
	 * Handle SET notification
	 */
	private void handleSet(Notification notification) {
		EStructuralFeature feature = (EStructuralFeature) notification
				.getFeature();
		if (skipFeature(feature)) {
			return;
		}

		// containment
		if (feature instanceof EReference
				&& ((EReference) feature).isContainment()) {
			return;
		}

		// cross reference and attribute
		if (notification.getOldValue() == notification.getNewValue()) {
			// no change
			return;
		}

		EObject element = (EObject) notification.getNotifier();
		Object newValue = notification.getNewValue();
		Set set = set(element, notification.getOldValue(), feature, newValue);
		append(set);

		if (feature instanceof EReference) {
			EObject oldValue = (EObject) notification.getOldValue();
			EObject deleted = getDeleted(oldValue);
			if (deleted != null) {
				deleteCache.get(deleted).add(set);
			}
		}
	}

	/**
	 * Replace a primitive change by another one in the changes
	 */
	protected void replace(PrimitiveChange toReplace, PrimitiveChange replaceBy) {
		List<PrimitiveChange> changes = changeContainer.getChanges();
		int index = changes.indexOf(toReplace);
		changes.set(index, replaceBy);
	}

	/**
	 * Get parent element which was deleted
	 */
	private EObject getDeleted(EObject element) {
		while (element != null) {
			if (deleteCache.containsKey(element)) {
				return element;
			}
			element = element.eContainer();
		}
		return null;
	}

	/** Returns elements. */
	public List<? extends EObject> getElements() {
		return elements;
	}
}
