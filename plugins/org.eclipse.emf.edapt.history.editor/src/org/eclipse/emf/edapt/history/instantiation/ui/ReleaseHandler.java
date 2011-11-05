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
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.instantiation.ReleaseCommand;
import org.eclipse.emf.edapt.history.instantiation.UpdatePackageNamespaceCommand;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
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
		MetamodelExtent extent = listener.getExtent();
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
		boolean ignore = MessageDialog
				.openConfirm(Display.getDefault().getActiveShell(),
						"Metamodel inconsistent",
						"The metamodel is inconsistent. Do you really want to release it?");
		return ignore;
	}

	/** Release the metamodel. */
	private void release(MetamodelExtent extent, EditingDomainListener listener) {
		EditingDomain domain = listener.getEditingDomain();
		if (!isNsURIChanged(extent, listener.getHistory().getLastRelease())) {
			History history = listener.getHistory();
			List<EPackage> rootPackages = history.getRootPackages();
			String source = inferSource(rootPackages);
			ReleaseDialog dialog = new ReleaseDialog(source);
			if (dialog.open() == IDialogConstants.OK_ID) {
				String target = dialog.getTarget();
				if (dialog.isUpdate()) {
					updateNamespaceURI(domain, rootPackages,
							dialog.getSource(), target);
					if (isNsURIChanged(extent, history.getLastRelease())) {
						addRelease(domain, listener, target);
					} else {
						MessageDialog
								.openError(Display.getDefault()
										.getActiveShell(), "Error",
										"Namepaces URIs not fully changed since last release");
					}
				} else {
					addRelease(domain, listener, target);
				}
			}
		} else {
			addRelease(domain, listener, null);
		}
	}

	/** Infer the label to be replaced from the packages. */
	private String inferSource(List<EPackage> ePackages) {
		try {
			String nsURI = ePackages.get(0).getNsURI();
			int index = nsURI.lastIndexOf('/');
			return nsURI.substring(index + 1);
		} catch (RuntimeException e) {
			return "";
		}
	}

	/** Update the namespace URI. */
	private void updateNamespaceURI(EditingDomain domain,
			List<EPackage> rootPackages, String source, String target) {
		Command command = new UpdatePackageNamespaceCommand(rootPackages,
				source, target);
		domain.getCommandStack().execute(command);
	}

	/** Add the new release to the history. */
	private void addRelease(EditingDomain domain,
			EditingDomainListener listener, String target) {
		Command command = new ReleaseCommand(listener, target);
		domain.getCommandStack().execute(command);
	}

	/**
	 * Check whether all namespace URIs have changed w.r.t. to a certain
	 * release.
	 */
	private boolean isNsURIChanged(MetamodelExtent extent, Release release) {
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
				URI.createURI("before"));
		reconstructor.reconstruct(release, true);
		return isNsURIChanged(extent.getRootPackages(), reconstructor);
	}

	/**
	 * Check whether all namespace URIs have changed w.r.t. to a reconstructed
	 * metamodel version.
	 */
	private boolean isNsURIChanged(Collection<EPackage> packages,
			EcoreForwardReconstructor reconstructor) {
		for (EPackage now : packages) {
			EPackage before = (EPackage) reconstructor.getMapping().getTarget(
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
