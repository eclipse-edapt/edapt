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
package org.eclipse.emf.edapt.cope.history;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Metamodel to specify the history of a metamodel
 * <!-- end-model-doc -->
 * @see org.eclipse.emf.edapt.cope.history.HistoryFactory
 * @model kind="package"
 * @generated
 */
public interface HistoryPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "history";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/edapt/cope/history/0.3";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "history";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	HistoryPackage eINSTANCE = org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.HistoryImpl <em>History</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getHistory()
	 * @generated
	 */
	int HISTORY = 0;

	/**
	 * The feature id for the '<em><b>Releases</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HISTORY__RELEASES = 0;

	/**
	 * The number of structural features of the '<em>History</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HISTORY_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.ReleaseImpl <em>Release</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.ReleaseImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getRelease()
	 * @generated
	 */
	int RELEASE = 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELEASE__DATE = 0;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELEASE__CHANGES = 1;

	/**
	 * The feature id for the '<em><b>History</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELEASE__HISTORY = 2;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELEASE__LABEL = 3;

	/**
	 * The number of structural features of the '<em>Release</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELEASE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.ChangeImpl <em>Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.ChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getChange()
	 * @generated
	 */
	int CHANGE = 2;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE__BREAKING = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE__DESCRIPTION = 1;

	/**
	 * The number of structural features of the '<em>Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.MigrateableChangeImpl <em>Migrateable Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.MigrateableChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getMigrateableChange()
	 * @generated
	 */
	int MIGRATEABLE_CHANGE = 22;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATEABLE_CHANGE__BREAKING = CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATEABLE_CHANGE__DESCRIPTION = CHANGE__DESCRIPTION;

	/**
	 * The number of structural features of the '<em>Migrateable Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATEABLE_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.PrimitiveChangeImpl <em>Primitive Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.PrimitiveChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getPrimitiveChange()
	 * @generated
	 */
	int PRIMITIVE_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_CHANGE__BREAKING = MIGRATEABLE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_CHANGE__DESCRIPTION = MIGRATEABLE_CHANGE__DESCRIPTION;

	/**
	 * The number of structural features of the '<em>Primitive Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_CHANGE_FEATURE_COUNT = MIGRATEABLE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.NoChangeImpl <em>No Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.NoChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getNoChange()
	 * @generated
	 */
	int NO_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NO_CHANGE__BREAKING = PRIMITIVE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NO_CHANGE__DESCRIPTION = PRIMITIVE_CHANGE__DESCRIPTION;

	/**
	 * The number of structural features of the '<em>No Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NO_CHANGE_FEATURE_COUNT = PRIMITIVE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.ContentChangeImpl <em>Content Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.ContentChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getContentChange()
	 * @generated
	 */
	int CONTENT_CHANGE = 5;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENT_CHANGE__BREAKING = PRIMITIVE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENT_CHANGE__DESCRIPTION = PRIMITIVE_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENT_CHANGE__TARGET = PRIMITIVE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENT_CHANGE__REFERENCE = PRIMITIVE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENT_CHANGE__REFERENCE_NAME = PRIMITIVE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Content Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENT_CHANGE_FEATURE_COUNT = PRIMITIVE_CHANGE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.NonDeleteImpl <em>Non Delete</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.NonDeleteImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getNonDelete()
	 * @generated
	 */
	int NON_DELETE = 6;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_DELETE__BREAKING = CONTENT_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_DELETE__DESCRIPTION = CONTENT_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_DELETE__TARGET = CONTENT_CHANGE__TARGET;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_DELETE__REFERENCE = CONTENT_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_DELETE__REFERENCE_NAME = CONTENT_CHANGE__REFERENCE_NAME;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_DELETE__ELEMENT = CONTENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Non Delete</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_DELETE_FEATURE_COUNT = CONTENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.CreateImpl <em>Create</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.CreateImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getCreate()
	 * @generated
	 */
	int CREATE = 7;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE__BREAKING = NON_DELETE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE__DESCRIPTION = NON_DELETE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE__TARGET = NON_DELETE__TARGET;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE__REFERENCE = NON_DELETE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE__REFERENCE_NAME = NON_DELETE__REFERENCE_NAME;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE__ELEMENT = NON_DELETE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE__CHANGES = NON_DELETE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Create</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATE_FEATURE_COUNT = NON_DELETE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.MoveImpl <em>Move</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.MoveImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getMove()
	 * @generated
	 */
	int MOVE = 8;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE__BREAKING = NON_DELETE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE__DESCRIPTION = NON_DELETE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE__TARGET = NON_DELETE__TARGET;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE__REFERENCE = NON_DELETE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE__REFERENCE_NAME = NON_DELETE__REFERENCE_NAME;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE__ELEMENT = NON_DELETE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE__SOURCE = NON_DELETE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Move</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_FEATURE_COUNT = NON_DELETE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.DeleteImpl <em>Delete</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.DeleteImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getDelete()
	 * @generated
	 */
	int DELETE = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.ValueChangeImpl <em>Value Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.ValueChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getValueChange()
	 * @generated
	 */
	int VALUE_CHANGE = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.SetImpl <em>Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.SetImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getSet()
	 * @generated
	 */
	int SET = 11;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.AddImpl <em>Add</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.AddImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getAdd()
	 * @generated
	 */
	int ADD = 12;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.RemoveImpl <em>Remove</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.RemoveImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getRemove()
	 * @generated
	 */
	int REMOVE = 13;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.CompositeChangeImpl <em>Composite Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.CompositeChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getCompositeChange()
	 * @generated
	 */
	int COMPOSITE_CHANGE = 14;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.OperationChangeImpl <em>Operation Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.OperationChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getOperationChange()
	 * @generated
	 */
	int OPERATION_CHANGE = 15;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.NamedElementImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 21;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.OperationInstanceImpl <em>Operation Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.OperationInstanceImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getOperationInstance()
	 * @generated
	 */
	int OPERATION_INSTANCE = 16;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.PlaceholderInstanceImpl <em>Placeholder Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.PlaceholderInstanceImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getPlaceholderInstance()
	 * @generated
	 */
	int PLACEHOLDER_INSTANCE = 17;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.ParameterInstanceImpl <em>Parameter Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.ParameterInstanceImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getParameterInstance()
	 * @generated
	 */
	int PARAMETER_INSTANCE = 18;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.ModelReferenceImpl <em>Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.ModelReferenceImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getModelReference()
	 * @generated
	 */
	int MODEL_REFERENCE = 19;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.VariableInstanceImpl <em>Variable Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.VariableInstanceImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getVariableInstance()
	 * @generated
	 */
	int VARIABLE_INSTANCE = 20;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.MigrationChangeImpl <em>Migration Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.MigrationChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getMigrationChange()
	 * @generated
	 */
	int MIGRATION_CHANGE = 23;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.edapt.cope.history.impl.InitializerChangeImpl <em>Initializer Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.edapt.cope.history.impl.InitializerChangeImpl
	 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getInitializerChange()
	 * @generated
	 */
	int INITIALIZER_CHANGE = 24;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INITIALIZER_CHANGE__BREAKING = CONTENT_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INITIALIZER_CHANGE__DESCRIPTION = CONTENT_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INITIALIZER_CHANGE__TARGET = CONTENT_CHANGE__TARGET;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INITIALIZER_CHANGE__REFERENCE = CONTENT_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INITIALIZER_CHANGE__REFERENCE_NAME = CONTENT_CHANGE__REFERENCE_NAME;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INITIALIZER_CHANGE__CHANGES = CONTENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Initializer Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INITIALIZER_CHANGE_FEATURE_COUNT = CONTENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE__BREAKING = INITIALIZER_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE__DESCRIPTION = INITIALIZER_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE__TARGET = INITIALIZER_CHANGE__TARGET;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE__REFERENCE = INITIALIZER_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE__REFERENCE_NAME = INITIALIZER_CHANGE__REFERENCE_NAME;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE__CHANGES = INITIALIZER_CHANGE__CHANGES;

	/**
	 * The feature id for the '<em><b>Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE__ELEMENT = INITIALIZER_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Delete</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_FEATURE_COUNT = INITIALIZER_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__BREAKING = PRIMITIVE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__DESCRIPTION = PRIMITIVE_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__ELEMENT = PRIMITIVE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__FEATURE = PRIMITIVE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__FEATURE_NAME = PRIMITIVE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Data Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__DATA_VALUE = PRIMITIVE_CHANGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Reference Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__REFERENCE_VALUE = PRIMITIVE_CHANGE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE__VALUE = PRIMITIVE_CHANGE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Value Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_CHANGE_FEATURE_COUNT = PRIMITIVE_CHANGE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__BREAKING = VALUE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__DESCRIPTION = VALUE_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__ELEMENT = VALUE_CHANGE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__FEATURE = VALUE_CHANGE__FEATURE;

	/**
	 * The feature id for the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__FEATURE_NAME = VALUE_CHANGE__FEATURE_NAME;

	/**
	 * The feature id for the '<em><b>Data Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__DATA_VALUE = VALUE_CHANGE__DATA_VALUE;

	/**
	 * The feature id for the '<em><b>Reference Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__REFERENCE_VALUE = VALUE_CHANGE__REFERENCE_VALUE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__VALUE = VALUE_CHANGE__VALUE;

	/**
	 * The feature id for the '<em><b>Old Data Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__OLD_DATA_VALUE = VALUE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Old Reference Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__OLD_REFERENCE_VALUE = VALUE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Old Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__OLD_VALUE = VALUE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET_FEATURE_COUNT = VALUE_CHANGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__BREAKING = VALUE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__DESCRIPTION = VALUE_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__ELEMENT = VALUE_CHANGE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__FEATURE = VALUE_CHANGE__FEATURE;

	/**
	 * The feature id for the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__FEATURE_NAME = VALUE_CHANGE__FEATURE_NAME;

	/**
	 * The feature id for the '<em><b>Data Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__DATA_VALUE = VALUE_CHANGE__DATA_VALUE;

	/**
	 * The feature id for the '<em><b>Reference Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__REFERENCE_VALUE = VALUE_CHANGE__REFERENCE_VALUE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD__VALUE = VALUE_CHANGE__VALUE;

	/**
	 * The number of structural features of the '<em>Add</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_FEATURE_COUNT = VALUE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__BREAKING = VALUE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__DESCRIPTION = VALUE_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__ELEMENT = VALUE_CHANGE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__FEATURE = VALUE_CHANGE__FEATURE;

	/**
	 * The feature id for the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__FEATURE_NAME = VALUE_CHANGE__FEATURE_NAME;

	/**
	 * The feature id for the '<em><b>Data Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__DATA_VALUE = VALUE_CHANGE__DATA_VALUE;

	/**
	 * The feature id for the '<em><b>Reference Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__REFERENCE_VALUE = VALUE_CHANGE__REFERENCE_VALUE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE__VALUE = VALUE_CHANGE__VALUE;

	/**
	 * The number of structural features of the '<em>Remove</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVE_FEATURE_COUNT = VALUE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_CHANGE__BREAKING = MIGRATEABLE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_CHANGE__DESCRIPTION = MIGRATEABLE_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_CHANGE__CHANGES = MIGRATEABLE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composite Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_CHANGE_FEATURE_COUNT = MIGRATEABLE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CHANGE__BREAKING = COMPOSITE_CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CHANGE__DESCRIPTION = COMPOSITE_CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CHANGE__CHANGES = COMPOSITE_CHANGE__CHANGES;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CHANGE__OPERATION = COMPOSITE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Operation Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CHANGE_FEATURE_COUNT = COMPOSITE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_INSTANCE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_INSTANCE__PARAMETERS = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Variables</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_INSTANCE__VARIABLES = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Operation Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_INSTANCE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLACEHOLDER_INSTANCE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLACEHOLDER_INSTANCE__VALUE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLACEHOLDER_INSTANCE__DATA_VALUE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Reference Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLACEHOLDER_INSTANCE__REFERENCE_VALUE = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Placeholder Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLACEHOLDER_INSTANCE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_INSTANCE__NAME = PLACEHOLDER_INSTANCE__NAME;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_INSTANCE__VALUE = PLACEHOLDER_INSTANCE__VALUE;

	/**
	 * The feature id for the '<em><b>Data Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_INSTANCE__DATA_VALUE = PLACEHOLDER_INSTANCE__DATA_VALUE;

	/**
	 * The feature id for the '<em><b>Reference Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_INSTANCE__REFERENCE_VALUE = PLACEHOLDER_INSTANCE__REFERENCE_VALUE;

	/**
	 * The number of structural features of the '<em>Parameter Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_INSTANCE_FEATURE_COUNT = PLACEHOLDER_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_REFERENCE__ELEMENT = 0;

	/**
	 * The number of structural features of the '<em>Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_REFERENCE_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INSTANCE__NAME = PLACEHOLDER_INSTANCE__NAME;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INSTANCE__VALUE = PLACEHOLDER_INSTANCE__VALUE;

	/**
	 * The feature id for the '<em><b>Data Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INSTANCE__DATA_VALUE = PLACEHOLDER_INSTANCE__DATA_VALUE;

	/**
	 * The feature id for the '<em><b>Reference Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INSTANCE__REFERENCE_VALUE = PLACEHOLDER_INSTANCE__REFERENCE_VALUE;

	/**
	 * The number of structural features of the '<em>Variable Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INSTANCE_FEATURE_COUNT = PLACEHOLDER_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Breaking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_CHANGE__BREAKING = CHANGE__BREAKING;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_CHANGE__DESCRIPTION = CHANGE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Migration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_CHANGE__MIGRATION = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_CHANGE__CHANGES = CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Migration Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 2;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.History <em>History</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>History</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.History
	 * @generated
	 */
	EClass getHistory();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.History#getReleases <em>Releases</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Releases</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.History#getReleases()
	 * @see #getHistory()
	 * @generated
	 */
	EReference getHistory_Releases();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Release <em>Release</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Release</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Release
	 * @generated
	 */
	EClass getRelease();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.Release#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Release#getDate()
	 * @see #getRelease()
	 * @generated
	 */
	EAttribute getRelease_Date();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.Release#getChanges <em>Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Release#getChanges()
	 * @see #getRelease()
	 * @generated
	 */
	EReference getRelease_Changes();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.emf.edapt.cope.history.Release#getHistory <em>History</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>History</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Release#getHistory()
	 * @see #getRelease()
	 * @generated
	 */
	EReference getRelease_History();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.Release#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Release#getLabel()
	 * @see #getRelease()
	 * @generated
	 */
	EAttribute getRelease_Label();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Change <em>Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Change
	 * @generated
	 */
	EClass getChange();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.Change#isBreaking <em>Breaking</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Breaking</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Change#isBreaking()
	 * @see #getChange()
	 * @generated
	 */
	EAttribute getChange_Breaking();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.Change#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Change#getDescription()
	 * @see #getChange()
	 * @generated
	 */
	EAttribute getChange_Description();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.PrimitiveChange <em>Primitive Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Primitive Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.PrimitiveChange
	 * @generated
	 */
	EClass getPrimitiveChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.NoChange <em>No Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>No Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.NoChange
	 * @generated
	 */
	EClass getNoChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.ContentChange <em>Content Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Content Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ContentChange
	 * @generated
	 */
	EClass getContentChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.ContentChange#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ContentChange#getTarget()
	 * @see #getContentChange()
	 * @generated
	 */
	EReference getContentChange_Target();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.ContentChange#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ContentChange#getReference()
	 * @see #getContentChange()
	 * @generated
	 */
	EReference getContentChange_Reference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.ContentChange#getReferenceName <em>Reference Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference Name</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ContentChange#getReferenceName()
	 * @see #getContentChange()
	 * @generated
	 */
	EAttribute getContentChange_ReferenceName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.NonDelete <em>Non Delete</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Non Delete</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.NonDelete
	 * @generated
	 */
	EClass getNonDelete();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.NonDelete#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.NonDelete#getElement()
	 * @see #getNonDelete()
	 * @generated
	 */
	EReference getNonDelete_Element();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Create <em>Create</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Create</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Create
	 * @generated
	 */
	EClass getCreate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Move <em>Move</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Move</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Move
	 * @generated
	 */
	EClass getMove();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.Move#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Move#getSource()
	 * @see #getMove()
	 * @generated
	 */
	EReference getMove_Source();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Delete <em>Delete</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Delete</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Delete
	 * @generated
	 */
	EClass getDelete();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.edapt.cope.history.Delete#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Element</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Delete#getElement()
	 * @see #getDelete()
	 * @generated
	 */
	EReference getDelete_Element();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.ValueChange <em>Value Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ValueChange
	 * @generated
	 */
	EClass getValueChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ValueChange#getElement()
	 * @see #getValueChange()
	 * @generated
	 */
	EReference getValueChange_Element();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Feature</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ValueChange#getFeature()
	 * @see #getValueChange()
	 * @generated
	 */
	EReference getValueChange_Feature();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getFeatureName <em>Feature Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Name</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ValueChange#getFeatureName()
	 * @see #getValueChange()
	 * @generated
	 */
	EAttribute getValueChange_FeatureName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getDataValue <em>Data Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ValueChange#getDataValue()
	 * @see #getValueChange()
	 * @generated
	 */
	EAttribute getValueChange_DataValue();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getReferenceValue <em>Reference Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ValueChange#getReferenceValue()
	 * @see #getValueChange()
	 * @generated
	 */
	EReference getValueChange_ReferenceValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.ValueChange#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ValueChange#getValue()
	 * @see #getValueChange()
	 * @generated
	 */
	EAttribute getValueChange_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Set <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Set</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Set
	 * @generated
	 */
	EClass getSet();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.Set#getOldDataValue <em>Old Data Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Old Data Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Set#getOldDataValue()
	 * @see #getSet()
	 * @generated
	 */
	EAttribute getSet_OldDataValue();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.Set#getOldReferenceValue <em>Old Reference Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Old Reference Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Set#getOldReferenceValue()
	 * @see #getSet()
	 * @generated
	 */
	EReference getSet_OldReferenceValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.Set#getOldValue <em>Old Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Old Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Set#getOldValue()
	 * @see #getSet()
	 * @generated
	 */
	EAttribute getSet_OldValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Add <em>Add</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Add
	 * @generated
	 */
	EClass getAdd();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.Remove <em>Remove</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remove</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.Remove
	 * @generated
	 */
	EClass getRemove();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.CompositeChange <em>Composite Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composite Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.CompositeChange
	 * @generated
	 */
	EClass getCompositeChange();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.CompositeChange#getChanges <em>Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.CompositeChange#getChanges()
	 * @see #getCompositeChange()
	 * @generated
	 */
	EReference getCompositeChange_Changes();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.OperationChange <em>Operation Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.OperationChange
	 * @generated
	 */
	EClass getOperationChange();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.edapt.cope.history.OperationChange#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Operation</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.OperationChange#getOperation()
	 * @see #getOperationChange()
	 * @generated
	 */
	EReference getOperationChange_Operation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.OperationInstance <em>Operation Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Instance</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.OperationInstance
	 * @generated
	 */
	EClass getOperationInstance();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.OperationInstance#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.OperationInstance#getParameters()
	 * @see #getOperationInstance()
	 * @generated
	 */
	EReference getOperationInstance_Parameters();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.OperationInstance#getVariables <em>Variables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Variables</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.OperationInstance#getVariables()
	 * @see #getOperationInstance()
	 * @generated
	 */
	EReference getOperationInstance_Variables();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.PlaceholderInstance <em>Placeholder Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Placeholder Instance</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.PlaceholderInstance
	 * @generated
	 */
	EClass getPlaceholderInstance();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.PlaceholderInstance#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.PlaceholderInstance#getValue()
	 * @see #getPlaceholderInstance()
	 * @generated
	 */
	EAttribute getPlaceholderInstance_Value();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.emf.edapt.cope.history.PlaceholderInstance#getDataValue <em>Data Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Data Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.PlaceholderInstance#getDataValue()
	 * @see #getPlaceholderInstance()
	 * @generated
	 */
	EAttribute getPlaceholderInstance_DataValue();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.PlaceholderInstance#getReferenceValue <em>Reference Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Reference Value</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.PlaceholderInstance#getReferenceValue()
	 * @see #getPlaceholderInstance()
	 * @generated
	 */
	EReference getPlaceholderInstance_ReferenceValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.ParameterInstance <em>Parameter Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter Instance</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ParameterInstance
	 * @generated
	 */
	EClass getParameterInstance();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.ModelReference <em>Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Reference</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ModelReference
	 * @generated
	 */
	EClass getModelReference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.edapt.cope.history.ModelReference#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.ModelReference#getElement()
	 * @see #getModelReference()
	 * @generated
	 */
	EReference getModelReference_Element();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.VariableInstance <em>Variable Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Instance</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.VariableInstance
	 * @generated
	 */
	EClass getVariableInstance();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.MigrateableChange <em>Migrateable Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Migrateable Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.MigrateableChange
	 * @generated
	 */
	EClass getMigrateableChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.MigrationChange <em>Migration Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Migration Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.MigrationChange
	 * @generated
	 */
	EClass getMigrationChange();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.edapt.cope.history.MigrationChange#getMigration <em>Migration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Migration</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.MigrationChange#getMigration()
	 * @see #getMigrationChange()
	 * @generated
	 */
	EAttribute getMigrationChange_Migration();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.MigrationChange#getChanges <em>Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.MigrationChange#getChanges()
	 * @see #getMigrationChange()
	 * @generated
	 */
	EReference getMigrationChange_Changes();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.edapt.cope.history.InitializerChange <em>Initializer Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Initializer Change</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.InitializerChange
	 * @generated
	 */
	EClass getInitializerChange();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.edapt.cope.history.InitializerChange#getChanges <em>Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes</em>'.
	 * @see org.eclipse.emf.edapt.cope.history.InitializerChange#getChanges()
	 * @see #getInitializerChange()
	 * @generated
	 */
	EReference getInitializerChange_Changes();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	HistoryFactory getHistoryFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.HistoryImpl <em>History</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getHistory()
		 * @generated
		 */
		EClass HISTORY = eINSTANCE.getHistory();

		/**
		 * The meta object literal for the '<em><b>Releases</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HISTORY__RELEASES = eINSTANCE.getHistory_Releases();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.ReleaseImpl <em>Release</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.ReleaseImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getRelease()
		 * @generated
		 */
		EClass RELEASE = eINSTANCE.getRelease();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELEASE__DATE = eINSTANCE.getRelease_Date();

		/**
		 * The meta object literal for the '<em><b>Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RELEASE__CHANGES = eINSTANCE.getRelease_Changes();

		/**
		 * The meta object literal for the '<em><b>History</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RELEASE__HISTORY = eINSTANCE.getRelease_History();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELEASE__LABEL = eINSTANCE.getRelease_Label();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.ChangeImpl <em>Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.ChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getChange()
		 * @generated
		 */
		EClass CHANGE = eINSTANCE.getChange();

		/**
		 * The meta object literal for the '<em><b>Breaking</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE__BREAKING = eINSTANCE.getChange_Breaking();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE__DESCRIPTION = eINSTANCE.getChange_Description();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.PrimitiveChangeImpl <em>Primitive Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.PrimitiveChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getPrimitiveChange()
		 * @generated
		 */
		EClass PRIMITIVE_CHANGE = eINSTANCE.getPrimitiveChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.NoChangeImpl <em>No Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.NoChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getNoChange()
		 * @generated
		 */
		EClass NO_CHANGE = eINSTANCE.getNoChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.ContentChangeImpl <em>Content Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.ContentChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getContentChange()
		 * @generated
		 */
		EClass CONTENT_CHANGE = eINSTANCE.getContentChange();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTENT_CHANGE__TARGET = eINSTANCE.getContentChange_Target();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTENT_CHANGE__REFERENCE = eINSTANCE.getContentChange_Reference();

		/**
		 * The meta object literal for the '<em><b>Reference Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTENT_CHANGE__REFERENCE_NAME = eINSTANCE.getContentChange_ReferenceName();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.NonDeleteImpl <em>Non Delete</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.NonDeleteImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getNonDelete()
		 * @generated
		 */
		EClass NON_DELETE = eINSTANCE.getNonDelete();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NON_DELETE__ELEMENT = eINSTANCE.getNonDelete_Element();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.CreateImpl <em>Create</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.CreateImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getCreate()
		 * @generated
		 */
		EClass CREATE = eINSTANCE.getCreate();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.MoveImpl <em>Move</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.MoveImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getMove()
		 * @generated
		 */
		EClass MOVE = eINSTANCE.getMove();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MOVE__SOURCE = eINSTANCE.getMove_Source();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.DeleteImpl <em>Delete</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.DeleteImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getDelete()
		 * @generated
		 */
		EClass DELETE = eINSTANCE.getDelete();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DELETE__ELEMENT = eINSTANCE.getDelete_Element();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.ValueChangeImpl <em>Value Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.ValueChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getValueChange()
		 * @generated
		 */
		EClass VALUE_CHANGE = eINSTANCE.getValueChange();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALUE_CHANGE__ELEMENT = eINSTANCE.getValueChange_Element();

		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALUE_CHANGE__FEATURE = eINSTANCE.getValueChange_Feature();

		/**
		 * The meta object literal for the '<em><b>Feature Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_CHANGE__FEATURE_NAME = eINSTANCE.getValueChange_FeatureName();

		/**
		 * The meta object literal for the '<em><b>Data Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_CHANGE__DATA_VALUE = eINSTANCE.getValueChange_DataValue();

		/**
		 * The meta object literal for the '<em><b>Reference Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALUE_CHANGE__REFERENCE_VALUE = eINSTANCE.getValueChange_ReferenceValue();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_CHANGE__VALUE = eINSTANCE.getValueChange_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.SetImpl <em>Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.SetImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getSet()
		 * @generated
		 */
		EClass SET = eINSTANCE.getSet();

		/**
		 * The meta object literal for the '<em><b>Old Data Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SET__OLD_DATA_VALUE = eINSTANCE.getSet_OldDataValue();

		/**
		 * The meta object literal for the '<em><b>Old Reference Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SET__OLD_REFERENCE_VALUE = eINSTANCE.getSet_OldReferenceValue();

		/**
		 * The meta object literal for the '<em><b>Old Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SET__OLD_VALUE = eINSTANCE.getSet_OldValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.AddImpl <em>Add</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.AddImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getAdd()
		 * @generated
		 */
		EClass ADD = eINSTANCE.getAdd();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.RemoveImpl <em>Remove</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.RemoveImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getRemove()
		 * @generated
		 */
		EClass REMOVE = eINSTANCE.getRemove();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.CompositeChangeImpl <em>Composite Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.CompositeChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getCompositeChange()
		 * @generated
		 */
		EClass COMPOSITE_CHANGE = eINSTANCE.getCompositeChange();

		/**
		 * The meta object literal for the '<em><b>Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSITE_CHANGE__CHANGES = eINSTANCE.getCompositeChange_Changes();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.OperationChangeImpl <em>Operation Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.OperationChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getOperationChange()
		 * @generated
		 */
		EClass OPERATION_CHANGE = eINSTANCE.getOperationChange();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_CHANGE__OPERATION = eINSTANCE.getOperationChange_Operation();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.OperationInstanceImpl <em>Operation Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.OperationInstanceImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getOperationInstance()
		 * @generated
		 */
		EClass OPERATION_INSTANCE = eINSTANCE.getOperationInstance();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_INSTANCE__PARAMETERS = eINSTANCE.getOperationInstance_Parameters();

		/**
		 * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_INSTANCE__VARIABLES = eINSTANCE.getOperationInstance_Variables();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.PlaceholderInstanceImpl <em>Placeholder Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.PlaceholderInstanceImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getPlaceholderInstance()
		 * @generated
		 */
		EClass PLACEHOLDER_INSTANCE = eINSTANCE.getPlaceholderInstance();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLACEHOLDER_INSTANCE__VALUE = eINSTANCE.getPlaceholderInstance_Value();

		/**
		 * The meta object literal for the '<em><b>Data Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLACEHOLDER_INSTANCE__DATA_VALUE = eINSTANCE.getPlaceholderInstance_DataValue();

		/**
		 * The meta object literal for the '<em><b>Reference Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLACEHOLDER_INSTANCE__REFERENCE_VALUE = eINSTANCE.getPlaceholderInstance_ReferenceValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.ParameterInstanceImpl <em>Parameter Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.ParameterInstanceImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getParameterInstance()
		 * @generated
		 */
		EClass PARAMETER_INSTANCE = eINSTANCE.getParameterInstance();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.ModelReferenceImpl <em>Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.ModelReferenceImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getModelReference()
		 * @generated
		 */
		EClass MODEL_REFERENCE = eINSTANCE.getModelReference();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_REFERENCE__ELEMENT = eINSTANCE.getModelReference_Element();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.VariableInstanceImpl <em>Variable Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.VariableInstanceImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getVariableInstance()
		 * @generated
		 */
		EClass VARIABLE_INSTANCE = eINSTANCE.getVariableInstance();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.NamedElementImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.MigrateableChangeImpl <em>Migrateable Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.MigrateableChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getMigrateableChange()
		 * @generated
		 */
		EClass MIGRATEABLE_CHANGE = eINSTANCE.getMigrateableChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.MigrationChangeImpl <em>Migration Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.MigrationChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getMigrationChange()
		 * @generated
		 */
		EClass MIGRATION_CHANGE = eINSTANCE.getMigrationChange();

		/**
		 * The meta object literal for the '<em><b>Migration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MIGRATION_CHANGE__MIGRATION = eINSTANCE.getMigrationChange_Migration();

		/**
		 * The meta object literal for the '<em><b>Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MIGRATION_CHANGE__CHANGES = eINSTANCE.getMigrationChange_Changes();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.edapt.cope.history.impl.InitializerChangeImpl <em>Initializer Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.edapt.cope.history.impl.InitializerChangeImpl
		 * @see org.eclipse.emf.edapt.cope.history.impl.HistoryPackageImpl#getInitializerChange()
		 * @generated
		 */
		EClass INITIALIZER_CHANGE = eINSTANCE.getInitializerChange();

		/**
		 * The meta object literal for the '<em><b>Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INITIALIZER_CHANGE__CHANGES = eINSTANCE.getInitializerChange_Changes();

	}

} //HistoryPackage
