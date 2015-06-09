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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.history.recorder.ui.CreateHistoryCommand;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.internal.common.URIUtils;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * Wizard to create a history for a set of metamodel resources.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CreateHistoryWizard extends Wizard {

	/** The metamodel editor. */
	private final EcoreEditor editor;

	/** The page to set the location and name of the history file. */
	private HistoryWizardNewFileCreationPage newFileCreationPage;

	/** The page to select the metamodel resources to be recorded. */
	private MetamodelResourceSelectionWizardPage metamodelsResourceSelectionPage;

	/** Constructor. */
	public CreateHistoryWizard(EcoreEditor editor) {
		this.editor = editor;

		setWindowTitle("Create History"); //$NON-NLS-1$
	}

	/** {@inheritDoc} */
	@Override
	public void addPages() {
		newFileCreationPage = new HistoryWizardNewFileCreationPage(editor);
		addPage(newFileCreationPage);

		metamodelsResourceSelectionPage = new MetamodelResourceSelectionWizardPage(
			editor);
		if (metamodelsResourceSelectionPage.isRequired()) {
			addPage(metamodelsResourceSelectionPage);
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean performFinish() {
		final List<Resource> metamodelResources = metamodelsResourceSelectionPage
			.getMetamodelResources();
		final URI historyURI = URIUtils.getURI(newFileCreationPage.getHistoryFile());
		final Command command = new CreateHistoryCommand(editor, metamodelResources,
			historyURI);
		editor.getEditingDomain().getCommandStack().execute(command);
		return true;
	}

	/** Page to set the location and name of the history file. */
	private static class HistoryWizardNewFileCreationPage extends
		WizardNewFileCreationPage {

		/** Constructor. */
		public HistoryWizardNewFileCreationPage(EcoreEditor editor) {
			super("NewHistoryFile", new StructuredSelection(getFile(editor) //$NON-NLS-1$
				.getParent()));

			setFileExtension(HistoryUtils.HISTORY_FILE_EXTENSION);
			setFileName(getFile(editor).getLocation().removeFileExtension()
				.addFileExtension(HistoryUtils.HISTORY_FILE_EXTENSION)
				.lastSegment());
			setTitle("File"); //$NON-NLS-1$
			setDescription("Choose the location and name of the history file."); //$NON-NLS-1$
		}

		/** Get the history file. */
		public IFile getHistoryFile() {
			return ResourcesPlugin.getWorkspace().getRoot().getFile(
				getContainerFullPath().append(getFileName()));
		}

		/** Get the file that is the input of the metamodel editor. */
		private static IFile getFile(EcoreEditor editor) {
			return ((IFileEditorInput) editor.getEditorInput()).getFile();
		}
	}

	/** Page to select the metamodel resources to be recorded. */
	private static class MetamodelResourceSelectionWizardPage extends
		WizardPage {

		/** The primary metamodel resource that needs to be recorded. */
		private Resource mainMetamodelResource;

		/** The other metamodel resources which can be selected for recording. */
		private List<Resource> otherMetamodelResources;

		/** The metamodel editor. */
		private final EcoreEditor editor;

		/** The label provider to display the metamodel resources. */
		private AdapterFactoryLabelProvider labelProvider;

		/** The table viewer to select the metamodel resources. */
		private CheckboxTableViewer tableViewer;

		/** Constructor. */
		public MetamodelResourceSelectionWizardPage(EcoreEditor editor) {
			super("SelectMetamodelResources"); //$NON-NLS-1$
			this.editor = editor;

			setTitle("Metamodel Resources"); //$NON-NLS-1$
			setDescription("Choose the other metamodel resources that should be recorded."); //$NON-NLS-1$
		}

		/** {@inheritDoc} */
		@Override
		public void createControl(Composite parent) {
			tableViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
			labelProvider = new AdapterFactoryLabelProvider(editor
				.getAdapterFactory());
			tableViewer.setLabelProvider(labelProvider);
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setInput(otherMetamodelResources);
			tableViewer.setAllChecked(true);
			setControl(tableViewer.getControl());
		}

		/**
		 * Check whether the selection is required, i.e. there are multiple
		 * possible metamodel resources.
		 */
		public boolean isRequired() {
			EcoreUtil.resolveAll(editor.getEditingDomain().getResourceSet());
			otherMetamodelResources = new ArrayList<Resource>();
			for (final Resource resource : editor.getEditingDomain().getResourceSet()
				.getResources()) {
				if (MetamodelUtils.isMetamodelResource(resource)) {
					if (mainMetamodelResource == null) {
						mainMetamodelResource = resource;
					} else {
						otherMetamodelResources.add(resource);
					}
				}
			}
			return !otherMetamodelResources.isEmpty();
		}

		/** Get the selected metamodel resources. */
		public List<Resource> getMetamodelResources() {
			if (tableViewer == null) {
				return Collections.singletonList(mainMetamodelResource);
			}
			final Object[] results = tableViewer.getCheckedElements();
			final List<Resource> metamodelResources = new ArrayList<Resource>();
			metamodelResources.add(mainMetamodelResource);
			for (final Object result : results) {
				if (result instanceof Resource) {
					metamodelResources.add((Resource) result);
				}
			}
			return metamodelResources;
		}

		/** {@inheritDoc} */
		@Override
		public void dispose() {
			super.dispose();
			labelProvider.dispose();
		}
	}
}
