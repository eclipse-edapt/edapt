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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.edapt.declaration.Constraint;
import org.eclipse.emf.edapt.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.declaration.IdentifiedElement;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.Parameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DeclarationPackageImpl extends EPackageImpl implements DeclarationPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass identifiedElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass libraryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass operationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass parameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass constraintEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry
	 * EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
	 * performs initialization of the package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.edapt.declaration.DeclarationPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DeclarationPackageImpl() {
		super(eNS_URI, DeclarationFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>
	 * This method is used to initialize {@link DeclarationPackage#eINSTANCE} when that field is accessed. Clients
	 * should not invoke it directly. Instead, they should simply access that field to obtain the package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DeclarationPackage init() {
		if (isInited) {
			return (DeclarationPackage) EPackage.Registry.INSTANCE.getEPackage(DeclarationPackage.eNS_URI);
		}

		// Obtain or create and register package
		final DeclarationPackageImpl theDeclarationPackage = (DeclarationPackageImpl) (EPackage.Registry.INSTANCE
			.get(eNS_URI) instanceof DeclarationPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
				: new DeclarationPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDeclarationPackage.createPackageContents();

		// Initialize created meta-data
		theDeclarationPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDeclarationPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DeclarationPackage.eNS_URI, theDeclarationPackage);
		return theDeclarationPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getIdentifiedElement() {
		return identifiedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getIdentifiedElement_Name() {
		return (EAttribute) identifiedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getIdentifiedElement_Description() {
		return (EAttribute) identifiedElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getLibrary() {
		return libraryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getLibrary_Operations() {
		return (EReference) libraryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getLibrary_Implementation() {
		return (EAttribute) libraryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getLibrary_Label() {
		return (EAttribute) libraryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getLibrary_Libraries() {
		return (EReference) libraryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getOperation() {
		return operationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperation_Library() {
		return (EReference) operationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperation_Parameters() {
		return (EReference) operationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperation_Constraints() {
		return (EReference) operationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperation_Deprecated() {
		return (EAttribute) operationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperation_Before() {
		return (EAttribute) operationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperation_After() {
		return (EAttribute) operationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperation_Implementation() {
		return (EAttribute) operationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperation_Label() {
		return (EAttribute) operationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperation_Breaking() {
		return (EAttribute) operationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getParameter() {
		return parameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getParameter_Operation() {
		return (EReference) parameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getParameter_Required() {
		return (EAttribute) parameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getParameter_Main() {
		return (EAttribute) parameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getParameter_Many() {
		return (EAttribute) parameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getParameter_Classifier() {
		return (EReference) parameterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getParameter_ClassifierName() {
		return (EAttribute) parameterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getConstraint() {
		return constraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getConstraint_Operation() {
		return (EReference) constraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getConstraint_Restricts() {
		return (EReference) constraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeclarationFactory getDeclarationFactory() {
		return (DeclarationFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) {
			return;
		}
		isCreated = true;

		// Create classes and their features
		identifiedElementEClass = createEClass(IDENTIFIED_ELEMENT);
		createEAttribute(identifiedElementEClass, IDENTIFIED_ELEMENT__NAME);
		createEAttribute(identifiedElementEClass, IDENTIFIED_ELEMENT__DESCRIPTION);

		libraryEClass = createEClass(LIBRARY);
		createEReference(libraryEClass, LIBRARY__OPERATIONS);
		createEAttribute(libraryEClass, LIBRARY__IMPLEMENTATION);
		createEAttribute(libraryEClass, LIBRARY__LABEL);
		createEReference(libraryEClass, LIBRARY__LIBRARIES);

		operationEClass = createEClass(OPERATION);
		createEReference(operationEClass, OPERATION__LIBRARY);
		createEReference(operationEClass, OPERATION__PARAMETERS);
		createEReference(operationEClass, OPERATION__CONSTRAINTS);
		createEAttribute(operationEClass, OPERATION__DEPRECATED);
		createEAttribute(operationEClass, OPERATION__BEFORE);
		createEAttribute(operationEClass, OPERATION__AFTER);
		createEAttribute(operationEClass, OPERATION__IMPLEMENTATION);
		createEAttribute(operationEClass, OPERATION__LABEL);
		createEAttribute(operationEClass, OPERATION__BREAKING);

		parameterEClass = createEClass(PARAMETER);
		createEReference(parameterEClass, PARAMETER__OPERATION);
		createEAttribute(parameterEClass, PARAMETER__REQUIRED);
		createEAttribute(parameterEClass, PARAMETER__MAIN);
		createEAttribute(parameterEClass, PARAMETER__MANY);
		createEReference(parameterEClass, PARAMETER__CLASSIFIER);
		createEAttribute(parameterEClass, PARAMETER__CLASSIFIER_NAME);

		constraintEClass = createEClass(CONSTRAINT);
		createEReference(constraintEClass, CONSTRAINT__OPERATION);
		createEReference(constraintEClass, CONSTRAINT__RESTRICTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) {
			return;
		}
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		libraryEClass.getESuperTypes().add(getIdentifiedElement());
		operationEClass.getESuperTypes().add(getIdentifiedElement());
		parameterEClass.getESuperTypes().add(getIdentifiedElement());
		constraintEClass.getESuperTypes().add(getIdentifiedElement());

		// Initialize classes and features; add operations and parameters
		initEClass(identifiedElementEClass, IdentifiedElement.class, "IdentifiedElement", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIdentifiedElement_Name(), ecorePackage.getEString(), "name", null, 1, 1, //$NON-NLS-1$
			IdentifiedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEAttribute(getIdentifiedElement_Description(), ecorePackage.getEString(), "description", null, 0, 1, //$NON-NLS-1$
			IdentifiedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(libraryEClass, Library.class, "Library", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getLibrary_Operations(), getOperation(), getOperation_Library(), "operations", null, 0, //$NON-NLS-1$
			-1, Library.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLibrary_Implementation(), ecorePackage.getEJavaClass(), "implementation", null, 1, 1, //$NON-NLS-1$
			Library.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
			IS_ORDERED);
		initEAttribute(getLibrary_Label(), ecorePackage.getEString(), "label", null, 1, 1, Library.class, !IS_TRANSIENT, //$NON-NLS-1$
			!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLibrary_Libraries(), getLibrary(), null, "libraries", null, 0, -1, Library.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(libraryEClass, getOperation(), "getOperation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(operationEClass, Operation.class, "Operation", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperation_Library(), getLibrary(), getLibrary_Operations(), "library", null, 1, 1, //$NON-NLS-1$
			Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOperation_Parameters(), getParameter(), getParameter_Operation(), "parameters", //$NON-NLS-1$
			null, 0, -1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOperation_Constraints(), getConstraint(), getConstraint_Operation(), "constraints", //$NON-NLS-1$
			null, 0, -1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOperation_Deprecated(), ecorePackage.getEBoolean(), "deprecated", "false", 0, 1, //$NON-NLS-1$ //$NON-NLS-2$
			Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
			IS_ORDERED);
		initEAttribute(getOperation_Before(), ecorePackage.getEString(), "before", null, 0, 1, Operation.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOperation_After(), ecorePackage.getEString(), "after", null, 0, 1, Operation.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOperation_Implementation(), ecorePackage.getEJavaClass(), "implementation", null, 1, 1, //$NON-NLS-1$
			Operation.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
			IS_ORDERED);
		initEAttribute(getOperation_Label(), ecorePackage.getEString(), "label", null, 1, 1, Operation.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOperation_Breaking(), ecorePackage.getEBoolean(), "breaking", null, 1, 1, Operation.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(operationEClass, getParameter(), "getParameter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(operationEClass, getParameter(), "getMainParameter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(operationEClass, ecorePackage.getEBoolean(), "refines", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op = addEOperation(operationEClass, getConstraint(), "getConstraint", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(parameterEClass, Parameter.class, "Parameter", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParameter_Operation(), getOperation(), getOperation_Parameters(), "operation", null, //$NON-NLS-1$
			1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_Required(), ecorePackage.getEBoolean(), "required", "true", 0, 1, Parameter.class, //$NON-NLS-1$ //$NON-NLS-2$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_Main(), ecorePackage.getEBoolean(), "main", null, 0, 1, Parameter.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_Many(), ecorePackage.getEBoolean(), "many", null, 0, 1, Parameter.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameter_Classifier(), ecorePackage.getEClassifier(), null, "classifier", null, 1, 1, //$NON-NLS-1$
			Parameter.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ClassifierName(), ecorePackage.getEString(), "classifierName", null, 1, 1, //$NON-NLS-1$
			Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
			IS_ORDERED);

		initEClass(constraintEClass, Constraint.class, "Constraint", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConstraint_Operation(), getOperation(), getOperation_Constraints(), "operation", //$NON-NLS-1$
			null, 1, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
			!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConstraint_Restricts(), getParameter(), null, "restricts", null, 0, 1, Constraint.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} // DeclarationPackageImpl
