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
package org.eclipse.emf.edapt.history.presentation.action;

import java.util.List;

import org.eclipse.emf.common.ui.dialogs.DiagnosticDialog;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.reconstruction.HistoryValidator;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;


/**
 * Action to perform validation for breaking changes
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ExtendedValidateAction extends ValidateAction {

	/**
	 * Constructor
	 */
	public ExtendedValidateAction() {
		EValidator.Registry.INSTANCE.put(HistoryPackage.eINSTANCE,
				new EValidator.Descriptor() {
					public EValidator getEValidator() {
						return org.eclipse.emf.edapt.spi.history.util.HistoryValidator.INSTANCE;
					}
				});
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("cast")
	@Override
	public void run() {
		EObject element = (EObject) selectedObjects.get(0);
		History history = (History) element.eResource().getContents().get(0);
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(
				history.eResource().getURI());
		reconstructor.addReconstructor(new HistoryValidator());
		reconstructor.reconstruct(history.getLastRelease(), false);
		super.run();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void handleDiagnostic(Diagnostic diagnostic) {
		int severity = diagnostic.getSeverity();
		String title = null;
		String message = null;

		if (severity == Diagnostic.ERROR || severity == Diagnostic.WARNING) {
			title = EMFEditUIPlugin.INSTANCE
					.getString("_UI_ValidationProblems_title");
			message = EMFEditUIPlugin.INSTANCE
					.getString("_UI_ValidationProblems_message");
		} else {
			title = EMFEditUIPlugin.INSTANCE
					.getString("_UI_ValidationResults_title");
			message = EMFEditUIPlugin.INSTANCE
					.getString(severity == Diagnostic.OK ? "_UI_ValidationOK_message"
							: "_UI_ValidationResults_message");
		}

		int result = 0;
		if (diagnostic.getSeverity() == Diagnostic.OK) {
			MessageDialog.openInformation(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), title, message);
			result = Window.CANCEL;
		} else {
			result = DiagnosticDialog.open(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), title, message,
					diagnostic);
		}

		if (eclipseResourcesUtil != null) {
			Resource resource = getResource(diagnostic);
			if (resource != null) {
				eclipseResourcesUtil.deleteMarkers(resource);
			}

			if (result == Window.OK) {
				if (!diagnostic.getChildren().isEmpty()) {
					List<?> data = (diagnostic.getChildren().get(0)).getData();
					if (!data.isEmpty() && data.get(0) instanceof EObject) {
						Object part = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage()
								.getActivePart();
						if (part instanceof ISetSelectionTarget) {
							((ISetSelectionTarget) part)
									.selectReveal(new StructuredSelection(data
											.get(0)));
						} else if (part instanceof IViewerProvider) {
							Viewer viewer = ((IViewerProvider) part)
									.getViewer();
							if (viewer != null) {
								viewer.setSelection(new StructuredSelection(
										data.get(0)), true);
							}
						}
					}
				}

				for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
					Resource childResource = getResource(diagnostic);
					if (childResource != null) {
						eclipseResourcesUtil.createMarkers(childResource, childDiagnostic);
					}
				}
			}
		}
	}

	/**
	 * Get the resource for a diagnostic
	 */
	private Resource getResource(Diagnostic diagnostic) {
		List<?> data = diagnostic.getData();
		if (data.size() > 0) {
			if (data.get(0) instanceof EObject) {
				EObject element = (EObject) data.get(0);
				return element.eResource();
			}
		}
		return null;
	}
}
