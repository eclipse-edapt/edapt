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
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edapt.common.ui.IValueValidator;


/**
 * Validator for parameters
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ParameterValueValidator implements IValueValidator {

	/**
	 * Choice of values (null means there is not restriction)
	 */
	@SuppressWarnings("unchecked")
	private final Collection choiceOfValues;

	/**
	 * Type of possible elements
	 */
	private final EClass type;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public ParameterValueValidator(Collection choiceOfValues, EClass type) {
		this.choiceOfValues = choiceOfValues;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPossibleValue(Object element) {
		if (element instanceof EClass
				&& ((EClass) element).getEPackage() == EcorePackage.eINSTANCE) {
			return false;
		}

		if (choiceOfValues != null) {
			return choiceOfValues.contains(element);
		}
		return type.isInstance(element);
	}

}
