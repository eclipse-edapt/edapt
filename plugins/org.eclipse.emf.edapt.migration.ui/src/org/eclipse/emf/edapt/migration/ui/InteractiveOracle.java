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
package org.eclipse.emf.edapt.migration.ui;

import java.util.List;

import org.eclipse.emf.edapt.migration.IOracle;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Model;

/**
 * Oracle that asks the user
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class InteractiveOracle implements IOracle {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <V> V choose(Instance instance, List<V> values, String message) {

		Model model = instance.getType().getModel();
		model.setReflection(true);
		
		ChoiceDialog dialog = new ChoiceDialog(instance, values, message);
		dialog.open();
		
		model.setReflection(false);
		
		return (V) dialog.getSelectedElement();
	}

}
