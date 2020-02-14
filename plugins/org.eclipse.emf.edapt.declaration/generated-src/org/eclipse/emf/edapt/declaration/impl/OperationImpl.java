/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.declaration.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edapt.declaration.Constraint;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#getLibrary <em>Library</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#getParameters <em>Parameters</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#getConstraints <em>Constraints</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#isDeprecated <em>Deprecated</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#getBefore <em>Before</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#getAfter <em>After</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#getImplementation <em>Implementation</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#getLabel <em>Label</em>}</li>
 * <li>{@link org.eclipse.emf.edapt.declaration.impl.OperationImpl#isBreaking <em>Breaking</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationImpl extends IdentifiedElementImpl implements Operation {
	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<Parameter> parameters;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<Constraint> constraints;

	/**
	 * The default value of the '{@link #isDeprecated() <em>Deprecated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isDeprecated()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DEPRECATED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDeprecated() <em>Deprecated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isDeprecated()
	 * @generated
	 * @ordered
	 */
	protected boolean deprecated = DEPRECATED_EDEFAULT;

	/**
	 * The default value of the '{@link #getBefore() <em>Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getBefore()
	 * @generated
	 * @ordered
	 */
	protected static final String BEFORE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBefore() <em>Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getBefore()
	 * @generated
	 * @ordered
	 */
	protected String before = BEFORE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAfter() <em>After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getAfter()
	 * @generated
	 * @ordered
	 */
	protected static final String AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAfter() <em>After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getAfter()
	 * @generated
	 * @ordered
	 */
	protected String after = AFTER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getImplementation() <em>Implementation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getImplementation()
	 * @generated
	 * @ordered
	 */
	protected Class implementation;

	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * The default value of the '{@link #isBreaking() <em>Breaking</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isBreaking()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BREAKING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBreaking() <em>Breaking</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isBreaking()
	 * @generated
	 * @ordered
	 */
	protected boolean breaking = BREAKING_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OperationImpl() {
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
		return DeclarationPackage.Literals.OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setLabel(String newLabel) {
		final String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired()) {
			eNotify(
				new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__LABEL, oldLabel, label));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isBreaking() {
		return breaking;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setBreaking(boolean newBreaking) {
		final boolean oldBreaking = breaking;
		breaking = newBreaking;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__BREAKING, oldBreaking,
				breaking));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Library getLibrary() {
		if (eContainerFeatureID() != DeclarationPackage.OPERATION__LIBRARY) {
			return null;
		}
		return (Library) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetLibrary(Library newLibrary, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newLibrary, DeclarationPackage.OPERATION__LIBRARY, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setLibrary(Library newLibrary) {
		if (newLibrary != eInternalContainer()
			|| eContainerFeatureID() != DeclarationPackage.OPERATION__LIBRARY && newLibrary != null) {
			if (EcoreUtil.isAncestor(this, newLibrary)) {
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			}
			NotificationChain msgs = null;
			if (eInternalContainer() != null) {
				msgs = eBasicRemoveFromContainer(msgs);
			}
			if (newLibrary != null) {
				msgs = ((InternalEObject) newLibrary).eInverseAdd(this, DeclarationPackage.LIBRARY__OPERATIONS,
					Library.class, msgs);
			}
			msgs = basicSetLibrary(newLibrary, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__LIBRARY, newLibrary,
				newLibrary));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentWithInverseEList<Parameter>(Parameter.class, this,
				DeclarationPackage.OPERATION__PARAMETERS, DeclarationPackage.PARAMETER__OPERATION);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<Constraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentWithInverseEList<Constraint>(Constraint.class, this,
				DeclarationPackage.OPERATION__CONSTRAINTS, DeclarationPackage.CONSTRAINT__OPERATION);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isDeprecated() {
		return deprecated;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDeprecated(boolean newDeprecated) {
		final boolean oldDeprecated = deprecated;
		deprecated = newDeprecated;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__DEPRECATED,
				oldDeprecated, deprecated));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getBefore() {
		return before;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setBefore(String newBefore) {
		final String oldBefore = before;
		before = newBefore;
		if (eNotificationRequired()) {
			eNotify(
				new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__BEFORE, oldBefore, before));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getAfter() {
		return after;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setAfter(String newAfter) {
		final String oldAfter = after;
		after = newAfter;
		if (eNotificationRequired()) {
			eNotify(
				new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__AFTER, oldAfter, after));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Class getImplementation() {
		return implementation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setImplementation(Class newImplementation) {
		final Class oldImplementation = implementation;
		implementation = newImplementation;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__IMPLEMENTATION,
				oldImplementation, implementation));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public Parameter getParameter(String name) {
		for (final Parameter parameter : getParameters()) {
			if (name.equals(parameter.getName())) {
				return parameter;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public Parameter getMainParameter() {
		for (final Parameter parameter : getParameters()) {
			if (parameter.isMain()) {
				return parameter;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public boolean refines() {
		return getBefore() != null || getAfter() != null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public Constraint getConstraint(String name) {
		for (final Constraint constraint : getConstraints()) {
			if (name.equals(constraint.getName())) {
				return constraint;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DeclarationPackage.OPERATION__LIBRARY:
			if (eInternalContainer() != null) {
				msgs = eBasicRemoveFromContainer(msgs);
			}
			return basicSetLibrary((Library) otherEnd, msgs);
		case DeclarationPackage.OPERATION__PARAMETERS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getParameters()).basicAdd(otherEnd, msgs);
		case DeclarationPackage.OPERATION__CONSTRAINTS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getConstraints()).basicAdd(otherEnd, msgs);
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
		case DeclarationPackage.OPERATION__LIBRARY:
			return basicSetLibrary(null, msgs);
		case DeclarationPackage.OPERATION__PARAMETERS:
			return ((InternalEList<?>) getParameters()).basicRemove(otherEnd, msgs);
		case DeclarationPackage.OPERATION__CONSTRAINTS:
			return ((InternalEList<?>) getConstraints()).basicRemove(otherEnd, msgs);
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
		case DeclarationPackage.OPERATION__LIBRARY:
			return eInternalContainer().eInverseRemove(this, DeclarationPackage.LIBRARY__OPERATIONS, Library.class,
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
		case DeclarationPackage.OPERATION__LIBRARY:
			return getLibrary();
		case DeclarationPackage.OPERATION__PARAMETERS:
			return getParameters();
		case DeclarationPackage.OPERATION__CONSTRAINTS:
			return getConstraints();
		case DeclarationPackage.OPERATION__DEPRECATED:
			return isDeprecated();
		case DeclarationPackage.OPERATION__BEFORE:
			return getBefore();
		case DeclarationPackage.OPERATION__AFTER:
			return getAfter();
		case DeclarationPackage.OPERATION__IMPLEMENTATION:
			return getImplementation();
		case DeclarationPackage.OPERATION__LABEL:
			return getLabel();
		case DeclarationPackage.OPERATION__BREAKING:
			return isBreaking();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DeclarationPackage.OPERATION__LIBRARY:
			setLibrary((Library) newValue);
			return;
		case DeclarationPackage.OPERATION__PARAMETERS:
			getParameters().clear();
			getParameters().addAll((Collection<? extends Parameter>) newValue);
			return;
		case DeclarationPackage.OPERATION__CONSTRAINTS:
			getConstraints().clear();
			getConstraints().addAll((Collection<? extends Constraint>) newValue);
			return;
		case DeclarationPackage.OPERATION__DEPRECATED:
			setDeprecated((Boolean) newValue);
			return;
		case DeclarationPackage.OPERATION__BEFORE:
			setBefore((String) newValue);
			return;
		case DeclarationPackage.OPERATION__AFTER:
			setAfter((String) newValue);
			return;
		case DeclarationPackage.OPERATION__IMPLEMENTATION:
			setImplementation((Class) newValue);
			return;
		case DeclarationPackage.OPERATION__LABEL:
			setLabel((String) newValue);
			return;
		case DeclarationPackage.OPERATION__BREAKING:
			setBreaking((Boolean) newValue);
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
		case DeclarationPackage.OPERATION__LIBRARY:
			setLibrary((Library) null);
			return;
		case DeclarationPackage.OPERATION__PARAMETERS:
			getParameters().clear();
			return;
		case DeclarationPackage.OPERATION__CONSTRAINTS:
			getConstraints().clear();
			return;
		case DeclarationPackage.OPERATION__DEPRECATED:
			setDeprecated(DEPRECATED_EDEFAULT);
			return;
		case DeclarationPackage.OPERATION__BEFORE:
			setBefore(BEFORE_EDEFAULT);
			return;
		case DeclarationPackage.OPERATION__AFTER:
			setAfter(AFTER_EDEFAULT);
			return;
		case DeclarationPackage.OPERATION__IMPLEMENTATION:
			setImplementation((Class) null);
			return;
		case DeclarationPackage.OPERATION__LABEL:
			setLabel(LABEL_EDEFAULT);
			return;
		case DeclarationPackage.OPERATION__BREAKING:
			setBreaking(BREAKING_EDEFAULT);
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
		case DeclarationPackage.OPERATION__LIBRARY:
			return getLibrary() != null;
		case DeclarationPackage.OPERATION__PARAMETERS:
			return parameters != null && !parameters.isEmpty();
		case DeclarationPackage.OPERATION__CONSTRAINTS:
			return constraints != null && !constraints.isEmpty();
		case DeclarationPackage.OPERATION__DEPRECATED:
			return deprecated != DEPRECATED_EDEFAULT;
		case DeclarationPackage.OPERATION__BEFORE:
			return BEFORE_EDEFAULT == null ? before != null : !BEFORE_EDEFAULT.equals(before);
		case DeclarationPackage.OPERATION__AFTER:
			return AFTER_EDEFAULT == null ? after != null : !AFTER_EDEFAULT.equals(after);
		case DeclarationPackage.OPERATION__IMPLEMENTATION:
			return implementation != null;
		case DeclarationPackage.OPERATION__LABEL:
			return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
		case DeclarationPackage.OPERATION__BREAKING:
			return breaking != BREAKING_EDEFAULT;
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
		result.append(" (deprecated: "); //$NON-NLS-1$
		result.append(deprecated);
		result.append(", before: "); //$NON-NLS-1$
		result.append(before);
		result.append(", after: "); //$NON-NLS-1$
		result.append(after);
		result.append(", implementation: "); //$NON-NLS-1$
		result.append(implementation);
		result.append(", label: "); //$NON-NLS-1$
		result.append(label);
		result.append(", breaking: "); //$NON-NLS-1$
		result.append(breaking);
		result.append(')');
		return result.toString();
	}

} // OperationImpl
