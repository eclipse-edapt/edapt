/*******************************************************************************
 * Copyright (c) 2007-2015 BMW Car IT, TUM, EclipseSource Muenchen GmbH, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.wizard.Wizard;

/**
 * {@link Wizard} to update the ns uris of multiple packages.
 *
 * @author jfaltermeier
 *
 */
public class ReleaseWizard extends Wizard {

	private final Map<EPackage, ReleaseWizardPage> pages = new LinkedHashMap<EPackage, ReleaseWizardPage>();

	private final Map<EPackage, Boolean> updateMap = new LinkedHashMap<EPackage, Boolean>();
	private final Map<EPackage, String> sourceMap = new LinkedHashMap<EPackage, String>();
	private final Map<EPackage, String> targetMap = new LinkedHashMap<EPackage, String>();

	private final List<EPackage> rootPackages;

	public ReleaseWizard(List<EPackage> rootPackages) {
		if (rootPackages == null || rootPackages.isEmpty()) {
			throw new IllegalArgumentException("There must be at least one root package."); //$NON-NLS-1$
		}
		this.rootPackages = rootPackages;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#getWindowTitle()
	 */
	@Override
	public String getWindowTitle() {
		return "Create Release"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		for (final EPackage ePackage : rootPackages) {
			final String source = inferSource(ePackage);
			pages
				.put(
					ePackage,
					new ReleaseWizardPage("Update namespace URI of package " + ePackage.getNsURI(), //$NON-NLS-1$
						"Enter the label to replace and the target label or deselect the update button", //$NON-NLS-1$
						null,
						source));
			addPage(pages.get(ePackage));
		}
	}

	/** Infer the label to be replaced from the package. */
	private String inferSource(EPackage ePackage) {
		try {
			final String nsURI = ePackage.getNsURI();
			final int index = nsURI.lastIndexOf('/');
			return nsURI.substring(index + 1);
		} catch (final RuntimeException e) {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Whether the ns uri of the given EPackage should be updated.
	 */
	public boolean updatePackage(EPackage ePackage) {
		if (!updateMap.containsKey(ePackage)) {
			return false;
		}
		return updateMap.get(ePackage);
	}

	/**
	 * Returns the source label to replace.
	 */
	public String getSource(EPackage ePackage) {
		return sourceMap.get(ePackage);
	}

	/**
	 * Returns the target label which replaces the source label.
	 */
	public String getTarget(EPackage ePackage) {
		return targetMap.get(ePackage);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		for (final Entry<EPackage, ReleaseWizardPage> entry : pages.entrySet()) {
			updateMap.put(entry.getKey(), entry.getValue().isUpdate());
			if (!entry.getValue().isUpdate()) {
				continue;
			}
			sourceMap.put(entry.getKey(), entry.getValue().getSource());
			targetMap.put(entry.getKey(), entry.getValue().getTarget());
		}
		pages.clear();
		return true;
	}

}
