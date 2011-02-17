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
package org.eclipse.emf.edapt.history.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.ValueChange;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ValueChangeImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ValueChangeImpl#getFeature <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ValueChangeImpl#getFeatureName <em>Feature Name</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ValueChangeImpl#getDataValue <em>Data Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ValueChangeImpl#getReferenceValue <em>Reference Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ValueChangeImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ValueChangeImpl extends PrimitiveChangeImpl implements ValueChange {
	/**
	 * The cached value of the '{@link #getElement() <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElement()
	 * @generated
	 * @ordered
	 */
	protected EObject element;

	/**
	 * The default value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected static final String FEATURE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected String featureName = FEATURE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDataValue() <em>Data Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataValue()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataValue() <em>Data Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataValue()
	 * @generated
	 * @ordered
	 */
	protected String dataValue = DATA_VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReferenceValue() <em>Reference Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceValue()
	 * @generated
	 * @ordered
	 */
	protected EObject referenceValue;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object VALUE_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ValueChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HistoryPackage.Literals.VALUE_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getElement() {
		if (element != null && element.eIsProxy()) {
			InternalEObject oldElement = (InternalEObject)element;
			element = eResolveProxy(oldElement);
			if (element != oldElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, HistoryPackage.VALUE_CHANGE__ELEMENT, oldElement, element));
			}
		}
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetElement() {
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElement(EObject newElement) {
		EObject oldElement = element;
		element = newElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HistoryPackage.VALUE_CHANGE__ELEMENT, oldElement, element));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature getFeature() {
		EStructuralFeature feature = basicGetFeature();
		return feature != null && feature.eIsProxy() ? (EStructuralFeature)eResolveProxy((InternalEObject)feature) : feature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EStructuralFeature basicGetFeature() {
		EObject element = this.getElement();
		String featureName = this.getFeatureName();
		if(element != null && featureName != null) {
			EClass clazz = element.eClass();
			return clazz.getEStructuralFeature(featureName);
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setFeature(EStructuralFeature newFeature) {
		if(newFeature != null) {
			this.setFeatureName(newFeature.getName());
		}
		else {
			this.setFeatureName(null);
		} 
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFeatureName(String newFeatureName) {
		String oldFeatureName = featureName;
		featureName = newFeatureName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HistoryPackage.VALUE_CHANGE__FEATURE_NAME, oldFeatureName, featureName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDataValue() {
		return dataValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataValue(String newDataValue) {
		String oldDataValue = dataValue;
		dataValue = newDataValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HistoryPackage.VALUE_CHANGE__DATA_VALUE, oldDataValue, dataValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getReferenceValue() {
		if (referenceValue != null && referenceValue.eIsProxy()) {
			InternalEObject oldReferenceValue = (InternalEObject)referenceValue;
			referenceValue = eResolveProxy(oldReferenceValue);
			if (referenceValue != oldReferenceValue) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, HistoryPackage.VALUE_CHANGE__REFERENCE_VALUE, oldReferenceValue, referenceValue));
			}
		}
		return referenceValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetReferenceValue() {
		return referenceValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferenceValue(EObject newReferenceValue) {
		EObject oldReferenceValue = referenceValue;
		referenceValue = newReferenceValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HistoryPackage.VALUE_CHANGE__REFERENCE_VALUE, oldReferenceValue, referenceValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object getValue() {
		EStructuralFeature feature = this.getFeature();
		if(feature instanceof EReference) {
			return this.getReferenceValue();
		}
		EAttribute attribute = (EAttribute) feature;
		EDataType type = attribute.getEAttributeType();
		return EcoreUtil.createFromString(type, this.getDataValue());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setValue(Object newValue) {
		EStructuralFeature feature = this.getFeature();
		if(feature instanceof EReference) {
			this.setReferenceValue((EObject) newValue);
		}
		else {
			EAttribute attribute = (EAttribute) feature;
			EDataType type = attribute.getEAttributeType();
			this.setDataValue(EcoreUtil.convertToString(type, newValue));
		} 
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case HistoryPackage.VALUE_CHANGE__ELEMENT:
				if (resolve) return getElement();
				return basicGetElement();
			case HistoryPackage.VALUE_CHANGE__FEATURE:
				if (resolve) return getFeature();
				return basicGetFeature();
			case HistoryPackage.VALUE_CHANGE__FEATURE_NAME:
				return getFeatureName();
			case HistoryPackage.VALUE_CHANGE__DATA_VALUE:
				return getDataValue();
			case HistoryPackage.VALUE_CHANGE__REFERENCE_VALUE:
				if (resolve) return getReferenceValue();
				return basicGetReferenceValue();
			case HistoryPackage.VALUE_CHANGE__VALUE:
				return getValue();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case HistoryPackage.VALUE_CHANGE__ELEMENT:
				setElement((EObject)newValue);
				return;
			case HistoryPackage.VALUE_CHANGE__FEATURE:
				setFeature((EStructuralFeature)newValue);
				return;
			case HistoryPackage.VALUE_CHANGE__FEATURE_NAME:
				setFeatureName((String)newValue);
				return;
			case HistoryPackage.VALUE_CHANGE__DATA_VALUE:
				setDataValue((String)newValue);
				return;
			case HistoryPackage.VALUE_CHANGE__REFERENCE_VALUE:
				setReferenceValue((EObject)newValue);
				return;
			case HistoryPackage.VALUE_CHANGE__VALUE:
				setValue(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case HistoryPackage.VALUE_CHANGE__ELEMENT:
				setElement((EObject)null);
				return;
			case HistoryPackage.VALUE_CHANGE__FEATURE:
				setFeature((EStructuralFeature)null);
				return;
			case HistoryPackage.VALUE_CHANGE__FEATURE_NAME:
				setFeatureName(FEATURE_NAME_EDEFAULT);
				return;
			case HistoryPackage.VALUE_CHANGE__DATA_VALUE:
				setDataValue(DATA_VALUE_EDEFAULT);
				return;
			case HistoryPackage.VALUE_CHANGE__REFERENCE_VALUE:
				setReferenceValue((EObject)null);
				return;
			case HistoryPackage.VALUE_CHANGE__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case HistoryPackage.VALUE_CHANGE__ELEMENT:
				return element != null;
			case HistoryPackage.VALUE_CHANGE__FEATURE:
				return basicGetFeature() != null;
			case HistoryPackage.VALUE_CHANGE__FEATURE_NAME:
				return FEATURE_NAME_EDEFAULT == null ? featureName != null : !FEATURE_NAME_EDEFAULT.equals(featureName);
			case HistoryPackage.VALUE_CHANGE__DATA_VALUE:
				return DATA_VALUE_EDEFAULT == null ? dataValue != null : !DATA_VALUE_EDEFAULT.equals(dataValue);
			case HistoryPackage.VALUE_CHANGE__REFERENCE_VALUE:
				return referenceValue != null;
			case HistoryPackage.VALUE_CHANGE__VALUE:
				return VALUE_EDEFAULT == null ? getValue() != null : !VALUE_EDEFAULT.equals(getValue());
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (featureName: ");
		result.append(featureName);
		result.append(", dataValue: ");
		result.append(dataValue);
		result.append(')');
		return result.toString();
	}

} //ValueChangeImpl
