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
package org.eclipse.emf.edapt.spi.history.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.spi.history.Add;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.ContentChange;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.Delete;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.InitializerChange;
import org.eclipse.emf.edapt.spi.history.MigrateableChange;
import org.eclipse.emf.edapt.spi.history.MigrationChange;
import org.eclipse.emf.edapt.spi.history.ModelReference;
import org.eclipse.emf.edapt.spi.history.Move;
import org.eclipse.emf.edapt.spi.history.NamedElement;
import org.eclipse.emf.edapt.spi.history.NoChange;
import org.eclipse.emf.edapt.spi.history.NonDelete;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.ParameterInstance;
import org.eclipse.emf.edapt.spi.history.PrimitiveChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edapt.spi.history.Remove;
import org.eclipse.emf.edapt.spi.history.Set;
import org.eclipse.emf.edapt.spi.history.ValueChange;
import org.eclipse.emf.edapt.spi.history.util.HistoryValidator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class HistoryPackageImpl extends EPackageImpl implements HistoryPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass historyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass releaseEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass changeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass primitiveChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass noChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass contentChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass nonDeleteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass createEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass moveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deleteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass valueChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass setEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass addEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass removeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass compositeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass operationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass operationInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass parameterInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass modelReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass namedElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass migrateableChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass migrationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass initializerChangeEClass = null;

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
	 * @see org.eclipse.emf.edapt.spi.history.HistoryPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private HistoryPackageImpl() {
		super(eNS_URI, HistoryFactory.eINSTANCE);
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
	 * This method is used to initialize {@link HistoryPackage#eINSTANCE} when that field is accessed. Clients should
	 * not invoke it directly. Instead, they should simply access that field to obtain the package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static HistoryPackage init() {
		if (isInited) {
			return (HistoryPackage) EPackage.Registry.INSTANCE.getEPackage(HistoryPackage.eNS_URI);
		}

		// Obtain or create and register package
		final HistoryPackageImpl theHistoryPackage = (HistoryPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof HistoryPackageImpl ? EPackage.Registry.INSTANCE
			.get(eNS_URI)
			: new HistoryPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DeclarationPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theHistoryPackage.createPackageContents();

		// Initialize created meta-data
		theHistoryPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theHistoryPackage,
				new EValidator.Descriptor() {
					@Override
					public EValidator getEValidator() {
						return HistoryValidator.INSTANCE;
					}
				});

		// Mark meta-data to indicate it can't be changed
		theHistoryPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(HistoryPackage.eNS_URI, theHistoryPackage);
		return theHistoryPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getHistory() {
		return historyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getHistory_Releases() {
		return (EReference) historyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getRelease() {
		return releaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getRelease_Date() {
		return (EAttribute) releaseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getRelease_Changes() {
		return (EReference) releaseEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getRelease_History() {
		return (EReference) releaseEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getRelease_Label() {
		return (EAttribute) releaseEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getChange() {
		return changeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getChange_Breaking() {
		return (EAttribute) changeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getChange_Description() {
		return (EAttribute) changeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getPrimitiveChange() {
		return primitiveChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getNoChange() {
		return noChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getContentChange() {
		return contentChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getContentChange_Target() {
		return (EReference) contentChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getContentChange_Reference() {
		return (EReference) contentChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getContentChange_ReferenceName() {
		return (EAttribute) contentChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getNonDelete() {
		return nonDeleteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getNonDelete_Element() {
		return (EReference) nonDeleteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getCreate() {
		return createEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getMove() {
		return moveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getMove_Source() {
		return (EReference) moveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDelete() {
		return deleteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDelete_Element() {
		return (EReference) deleteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getValueChange() {
		return valueChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getValueChange_Element() {
		return (EReference) valueChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getValueChange_Feature() {
		return (EReference) valueChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getValueChange_FeatureName() {
		return (EAttribute) valueChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getValueChange_DataValue() {
		return (EAttribute) valueChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getValueChange_ReferenceValue() {
		return (EReference) valueChangeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getValueChange_Value() {
		return (EAttribute) valueChangeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getSet() {
		return setEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getSet_OldDataValue() {
		return (EAttribute) setEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getSet_OldReferenceValue() {
		return (EReference) setEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getSet_OldValue() {
		return (EAttribute) setEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getAdd() {
		return addEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getRemove() {
		return removeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getCompositeChange() {
		return compositeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getCompositeChange_Changes() {
		return (EReference) compositeChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getOperationChange() {
		return operationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationChange_Operation() {
		return (EReference) operationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getOperationInstance() {
		return operationInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationInstance_Parameters() {
		return (EReference) operationInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getParameterInstance() {
		return parameterInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getParameterInstance_Value() {
		return (EAttribute) parameterInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getParameterInstance_DataValue() {
		return (EAttribute) parameterInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getParameterInstance_ReferenceValue() {
		return (EReference) parameterInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getModelReference() {
		return modelReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getModelReference_Element() {
		return (EReference) modelReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getNamedElement() {
		return namedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getNamedElement_Name() {
		return (EAttribute) namedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getMigrateableChange() {
		return migrateableChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getMigrationChange() {
		return migrationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getMigrationChange_Migration() {
		return (EAttribute) migrationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getMigrationChange_Changes() {
		return (EReference) migrationChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getInitializerChange() {
		return initializerChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getInitializerChange_Changes() {
		return (EReference) initializerChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public HistoryFactory getHistoryFactory() {
		return (HistoryFactory) getEFactoryInstance();
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
		historyEClass = createEClass(HISTORY);
		createEReference(historyEClass, HISTORY__RELEASES);

		releaseEClass = createEClass(RELEASE);
		createEAttribute(releaseEClass, RELEASE__DATE);
		createEReference(releaseEClass, RELEASE__CHANGES);
		createEReference(releaseEClass, RELEASE__HISTORY);
		createEAttribute(releaseEClass, RELEASE__LABEL);

		changeEClass = createEClass(CHANGE);
		createEAttribute(changeEClass, CHANGE__BREAKING);
		createEAttribute(changeEClass, CHANGE__DESCRIPTION);

		primitiveChangeEClass = createEClass(PRIMITIVE_CHANGE);

		noChangeEClass = createEClass(NO_CHANGE);

		contentChangeEClass = createEClass(CONTENT_CHANGE);
		createEReference(contentChangeEClass, CONTENT_CHANGE__TARGET);
		createEReference(contentChangeEClass, CONTENT_CHANGE__REFERENCE);
		createEAttribute(contentChangeEClass, CONTENT_CHANGE__REFERENCE_NAME);

		nonDeleteEClass = createEClass(NON_DELETE);
		createEReference(nonDeleteEClass, NON_DELETE__ELEMENT);

		createEClass = createEClass(CREATE);

		moveEClass = createEClass(MOVE);
		createEReference(moveEClass, MOVE__SOURCE);

		deleteEClass = createEClass(DELETE);
		createEReference(deleteEClass, DELETE__ELEMENT);

		valueChangeEClass = createEClass(VALUE_CHANGE);
		createEReference(valueChangeEClass, VALUE_CHANGE__ELEMENT);
		createEReference(valueChangeEClass, VALUE_CHANGE__FEATURE);
		createEAttribute(valueChangeEClass, VALUE_CHANGE__FEATURE_NAME);
		createEAttribute(valueChangeEClass, VALUE_CHANGE__DATA_VALUE);
		createEReference(valueChangeEClass, VALUE_CHANGE__REFERENCE_VALUE);
		createEAttribute(valueChangeEClass, VALUE_CHANGE__VALUE);

		setEClass = createEClass(SET);
		createEAttribute(setEClass, SET__OLD_DATA_VALUE);
		createEReference(setEClass, SET__OLD_REFERENCE_VALUE);
		createEAttribute(setEClass, SET__OLD_VALUE);

		addEClass = createEClass(ADD);

		removeEClass = createEClass(REMOVE);

		compositeChangeEClass = createEClass(COMPOSITE_CHANGE);
		createEReference(compositeChangeEClass, COMPOSITE_CHANGE__CHANGES);

		operationChangeEClass = createEClass(OPERATION_CHANGE);
		createEReference(operationChangeEClass, OPERATION_CHANGE__OPERATION);

		operationInstanceEClass = createEClass(OPERATION_INSTANCE);
		createEReference(operationInstanceEClass, OPERATION_INSTANCE__PARAMETERS);

		parameterInstanceEClass = createEClass(PARAMETER_INSTANCE);
		createEAttribute(parameterInstanceEClass, PARAMETER_INSTANCE__VALUE);
		createEAttribute(parameterInstanceEClass, PARAMETER_INSTANCE__DATA_VALUE);
		createEReference(parameterInstanceEClass, PARAMETER_INSTANCE__REFERENCE_VALUE);

		modelReferenceEClass = createEClass(MODEL_REFERENCE);
		createEReference(modelReferenceEClass, MODEL_REFERENCE__ELEMENT);

		namedElementEClass = createEClass(NAMED_ELEMENT);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__NAME);

		migrateableChangeEClass = createEClass(MIGRATEABLE_CHANGE);

		migrationChangeEClass = createEClass(MIGRATION_CHANGE);
		createEAttribute(migrationChangeEClass, MIGRATION_CHANGE__MIGRATION);
		createEReference(migrationChangeEClass, MIGRATION_CHANGE__CHANGES);

		initializerChangeEClass = createEClass(INITIALIZER_CHANGE);
		createEReference(initializerChangeEClass, INITIALIZER_CHANGE__CHANGES);
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

		// Obtain other dependent packages
		final DeclarationPackage theDeclarationPackage = (DeclarationPackage) EPackage.Registry.INSTANCE
			.getEPackage(DeclarationPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		primitiveChangeEClass.getESuperTypes().add(getMigrateableChange());
		noChangeEClass.getESuperTypes().add(getPrimitiveChange());
		contentChangeEClass.getESuperTypes().add(getPrimitiveChange());
		nonDeleteEClass.getESuperTypes().add(getContentChange());
		createEClass.getESuperTypes().add(getNonDelete());
		createEClass.getESuperTypes().add(getInitializerChange());
		moveEClass.getESuperTypes().add(getNonDelete());
		deleteEClass.getESuperTypes().add(getInitializerChange());
		valueChangeEClass.getESuperTypes().add(getPrimitiveChange());
		setEClass.getESuperTypes().add(getValueChange());
		addEClass.getESuperTypes().add(getValueChange());
		removeEClass.getESuperTypes().add(getValueChange());
		compositeChangeEClass.getESuperTypes().add(getMigrateableChange());
		operationChangeEClass.getESuperTypes().add(getCompositeChange());
		operationInstanceEClass.getESuperTypes().add(getNamedElement());
		parameterInstanceEClass.getESuperTypes().add(getNamedElement());
		migrateableChangeEClass.getESuperTypes().add(getChange());
		migrationChangeEClass.getESuperTypes().add(getChange());
		initializerChangeEClass.getESuperTypes().add(getContentChange());

		// Initialize classes and features; add operations and parameters
		initEClass(historyEClass, History.class, "History", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getHistory_Releases(), getRelease(), getRelease_History(), "releases", null, 0, -1, //$NON-NLS-1$
			History.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(historyEClass, getRelease(), "getFirstRelease", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(historyEClass, getRelease(), "getLastRelease", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(historyEClass, ecorePackage.getEPackage(), "getRootPackages", 0, -1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(historyEClass, getRelease(), "getLatestRelease", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(releaseEClass, Release.class, "Release", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getRelease_Date(), ecorePackage.getEDate(), "date", null, 0, 1, Release.class, !IS_TRANSIENT, //$NON-NLS-1$
			!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRelease_Changes(), getChange(), null, "changes", null, 0, -1, Release.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEReference(getRelease_History(), getHistory(), getHistory_Releases(), "history", null, 1, 1, //$NON-NLS-1$
			Release.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRelease_Label(), ecorePackage.getEString(), "label", null, 0, 1, Release.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(releaseEClass, getRelease(), "getNextRelease", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(releaseEClass, getRelease(), "getPreviousRelease", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(releaseEClass, ecorePackage.getEBoolean(), "isFirstRelease", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(releaseEClass, ecorePackage.getEBoolean(), "isLastRelease", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(releaseEClass, ecorePackage.getEInt(), "getNumber", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(releaseEClass, ecorePackage.getEBoolean(), "isLatestRelease", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(changeEClass, Change.class, "Change", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getChange_Breaking(), ecorePackage.getEBoolean(), "breaking", null, 0, 1, Change.class, //$NON-NLS-1$
			IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChange_Description(), ecorePackage.getEString(), "description", null, 0, 1, Change.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(changeEClass, getRelease(), "getRelease", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(primitiveChangeEClass, PrimitiveChange.class, "PrimitiveChange", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);

		initEClass(noChangeEClass, NoChange.class, "NoChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(contentChangeEClass, ContentChange.class, "ContentChange", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContentChange_Target(), ecorePackage.getEObject(), null, "target", null, 0, 1, //$NON-NLS-1$
			ContentChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContentChange_Reference(), ecorePackage.getEReference(), null, "reference", null, 0, 1, //$NON-NLS-1$
			ContentChange.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentChange_ReferenceName(), ecorePackage.getEString(), "referenceName", null, 0, 1, //$NON-NLS-1$
			ContentChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(nonDeleteEClass, NonDelete.class, "NonDelete", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNonDelete_Element(), ecorePackage.getEObject(), null, "element", null, 1, 1, NonDelete.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(createEClass, Create.class, "Create", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(moveEClass, Move.class, "Move", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getMove_Source(), ecorePackage.getEObject(), null, "source", null, 1, 1, Move.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(deleteEClass, Delete.class, "Delete", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getDelete_Element(), ecorePackage.getEObject(), null, "element", null, 1, 1, Delete.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(valueChangeEClass, ValueChange.class, "ValueChange", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getValueChange_Element(), ecorePackage.getEObject(), null, "element", null, 1, 1, //$NON-NLS-1$
			ValueChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getValueChange_Feature(), ecorePackage.getEStructuralFeature(), null, "feature", null, 1, 1, //$NON-NLS-1$
			ValueChange.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueChange_FeatureName(), ecorePackage.getEString(), "featureName", null, 1, 1, //$NON-NLS-1$
			ValueChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueChange_DataValue(), ecorePackage.getEString(), "dataValue", null, 0, 1, //$NON-NLS-1$
			ValueChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEReference(getValueChange_ReferenceValue(), ecorePackage.getEObject(), null, "referenceValue", null, 0, 1, //$NON-NLS-1$
			ValueChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueChange_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, ValueChange.class, //$NON-NLS-1$
			IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(setEClass, Set.class, "Set", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getSet_OldDataValue(), ecorePackage.getEString(), "oldDataValue", null, 0, 1, Set.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSet_OldReferenceValue(), ecorePackage.getEObject(), null, "oldReferenceValue", null, 0, 1, //$NON-NLS-1$
			Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
			IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSet_OldValue(), ecorePackage.getEJavaObject(), "oldValue", null, 0, 1, Set.class, //$NON-NLS-1$
			IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(addEClass, Add.class, "Add", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(removeEClass, Remove.class, "Remove", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(compositeChangeEClass, CompositeChange.class, "CompositeChange", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCompositeChange_Changes(), getPrimitiveChange(), null, "changes", null, 0, -1, //$NON-NLS-1$
			CompositeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(operationChangeEClass, OperationChange.class, "OperationChange", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperationChange_Operation(), getOperationInstance(), null, "operation", null, 1, 1, //$NON-NLS-1$
			OperationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(operationInstanceEClass, OperationInstance.class, "OperationInstance", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperationInstance_Parameters(), getParameterInstance(), null, "parameters", null, 0, -1, //$NON-NLS-1$
			OperationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(operationInstanceEClass, getParameterInstance(), "getParameter", 0, 1, //$NON-NLS-1$
			IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op = addEOperation(operationInstanceEClass, ecorePackage.getEJavaObject(), "getParameterValue", 0, 1, //$NON-NLS-1$
			IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(operationInstanceEClass, theDeclarationPackage.getOperation(), "getOperation", 0, 1, IS_UNIQUE, //$NON-NLS-1$
			IS_ORDERED);

		op = addEOperation(operationInstanceEClass, null, "setParameterValue", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEJavaObject(), "value", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(parameterInstanceEClass, ParameterInstance.class, "ParameterInstance", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameterInstance_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, //$NON-NLS-1$
			ParameterInstance.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterInstance_DataValue(), ecorePackage.getEString(), "dataValue", null, 0, -1, //$NON-NLS-1$
			ParameterInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEReference(getParameterInstance_ReferenceValue(), getModelReference(), null, "referenceValue", null, //$NON-NLS-1$
			0, -1, ParameterInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
			!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(parameterInstanceEClass, theDeclarationPackage.getParameter(), "getParameter", 0, 1, IS_UNIQUE, //$NON-NLS-1$
			IS_ORDERED);

		initEClass(modelReferenceEClass, ModelReference.class, "ModelReference", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getModelReference_Element(), ecorePackage.getEObject(), null, "element", null, 1, 1, //$NON-NLS-1$
			ModelReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedElementEClass, NamedElement.class, "NamedElement", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedElement_Name(), ecorePackage.getEString(), "name", null, 1, 1, NamedElement.class, //$NON-NLS-1$
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(migrateableChangeEClass, MigrateableChange.class, "MigrateableChange", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);

		initEClass(migrationChangeEClass, MigrationChange.class, "MigrationChange", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMigrationChange_Migration(), ecorePackage.getEString(), "migration", null, 1, 1, //$NON-NLS-1$
			MigrationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEReference(getMigrationChange_Changes(), getMigrateableChange(), null, "changes", null, 0, -1, //$NON-NLS-1$
			MigrationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(initializerChangeEClass, InitializerChange.class, "InitializerChange", IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInitializerChange_Changes(), getValueChange(), null, "changes", null, 0, -1, //$NON-NLS-1$
			InitializerChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/emf/2002/Ecore
		createEcoreAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void createEcoreAnnotations() {
		final String source = "http://www.eclipse.org/emf/2002/Ecore"; //$NON-NLS-1$
		addAnnotation(changeEClass,
			source,
			new String[] {
				"constraints", "Breaking" //$NON-NLS-1$ //$NON-NLS-2$
			});
		addAnnotation(deleteEClass,
			source,
			new String[] {
				"text", "" //$NON-NLS-1$ //$NON-NLS-2$
			});
	}

} // HistoryPackageImpl
