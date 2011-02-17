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
package org.eclipse.emf.edapt.cope.history.reconstruction.ui;

import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.cope.common.ui.ModelSash;
import org.eclipse.emf.edapt.cope.common.ui.PropertiesTableViewer;
import org.eclipse.emf.edapt.cope.history.reconstruction.MappingBase;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TreeItem;


/**
 * Adapter to listen to the difference viewer that automatically updates
 * source and target metamodel viewer
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class DiffSelectionAdapter extends SelectionAdapter {

	/**
	 * Viewer for the structure of the source metamodel
	 */
	private final Viewer sourceStructureViewer;

	/**
	 * Viewer for the properties of the source metamodel (can be null)
	 */
	private final PropertiesTableViewer sourcePropertiesViewer;
	
	/**
	 * Viewer for the structure of the target metamodel
	 */
	private final Viewer targetStructureViewer;

	/**
	 * Viewer for the properties of the target metamodel (can be null)
	 */
	private final PropertiesTableViewer targetPropertiesViewer;
	
	/**
	 * Mapping between elements of source and target metamodel
	 */
	private MappingBase mapping;
	
	/**
	 * Constructor
	 */
	public DiffSelectionAdapter(ModelSash sourceSash, ModelSash targetSash) {
		this(sourceSash.getStructureViewer(), sourceSash.getPropertiesViewer(), targetSash.getStructureViewer(), targetSash.getPropertiesViewer());
	}

	/**
	 * Constructor
	 */
	public DiffSelectionAdapter(Viewer sourceStructureViewer,
			PropertiesTableViewer sourcePropertiesViewer,
			Viewer targetStructureViewer,
			PropertiesTableViewer targetPropertiesViewer) {
		this.sourceStructureViewer = sourceStructureViewer;
		this.sourcePropertiesViewer = sourcePropertiesViewer;
		this.targetStructureViewer = targetStructureViewer;
		this.targetPropertiesViewer = targetPropertiesViewer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		Object data = ((TreeItem) e.item).getData();
		if(data instanceof EObject) {
			EObject element = (EObject) data;
			DiffSwitch<Boolean> s = new DiffSwitch<Boolean>() {
				
				@Override
				public Boolean caseDiffGroup(DiffGroup object) {
					EObject source = object.getRightParent();
					selectSource(source);
					if(mapping != null) {
						selectTarget(mapping.getTarget(source));
					}
					return true;
				}
				
				@Override
				public Boolean caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
					selectSource(object.getRightParent());
					selectTarget(object.getLeftElement());
					return true;
				}
				
				@Override
				public Boolean caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
					selectSource(object.getRightElement());
					selectTarget(object.getLeftParent());
					return true;
				}
				
				@Override
				public Boolean caseUpdateModelElement(UpdateModelElement object) {
					selectSource(object.getRightElement());
					selectTarget(object.getLeftElement());
					return true;
				}
				
				@Override
				public Boolean caseReferenceChange(ReferenceChange object) {
					selectSource(object.getRightElement());
					selectTarget(object.getLeftElement());
					selectSourceProperty(object.getReference());
					selectTargetProperty(object.getReference());
					return true;
				}
				
				@Override
				public Boolean caseAttributeChange(AttributeChange object) {
					selectSource(object.getRightElement());
					selectTarget(object.getLeftElement());
					selectSourceProperty(object.getAttribute());
					selectTargetProperty(object.getAttribute());
					return true;
				}

			};
			s.doSwitch(element);
		}
	}

	/**
	 * Selects an element in the structure viewer of the source metamodel
	 */
	public void selectSource(EObject element) {
		sourceStructureViewer.setSelection(element == null ? StructuredSelection.EMPTY : new StructuredSelection(element), true);
	}

	/**
	 * Selects an element in the structure viewer of the target metamodel
	 */
	public void selectTarget(EObject element) {
		targetStructureViewer.setSelection(element == null ? StructuredSelection.EMPTY : new StructuredSelection(element), true);
	}

	/**
	 * Selects a feature in the properties viewer of the source metamodel
	 */
	private void selectSourceProperty(EStructuralFeature feature) {
		selectProperty(sourcePropertiesViewer, feature);
	}
	
	/**
	 * Selects a feature in the properties viewer of the target metamodel
	 */
	private void selectTargetProperty(EStructuralFeature feature) {
		selectProperty(targetPropertiesViewer, feature);
	}

	/**
	 * Select a feature in a properties viewer
	 */
	private void selectProperty(Viewer viewer, EStructuralFeature feature) {
		if(viewer != null) {
			viewer.setSelection(new StructuredSelection(feature), true);
		}
	}

	/** Sets mapping. */
	public void setMapping(MappingBase mapping) {
		this.mapping = mapping;
	}

	/** Returns mapping. */
	public MappingBase getMapping() {
		return mapping;
	}

}
