/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.instantiation.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.history.instantiation.ReleaseCommand;
import org.eclipse.emf.edapt.history.instantiation.UpdatePackageNamespaceCommand;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Action to release an new version.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ReleaseHandler extends EditingDomainListenerHandlerBase {

	/** {@inheritDoc} */
	@Override
	protected Object execute(EditingDomainListener listener, EcoreEditor editor) {
		final MetamodelExtent extent = listener.getExtent();
		if (isConsistent(extent)) {
			release(extent, listener);
		}
		return null;
	}

	/** Check whether the metamodel is consistent before release. */
	private boolean isConsistent(MetamodelExtent extent) {
		if (extent.isConsistent()) {
			return true;
		}
		final boolean ignore = MessageDialog
			.openConfirm(Display.getDefault().getActiveShell(), "Metamodel inconsistent", //$NON-NLS-1$
				"The metamodel is inconsistent. Do you really want to release it?"); //$NON-NLS-1$
		return ignore;
	}

	/** Release the metamodel. */
	private void release(MetamodelExtent extent, EditingDomainListener listener) {
		try {
			final EditingDomain domain = listener.getEditingDomain();
			if (!isNsURIChanged(extent, listener.getHistory().getLastRelease())) {
				final History history = listener.getHistory();
				final List<EPackage> rootPackages = history.getRootPackages();
				final ReleaseWizard releaseWizard = new ReleaseWizard(rootPackages);
				final WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), releaseWizard);
				if (dialog.open() == Window.OK) {
					for (final EPackage ePackage : rootPackages) {
						if (!releaseWizard.updatePackage(ePackage)) {
							continue;
						}
						final String source = releaseWizard.getSource(ePackage);
						final String target = releaseWizard.getTarget(ePackage);
						updateNamespaceURI(domain, Collections.singletonList(ePackage), source, target);
					}
					addRelease(domain, listener, null);
				}
			} else {
				addRelease(domain, listener, null);
			}
		} catch (final InvocationTargetException ex) {
			ErrorDialog.openError(
				Display.getDefault().getActiveShell(),
				"Error during release", //$NON-NLS-1$
				"An error occurred during the release. Did you record all changes?", //$NON-NLS-1$
				LoggingUtils.createMultiStatus(
					HistoryEditorPlugin.getPlugin(),
					IStatus.ERROR,
					"Exception during reconstruction...", //$NON-NLS-1$
					ex.getTargetException()));
		}
	}

	/** Update the namespace URI. */
	private void updateNamespaceURI(EditingDomain domain,
		List<EPackage> rootPackages, String source, String target) {
		final Command command = new UpdatePackageNamespaceCommand(rootPackages,
			source, target);
		domain.getCommandStack().execute(command);
	}

	/** Add the new release to the history. */
	private void addRelease(EditingDomain domain,
		EditingDomainListener listener, String target) {
		final Command command = new ReleaseCommand(listener, target);
		domain.getCommandStack().execute(command);
	}

	/**
	 * Check whether all namespace URIs have changed w.r.t. to a certain
	 * release.
	 *
	 * @throws InvocationTargetException in case the reconstructor throws any exception
	 */
	private boolean isNsURIChanged(MetamodelExtent extent, Release release) throws InvocationTargetException {
		final EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
			URI.createURI("before")); //$NON-NLS-1$
		try {
			reconstructor.reconstruct(release, true);
		} catch (final RuntimeException ex) {
			throw new InvocationTargetException(ex);
		}
		return isNsURIChanged(extent.getRootPackages(), reconstructor);
	}

	/**
	 * Check whether all namespace URIs have changed w.r.t. to a reconstructed
	 * metamodel version.
	 */
	private boolean isNsURIChanged(Collection<EPackage> packages,
		EcoreForwardReconstructor reconstructor) {
		for (final EPackage now : packages) {
			final EPackage before = (EPackage) reconstructor.getMapping().getTarget(
				now);
			if (before != null && now.getNsURI().equals(before.getNsURI())) {
				return false;
			}
			if (!isNsURIChanged(now.getESubpackages(), reconstructor)) {
				return false;
			}
		}
		return true;
	}
}
