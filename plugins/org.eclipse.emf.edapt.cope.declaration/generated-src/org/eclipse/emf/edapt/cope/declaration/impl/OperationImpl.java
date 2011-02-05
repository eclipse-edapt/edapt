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
package org.eclipse.emf.edapt.cope.declaration.impl;

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
import org.eclipse.emf.edapt.cope.declaration.Constraint;
import org.eclipse.emf.edapt.cope.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.cope.declaration.DescribedElement;
import org.eclipse.emf.edapt.cope.declaration.LabeledElement;
import org.eclipse.emf.edapt.cope.declaration.Library;
import org.eclipse.emf.edapt.cope.declaration.Operation;
import org.eclipse.emf.edapt.cope.declaration.Parameter;
import org.eclipse.emf.edapt.cope.declaration.Variable;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getLibrary <em>Library</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getVariables <em>Variables</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#isDeprecated <em>Deprecated</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#isDeleting <em>Deleting</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getBefore <em>Before</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.cope.declaration.impl.OperationImpl#getAfter <em>After</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationImpl extends IdentifiedElementImpl implements Operation {
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
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<Parameter> parameters;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<Constraint> constraints;

	/**
	 * The cached value of the '{@link #getVariables() <em>Variables</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariables()
	 * @generated
	 * @ordered
	 */
	protected EList<Variable> variables;

	/**
	 * The default value of the '{@link #isDeprecated() <em>Deprecated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeprecated()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DEPRECATED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDeprecated() <em>Deprecated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeprecated()
	 * @generated
	 * @ordered
	 */
	protected boolean deprecated = DEPRECATED_EDEFAULT;

	/**
	 * The default value of the '{@link #isDeleting() <em>Deleting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeleting()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DELETING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDeleting() <em>Deleting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeleting()
	 * @generated
	 * @ordered
	 */
	protected boolean deleting = DELETING_EDEFAULT;

	/**
	 * The default value of the '{@link #getBefore() <em>Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBefore()
	 * @generated
	 * @ordered
	 */
	protected static final String BEFORE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBefore() <em>Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBefore()
	 * @generated
	 * @ordered
	 */
	protected String before = BEFORE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAfter() <em>After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfter()
	 * @generated
	 * @ordered
	 */
	protected static final String AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAfter() <em>After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfter()
	 * @generated
	 * @ordered
	 */
	protected String after = AFTER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeclarationPackage.Literals.OPERATION;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Library getLibrary() {
		if (eContainerFeatureID() != DeclarationPackage.OPERATION__LIBRARY) return null;
		return (Library)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLibrary(Library newLibrary, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newLibrary, DeclarationPackage.OPERATION__LIBRARY, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLibrary(Library newLibrary) {
		if (newLibrary != eInternalContainer() || (eContainerFeatureID() != DeclarationPackage.OPERATION__LIBRARY && newLibrary != null)) {
			if (EcoreUtil.isAncestor(this, newLibrary))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newLibrary != null)
				msgs = ((InternalEObject)newLibrary).eInverseAdd(this, DeclarationPackage.LIBRARY__OPERATIONS, Library.class, msgs);
			msgs = basicSetLibrary(newLibrary, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__LIBRARY, newLibrary, newLibrary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentWithInverseEList<Parameter>(Parameter.class, this, DeclarationPackage.OPERATION__PARAMETERS, DeclarationPackage.PARAMETER__OPERATION);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Constraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentWithInverseEList<Constraint>(Constraint.class, this, DeclarationPackage.OPERATION__CONSTRAINTS, DeclarationPackage.CONSTRAINT__OPERATION);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Variable> getVariables() {
		if (variables == null) {
			variables = new EObjectContainmentWithInverseEList<Variable>(Variable.class, this, DeclarationPackage.OPERATION__VARIABLES, DeclarationPackage.VARIABLE__OPERATION);
		}
		return variables;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDeprecated() {
		return deprecated;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeprecated(boolean newDeprecated) {
		boolean oldDeprecated = deprecated;
		deprecated = newDeprecated;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__DEPRECATED, oldDeprecated, deprecated));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDeleting() {
		return deleting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeleting(boolean newDeleting) {
		boolean oldDeleting = deleting;
		deleting = newDeleting;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__DELETING, oldDeleting, deleting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBefore() {
		return before;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBefore(String newBefore) {
		String oldBefore = before;
		before = newBefore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__BEFORE, oldBefore, before));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAfter() {
		return after;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAfter(String newAfter) {
		String oldAfter = after;
		after = newAfter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeclarationPackage.OPERATION__AFTER, oldAfter, after));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Parameter getParameter(String name) {
		for (Parameter parameter : this.getParameters()) {
			if (name.equals(parameter.getName())) {
				return parameter;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Variable getVariable(String name) {
		for (Variable variable : this.getVariables()) {
			if (name.equals(variable.getName())) {
				return variable;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Parameter getMainParameter() {
		for (Parameter parameter : this.getParameters()) {
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
	public boolean refines() {
		return getBefore() != null || getAfter() != null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeclarationPackage.OPERATION__LIBRARY:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetLibrary((Library)otherEnd, msgs);
			case DeclarationPackage.OPERATION__PARAMETERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getParameters()).basicAdd(otherEnd, msgs);
			case DeclarationPackage.OPERATION__CONSTRAINTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getConstraints()).basicAdd(otherEnd, msgs);
			case DeclarationPackage.OPERATION__VARIABLES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getVariables()).basicAdd(otherEnd, msgs);
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
			case DeclarationPackage.OPERATION__LIBRARY:
				return basicSetLibrary(null, msgs);
			case DeclarationPackage.OPERATION__PARAMETERS:
				return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
			case DeclarationPackage.OPERATION__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case DeclarationPackage.OPERATION__VARIABLES:
				return ((InternalEList<?>)getVariables()).basicRemove(otherEnd, msgs);
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
			case DeclarationPackage.OPERATION__LIBRARY:
				return eInternalContainer().eInverseRemove(this, DeclarationPackage.LIBRARY__OPERATIONS, Library.class, msgs);
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
			case DeclarationPackage.OPERATION__DESCRIPTION:
				return getDescription();
			case DeclarationPackage.OPERATION__LABEL:
				return getLabel();
			case DeclarationPackage.OPERATION__LIBRARY:
				return getLibrary();
			case DeclarationPackage.OPERATION__PARAMETERS:
				return getParameters();
			case DeclarationPackage.OPERATION__CONSTRAINTS:
				return getConstraints();
			case DeclarationPackage.OPERATION__VARIABLES:
				return getVariables();
			case DeclarationPackage.OPERATION__DEPRECATED:
				return isDeprecated();
			case DeclarationPackage.OPERATION__DELETING:
				return isDeleting();
			case DeclarationPackage.OPERATION__BEFORE:
				return getBefore();
			case DeclarationPackage.OPERATION__AFTER:
				return getAfter();
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
			case DeclarationPackage.OPERATION__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DeclarationPackage.OPERATION__LABEL:
				setLabel((String)newValue);
				return;
			case DeclarationPackage.OPERATION__LIBRARY:
				setLibrary((Library)newValue);
				return;
			case DeclarationPackage.OPERATION__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection<? extends Parameter>)newValue);
				return;
			case DeclarationPackage.OPERATION__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends Constraint>)newValue);
				return;
			case DeclarationPackage.OPERATION__VARIABLES:
				getVariables().clear();
				getVariables().addAll((Collection<? extends Variable>)newValue);
				return;
			case DeclarationPackage.OPERATION__DEPRECATED:
				setDeprecated((Boolean)newValue);
				return;
			case DeclarationPackage.OPERATION__DELETING:
				setDeleting((Boolean)newValue);
				return;
			case DeclarationPackage.OPERATION__BEFORE:
				setBefore((String)newValue);
				return;
			case DeclarationPackage.OPERATION__AFTER:
				setAfter((String)newValue);
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
			case DeclarationPackage.OPERATION__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DeclarationPackage.OPERATION__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case DeclarationPackage.OPERATION__LIBRARY:
				setLibrary((Library)null);
				return;
			case DeclarationPackage.OPERATION__PARAMETERS:
				getParameters().clear();
				return;
			case DeclarationPackage.OPERATION__CONSTRAINTS:
				getConstraints().clear();
				return;
			case DeclarationPackage.OPERATION__VARIABLES:
				getVariables().clear();
				return;
			case DeclarationPackage.OPERATION__DEPRECATED:
				setDeprecated(DEPRECATED_EDEFAULT);
				return;
			case DeclarationPackage.OPERATION__DELETING:
				setDeleting(DELETING_EDEFAULT);
				return;
			case DeclarationPackage.OPERATION__BEFORE:
				setBefore(BEFORE_EDEFAULT);
				return;
			case DeclarationPackage.OPERATION__AFTER:
				setAfter(AFTER_EDEFAULT);
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
			case DeclarationPackage.OPERATION__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DeclarationPackage.OPERATION__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case DeclarationPackage.OPERATION__LIBRARY:
				return getLibrary() != null;
			case DeclarationPackage.OPERATION__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
			case DeclarationPackage.OPERATION__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case DeclarationPackage.OPERATION__VARIABLES:
				return variables != null && !variables.isEmpty();
			case DeclarationPackage.OPERATION__DEPRECATED:
				return deprecated != DEPRECATED_EDEFAULT;
			case DeclarationPackage.OPERATION__DELETING:
				return deleting != DELETING_EDEFAULT;
			case DeclarationPackage.OPERATION__BEFORE:
				return BEFORE_EDEFAULT == null ? before != null : !BEFORE_EDEFAULT.equals(before);
			case DeclarationPackage.OPERATION__AFTER:
				return AFTER_EDEFAULT == null ? after != null : !AFTER_EDEFAULT.equals(after);
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
				case DeclarationPackage.OPERATION__DESCRIPTION: return DeclarationPackage.DESCRIBED_ELEMENT__DESCRIPTION;
				default: return -1;
			}
		}
		if (baseClass == LabeledElement.class) {
			switch (derivedFeatureID) {
				case DeclarationPackage.OPERATION__LABEL: return DeclarationPackage.LABELED_ELEMENT__LABEL;
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
				case DeclarationPackage.DESCRIBED_ELEMENT__DESCRIPTION: return DeclarationPackage.OPERATION__DESCRIPTION;
				default: return -1;
			}
		}
		if (baseClass == LabeledElement.class) {
			switch (baseFeatureID) {
				case DeclarationPackage.LABELED_ELEMENT__LABEL: return DeclarationPackage.OPERATION__LABEL;
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
		result.append(", label: ");
		result.append(label);
		result.append(", deprecated: ");
		result.append(deprecated);
		result.append(", deleting: ");
		result.append(deleting);
		result.append(", before: ");
		result.append(before);
		result.append(", after: ");
		result.append(after);
		result.append(')');
		return result.toString();
	}

} //OperationImpl
