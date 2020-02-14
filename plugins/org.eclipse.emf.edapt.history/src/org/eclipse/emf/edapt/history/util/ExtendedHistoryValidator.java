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
package org.eclipse.emf.edapt.history.util;

import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.util.HistoryValidator;

/**
 * An extended validator for a history that takes breaking changes into account
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ExtendedHistoryValidator extends HistoryValidator {

	/**
	 * Instance of validator
	 */
	public static final ExtendedHistoryValidator INSTANCE = new ExtendedHistoryValidator();

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean validateChange_Breaking(Change change, DiagnosticChain diagnostics, Map context) {

		if (change.isBreaking()) {
			if (diagnostics != null) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
					DIAGNOSTIC_SOURCE,
					0,
					EcorePlugin.INSTANCE.getString("_UI_GenericConstraint_diagnostic", new Object[] { "Breaking", //$NON-NLS-1$ //$NON-NLS-2$
						getObjectLabel(change, context) }),
					new Object[] { change }));
			}
			return false;
		}
		return true;
	}
}
