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
package org.eclipse.emf.edapt.migration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.migration.DiagnosticException;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metamodel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The metamodel in the repository consisting of several resources
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.edapt.migration.Metamodel#getResources <em>Resources</em>}</li>
 *   <li>{@link org.eclipse.emf.edapt.migration.Metamodel#getRepository <em>Repository</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.edapt.migration.MigrationPackage#getMetamodel()
 * @model
 * @generated
 */
public interface Metamodel extends EObject {
	/**
	 * Returns the value of the '<em><b>Resources</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.edapt.migration.MetamodelResource}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resources</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The resources of which this metamodel consists
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Resources</em>' containment reference list.
	 * @see org.eclipse.emf.edapt.migration.MigrationPackage#getMetamodel_Resources()
	 * @model containment="true"
	 * @generated
	 */
	EList<MetamodelResource> getResources();

	/**
	 * Returns the value of the '<em><b>Repository</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.edapt.migration.Repository#getMetamodel <em>Metamodel</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repository</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The repository to which the metamodel belongs
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Repository</em>' container reference.
	 * @see #setRepository(Repository)
	 * @see org.eclipse.emf.edapt.migration.MigrationPackage#getMetamodel_Repository()
	 * @see org.eclipse.emf.edapt.migration.Repository#getMetamodel
	 * @model opposite="metamodel" transient="false"
	 * @generated
	 */
	Repository getRepository();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.edapt.migration.Metamodel#getRepository <em>Repository</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repository</em>' container reference.
	 * @see #getRepository()
	 * @generated
	 */
	void setRepository(Repository value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EPackages</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The root packages of all resources of which the metamodel consists
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<EPackage> getEPackages();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve a package of the metamodel by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EPackage getEPackage(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve a classifier of the metamodel by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EClassifier getEClassifier(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve a feature of the metamodel by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EStructuralFeature getEFeature(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve a class of the metamodel by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EClass getEClass(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve a reference of the metamodel by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EReference getEReference(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve an attribute of the metamodel by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EAttribute getEAttribute(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve a data type of the metamodel by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EDataType getEDataType(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Resolve a metamodel element by its fully qualified name
	 * <!-- end-model-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	EModelElement getElement(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Validate the metamodel
	 * <!-- end-model-doc -->
	 * @model exceptions="org.eclipse.emf.edapt.migration.DiagnosticException"
	 * @generated
	 */
	void validate() throws DiagnosticException;

} // Metamodel