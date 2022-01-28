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
package org.eclipse.emf.edapt.internal.migration.internal;

import java.text.MessageFormat;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edapt.internal.common.EcoreUtils;
import org.eclipse.emf.edapt.internal.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.common.ReversableMap;
import org.eclipse.emf.edapt.internal.common.TwoWayIdentityHashMap;
import org.eclipse.emf.edapt.spi.migration.AttributeSlot;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.MetamodelResource;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.ModelResource;
import org.eclipse.emf.edapt.spi.migration.ReferenceSlot;
import org.eclipse.emf.edapt.spi.migration.Slot;
import org.eclipse.emf.edapt.spi.migration.Type;

/**
 * Convert a model graph to an EMF model.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 4D9E84B44F84E9C760AB3E79983988B4
 */
public class BackwardConverter {

	/** Mapping from graph nodes to EMF model elements. */
	protected ReversableMap<Instance, EObject> mapping;
	protected Model model;

	/** Convert model graph to EMF elements. */
	public ResourceSet convert(Model model) {
		this.model = model;
		model.getMetamodel().refreshCaches();

		mapping = new TwoWayIdentityHashMap<Instance, EObject>();

		initObjects(model);
		final ResourceSet resourceSet = initResources(model);
		initProperties(model);
		adjustUUIDs(model);

		return resourceSet;
	}

	/** Create an EMF model element for each node. */
	protected void initObjects(Model model) {
		for (final Type type : model.getTypes()) {
			createObjects(type);
		}
	}

	/** Create all EMF model elements of a certain type. */
	protected void createObjects(Type type) {
		final EClass sourceClass = type.getEClass();
		final EClass targetClass = resolveEClass(sourceClass);
		for (final Instance element : type.getInstances()) {
			final EObject eObject = EcoreUtil.create(targetClass);
			if (element.isProxy()) {
				if (isMetamodelProxy(element.getUri())) {
					final URI resolvedProxyUri = resolveMetamodelProxy(element.getUri());
					((InternalEObject) eObject).eSetProxyURI(resolvedProxyUri);
				} else {
					((InternalEObject) eObject).eSetProxyURI(element.getUri());
				}
			}
			mapping.put(element, eObject);
		}
	}

	/**
	 * @return true if the proxyUri points to a meta element in the migrated metamodel (e.g. an EClass of the migrated
	 *         model).
	 */
	protected boolean isMetamodelProxy(URI proxyUri) {
		final MetamodelResource metaRes = getMetamodelResource();
		// Remove fragment and query. The uri proxies to an element in the metamodel if the base URI is the same.
		final URI baseUri = proxyUri.trimFragment().trimQuery();
		return metaRes.getUri().equals(baseUri);
	}

	/**
	 * Proxy urls to meta elements (e.g. EClasses, EStructuralFeatures) that are part of the migrated metamodel point to
	 * the virtual URI of the migration metamodel resource. This URI is no longer valid after the migration finished.
	 * Without additional resolvement this leads to broken proxy URIs in the migrated data.
	 * Thus, it is resolved to the real URI of the meta element in its migrated version.
	 */
	protected URI resolveMetamodelProxy(URI proxyUri) {
		final MetamodelResource metaRes = getMetamodelResource();
		final Resource packagesRes = getSourceResource(metaRes);
		final EObject resolvedMetaObject = packagesRes.getEObject(proxyUri.fragment());

		if (resolvedMetaObject instanceof EClassifier) {
			final EClassifier classifier = (EClassifier) resolvedMetaObject;
			return URI.createURI(String.format("%s#//%s", classifier.getEPackage().getNsURI(), classifier.getName())); //$NON-NLS-1$
		} else if (resolvedMetaObject instanceof EStructuralFeature) {
			final EStructuralFeature feature = (EStructuralFeature) resolvedMetaObject;
			final EClass containingClass = feature.getEContainingClass();
			return URI.createURI(String.format("%s#//%s/%s", containingClass.getEPackage().getNsURI(), //$NON-NLS-1$
				containingClass.getName(), feature.getName()));
		}

		// fallback: original proxy uri. will be broken but migration continues
		return proxyUri;
	}

	/** @return The {@link MetamodelResource} of the migrated metamodel. */
	protected MetamodelResource getMetamodelResource() {
		// The metamodel is always contained in the first resource.
		return model.getMetamodel().getResources().get(0);
	}

	/**
	 * {@link MetamodelResource}s do not actually contain any packages but only reference them. This returns the actual
	 * {@link Resource} containing this MetamodelResource's packages.
	 */
	protected Resource getSourceResource(MetamodelResource metamodelResource) {
		return metamodelResource.getRootPackages().get(0).eResource();
	}

	/** Resolve the class to which an instance should be converted. */
	protected EClass resolveEClass(EClass eClass) {
		return eClass;
	}

	/** Determine root EMF model elements. */
	protected ResourceSet initResources(Model model) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		ResourceUtils.register(model.getMetamodel().getEPackages(),
			resourceSet.getPackageRegistry());
		for (final ModelResource modelResource : model.getResources()) {
			final Resource resource = resourceSet.createResource(modelResource
				.getUri());
			if (resource instanceof XMLResource) {
				final XMLResource xmlResource = (XMLResource) resource;
				if (modelResource.getEncoding() != null) {
					xmlResource.setEncoding(modelResource.getEncoding());
				}
			}
			for (final Instance element : modelResource.getRootInstances()) {
				resource.getContents().add(resolve(element));
			}
		}
		return resourceSet;
	}

	/** Initialize the EMF model elements based on the edges. */
	protected void initProperties(Model model) {
		for (final Type type : model.getTypes()) {
			for (final Instance instance : type.getInstances()) {
				initProperties(instance);
			}
		}
	}

	/** Initialize an EMF model element based on the edges outgoing from a node. */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initProperties(Instance element) {
		final EObject eObject = resolve(element);
		for (final Slot slot : element.getSlots()) {
			final EStructuralFeature sourceFeature = slot.getEFeature();
			final EStructuralFeature targetFeature = resolveFeature(sourceFeature);
			if (ignore(sourceFeature)) {
				continue;
			}
			if (slot instanceof AttributeSlot) {
				if (sourceFeature.getEType() instanceof EEnum) {
					final AttributeSlot attributeSlot = (AttributeSlot) slot;
					if (sourceFeature.isMany()) {
						final EList values = (EList) eObject.eGet(targetFeature);
						for (final Object value : attributeSlot.getValues()) {
							values.add(resolveLiteral(value));
						}
					} else {
						if (!attributeSlot.getValues().isEmpty()) {
							eObject.eSet(targetFeature,
								resolveLiteral(attributeSlot.getValues().get(0)));
						}
					}
				} else {
					eObject.eSet(targetFeature, element.get(sourceFeature));
				}
			} else {
				final ReferenceSlot referenceSlot = (ReferenceSlot) slot;
				if (sourceFeature.isMany()) {
					final EList values = (EList) eObject.eGet(targetFeature);
					int index = 0;
					for (final Instance value : referenceSlot.getValues()) {
						final EObject valueEObject = resolve(value);
						if (sourceFeature.isUnique()
							&& values.contains(valueEObject)) {
							values.move(index, valueEObject);
						} else {
							values.add(index, valueEObject);
						}
						index++;
					}
				} else {
					if (!referenceSlot.getValues().isEmpty()) {
						eObject.eSet(targetFeature, resolve(referenceSlot
							.getValues().get(0)));
					}
				}
			}
		}
	}

	/**
	 * Adjust UUIDs in EMF model.
	 * Needs to be done after containment hierarchy in EMF model is established
	 * (done in {@link #initProperties(Model)}).
	 */
	protected void adjustUUIDs(Model model) {
		for (final Type type : model.getTypes()) {
			for (final Instance instance : type.getInstances()) {
				final String uuid = instance.getUuid();
				if (uuid != null) {
					final EObject eObject = resolve(instance);
					EcoreUtils.setUUID(eObject, uuid);
				}
			}
		}
	}

	/** Resolve the feature to which a value should be transferred. */
	protected EStructuralFeature resolveFeature(EStructuralFeature feature) {
		return feature;
	}

	/** Resolve the literal value of an enumeration. */
	protected Enumerator resolveLiteral(Object literal) {
		if (EEnumLiteral.class.isInstance(literal)) {
			return EEnumLiteral.class.cast(literal);
		} else if (Enumerator.class.isInstance(literal)) {
			return Enumerator.class.cast(literal);
		}
		throw new IllegalArgumentException(MessageFormat.format(
			"Unexpected literal {0} of type {1} cannot be converted to an Enumerator", literal, literal.getClass())); //$NON-NLS-1$
	}

	/**
	 * Determines whether a certain feature should be ignored during conversion.
	 */
	protected boolean ignore(EStructuralFeature feature) {
		return feature.isTransient()
			|| !feature.isChangeable()
			||
			// according to
			// http://www.eclipse.org/newsportal/article.php?id=26780&group=eclipse.tools.emf
			// the following three references need to be ignored
			EcorePackage.eINSTANCE.getEClass_ESuperTypes().equals(feature)
			|| EcorePackage.eINSTANCE.getETypedElement_EType().equals(
				feature)
			|| EcorePackage.eINSTANCE.getEOperation_EExceptions().equals(
				feature);
	}

	/** Get the EMF model element corresponding to a node. */
	protected EObject resolve(Instance instance) {
		return mapping.get(instance);
	}
}
