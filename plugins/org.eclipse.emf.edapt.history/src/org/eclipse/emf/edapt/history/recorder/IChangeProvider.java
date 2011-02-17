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
package org.eclipse.emf.edapt.history.recorder;

import java.util.List;

import org.eclipse.emf.edapt.history.Change;
import org.eclipse.emf.edapt.history.PrimitiveChange;


/**
 * To indicate that a command is able to assemble a representation of the change it causes
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public interface IChangeProvider {

	/**
	 * Provide a representation of the change it causes
	 * 
	 * @param changes The primitive changes which were recorded
	 * @return Change representation
	 */
	List<Change> getChanges(List<PrimitiveChange> changes);
}
