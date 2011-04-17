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
package org.eclipse.emf.edapt.tests.migration;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.migration.BackwardConverter;
import org.eclipse.emf.edapt.migration.ForwardConverter;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ModelResource;
import org.eclipse.emf.edapt.migration.Persistency;
import org.eclipse.emf.edapt.migration.BackupUtils.URIMapper;

/**
 * Tests for {@link ForwardConverter} and {@link BackwardConverter}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ConverterTest extends TestCase {
	
	/**
	 * Mapping between two models
	 */
	private Map<EObject, EObject> mapping;

	/**
	 * Test whether both converters maintain the order of multi-valued references
	 */
	public void testOrder() throws IOException, MigrationException {
		
		URI contextURI = URIUtils.getURI("data/node");
		URI metamodelURI = contextURI.appendSegment("node.ecore");
		URI model1URI = contextURI.appendSegment("split.xmi");
		URI model2URI = contextURI.appendSegment("split_2.xmi");
		
		Metamodel metamodel = Persistency.loadMetamodel(metamodelURI);
		Model model = Persistency.loadModel(model1URI, metamodel);	
		model.validate();

		URIMapper mapper = new URIMapper() {

			public URI map(URI uri) {
				String name = uri.trimFileExtension().lastSegment() + "_2";
				String extension = uri.fileExtension();
				return uri.trimSegments(1).appendSegment(name).appendFileExtension(extension);
			}
			
		};

		for(ModelResource resource : model.getResources()) {
			resource.setUri(mapper.map(resource.getUri()));
		}
		
		Persistency.saveModel(model);		
		
		Resource model1 = ResourceUtils.loadResourceSet(model1URI, metamodel.getEPackages()).getResources().get(0);
		Resource model2 = ResourceUtils.loadResourceSet(model2URI, metamodel.getEPackages()).getResources().get(0);
		
		EObject root1 = model1.getContents().get(0);
		EObject root2 = model2.getContents().get(0);
		
		checkOrder(root1, root2);
	}

	/**
	 * Check whether the order is maintained
	 */
	private void checkOrder(EObject root1, EObject root2) {
		mapping = new IdentityHashMap<EObject, EObject>();		
		checkContainment(root1, root2);
		checkCrossReference(root1);
	}

	/**
	 * Check the order for containment references
	 */
	@SuppressWarnings("unchecked")
	private void checkContainment(EObject element1, EObject element2) {
		
		Assert.assertNotSame(element1, element2);
		
		EClass type = element1.eClass();
		
		EStructuralFeature feature = type.getEStructuralFeature("name");
		Assert.assertEquals(element1.eGet(feature), element2.eGet(feature));
		
		Assert.assertNull(mapping.put(element1, element2));
		
		for(EReference containment : type.getEAllContainments()) {
			if(containment.isMany()) {
				List<EObject> list1 = ((List<EObject>) element1.eGet(containment));
				List<EObject> list2 = ((List<EObject>) element2.eGet(containment));
				
				Assert.assertEquals(list1.size(), list2.size());
				
				for(int i = 0, n = list1.size(); i < n; i++) {
					EObject child1 = list1.get(i);
					EObject child2 = list2.get(i);					
					checkContainment(child1, child2);
				}
			}
			else {
				EObject child1 = (EObject) element1.eGet(containment);
				EObject child2 = (EObject) element2.eGet(containment);
				
				Assert.assertTrue(child1 != null && child2 != null || child1 == null && child2 == null);
				
				if(child1 != null) {
					checkContainment(child1, child2);
				}
			}
		}
	}

	/**
	 * Check the order for cross references
	 */
	@SuppressWarnings("unchecked")
	private void checkCrossReference(EObject element1) {
		EObject element2 = mapping.get(element1);
		
		EClass type = element1.eClass();
		EStructuralFeature feature = type.getEStructuralFeature("name");
		Assert.assertEquals(element1.eGet(feature), element2.eGet(feature));
		
		for(EReference reference : type.getEAllReferences()) {
			if(reference.isContainment()) {
				continue;
			}
			
			if(reference.isMany()) {
				List<EObject> list1 = ((List<EObject>) element1.eGet(reference));
				List<EObject> list2 = ((List<EObject>) element2.eGet(reference));
				
				Assert.assertEquals(list1.size(), list2.size());
				
				for(int i = 0, n = list1.size(); i < n; i++) {
					EObject target1 = list1.get(i);
					EObject target2 = list2.get(i);					
					Assert.assertSame(mapping.get(target1), target2);
				}
			}
			else {
				EObject target1 = (EObject) element1.eGet(reference);
				EObject target2 = (EObject) element2.eGet(reference);
				
				Assert.assertSame(mapping.get(target1), target2);
			}
		}
		
		for(EObject child1 : element1.eContents()) {
			checkCrossReference(child1);
		}
	}
}
