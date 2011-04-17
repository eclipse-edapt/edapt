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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.history.HistoryFactory;
import org.eclipse.emf.edapt.history.HistoryPackage;
import org.eclipse.emf.edapt.history.ModelReference;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ParameterInstanceImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ParameterInstanceImpl#getDataValue <em>Data Value</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.history.impl.ParameterInstanceImpl#getReferenceValue <em>Reference Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterInstanceImpl extends NamedElementImpl implements ParameterInstance {
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
	 * The cached value of the '{@link #getDataValue() <em>Data Value</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataValue()
	 * @generated
	 * @ordered
	 */
	protected EList<String> dataValue;
	/**
	 * The cached value of the '{@link #getReferenceValue() <em>Reference Value</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceValue()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelReference> referenceValue;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HistoryPackage.Literals.PARAMETER_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object getValue() {
		Parameter parameter = this.getParameter();
		EClassifier classifier = parameter.getClassifier();
		
		if(parameter.isMany()) {
			if(classifier instanceof EClass) {
				List<EObject> result = new ArrayList<EObject>();
				for(ModelReference reference : this.getReferenceValue()) {
					result.add(reference.getElement());
				}
				return result;
			}
			EDataType dataType = (EDataType) classifier;
			List<String> dataValue = this.getDataValue();
			List<Object> result = new ArrayList<Object>();
			for(Iterator<String> i = dataValue.iterator(); i.hasNext(); ) {
				String stringValue = i.next();
				result.add(EcoreUtil.createFromString(dataType, stringValue));
			}
			return result;
		}
		if(classifier instanceof EClass) {
			List<ModelReference> referenceValue = this.getReferenceValue();
			if(referenceValue.isEmpty()) {
				return null;
			}
			ModelReference reference = referenceValue.get(0); 
			return reference.getElement();
		}
		EDataType dataType = (EDataType) classifier;
		List<String> dataValue = this.getDataValue();
		if(dataValue.isEmpty()) {
			return null;
		}
		String stringValue = dataValue.get(0);
		return EcoreUtil.createFromString(dataType, stringValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object newValue) {
		Parameter parameter = this.getParameter();
		if(parameter.isMany()) {
			if(parameter.getClassifier() instanceof EClass) {
				List<ModelReference> referenceValue = this.getReferenceValue();
				referenceValue.clear();
				Collection<EObject> newReferenceValue = (Collection<EObject>) newValue;
				if(newReferenceValue != null) {
					for(EObject element : newReferenceValue) {
						ModelReference reference = HistoryFactory.eINSTANCE.createModelReference();
						reference.setElement(element);
						referenceValue.add(reference);
					}
				}
			}
			else {
				EDataType dataType = (EDataType) parameter.getClassifier();
				List<String> dataValue = this.getDataValue();
				dataValue.clear();
				Collection<String> newDataValue = (Collection<String>) newValue;
				for(Iterator<String> i = newDataValue.iterator(); i.hasNext(); ) {
					dataValue.add(EcoreUtil.convertToString(dataType, i.next()));
				}
			}
		}
		else {
			if(parameter.getClassifier() instanceof EClass) {
				List<ModelReference> referenceValue = this.getReferenceValue();
				referenceValue.clear();
				if(newValue != null) {
					ModelReference reference = HistoryFactory.eINSTANCE.createModelReference();
					reference.setElement((EObject) newValue);
					referenceValue.add(reference);
				}
			}
			else {
				EDataType dataType = (EDataType) parameter.getClassifier();
				List<String> dataValue = this.getDataValue();
				dataValue.clear();
				String stringValue = EcoreUtil.convertToString(dataType, newValue);
				dataValue.add(stringValue);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getDataValue() {
		if (dataValue == null) {
			dataValue = new EDataTypeUniqueEList<String>(String.class, this, HistoryPackage.PARAMETER_INSTANCE__DATA_VALUE);
		}
		return dataValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelReference> getReferenceValue() {
		if (referenceValue == null) {
			referenceValue = new EObjectContainmentEList<ModelReference>(ModelReference.class, this, HistoryPackage.PARAMETER_INSTANCE__REFERENCE_VALUE);
		}
		return referenceValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Parameter getParameter() {
		OperationInstance operationInstance = (OperationInstance) this.eContainer();
		Operation operation = operationInstance.getOperation();
		return operation.getParameter(this.getName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case HistoryPackage.PARAMETER_INSTANCE__REFERENCE_VALUE:
				return ((InternalEList<?>)getReferenceValue()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case HistoryPackage.PARAMETER_INSTANCE__VALUE:
				return getValue();
			case HistoryPackage.PARAMETER_INSTANCE__DATA_VALUE:
				return getDataValue();
			case HistoryPackage.PARAMETER_INSTANCE__REFERENCE_VALUE:
				return getReferenceValue();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case HistoryPackage.PARAMETER_INSTANCE__VALUE:
				setValue(newValue);
				return;
			case HistoryPackage.PARAMETER_INSTANCE__DATA_VALUE:
				getDataValue().clear();
				getDataValue().addAll((Collection<? extends String>)newValue);
				return;
			case HistoryPackage.PARAMETER_INSTANCE__REFERENCE_VALUE:
				getReferenceValue().clear();
				getReferenceValue().addAll((Collection<? extends ModelReference>)newValue);
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
			case HistoryPackage.PARAMETER_INSTANCE__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case HistoryPackage.PARAMETER_INSTANCE__DATA_VALUE:
				getDataValue().clear();
				return;
			case HistoryPackage.PARAMETER_INSTANCE__REFERENCE_VALUE:
				getReferenceValue().clear();
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
			case HistoryPackage.PARAMETER_INSTANCE__VALUE:
				return VALUE_EDEFAULT == null ? getValue() != null : !VALUE_EDEFAULT.equals(getValue());
			case HistoryPackage.PARAMETER_INSTANCE__DATA_VALUE:
				return dataValue != null && !dataValue.isEmpty();
			case HistoryPackage.PARAMETER_INSTANCE__REFERENCE_VALUE:
				return referenceValue != null && !referenceValue.isEmpty();
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
		result.append(" (dataValue: ");
		result.append(dataValue);
		result.append(')');
		return result.toString();
	}

} //ParameterInstanceImpl
