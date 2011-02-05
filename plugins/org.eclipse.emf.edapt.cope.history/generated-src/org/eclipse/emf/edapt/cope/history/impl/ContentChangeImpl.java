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
package org.eclipse.emf.edapt.cope.history.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.edapt.cope.history.ContentChange;
import org.eclipse.emf.edapt.cope.history.HistoryPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Content Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.impl.ContentChangeImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.impl.ContentChangeImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.history.impl.ContentChangeImpl#getReferenceName <em>Reference Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ContentChangeImpl extends PrimitiveChangeImpl implements ContentChange {
	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject target;
	/**
	 * The default value of the '{@link #getReferenceName() <em>Reference Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceName()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENCE_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getReferenceName() <em>Reference Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceName()
	 * @generated
	 * @ordered
	 */
	protected String referenceName = REFERENCE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContentChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HistoryPackage.Literals.CONTENT_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, HistoryPackage.CONTENT_CHANGE__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTarget(EObject newTarget) {
		EObject oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HistoryPackage.CONTENT_CHANGE__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReference() {
		EReference reference = basicGetReference();
		return reference != null && reference.eIsProxy() ? (EReference)eResolveProxy((InternalEObject)reference) : reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EReference basicGetReference() {
		EObject target = ((ContentChange) this).getTarget();
		String referenceName = ((ContentChange) this).getReferenceName();
		if(target != null && referenceName != null) {
			EClass clazz = target.eClass();
			EStructuralFeature feature = clazz.getEStructuralFeature(referenceName);
			if(feature instanceof EReference) {
				return (EReference) feature;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setReference(EReference newReference) {
		if(newReference != null) {
			this.setReferenceName(newReference.getName());
		}
		else {
			this.setReferenceName(null);
		} 
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReferenceName() {
		return referenceName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferenceName(String newReferenceName) {
		String oldReferenceName = referenceName;
		referenceName = newReferenceName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HistoryPackage.CONTENT_CHANGE__REFERENCE_NAME, oldReferenceName, referenceName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case HistoryPackage.CONTENT_CHANGE__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case HistoryPackage.CONTENT_CHANGE__REFERENCE:
				if (resolve) return getReference();
				return basicGetReference();
			case HistoryPackage.CONTENT_CHANGE__REFERENCE_NAME:
				return getReferenceName();
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
			case HistoryPackage.CONTENT_CHANGE__TARGET:
				setTarget((EObject)newValue);
				return;
			case HistoryPackage.CONTENT_CHANGE__REFERENCE:
				setReference((EReference)newValue);
				return;
			case HistoryPackage.CONTENT_CHANGE__REFERENCE_NAME:
				setReferenceName((String)newValue);
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
			case HistoryPackage.CONTENT_CHANGE__TARGET:
				setTarget((EObject)null);
				return;
			case HistoryPackage.CONTENT_CHANGE__REFERENCE:
				setReference((EReference)null);
				return;
			case HistoryPackage.CONTENT_CHANGE__REFERENCE_NAME:
				setReferenceName(REFERENCE_NAME_EDEFAULT);
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
			case HistoryPackage.CONTENT_CHANGE__TARGET:
				return target != null;
			case HistoryPackage.CONTENT_CHANGE__REFERENCE:
				return basicGetReference() != null;
			case HistoryPackage.CONTENT_CHANGE__REFERENCE_NAME:
				return REFERENCE_NAME_EDEFAULT == null ? referenceName != null : !REFERENCE_NAME_EDEFAULT.equals(referenceName);
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
		result.append(" (referenceName: ");
		result.append(referenceName);
		result.append(')');
		return result.toString();
	}

} //ContentChangeImpl
