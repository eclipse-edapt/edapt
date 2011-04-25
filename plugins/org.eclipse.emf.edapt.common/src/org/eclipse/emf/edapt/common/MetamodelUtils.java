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
package org.eclipse.emf.edapt.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

/**
 * Helper methods for metamodel access.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class MetamodelUtils {

	/** Name for generic types. */
	final static String GENERIC = "Generic";

	/** Constructor. */
	private MetamodelUtils() {
		// hidden, since this class only provides static helper methods
	}

	/** Check whether a reference is of generic type. */
	public static boolean isGenericReference(EReference reference) {
		return reference.getEReferenceType() == EcorePackage.eINSTANCE
				.getEGenericType();
	}

	/** Get the corresponding reference which is not generic. */
	public static EReference getNonGenericReference(EReference genericReference) {
		EClass c = genericReference.getEContainingClass();
		String referenceName = genericReference.getName().replace(GENERIC, "");
		EReference nonGenericReference = (EReference) c
				.getEStructuralFeature(referenceName);
		return nonGenericReference;

	}

	/** Get the corresponding reference which is generic. */
	public static EReference getGenericReference(EReference reference) {
		String name = reference.getName();
		name = name.charAt(0) + GENERIC + name.substring(1);
		return (EReference) reference.getEContainingClass()
				.getEStructuralFeature(name);
	}

	/** Get operations of a class having a certain name. */
	public static List<EOperation> getOperations(EClass eClass, String name) {
		List<EOperation> operations = new ArrayList<EOperation>();
		for (EOperation operation : eClass.getEOperations()) {
			if (name.equals(operation.getName())) {
				operations.add(operation);
			}
		}
		return operations;
	}

	/** Get the operation of a class with a certain signature. */
	public static EOperation getOperation(EClass eClass, String name,
			EClassifier... parameterTypes) {
		List<EOperation> operations = getOperations(eClass, name);
		// if we have only one operation with that name, then we're finished
		if (operations.size() == 1) {
			return operations.get(0);
		}
		// remove operations having a different number of parameters
		for (Iterator<EOperation> i = operations.iterator(); i.hasNext();) {
			EOperation operation = i.next();
			if (operation.getEParameters().size() != parameterTypes.length) {
				i.remove();
			}
		}
		if (operations.size() == 1) {
			return operations.get(0);
		}
		// compare the parameter types
		for (EOperation operation : operations) {
			boolean found = true;
			int i = 0;
			for (EParameter parameter : operation.getEParameters()) {
				if (parameter.isMany()) {
					if (parameterTypes[i] != EcorePackage.eINSTANCE.getEEList()) {
						found = false;
					}
				} else {
					if (parameterTypes[i] != parameter.getEType()) {
						found = false;
					}
				}
				i++;
			}
			if (found) {
				return operation;
			}
		}
		return null;
	}

	/**
	 * Copy a metamodel element. Do not copy opposites in case this might lead
	 * to an inconsistency.
	 */
	@SuppressWarnings("unchecked")
	public static <V extends EModelElement> V copy(V modelElement) {
		Copier copier = new Copier() {
			@Override
			protected void copyReference(EReference eReference,
					EObject eObject, EObject copyEObject) {
				if (eReference == EcorePackage.eINSTANCE
						.getEReference_EOpposite()) {
					if (eObject.eIsSet(eReference)) {
						Object value = eObject.eGet(eReference);
						if (get(value) == null) {
							return;
						}
					}
				}
				super.copyReference(eReference, eObject, copyEObject);
			}
		};
		V result = (V) copier.copy(modelElement);
		copier.copyReferences();
		return result;
	}

	/** Get all root packages in a resource set. */
	public static Collection<EPackage> getAllRootPackages(
			ResourceSet resourceSet) {
		List<EPackage> result = new ArrayList<EPackage>();

		for (Resource resource : resourceSet.getResources()) {
			if (!MetamodelUtils.isMetamodelResource(resource))
				continue;

			for (EObject o : resource.getContents()) {
				if (o instanceof EPackage) {
					EPackage p = (EPackage) o;
					result.add(p);
				}
			}
		}
		return result;
	}

	/** Check whether the resource contains a metamodel. */
	public static boolean isMetamodelResource(Resource resource) {
		URI uri = resource.getURI();
		return MetamodelUtils.isMetamodelURI(uri);
	}

	/** Check whether the URI denotes a resource containing a metamodel. */
	public static boolean isMetamodelURI(URI uri) {
		return ResourceUtils.ECORE_FILE_EXTENSION.equals(uri.fileExtension());
	}

	/** Get any root package in a resource set. */
	@SuppressWarnings("cast")
	public static EPackage getRootPackage(ResourceSet resourceSet) {
		Resource resource = (Resource) resourceSet.getResources().get(0);
		return (EPackage) resource.getContents().get(0);
	}

	/**
	 * Check whether a class is concrete, i.e. neither abstract nor an
	 * interface.
	 */
	public static boolean isConcrete(EClass eClass) {
		return !(eClass.isAbstract() || eClass.isInterface());
	}

	/**
	 * Subtract the types that are provided by a class from the types that are
	 * provided by another class.
	 */
	public static List<EClass> subtractTypes(EClass minuend, EClass subtrahend) {

		List<EClass> superTypes = new ArrayList<EClass>();

		superTypes.addAll(minuend.getEAllSuperTypes());
		superTypes.add(0, minuend);

		superTypes.removeAll(subtrahend.getEAllSuperTypes());
		superTypes.remove(subtrahend);

		return superTypes;
	}

	/**
	 * Subtract the features that are provided by a class from the types that
	 * are provided by another class.
	 */
	public static List<EStructuralFeature> subtractFeatures(EClass minuend,
			EClass subtrahend) {

		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		List<EClass> superTypes = subtractTypes(minuend, subtrahend);

		for (EClass superType : superTypes) {
			features.addAll(superType.getEStructuralFeatures());
		}

		return features;
	}
}
