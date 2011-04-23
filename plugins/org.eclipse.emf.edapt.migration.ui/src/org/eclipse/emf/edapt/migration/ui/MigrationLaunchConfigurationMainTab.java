/*--------------------------------------------------------------------------+
$Id$
|                                                                          |
| Copyright 2005-2010 Technische Universitaet Muenchen                     |
|                                                                          |
| Licensed under the Apache License, Version 2.0 (the "License");          |
| you may not use this file except in compliance with the License.         |
| You may obtain a copy of the License at                                  |
|                                                                          |
|    http://www.apache.org/licenses/LICENSE-2.0                            |
|                                                                          |
| Unless required by applicable law or agreed to in writing, software      |
| distributed under the License is distributed on an "AS IS" BASIS,        |
| WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. |
| See the License for the specific language governing permissions and      |
| limitations under the License.                                           |
+--------------------------------------------------------------------------*/
package org.eclipse.emf.edapt.migration.ui;

import static org.eclipse.emf.edapt.migration.ui.LaunchUtils.getAttribute;
import static org.eclipse.emf.edapt.migration.ui.MigrationLaunchConfigurationDelegate.AUTOMATIC;
import static org.eclipse.emf.edapt.migration.ui.MigrationLaunchConfigurationDelegate.HISTORY;
import static org.eclipse.emf.edapt.migration.ui.MigrationLaunchConfigurationDelegate.MODELS;
import static org.eclipse.emf.edapt.migration.ui.MigrationLaunchConfigurationDelegate.RELEASE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.execution.ClassLoaderFacade;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

/**
 * Tab to specify the main launch configuration attributes of the migration.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 3F38CE5FD3EE3E6D990141AF90D7B613
 */
class MigrationLaunchConfigurationMainTab extends
		AbstractLaunchConfigurationTab {

	/** Text field to edit the path of the history file. */
	private Text historyText;

	/** Table viewer to display the paths of the model files. */
	private TableViewer modelViewer;

	/** Paths of the model files. */
	private final List<String> modelURIs = new ArrayList<String>();

	/** Check box to toggle automatic detection of the release. */
	private Button autoCheck;

	/** Combo box to select the release. */
	private ComboViewer releaseCombo;

	/** Text field to edit the JVM arguments. */
	private Text vmArgsText;

	/** {@inheritDoc} */
	public void createControl(Composite parent) {
		Composite tabControl = new Composite(parent, SWT.NONE);
		setControl(tabControl);
		tabControl.setLayout(new GridLayout());

		createHistoryGroup(tabControl);
		createModelGroup(tabControl);
		createReleaseGroup(tabControl);
		createVMArgsGroup(tabControl);
	}

	/** Create the group to select the history file. */
	private void createHistoryGroup(Composite parent) {
		Group historyGroup = createGroupControl(parent, "History", 2);
		historyText = new Text(historyGroup, SWT.SINGLE | SWT.BORDER);
		historyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		historyText.addModifyListener(new TextModifyListener());
		createPushButton(historyGroup, "Browse...", null).addSelectionListener(
				new BrowseButtonListener());
	}

	/** Create the group to select the model files. */
	private void createModelGroup(Composite parent) {
		Group modelGroup = createGroupControl(parent, "Models", 2);
		modelViewer = new TableViewer(modelGroup, SWT.MULTI | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 5 * modelViewer.getTable().getItemHeight();
		modelViewer.getTable().setLayoutData(data);
		modelViewer.setContentProvider(new ArrayContentProvider());
		modelViewer.setLabelProvider(new LabelProvider());
		modelViewer.setInput(modelURIs);

		Composite buttonComposite = new Composite(modelGroup, SWT.None);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		buttonComposite.setLayout(layout);
		createPushButton(buttonComposite, "Add...", null).addSelectionListener(
				new AddButtonListener());
		createPushButton(buttonComposite, "Remove", null).addSelectionListener(
				new RemoveButtonListener());
	}

	/** Create the group to select the release to which the models conform. */
	private void createReleaseGroup(Composite parent) {
		Group releaseGroup = createGroupControl(parent, "Release", 2);
		autoCheck = new Button(releaseGroup, SWT.CHECK);
		autoCheck.setText("Auto");
		autoCheck.addSelectionListener(new AutoCheckListener());

		releaseCombo = new ComboViewer(releaseGroup, SWT.None);
		releaseCombo.getCombo().setLayoutData(
				new GridData(GridData.FILL_HORIZONTAL));
		releaseCombo.setContentProvider(new ArrayContentProvider());
		releaseCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				Release release = (Release) element;
				String text = "Release " + release.getNumber();
				if (release.getLabel() != null) {
					text += ": " + release.getLabel();
				}
				return text;
			}
		});
		releaseCombo.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Release r1 = (Release) e1;
				Release r2 = (Release) e2;
				return r1.getNumber() - r2.getNumber();
			}
		});
		releaseCombo.addSelectionChangedListener(new ReleaseComboListener());
	}

	/** Get the releases to which the models may conform. */
	private Set<Release> getReleases() {
		try {
			IFile file = FileUtils.getFile(historyText.getText());
			URI historyURI = URIUtils.getURI(file);
			Migrator migrator = new Migrator(historyURI, new ClassLoaderFacade(
					Thread.currentThread().getContextClassLoader()));
			file = FileUtils.getFile(modelURIs.get(0));
			URI modelURI = URIUtils.getURI(file);
			return migrator.getRelease(modelURI);
		} catch (MigrationException e) {
			return Collections.emptySet();
		}
	}

	/**
	 * Creates group control that contains controls to edit virtual machine
	 * arguments.
	 */
	private void createVMArgsGroup(Composite tabControl) {
		vmArgsText = createMultiLineTextGroup(tabControl, "Java VM Arguments");
	}

	/**
	 * Creates a group control with a multiline text field and returns the text
	 * field. The created text is setup reasonably for this tab, i.e. required
	 * listeners are registered.
	 */
	private Text createMultiLineTextGroup(Composite tabControl, String groupName) {
		Group group = createGroupControl(tabControl, groupName, 1);
		Text text = new Text(group, SWT.MULTI | SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 40)
				.applyTo(text);
		text.addModifyListener(new TextModifyListener());
		return text;
	}

	/** Creates a group control that serves as container for controls */
	private Group createGroupControl(Composite main, String groupName,
			int columns) {
		Group group = new Group(main, SWT.NONE);
		group.setText(groupName);
		group.setLayout(new GridLayout(columns, false));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return group;
	}

	/** {@inheritDoc} */
	public String getName() {
		return "Main";
	}

	/** {@inheritDoc} */
	public void initializeFrom(ILaunchConfiguration configuration) {
		// history
		historyText.setText(getAttribute(configuration, HISTORY, ""));

		// models
		modelURIs.addAll(getAttribute(configuration, MODELS, Collections
				.<String> emptyList()));
		modelViewer.refresh();

		// release
		autoCheck.setSelection(getAttribute(configuration, AUTOMATIC, true));
		if (autoCheck.getSelection()) {
			releaseCombo.getCombo().setEnabled(false);
		} else {
			Set<Release> releases = getReleases();
			releaseCombo.setInput(releases);
			int releaseNumber = getAttribute(configuration, RELEASE, -1);
			if (releaseNumber >= 0) {
				Release release = HistoryUtils.getRelease(releases,
						releaseNumber);
				if (release != null) {
					releaseCombo.setSelection(new StructuredSelection(release));
				}
			}
		}

		// VM arguments
		vmArgsText.setText(getAttribute(configuration,
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""));
	}

	/** {@inheritDoc} */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// history
		String historyFilename = historyText.getText();
		configuration.setAttribute(HISTORY, historyFilename);

		// class path
		try {
			IFile file = FileUtils.getFile(historyFilename);
			if (file != null) {
				IProject project = file.getProject();
				configuration.setAttribute(
						IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
						project.getName());
			}
		} catch (IllegalArgumentException e) {
			// do not set default class path
		}

		// models
		configuration.setAttribute(MODELS, modelURIs);

		// release
		configuration.setAttribute(AUTOMATIC, autoCheck.getSelection());
		if (!autoCheck.getSelection()) {
			Release release = SelectionUtils.getSelectedElement(releaseCombo
					.getSelection());
			if (release != null) {
				configuration.setAttribute(RELEASE, release.getNumber());
			}
		}

		// VM arguments
		configuration.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmArgsText
						.getText());
	}

	/** {@inheritDoc} */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		// history
		String historyFilename = historyText.getText();
		if (historyFilename.length() == 0) {
			setErrorMessage("History not specified");
			return false;
		}

		IFile historyFile = FileUtils.getFile(historyFilename);
		if (!historyFile.exists()) {
			setErrorMessage("History does not exist.");
			return false;
		}

		// models
		if (modelURIs.isEmpty()) {
			setErrorMessage("No model specified");
			return false;
		}

		// release
		if (!autoCheck.getSelection()
				&& SelectionUtils.getSelectedElement(releaseCombo
						.getSelection()) == null) {
			setErrorMessage("Release must be set");
			return false;
		}

		setErrorMessage(null);
		return true;
	}

	/** Listener that updates the dialog after text fields where modified. */
	private class TextModifyListener implements ModifyListener {
		/** {@inheritDoc} */
		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * This listener is attached to the browse button and open a file selection
	 * dialog if activated.
	 */
	private class BrowseButtonListener extends SelectionAdapter {

		/** {@inheritDoc} */
		@Override
		public void widgetSelected(SelectionEvent e) {
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace()
					.getRoot();
			FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(
					getShell(), false, workspaceRoot, IResource.DEPTH_INFINITE
							| IResource.FILE);
			dialog
					.setInitialPattern(("*." + HistoryUtils.HISTORY_FILE_EXTENSION));

			if (dialog.open() == Window.OK) {
				Object result[] = dialog.getResult();
				if (result.length == 1) {
					IFile resource = (IFile) result[0];
					IPath path = resource.getFullPath();
					historyText.setText(path.toString());
				}
			}
		}
	}

	/**
	 * This listener is attached to the add button and open a file selection
	 * dialog if activated.
	 */
	private class AddButtonListener extends SelectionAdapter {

		/** {@inheritDoc} */
		@Override
		public void widgetSelected(SelectionEvent e) {
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace()
					.getRoot();
			FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(
					getShell(), false, workspaceRoot, IResource.DEPTH_INFINITE
							| IResource.FILE);
			dialog.setInitialPattern("*.*");

			if (dialog.open() == Window.OK) {
				Object result[] = dialog.getResult();
				if (result.length == 1) {
					IFile resource = (IFile) result[0];
					IPath path = resource.getFullPath();
					modelURIs.add(path.toString());
					modelViewer.refresh();
					updateLaunchConfigurationDialog();
				}
			}
		}
	}

	/**
	 * This listener is attached to the remove button and removes models if
	 * activated.
	 */
	private class RemoveButtonListener extends SelectionAdapter {
		/** {@inheritDoc} */
		@Override
		public void widgetSelected(SelectionEvent e) {
			List<String> elements = SelectionUtils
					.getSelectedElements(modelViewer.getSelection());
			modelURIs.removeAll(elements);
			modelViewer.refresh();
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * This listener is attached to the auto check box and toggles automatic
	 * detection of the release.
	 */
	private class AutoCheckListener extends SelectionAdapter {
		/** {@inheritDoc} */
		@Override
		public void widgetSelected(SelectionEvent e) {
			releaseCombo.getCombo().setEnabled(!autoCheck.getSelection());
			releaseCombo.setInput(getReleases());
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * This listener is attached to the combo box and toggles an update of the
	 * configuration dialog.
	 */
	private class ReleaseComboListener implements ISelectionChangedListener {
		/** {@inheritDoc} */
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			updateLaunchConfigurationDialog();
		}
	}
}