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
package org.eclipse.emf.edapt.cope.migration.ui;

import org.eclipse.emf.edapt.cope.migration.Instance;
import org.eclipse.emf.edapt.cope.migration.Model;
import org.eclipse.emf.edapt.cope.migration.execution.IDebugger;

/**
 * Debugger that shows the state of the model to the user
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class InteractiveDebugger implements IDebugger {

	/**
	 * {@inheritDoc}
	 */
	public void debug(Instance instance, String message) {
		
		Model model = instance.getType().getModel();
		model.setReflection(true);
		
		DebugDialog dialog = new DebugDialog(instance, message);
		dialog.open();
		
		model.setReflection(false);
	}

}
