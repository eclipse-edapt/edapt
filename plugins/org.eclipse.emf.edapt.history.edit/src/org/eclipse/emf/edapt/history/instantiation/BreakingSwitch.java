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
package org.eclipse.emf.edapt.history.instantiation;

import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.util.DiffSwitch;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * A switch to determine which changes are breaking
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class BreakingSwitch extends DiffSwitch<Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean caseAttributeChange(AttributeChange change) {
		EAttribute attribute = change.getAttribute();
		
		// changes to EClass
		if (change.getRightElement() instanceof EClass) {
			EClass target = (EClass) change.getLeftElement();
			EClass source = (EClass) change.getRightElement();
			switch(attribute.getFeatureID()) {
			// abstract: false -> true (unless already interface)
			case EcorePackage.ECLASS__ABSTRACT:
				if(target.isAbstract() && !source.isInterface()) {
					return true;
				}
				break;
			// interface: false -> true (unless already abstract)
			case EcorePackage.ECLASS__INTERFACE:
				if(target.isInterface() && !source.isAbstract()) {
					return true;
				}
				break;
			}
		}
		
		// changes to EStructuralFeature
		if (change.getRightElement() instanceof EStructuralFeature) {
			EStructuralFeature target = (EStructuralFeature) change
					.getLeftElement();
			EStructuralFeature source = (EStructuralFeature) change
					.getRightElement();
			switch(attribute.getFeatureID()) {
			// lowerBound: increasing
			case EcorePackage.ESTRUCTURAL_FEATURE__LOWER_BOUND:
				if(source.getLowerBound() < target.getLowerBound()) {
					return true;
				}
				break;
			// upperBound: decreasing
			case EcorePackage.ESTRUCTURAL_FEATURE__UPPER_BOUND:
				if(source.getUpperBound() > target.getUpperBound() && target.getUpperBound() != -1 ||
						source.getUpperBound() == -1 && target.getUpperBound() != -1) {
					return true;
				}
				break;
			// unique: false -> true
			case EcorePackage.ESTRUCTURAL_FEATURE__UNIQUE:
				if(target.isUnique()) {
					return true;
				}
				break;
			// transient: false -> true
			case EcorePackage.ESTRUCTURAL_FEATURE__TRANSIENT:
				if(target.isTransient()) {
					return true;
				}
				break;
			}
		}
		
		// changes to EReference
		if(change.getLeftElement() instanceof EReference) {
			switch(attribute.getFeatureID()) {
			// containment
			case EcorePackage.EREFERENCE__CONTAINMENT:
				return true;
			}
		}
				
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean caseReferenceChange(ReferenceChange change) {
		EReference reference = change.getReference();
		
		// changes to EClass
		if (change.getRightElement() instanceof EClass) {
			switch(reference.getFeatureID()) {
			// eSuperTypes: remove
			case EcorePackage.ECLASS__ESUPER_TYPES:
				return change instanceof ReferenceChangeRightTarget;
			}
		}
		
		// changes to EStructuralFeature 
		if (change.getRightElement() instanceof EStructuralFeature) {
			switch(reference.getFeatureID()) {
			// eType
			case EcorePackage.ESTRUCTURAL_FEATURE__ETYPE:
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean caseModelElementChangeRightTarget(
			ModelElementChangeRightTarget change) {
		
		if (change.getRightElement() instanceof EPackage) {
			return true;
		}
		if (change.getRightElement() instanceof EClassifier) {
			return true;
		}
		if (change.getRightElement() instanceof EStructuralFeature) {
			return true;
		}
		if (change.getRightElement() instanceof EEnumLiteral) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean caseMoveModelElement(MoveModelElement change) {

		if (change.getRightElement() instanceof EStructuralFeature) {
			return true;
		}
		if (change.getRightElement() instanceof EEnumLiteral) {
			return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean defaultCase(EObject object) {
		return false;
	}
}
