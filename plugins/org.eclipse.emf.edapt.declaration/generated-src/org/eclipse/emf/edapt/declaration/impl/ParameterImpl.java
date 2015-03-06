/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.declaration.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#getOperation <em>Operation</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#isRequired <em>Required</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#isMain <em>Main</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#isMany <em>Many</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#getClassifier <em>Classifier</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.ParameterImpl#getClassifierName <em>Classifier Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterImpl extends IdentifiedElementImpl implements Parameter {
	/**
	 * The default value of the '{@link #isRequired() <em>Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isRequired()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REQUIRED_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isRequired() <em>Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isRequired()
	 * @generated
	 * @ordered
	 */
	protected boolean required = REQUIRED_EDEFAULT;

	/**
	 * The default value of the '{@link #isMain() <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isMain()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MAIN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMain() <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isMain()
	 * @generated
	 * @ordered
	 */
	protected boolean main = MAIN_EDEFAULT;

	/**
	 * The default value of the '{@link #isMany() <em>Many</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isMany()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MANY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMany() <em>Many</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isMany()
	 * @generated
	 * @ordered
	 */
	protected boolean many = MANY_EDEFAULT;

	/**
	 * The default value of the '{@link #getClassifierName() <em>Classifier Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getClassifierName()
	 * @generated
	 * @ordered
	 */
	protected static final String CLASSIFIER_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getClassifierName() <em>Classifier Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getClassifierName()
	 * @generated
	 * @ordered
	 */
	protected String classifierName = CLASSIFIER_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeclarationPackage.Literals.PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Operation getOperation() {
		if (eContainerFeatureID() != DeclarationPackage.PARAMETER__OPERATION) {
			return null;
		}
		return (Operation) eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetOperation(Operation newOperation, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newOperation, DeclarationPackage.PARAMETER__OPERATION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setOperation(Operation newOperation) {
		if (newOperation != eInternalContainer()
			|| eContainerFeatureID() != DeclarationPackage.PARAMETER__OPERATION && newOperation != null) {
			if (EcoreUtil.isAncestor(this, newOperation)) {
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			}
			NotificationChain msgs = null;
			if (eInternalContainer() != null) {
				msgs = eBasicRemoveFromContainer(msgs);
			}
			if (newOperation != null) {
				msgs = ((InternalEObject) newOperation).eInverseAdd(this, DeclarationPackage.OPERATION__PARAMETERS,
					Operation.class, msgs);
			}
			msgs = basicSetOperation(newOperation, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		}
		else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__OPERATION,
				newOperation, newOperation));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isRequired() {
		return required;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRequired(boolean newRequired) {
		final boolean oldRequired = required;
		required = newRequired;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__REQUIRED, oldRequired,
				required));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isMain() {
		return main;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMain(boolean newMain) {
		final boolean oldMain = main;
		main = newMain;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__MAIN, oldMain, main));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isMany() {
		return many;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMany(boolean newMany) {
		final boolean oldMany = many;
		many = newMany;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__MANY, oldMany, many));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClassifier getClassifier() {
		final EClassifier classifier = basicGetClassifier();
		return classifier != null && classifier.eIsProxy() ? (EClassifier) eResolveProxy((InternalEObject) classifier)
			: classifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	public EClassifier basicGetClassifier() {
		return EcorePackage.eINSTANCE.getEClassifier(getClassifierName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public void setClassifier(EClassifier newClassifier) {
		setClassifierName(newClassifier.getName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getClassifierName() {
		return classifierName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setClassifierName(String newClassifierName) {
		final String oldClassifierName = classifierName;
		classifierName = newClassifierName;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.PARAMETER__CLASSIFIER_NAME,
				oldClassifierName, classifierName));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DeclarationPackage.PARAMETER__OPERATION:
			if (eInternalContainer() != null) {
				msgs = eBasicRemoveFromContainer(msgs);
			}
			return basicSetOperation((Operation) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
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
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case DeclarationPackage.PARAMETER__OPERATION:
			return eInternalContainer().eInverseRemove(this, DeclarationPackage.OPERATION__PARAMETERS, Operation.class,
				msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DeclarationPackage.PARAMETER__OPERATION:
			return getOperation();
		case DeclarationPackage.PARAMETER__REQUIRED:
			return isRequired();
		case DeclarationPackage.PARAMETER__MAIN:
			return isMain();
		case DeclarationPackage.PARAMETER__MANY:
			return isMany();
		case DeclarationPackage.PARAMETER__CLASSIFIER:
			if (resolve) {
				return getClassifier();
			}
			return basicGetClassifier();
		case DeclarationPackage.PARAMETER__CLASSIFIER_NAME:
			return getClassifierName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DeclarationPackage.PARAMETER__OPERATION:
			setOperation((Operation) newValue);
			return;
		case DeclarationPackage.PARAMETER__REQUIRED:
			setRequired((Boolean) newValue);
			return;
		case DeclarationPackage.PARAMETER__MAIN:
			setMain((Boolean) newValue);
			return;
		case DeclarationPackage.PARAMETER__MANY:
			setMany((Boolean) newValue);
			return;
		case DeclarationPackage.PARAMETER__CLASSIFIER:
			setClassifier((EClassifier) newValue);
			return;
		case DeclarationPackage.PARAMETER__CLASSIFIER_NAME:
			setClassifierName((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case DeclarationPackage.PARAMETER__OPERATION:
			setOperation((Operation) null);
			return;
		case DeclarationPackage.PARAMETER__REQUIRED:
			setRequired(REQUIRED_EDEFAULT);
			return;
		case DeclarationPackage.PARAMETER__MAIN:
			setMain(MAIN_EDEFAULT);
			return;
		case DeclarationPackage.PARAMETER__MANY:
			setMany(MANY_EDEFAULT);
			return;
		case DeclarationPackage.PARAMETER__CLASSIFIER:
			setClassifier((EClassifier) null);
			return;
		case DeclarationPackage.PARAMETER__CLASSIFIER_NAME:
			setClassifierName(CLASSIFIER_NAME_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case DeclarationPackage.PARAMETER__OPERATION:
			return getOperation() != null;
		case DeclarationPackage.PARAMETER__REQUIRED:
			return required != REQUIRED_EDEFAULT;
		case DeclarationPackage.PARAMETER__MAIN:
			return main != MAIN_EDEFAULT;
		case DeclarationPackage.PARAMETER__MANY:
			return many != MANY_EDEFAULT;
		case DeclarationPackage.PARAMETER__CLASSIFIER:
			return basicGetClassifier() != null;
		case DeclarationPackage.PARAMETER__CLASSIFIER_NAME:
			return CLASSIFIER_NAME_EDEFAULT == null ? classifierName != null : !CLASSIFIER_NAME_EDEFAULT
				.equals(classifierName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (required: "); //$NON-NLS-1$
		result.append(required);
		result.append(", main: "); //$NON-NLS-1$
		result.append(main);
		result.append(", many: "); //$NON-NLS-1$
		result.append(many);
		result.append(", classifierName: "); //$NON-NLS-1$
		result.append(classifierName);
		result.append(')');
		return result.toString();
	}

} // ParameterImpl
