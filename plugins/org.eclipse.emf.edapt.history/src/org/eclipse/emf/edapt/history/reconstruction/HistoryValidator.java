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
package org.eclipse.emf.edapt.history.reconstruction;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreSwitch;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.TypeUtils;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.InitializerChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.util.HistorySwitch;


/**
 * A reconstructor that follows the history and checks whether changes are
 * breaking
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class HistoryValidator extends ReconstructorBase {

	/**
	 * Switch to distinguish between different types of changes
	 */
	private ValidatorSwitch validatorSwitch;

	/**
	 * Mapping
	 */
	private Mapping mapping;

	/**
	 * Elements created within a certain period
	 */
	private final Set<EObject> createdElements;

	/**
	 * Whether current release is the first release
	 */
	private boolean inFirstRelease;

	/**
	 * Whether reconstruction currently is inside a migration change
	 */
	private boolean inMigrationChange;

	/**
	 * Whether reconstruction currently is inside an operation change
	 */
	private boolean inOperationChange;

	/**
	 * Whether reconstruction currently is inside an initializer change
	 */
	private boolean inInitializerChange;

	/**
	 * Default constructor
	 * 
	 */
	public HistoryValidator() {
		createdElements = new HashSet<EObject>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		this.mapping = mapping;
		validatorSwitch = new ValidatorSwitch();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startRelease(Release originalRelease) {
		inFirstRelease = originalRelease.isFirstRelease();
		reset();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startChange(Change originalChange) {

		if (canIgnore()) {
			originalChange.setBreaking(false);
			return;
		}

		originalChange.setBreaking(validatorSwitch.doSwitch(originalChange));

		startIgnore(originalChange);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endChange(Change change) {

		endIgnore(change);

		if (canIgnore()) {
			return;
		}

		endUpdate(change);
	}

	/**
	 * Check whether the current change can be ignored
	 */
	private boolean canIgnore() {
		return inFirstRelease || inMigrationChange || inOperationChange
				|| inInitializerChange;
	}

	/**
	 * Set ignore status when starting a change
	 */
	private void startIgnore(Change change) {
		if (change instanceof MigrationChange) {
			inMigrationChange = true;
			change.setBreaking(false);
			reset();
		} else if (change instanceof OperationChange) {
			inOperationChange = true;
			change.setBreaking(false);
			reset();
		} else if (change instanceof InitializerChange) {
			inInitializerChange = true;
			change.setBreaking(false);
		}
	}

	/**
	 * Unset ignore status when ending a change
	 */
	private void endIgnore(Change change) {
		if (change instanceof MigrationChange) {
			inMigrationChange = false;
			reset();
		} else if (change instanceof OperationChange) {
			inOperationChange = false;
			reset();
		} else if (change instanceof InitializerChange) {
			inInitializerChange = false;
		}
	}

	/**
	 * Perform updates when ending a change
	 */
	private void endUpdate(Change change) {
		if (change instanceof Create) {
			// add created element
			Create createChild = (Create) change;
			EObject created = mapping.resolveTarget(createChild.getElement());
			created(created);
		} else if (change instanceof CompositeChange
				&& !(change instanceof OperationChange)) {
			// update breaking status of composite change
			CompositeChange compositeChange = (CompositeChange) change;
			boolean breaking = false;
			for (PrimitiveChange primitiveChange : compositeChange.getChanges()) {
				breaking |= primitiveChange.isBreaking();
			}
			compositeChange.setBreaking(breaking);
			reset();
		}
	}

	/**
	 * Register a created element
	 */
	private void created(EObject element) {
		createdElements.add(element);
	}

	/**
	 * Check whether an element was recently created
	 * 
	 * @param element
	 *            Element
	 * @return true if the element was recently created, false otherwise
	 */
	private boolean isCreated(EObject element) {
		return createdElements.contains(element);
	}

	/**
	 * Reset the registry of recently created elements
	 */
	private void reset() {
		createdElements.clear();
	}

	/**
	 * Switch to distinguish between different types of changes
	 */
	private class ValidatorSwitch extends HistorySwitch<Boolean> {

		/**
		 * Ecore package
		 */
		private final EcorePackage p = EcorePackage.eINSTANCE;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseSet(final org.eclipse.emf.edapt.history.Set set) {
			final EStructuralFeature feature = set.getFeature();
			EObject element = mapping.resolveTarget(set.getElement());
			if (isCreated(element)) {
				return false;
			}

			/**
			 * Switch distinguish element types
			 */
			EcoreSwitch<Boolean> setValidatorSwitch = new EcoreSwitch<Boolean>() {
				@Override
				public Boolean caseETypedElement(ETypedElement object) {

					if (object instanceof EOperation) {
						return false;
					} else if (object instanceof EStructuralFeature
							&& (feature).isDerived()) {
						return false;
					}

					switch (feature.getFeatureID()) {
						// ETypedElement.unique -> true
						case EcorePackage.ETYPED_ELEMENT__UNIQUE: {
							boolean value = (Boolean) set.getValue();
							if (value) {
								return true;
							}
							break;
						}
						// ETypedElement.upperBound
						case EcorePackage.ETYPED_ELEMENT__UPPER_BOUND: {
							int value = (Integer) set.getValue();
							int oldValue = (Integer) set.getOldValue();
							// decrease
							if (oldValue < 0) {
								return true;
							} else if (value >= 0 && value < oldValue) {
								return true;
							}
							break;
						}
						// ETypedElement.eType
						case EcorePackage.ETYPED_ELEMENT__ETYPE: {
							if (set.getElement() instanceof EReference) {
								EClass oldValue = (EClass) mapping
										.resolveTarget(set.getOldValue());
								EClass newValue = (EClass) mapping.resolveTarget(set
										.getValue());
								if (!TypeUtils.ancestor(oldValue, newValue)) {
									return true;
								}
							} else {
								return true;
							}
							break;
						}
					}

					return null;
				}
				
				@Override
				public Boolean caseEStructuralFeature(EStructuralFeature object) {
					switch (feature.getFeatureID()) {
						// EStructuralFeature.transient -> true
						case EcorePackage.ESTRUCTURAL_FEATURE__TRANSIENT: {
							boolean value = (Boolean) set.getValue();
							if (value) {
								return true;
							}
							break;
						}
					}
					
					return null;
				}
				
				@Override
				public Boolean caseEAttribute(EAttribute object) {
					switch(feature.getFeatureID()) {
						// EAttribute.iD -> true
						case EcorePackage.EATTRIBUTE__ID: {
							boolean value = (Boolean) set.getValue();
							if (value) {
								return true;
							}
							break;
						}
					}
					
					return null;
				}
				
				@Override
				public Boolean caseEReference(EReference object) {
					switch(feature.getFeatureID()) {
						// EReference.containment
						case EcorePackage.EREFERENCE__CONTAINMENT: {
							return true;
						}
						// EReference.eOpposite
						case EcorePackage.EREFERENCE__EOPPOSITE: {
							return true;
						}
					}

					return null;
				}

				@Override
				public Boolean caseEClass(EClass eClass) {
					switch (feature.getFeatureID()) {
						case EcorePackage.ECLASS__ABSTRACT: {
							// EClass.abstract -> true
							if (feature == p.getEClass_Abstract()) {
								boolean value = (Boolean) set.getValue();
								if (value && !eClass.isInterface()) {
									return true;
								}
							}
							break;
						}
						// EClass.interface -> true
						case EcorePackage.ECLASS__INTERFACE: {
							boolean value = (Boolean) set.getValue();
								if (value && !eClass.isAbstract()) {
									return true;
								}
							break;
						}
					}

					return null;
				}

				@Override
				public Boolean defaultCase(EObject object) {
					return false;
				}
			};

			return setValidatorSwitch.doSwitch(element);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseRemove(Remove remove) {
			EStructuralFeature feature = remove.getFeature();
			EObject element = mapping.resolveTarget(remove.getElement());
			if (isCreated(element)) {
				return false;
			}

			// EClass.eSuperTypes
			if (element instanceof EClass) {
				switch (feature.getFeatureID()) {
				case EcorePackage.ECLASS__ESUPER_TYPES:
					EClass eClass = (EClass) remove.getValue();
					if (!eClass.getEAllStructuralFeatures().isEmpty()) {
						return true;
					}
					break;
				}
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseDelete(Delete delete) {
			EObject element = mapping.resolveTarget(delete.getElement());
			if (isCreated(element)) {
				return false;
			}

			if (element instanceof EStructuralFeature) {
				return true;
			} else if (!delete.getChanges().isEmpty()) {
				return true;
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseMove(Move move) {
			EObject element = move.getElement();
			if (element instanceof EStructuralFeature) {
				return true;
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean defaultCase(EObject object) {
			return false;
		}
	}
}
