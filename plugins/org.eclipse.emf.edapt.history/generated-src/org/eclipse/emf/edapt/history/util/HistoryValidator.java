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
package org.eclipse.emf.edapt.history.util;

import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.edapt.history.*;
import org.eclipse.emf.edapt.history.Add;
import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.CompositeChange;
import org.eclipse.emf.edapt.history.ContentChange;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.HistoryPlugin;
import org.eclipse.emf.edapt.history.InitializerChange;
import org.eclipse.emf.edapt.history.MigrateableChange;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.ModelReference;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.NamedElement;
import org.eclipse.emf.edapt.history.NoChange;
import org.eclipse.emf.edapt.history.NonDelete;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;
import org.eclipse.emf.edapt.history.PrimitiveChange;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.Set;
import org.eclipse.emf.edapt.history.ValueChange;


/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.edapt.history.HistoryPackage
 * @generated
 */
public class HistoryValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final HistoryValidator INSTANCE = new HistoryValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.emf.edapt.history";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HistoryValidator() {
		super();
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return HistoryPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case HistoryPackage.HISTORY:
				return validateHistory((History)value, diagnostics, context);
			case HistoryPackage.RELEASE:
				return validateRelease((Release)value, diagnostics, context);
			case HistoryPackage.CHANGE:
				return validateChange((Change)value, diagnostics, context);
			case HistoryPackage.PRIMITIVE_CHANGE:
				return validatePrimitiveChange((PrimitiveChange)value, diagnostics, context);
			case HistoryPackage.NO_CHANGE:
				return validateNoChange((NoChange)value, diagnostics, context);
			case HistoryPackage.CONTENT_CHANGE:
				return validateContentChange((ContentChange)value, diagnostics, context);
			case HistoryPackage.NON_DELETE:
				return validateNonDelete((NonDelete)value, diagnostics, context);
			case HistoryPackage.CREATE:
				return validateCreate((Create)value, diagnostics, context);
			case HistoryPackage.MOVE:
				return validateMove((Move)value, diagnostics, context);
			case HistoryPackage.DELETE:
				return validateDelete((Delete)value, diagnostics, context);
			case HistoryPackage.VALUE_CHANGE:
				return validateValueChange((ValueChange)value, diagnostics, context);
			case HistoryPackage.SET:
				return validateSet((Set)value, diagnostics, context);
			case HistoryPackage.ADD:
				return validateAdd((Add)value, diagnostics, context);
			case HistoryPackage.REMOVE:
				return validateRemove((Remove)value, diagnostics, context);
			case HistoryPackage.COMPOSITE_CHANGE:
				return validateCompositeChange((CompositeChange)value, diagnostics, context);
			case HistoryPackage.OPERATION_CHANGE:
				return validateOperationChange((OperationChange)value, diagnostics, context);
			case HistoryPackage.OPERATION_INSTANCE:
				return validateOperationInstance((OperationInstance)value, diagnostics, context);
			case HistoryPackage.PARAMETER_INSTANCE:
				return validateParameterInstance((ParameterInstance)value, diagnostics, context);
			case HistoryPackage.MODEL_REFERENCE:
				return validateModelReference((ModelReference)value, diagnostics, context);
			case HistoryPackage.NAMED_ELEMENT:
				return validateNamedElement((NamedElement)value, diagnostics, context);
			case HistoryPackage.MIGRATEABLE_CHANGE:
				return validateMigrateableChange((MigrateableChange)value, diagnostics, context);
			case HistoryPackage.MIGRATION_CHANGE:
				return validateMigrationChange((MigrationChange)value, diagnostics, context);
			case HistoryPackage.INITIALIZER_CHANGE:
				return validateInitializerChange((InitializerChange)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateHistory(History history, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(history, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRelease(Release release, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(release, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateChange(Change change, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(change, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(change, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(change, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(change, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(change, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(change, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(change, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(change, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(change, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(change, diagnostics, context);
		return result;
	}

	/**
	 * Validates the Breaking constraint of '<em>Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateChange_Breaking(Change change, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (change.isBreaking()) {
			if (diagnostics != null) {
				diagnostics.add
					(new BasicDiagnostic
						(Diagnostic.ERROR,
						 DIAGNOSTIC_SOURCE,
						 0,
						 EcorePlugin.INSTANCE.getString("_UI_GenericConstraint_diagnostic", new Object[] { "Breaking", getObjectLabel(change, context) }),
						 new Object[] { change }));
			}
			return false;
		}
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePrimitiveChange(PrimitiveChange primitiveChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(primitiveChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(primitiveChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(primitiveChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNoChange(NoChange noChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(noChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(noChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(noChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateContentChange(ContentChange contentChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(contentChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(contentChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(contentChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNonDelete(NonDelete nonDelete, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(nonDelete, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(nonDelete, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(nonDelete, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCreate(Create create, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(create, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(create, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(create, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(create, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(create, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(create, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(create, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(create, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(create, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(create, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMove(Move move, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(move, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(move, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(move, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(move, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(move, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(move, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(move, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(move, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(move, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(move, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDelete(Delete delete, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(delete, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(delete, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(delete, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateValueChange(ValueChange valueChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(valueChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(valueChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(valueChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSet(Set set, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(set, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(set, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(set, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(set, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(set, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(set, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(set, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(set, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(set, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(set, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAdd(Add add, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(add, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(add, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(add, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(add, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(add, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(add, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(add, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(add, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(add, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(add, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRemove(Remove remove, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(remove, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(remove, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(remove, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCompositeChange(CompositeChange compositeChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(compositeChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(compositeChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(compositeChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOperationChange(OperationChange operationChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(operationChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(operationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(operationChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOperationInstance(OperationInstance operationInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(operationInstance, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateParameterInstance(ParameterInstance parameterInstance, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(parameterInstance, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateModelReference(ModelReference modelReference, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(modelReference, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamedElement(NamedElement namedElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(namedElement, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMigrateableChange(MigrateableChange migrateableChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(migrateableChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(migrateableChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(migrateableChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMigrationChange(MigrationChange migrationChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(migrationChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(migrationChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(migrationChange, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateInitializerChange(InitializerChange initializerChange, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(initializerChange, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(initializerChange, diagnostics, context);
		if (result || diagnostics != null) result &= validateChange_Breaking(initializerChange, diagnostics, context);
		return result;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return HistoryPlugin.INSTANCE;
	}

} //HistoryValidator
