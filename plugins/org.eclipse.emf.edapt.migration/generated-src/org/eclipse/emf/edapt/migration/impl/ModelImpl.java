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
package org.eclipse.emf.edapt.migration.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edapt.migration.AttributeSlot;
import org.eclipse.emf.edapt.migration.DiagnosticException;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.MigrationFactory;
import org.eclipse.emf.edapt.migration.MigrationPackage;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ModelResource;
import org.eclipse.emf.edapt.migration.ReferenceSlot;
import org.eclipse.emf.edapt.migration.Repository;
import org.eclipse.emf.edapt.migration.Slot;
import org.eclipse.emf.edapt.migration.Type;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.impl.ModelImpl#getMetamodel <em>Metamodel</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.impl.ModelImpl#getTypes <em>Types</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.impl.ModelImpl#isReflection <em>Reflection</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.impl.ModelImpl#getResources <em>Resources</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.impl.ModelImpl#getRepository <em>Repository</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelImpl extends EObjectImpl implements Model {
	/**
	 * The cached value of the '{@link #getMetamodel() <em>Metamodel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetamodel()
	 * @generated
	 * @ordered
	 */
	protected Metamodel metamodel;

	/**
	 * The cached value of the '{@link #getTypes() <em>Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<Type> types;

	/**
	 * The default value of the '{@link #isReflection() <em>Reflection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReflection()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REFLECTION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReflection() <em>Reflection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReflection()
	 * @generated
	 * @ordered
	 */
	protected boolean reflection = REFLECTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getResources() <em>Resources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResources()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelResource> resources;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MigrationPackage.Literals.MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metamodel getMetamodel() {
		if (metamodel != null && ((EObject)metamodel).eIsProxy()) {
			InternalEObject oldMetamodel = (InternalEObject)metamodel;
			metamodel = (Metamodel)eResolveProxy(oldMetamodel);
			if (metamodel != oldMetamodel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MigrationPackage.MODEL__METAMODEL, oldMetamodel, metamodel));
			}
		}
		return metamodel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metamodel basicGetMetamodel() {
		return metamodel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMetamodel(Metamodel newMetamodel) {
		Metamodel oldMetamodel = metamodel;
		metamodel = newMetamodel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MigrationPackage.MODEL__METAMODEL, oldMetamodel, metamodel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Type> getTypes() {
		if (types == null) {
			types = new EObjectContainmentWithInverseEList<Type>(Type.class, this, MigrationPackage.MODEL__TYPES, MigrationPackage.TYPE__MODEL);
		}
		return types;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isReflection() {
		return reflection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReflection(boolean newReflection) {
		boolean oldReflection = reflection;
		reflection = newReflection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MigrationPackage.MODEL__REFLECTION, oldReflection, reflection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelResource> getResources() {
		if (resources == null) {
			resources = new EObjectContainmentWithInverseEList<ModelResource>(ModelResource.class, this, MigrationPackage.MODEL__RESOURCES, MigrationPackage.MODEL_RESOURCE__MODEL);
		}
		return resources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Repository getRepository() {
		if (eContainerFeatureID() != MigrationPackage.MODEL__REPOSITORY) return null;
		return (Repository)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRepository(Repository newRepository, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRepository, MigrationPackage.MODEL__REPOSITORY, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepository(Repository newRepository) {
		if (newRepository != eInternalContainer() || (eContainerFeatureID() != MigrationPackage.MODEL__REPOSITORY && newRepository != null)) {
			if (EcoreUtil.isAncestor(this, (EObject)newRepository))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRepository != null)
				msgs = ((InternalEObject)newRepository).eInverseAdd(this, MigrationPackage.REPOSITORY__MODEL, Repository.class, msgs);
			msgs = basicSetRepository(newRepository, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MigrationPackage.MODEL__REPOSITORY, newRepository, newRepository));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Instance> getAllInstances(EClass eClass) {
		EList<Instance> instances = new UniqueEList<Instance>();
		
		for(Type type : this.getTypes()) {
			if(eClass.isSuperTypeOf(type.getEClass())) {
				instances.addAll(type.getInstances());
			}
		}
		
		return instances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Instance> getInstances(EClass eClass) {
		try {
			Type type = this.getType(eClass);
			return new UniqueEList<Instance>(type.getInstances());
		}
		catch(NullPointerException e) {
			return new UniqueEList<Instance>();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Type getType(EClass eClass) {
		for(Type type : this.getTypes()) {
			if(type.getEClass() == eClass) {
				return type;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Map<EClass, Set<Instance>> createExtentMap() {
		return new LazyExtentMap(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Instance newInstance(EClass eClass) {
		Type type = this.getCreateType(eClass);
		return type.newInstance();
	}

	/**
	 * Get or create an instance container
	 * 
	 * @param eClass
	 * @return Instance container
	 */
	Type getCreateType(EClass eClass) {
		Type type = this.getType(eClass);
		if(type == null) {
			type = MigrationFactory.eINSTANCE.createType();
			type.setEClass(eClass);
			this.getTypes().add(type);
		}
		return type;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void delete(Instance instance) {
		for (EReference reference : instance.getEClass().getEAllReferences()) {
			if (reference.isContainment()) {
	    	if(reference.isMany()) {
		    	List<Instance> children = instance.get(reference);
		    	for(Instance child : children) {
		    		this.delete(child);
		    	}
	    	}
	    	else {
	    		Instance child = instance.get(reference);
	    		if(child != null) {
	    			this.delete(child);
	    		}
	    	}
			}
	    }
	    this.remove(instance);
	}

	
	/**
	 * Remove an instance from the model
	 * 
	 * @param instance
	 */
	private void remove(Instance instance) {
		Type type = instance.getType();
		for(Slot slot : new ArrayList<Slot>(instance.getSlots())) {
			instance.unset(slot.getEFeature());
		}
		for(ReferenceSlot slot : new ArrayList<ReferenceSlot>(instance.getReferences())) {
			slot.getInstance().remove(slot.getEReference(), instance);
		}
		this.removeDeleteType(type, instance);
	}

	/**
	 * Remove an instance from a container
	 * (delete the instance container if empty afterwards)
	 * 
	 * @param type
	 * @param instance
	 */
	void removeDeleteType(Type type, Instance instance) {		
		type.getInstances().remove(instance);
		if(type.getInstances().isEmpty()) {
			this.getTypes().remove(type);
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void validate() throws MigrationException {
		BasicDiagnostic chain = new BasicDiagnostic();
		for(ModelResource modelResource : this.getResources()) {
			for(Instance root : modelResource.getRootInstances()) {
				root.validate(chain);
			}
		}
		if(chain.getSeverity() != Diagnostic.OK) {
			throw new MigrationException(new DiagnosticException("Model not valid", chain));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void checkConformance() throws MigrationException {
		Diagnostician diagnostician = new Diagnostician() {
			@Override
			public String getObjectLabel(EObject object) {
				if (object instanceof Instance) {
					Instance instance = (Instance) object;
					return "Instance of type \""
							+ instance.getEClass().getName() + "\"";
				} else if (object instanceof ReferenceSlot) {
					ReferenceSlot referenceSlot = (ReferenceSlot) object;
					return "Reference \""
							+ referenceSlot.getEReference().getName()
							+ "\" of "
							+ getObjectLabel((EObject) referenceSlot
									.getInstance());
				} else if (object instanceof AttributeSlot) {
					AttributeSlot referenceSlot = (AttributeSlot) object;
					return "Attribute \""
							+ referenceSlot.getEAttribute().getName()
							+ "\" of "
							+ getObjectLabel((EObject) referenceSlot
									.getInstance());
				}
				return super.getObjectLabel(object);
			}
		};
		Diagnostic diagnostic = diagnostician.validate(this);
		if (diagnostic.getSeverity() != Diagnostic.OK) {
			throw new MigrationException(new DiagnosticException("Model inconsistent", diagnostic));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void commit() throws MigrationException {
		this.getMetamodel().validate();
		this.checkConformance();
		this.validate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Instance> getAllInstances(String className) {
		EClass eClass = checkAndGetClass(className);
		return getAllInstances(eClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Instance> getInstances(String className) {
		EClass eClass = checkAndGetClass(className);
		return getInstances(eClass);
	}

	private EClass checkAndGetClass(String className) {
		return getMetamodel().getEClass(className);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Instance newInstance(String className) {
		EClass eClass = checkAndGetClass(className);
		return newInstance(eClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ModelResource newResource(URI uri) {
		ModelResource resource = MigrationFactory.eINSTANCE
				.createModelResource();
		resource.setUri(uri);
		getResources().add(resource);
		return resource;
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
			case MigrationPackage.MODEL__TYPES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getTypes()).basicAdd(otherEnd, msgs);
			case MigrationPackage.MODEL__RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getResources()).basicAdd(otherEnd, msgs);
			case MigrationPackage.MODEL__REPOSITORY:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRepository((Repository)otherEnd, msgs);
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
			case MigrationPackage.MODEL__TYPES:
				return ((InternalEList<?>)getTypes()).basicRemove(otherEnd, msgs);
			case MigrationPackage.MODEL__RESOURCES:
				return ((InternalEList<?>)getResources()).basicRemove(otherEnd, msgs);
			case MigrationPackage.MODEL__REPOSITORY:
				return basicSetRepository(null, msgs);
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
			case MigrationPackage.MODEL__REPOSITORY:
				return eInternalContainer().eInverseRemove(this, MigrationPackage.REPOSITORY__MODEL, Repository.class, msgs);
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
			case MigrationPackage.MODEL__METAMODEL:
				if (resolve) return getMetamodel();
				return basicGetMetamodel();
			case MigrationPackage.MODEL__TYPES:
				return getTypes();
			case MigrationPackage.MODEL__REFLECTION:
				return isReflection();
			case MigrationPackage.MODEL__RESOURCES:
				return getResources();
			case MigrationPackage.MODEL__REPOSITORY:
				return getRepository();
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
			case MigrationPackage.MODEL__METAMODEL:
				setMetamodel((Metamodel)newValue);
				return;
			case MigrationPackage.MODEL__TYPES:
				getTypes().clear();
				getTypes().addAll((Collection<? extends Type>)newValue);
				return;
			case MigrationPackage.MODEL__REFLECTION:
				setReflection((Boolean)newValue);
				return;
			case MigrationPackage.MODEL__RESOURCES:
				getResources().clear();
				getResources().addAll((Collection<? extends ModelResource>)newValue);
				return;
			case MigrationPackage.MODEL__REPOSITORY:
				setRepository((Repository)newValue);
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
			case MigrationPackage.MODEL__METAMODEL:
				setMetamodel((Metamodel)null);
				return;
			case MigrationPackage.MODEL__TYPES:
				getTypes().clear();
				return;
			case MigrationPackage.MODEL__REFLECTION:
				setReflection(REFLECTION_EDEFAULT);
				return;
			case MigrationPackage.MODEL__RESOURCES:
				getResources().clear();
				return;
			case MigrationPackage.MODEL__REPOSITORY:
				setRepository((Repository)null);
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
			case MigrationPackage.MODEL__METAMODEL:
				return metamodel != null;
			case MigrationPackage.MODEL__TYPES:
				return types != null && !types.isEmpty();
			case MigrationPackage.MODEL__REFLECTION:
				return reflection != REFLECTION_EDEFAULT;
			case MigrationPackage.MODEL__RESOURCES:
				return resources != null && !resources.isEmpty();
			case MigrationPackage.MODEL__REPOSITORY:
				return getRepository() != null;
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
		result.append(" (reflection: ");
		result.append(reflection);
		result.append(')');
		return result.toString();
	}

} //ModelImpl
