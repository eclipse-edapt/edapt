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
package org.eclipse.emf.edapt.declaration.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.declaration.DescribedElement;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#isRequired <em>Required</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#getChoiceExpression <em>Choice Expression</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#isMain <em>Main</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterImpl extends PlaceholderImpl implements Parameter {
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #isRequired() <em>Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRequired()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REQUIRED_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isRequired() <em>Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRequired()
	 * @generated
	 * @ordered
	 */
	protected boolean required = REQUIRED_EDEFAULT;

	/**
	 * The default value of the '{@link #getChoiceExpression() <em>Choice Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChoiceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String CHOICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getChoiceExpression() <em>Choice Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChoiceExpression()
	 * @generated
	 * @ordered
	 */
	protected String choiceExpression = CHOICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #isMain() <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMain()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MAIN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMain() <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMain()
	 * @generated
	 * @ordered
	 */
	protected boolean main = MAIN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeclarationPackage.Literals.PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operation getOperation() {
		if (eContainerFeatureID() != DeclarationPackage.PARAMETER__OPERATION) return null;
		return (Operation)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOperation(Operation newOperation, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newOperation, DeclarationPackage.PARAMETER__OPERATION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperation(Operation newOperation) {
		if (newOperation != eInternalContainer() || (eContainerFeatureID() != DeclarationPackage.PARAMETER__OPERATION && newOperation != null)) {
			if (EcoreUtil.isAncestor(this, newOperation))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOperation != null)
				msgs = ((InternalEObject)newOperation).eInverseAdd(this, DeclarationPackage.OPERATION__PARAMETERS, Operation.class, msgs);
			msgs = basicSetOperation(newOperation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__OPERATION, newOperation, newOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequired(boolean newRequired) {
		boolean oldRequired = required;
		required = newRequired;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__REQUIRED, oldRequired, required));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getChoiceExpression() {
		return choiceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChoiceExpression(String newChoiceExpression) {
		String oldChoiceExpression = choiceExpression;
		choiceExpression = newChoiceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__CHOICE_EXPRESSION, oldChoiceExpression, choiceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMain() {
		return main;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMain(boolean newMain) {
		boolean oldMain = main;
		main = newMain;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__MAIN, oldMain, main));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeclarationPackage.PARAMETER__OPERATION:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetOperation((Operation)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeclarationPackage.PARAMETER__OPERATION:
				return basicSetOperation(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case DeclarationPackage.PARAMETER__OPERATION:
				return eInternalContainer().eInverseRemove(this, DeclarationPackage.OPERATION__PARAMETERS, Operation.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DeclarationPackage.PARAMETER__DESCRIPTION:
				return getDescription();
			case DeclarationPackage.PARAMETER__OPERATION:
				return getOperation();
			case DeclarationPackage.PARAMETER__REQUIRED:
				return isRequired();
			case DeclarationPackage.PARAMETER__CHOICE_EXPRESSION:
				return getChoiceExpression();
			case DeclarationPackage.PARAMETER__MAIN:
				return isMain();
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
			case DeclarationPackage.PARAMETER__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DeclarationPackage.PARAMETER__OPERATION:
				setOperation((Operation)newValue);
				return;
			case DeclarationPackage.PARAMETER__REQUIRED:
				setRequired((Boolean)newValue);
				return;
			case DeclarationPackage.PARAMETER__CHOICE_EXPRESSION:
				setChoiceExpression((String)newValue);
				return;
			case DeclarationPackage.PARAMETER__MAIN:
				setMain((Boolean)newValue);
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
			case DeclarationPackage.PARAMETER__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DeclarationPackage.PARAMETER__OPERATION:
				setOperation((Operation)null);
				return;
			case DeclarationPackage.PARAMETER__REQUIRED:
				setRequired(REQUIRED_EDEFAULT);
				return;
			case DeclarationPackage.PARAMETER__CHOICE_EXPRESSION:
				setChoiceExpression(CHOICE_EXPRESSION_EDEFAULT);
				return;
			case DeclarationPackage.PARAMETER__MAIN:
				setMain(MAIN_EDEFAULT);
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
			case DeclarationPackage.PARAMETER__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DeclarationPackage.PARAMETER__OPERATION:
				return getOperation() != null;
			case DeclarationPackage.PARAMETER__REQUIRED:
				return required != REQUIRED_EDEFAULT;
			case DeclarationPackage.PARAMETER__CHOICE_EXPRESSION:
				return CHOICE_EXPRESSION_EDEFAULT == null ? choiceExpression != null : !CHOICE_EXPRESSION_EDEFAULT.equals(choiceExpression);
			case DeclarationPackage.PARAMETER__MAIN:
				return main != MAIN_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == DescribedElement.class) {
			switch (derivedFeatureID) {
				case DeclarationPackage.PARAMETER__DESCRIPTION: return DeclarationPackage.DESCRIBED_ELEMENT__DESCRIPTION;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == DescribedElement.class) {
			switch (baseFeatureID) {
				case DeclarationPackage.DESCRIBED_ELEMENT__DESCRIPTION: return DeclarationPackage.PARAMETER__DESCRIPTION;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (description: ");
		result.append(description);
		result.append(", required: ");
		result.append(required);
		result.append(", choiceExpression: ");
		result.append(choiceExpression);
		result.append(", main: ");
		result.append(main);
		result.append(')');
		return result.toString();
	}

} //ParameterImpl
