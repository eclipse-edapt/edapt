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
package org.eclipse.emf.edapt.spi.history.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.spi.history.*;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage
 * @generated
 */
public class HistorySwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static HistoryPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HistorySwitch() {
		if (modelPackage == null) {
			modelPackage = HistoryPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case HistoryPackage.HISTORY: {
				History history = (History)theEObject;
				T result = caseHistory(history);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.RELEASE: {
				Release release = (Release)theEObject;
				T result = caseRelease(release);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.CHANGE: {
				Change change = (Change)theEObject;
				T result = caseChange(change);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.PRIMITIVE_CHANGE: {
				PrimitiveChange primitiveChange = (PrimitiveChange)theEObject;
				T result = casePrimitiveChange(primitiveChange);
				if (result == null) result = caseMigrateableChange(primitiveChange);
				if (result == null) result = caseChange(primitiveChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.NO_CHANGE: {
				NoChange noChange = (NoChange)theEObject;
				T result = caseNoChange(noChange);
				if (result == null) result = casePrimitiveChange(noChange);
				if (result == null) result = caseMigrateableChange(noChange);
				if (result == null) result = caseChange(noChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.CONTENT_CHANGE: {
				ContentChange contentChange = (ContentChange)theEObject;
				T result = caseContentChange(contentChange);
				if (result == null) result = casePrimitiveChange(contentChange);
				if (result == null) result = caseMigrateableChange(contentChange);
				if (result == null) result = caseChange(contentChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.NON_DELETE: {
				NonDelete nonDelete = (NonDelete)theEObject;
				T result = caseNonDelete(nonDelete);
				if (result == null) result = caseContentChange(nonDelete);
				if (result == null) result = casePrimitiveChange(nonDelete);
				if (result == null) result = caseMigrateableChange(nonDelete);
				if (result == null) result = caseChange(nonDelete);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.CREATE: {
				Create create = (Create)theEObject;
				T result = caseCreate(create);
				if (result == null) result = caseNonDelete(create);
				if (result == null) result = caseInitializerChange(create);
				if (result == null) result = caseContentChange(create);
				if (result == null) result = casePrimitiveChange(create);
				if (result == null) result = caseMigrateableChange(create);
				if (result == null) result = caseChange(create);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.MOVE: {
				Move move = (Move)theEObject;
				T result = caseMove(move);
				if (result == null) result = caseNonDelete(move);
				if (result == null) result = caseContentChange(move);
				if (result == null) result = casePrimitiveChange(move);
				if (result == null) result = caseMigrateableChange(move);
				if (result == null) result = caseChange(move);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.DELETE: {
				Delete delete = (Delete)theEObject;
				T result = caseDelete(delete);
				if (result == null) result = caseInitializerChange(delete);
				if (result == null) result = caseContentChange(delete);
				if (result == null) result = casePrimitiveChange(delete);
				if (result == null) result = caseMigrateableChange(delete);
				if (result == null) result = caseChange(delete);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.VALUE_CHANGE: {
				ValueChange valueChange = (ValueChange)theEObject;
				T result = caseValueChange(valueChange);
				if (result == null) result = casePrimitiveChange(valueChange);
				if (result == null) result = caseMigrateableChange(valueChange);
				if (result == null) result = caseChange(valueChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.SET: {
				Set set = (Set)theEObject;
				T result = caseSet(set);
				if (result == null) result = caseValueChange(set);
				if (result == null) result = casePrimitiveChange(set);
				if (result == null) result = caseMigrateableChange(set);
				if (result == null) result = caseChange(set);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.ADD: {
				Add add = (Add)theEObject;
				T result = caseAdd(add);
				if (result == null) result = caseValueChange(add);
				if (result == null) result = casePrimitiveChange(add);
				if (result == null) result = caseMigrateableChange(add);
				if (result == null) result = caseChange(add);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.REMOVE: {
				Remove remove = (Remove)theEObject;
				T result = caseRemove(remove);
				if (result == null) result = caseValueChange(remove);
				if (result == null) result = casePrimitiveChange(remove);
				if (result == null) result = caseMigrateableChange(remove);
				if (result == null) result = caseChange(remove);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.COMPOSITE_CHANGE: {
				CompositeChange compositeChange = (CompositeChange)theEObject;
				T result = caseCompositeChange(compositeChange);
				if (result == null) result = caseMigrateableChange(compositeChange);
				if (result == null) result = caseChange(compositeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.OPERATION_CHANGE: {
				OperationChange operationChange = (OperationChange)theEObject;
				T result = caseOperationChange(operationChange);
				if (result == null) result = caseCompositeChange(operationChange);
				if (result == null) result = caseMigrateableChange(operationChange);
				if (result == null) result = caseChange(operationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.OPERATION_INSTANCE: {
				OperationInstance operationInstance = (OperationInstance)theEObject;
				T result = caseOperationInstance(operationInstance);
				if (result == null) result = caseNamedElement(operationInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.PARAMETER_INSTANCE: {
				ParameterInstance parameterInstance = (ParameterInstance)theEObject;
				T result = caseParameterInstance(parameterInstance);
				if (result == null) result = caseNamedElement(parameterInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.MODEL_REFERENCE: {
				ModelReference modelReference = (ModelReference)theEObject;
				T result = caseModelReference(modelReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.NAMED_ELEMENT: {
				NamedElement namedElement = (NamedElement)theEObject;
				T result = caseNamedElement(namedElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.MIGRATEABLE_CHANGE: {
				MigrateableChange migrateableChange = (MigrateableChange)theEObject;
				T result = caseMigrateableChange(migrateableChange);
				if (result == null) result = caseChange(migrateableChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.MIGRATION_CHANGE: {
				MigrationChange migrationChange = (MigrationChange)theEObject;
				T result = caseMigrationChange(migrationChange);
				if (result == null) result = caseChange(migrationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HistoryPackage.INITIALIZER_CHANGE: {
				InitializerChange initializerChange = (InitializerChange)theEObject;
				T result = caseInitializerChange(initializerChange);
				if (result == null) result = caseContentChange(initializerChange);
				if (result == null) result = casePrimitiveChange(initializerChange);
				if (result == null) result = caseMigrateableChange(initializerChange);
				if (result == null) result = caseChange(initializerChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>History</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>History</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHistory(History object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Release</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Release</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRelease(Release object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChange(Change object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Primitive Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Primitive Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePrimitiveChange(PrimitiveChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>No Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>No Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNoChange(NoChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Content Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Content Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContentChange(ContentChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Non Delete</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Non Delete</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNonDelete(NonDelete object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Create</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Create</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCreate(Create object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Move</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Move</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMove(Move object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Delete</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Delete</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDelete(Delete object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Value Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Value Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseValueChange(ValueChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSet(Set object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Add</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Add</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAdd(Add object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remove</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remove</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRemove(Remove object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Composite Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Composite Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCompositeChange(CompositeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationChange(OperationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationInstance(OperationInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parameter Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameter Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParameterInstance(ParameterInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelReference(ModelReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedElement(NamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Migrateable Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Migrateable Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMigrateableChange(MigrateableChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Migration Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Migration Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMigrationChange(MigrationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Initializer Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Initializer Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInitializerChange(InitializerChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //HistorySwitch
