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
package org.eclipse.emf.edapt.history.reconstruction.ui;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.reconstruction.MigratorCodeGenerator;
import org.eclipse.emf.edapt.history.reconstruction.MigratorProjectGenerator;
import org.eclipse.emf.edapt.history.reconstruction.SpecialMigratorCodeGenerator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * Action to generate a migrator
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class GenerateSpecialMigratorHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		final History history = SelectionUtils.getSelectedElement(selection);
		IFile historyFile = URIUtils.getFile(history.eResource().getURI());
		
		final IProject historyProject = historyFile.getProject();
		
		ResourceDialog dialog = new ResourceDialog(Display.getDefault().getActiveShell(), "Additional Metamodel", SWT.None);
		if(dialog.open() == IDialogConstants.OK_ID) {
			
			final List<URI> addMetamodelURIs = dialog.getURIs();
			
			Job generatorJob = new Job("Generate Migration") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					
					try {
						IProject project = getMigratorProject(historyProject);
						MigratorProjectGenerator generator = new MigratorProjectGenerator(history, project) {
							@Override
							protected MigratorCodeGenerator createMigratorGenerator(IFolder folder) {
								return new SpecialMigratorCodeGenerator(URIUtils.getURI(folder), addMetamodelURIs);
							}
						};
						generator.generate(monitor);
					}
					catch(CoreException e) {
						LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
					}
					catch (IOException e) {
						LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
					}
					finally {
						monitor.done();
					}
					
					return Status.OK_STATUS;
				}
				
			};
			generatorJob.setUser(true);
			generatorJob.schedule();
		}

		return null;
	}

	/** Get the migrator project. */
	private IProject getMigratorProject(final IProject historyProject) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject(historyProject.getName() + ".migrator");
		return project;
	}
}
