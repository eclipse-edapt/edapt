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

import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.BACKUP;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.HISTORY;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.MODELS;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.SOURCE_RELEASE;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.TARGET_RELEASE;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.VALIDATION_LEVEL;
import static org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption.VM_ARGUMENTS;
import static org.eclipse.emf.edapt.migration.ui.LaunchUtils.getAttribute;

import java.util.ArrayList;
import java.util.Collection;
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
import org.eclipse.emf.edapt.common.StringUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.history.util.HistoryUtils;
import org.eclipse.emf.edapt.migration.execution.ClassLoaderFacade;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.MigratorCommandLineOption;
import org.eclipse.emf.edapt.migration.execution.ValidationLevel;
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

	/** Check box to toggle automatic detection of the target release. */
	private Button sourceReleaseCheck;

	/** Combo box to select the source release. */
	private ComboViewer sourceReleaseCombo;

	/** Check box to toggle automatic detection of the target release. */
	private Button targetReleaseCheck;

	/** Combo box to select the target release. */
	private ComboViewer targetReleaseCombo;

	/** Combo box to set the validation level. */
	private ComboViewer validationCombo;

	/** Text field to edit the JVM arguments. */
	private Text vmArgsText;

	/** Migrator for current history. */
	private Migrator migrator;

	private Button backupCheck;

	/** {@inheritDoc} */
	public void createControl(Composite parent) {
		Composite tabControl = new Composite(parent, SWT.NONE);
		setControl(tabControl);
		tabControl.setLayout(new GridLayout());

		createHistoryGroup(tabControl);
		createModelGroup(tabControl);
		createSourceReleaseGroup(tabControl);
		createTargetReleaseCombo(tabControl);
		createValidationGroup(tabControl);
		createBackupGroup(tabControl);
		createVMArgsGroup(tabControl);
	}

	/** Create the group to select the history file. */
	private void createHistoryGroup(Composite parent) {
		Group historyGroup = createGroupControl(parent, HISTORY, 2);
		historyText = new Text(historyGroup, SWT.SINGLE | SWT.BORDER);
		historyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		historyText.addModifyListener(new HistoryTextModifyListener());
		createPushButton(historyGroup, "Browse...", null).addSelectionListener(
				new BrowseButtonListener());
	}

	/** Create the group to select the model files. */
	private void createModelGroup(Composite parent) {
		Group modelGroup = createGroupControl(parent, MODELS, 2);
		modelViewer = new TableViewer(modelGroup, SWT.MULTI | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 3 * modelViewer.getTable().getItemHeight();
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

	/** Create the group to select the source release of the migration. */
	private void createSourceReleaseGroup(Composite parent) {
		Group releaseGroup = createGroupControl(parent, SOURCE_RELEASE, 2);
		sourceReleaseCheck = createAutoButton(releaseGroup);
		sourceReleaseCombo = createReleaseCombo(releaseGroup);
		sourceReleaseCombo.setContentProvider(new ArrayContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				return getSourceReleases().toArray();
			}
		});
		sourceReleaseCheck
				.addSelectionListener(new SourceReleaseCheckListener());
	}

	/** Create the group to select the target release of the migration. */
	private void createTargetReleaseCombo(Composite parent) {
		Group releaseGroup = createGroupControl(parent, TARGET_RELEASE, 2);
		targetReleaseCheck = createAutoButton(releaseGroup);
		targetReleaseCombo = createReleaseCombo(releaseGroup);
		targetReleaseCombo.setContentProvider(new ArrayContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				return getTargetReleases().toArray();
			}
		});
		targetReleaseCheck
				.addSelectionListener(new TargetReleaseCheckListener());
	}

	/** Create the button to set the automatic detection of a release. */
	private Button createAutoButton(Composite parent) {
		return createCheckButton(parent, "Auto");
	}

	/** Create a combo box to select a release. */
	private ComboViewer createReleaseCombo(Composite composite) {
		ComboViewer releaseCombo = new ComboViewer(composite);
		releaseCombo.getCombo().setLayoutData(
				new GridData(GridData.FILL_HORIZONTAL));
		releaseCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				Release release = (Release) element;
				String text = "Release " + release.getNumber();
				String label = release.getLabel();
				if (label != null && label.length() > 0) {
					text += ": " + label;
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
		releaseCombo.addSelectionChangedListener(new ComboListener());
		return releaseCombo;
	}

	/** Create the group to set the level of the validation. */
	private void createValidationGroup(Composite parent) {
		Group validationGroup = createGroupControl(parent, VALIDATION_LEVEL, 1);

		validationCombo = new ComboViewer(validationGroup);
		validationCombo.getCombo().setLayoutData(
				new GridData(GridData.FILL_HORIZONTAL));
		validationCombo.setContentProvider(new ArrayContentProvider());
		validationCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				ValidationLevel level = (ValidationLevel) element;
				return StringUtils.upperCamelCaseToText(level.name());
			}
		});
		validationCombo.setInput(ValidationLevel.values());
		validationCombo.addSelectionChangedListener(new ComboListener());
	}

	/** Get the source releases to which the models may conform. */
	private Set<Release> getSourceReleases() {
		initMigrator();
		if (migrator != null && !modelURIs.isEmpty()) {
			IFile file = FileUtils.getFile(modelURIs.get(0));
			URI modelURI = URIUtils.getURI(file);
			return migrator.getRelease(modelURI);
		}
		return Collections.emptySet();
	}

	/** Get the possible target releases. */
	private Collection<Release> getTargetReleases() {
		initMigrator();
		if (migrator != null) {
			return migrator.getReleases();
		}
		return Collections.emptySet();
	}

	/** Initialize the migrator for the current history. */
	private void initMigrator() {
		if (migrator == null) {
			try {
				IFile file = FileUtils.getFile(historyText.getText());
				URI historyURI = URIUtils.getURI(file);
				migrator = new Migrator(historyURI, new ClassLoaderFacade(
						Thread.currentThread().getContextClassLoader()));
			} catch (Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Create the group to set whether a backup should be created before
	 * migration.
	 */
	public void createBackupGroup(Composite parent) {
		Group group = createGroupControl(parent, BACKUP, 1);
		backupCheck = createCheckButton(group,
				"Create a backup of the models before migration");
	}

	/**
	 * Creates group control that contains controls to edit virtual machine
	 * arguments.
	 */
	private void createVMArgsGroup(Composite parent) {
		vmArgsText = createMultiLineTextGroup(parent, VM_ARGUMENTS);
	}

	/**
	 * Creates a group control with a multiline text field and returns the text
	 * field. The created text is setup reasonably for this tab, i.e. required
	 * listeners are registered.
	 */
	private Text createMultiLineTextGroup(Composite parent,
			MigratorCommandLineOption option) {
		Group group = createGroupControl(parent, option, 1);
		Text text = new Text(group, SWT.MULTI | SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 40)
				.applyTo(text);
		text.addModifyListener(new TextModifyListener());
		return text;
	}

	/** Creates a group control that serves as container for controls */
	private Group createGroupControl(Composite main,
			MigratorCommandLineOption option, int columns) {
		Group group = new Group(main, SWT.NONE);
		group.setText(StringUtils.upperCamelCaseToText(option.name()));
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
		migrator = null;

		int sourceReleaseNumber = getAttribute(configuration,
				SOURCE_RELEASE.id(), -1);
		sourceReleaseCheck.setSelection(sourceReleaseNumber == -1);
		int targetReleaseNumber = getAttribute(configuration,
				TARGET_RELEASE.id(), -1);
		targetReleaseCheck.setSelection(targetReleaseNumber == -1);

		// history
		historyText.setText(getAttribute(configuration, HISTORY.id(), ""));

		// models
		modelURIs.clear();
		modelURIs.addAll(getAttribute(configuration, MODELS.id(),
				Collections.<String> emptyList()));
		modelViewer.refresh();

		// release
		initializeReleaseCombo(sourceReleaseCheck, sourceReleaseCombo,
				sourceReleaseNumber, SOURCE_RELEASE);
		initializeReleaseCombo(targetReleaseCheck, targetReleaseCombo,
				targetReleaseNumber, TARGET_RELEASE);

		// validation
		String validation = getAttribute(configuration, VALIDATION_LEVEL.id(),
				ValidationLevel.CUSTOM_MIGRATION.toString());
		validationCombo.setSelection(new StructuredSelection(ValidationLevel
				.valueOf(validation)));

		// backup
		boolean backup = getAttribute(configuration, BACKUP.id(), false);
		backupCheck.setSelection(backup);

		// VM arguments
		vmArgsText.setText(getAttribute(configuration,
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""));
	}

	/** Initialize the combo viewer to select a release. */
	private void initializeReleaseCombo(Button check, ComboViewer combo,
			int releaseNumber, MigratorCommandLineOption option) {
		combo.getCombo().setEnabled(!check.getSelection());
		if (check.getSelection()) {
			combo.setSelection(StructuredSelection.EMPTY);
		} else {
			combo.setInput(new Object());
			if (releaseNumber >= 0) {
				Collection<Release> releases = option == SOURCE_RELEASE ? getSourceReleases()
						: getTargetReleases();
				Release release = HistoryUtils.getRelease(releases,
						releaseNumber);
				if (release != null) {
					combo.setSelection(new StructuredSelection(release));
				}
			}
		}
	}

	/** {@inheritDoc} */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// history
		String historyFilename = historyText.getText();
		configuration.setAttribute(HISTORY.id(), historyFilename);

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
		configuration.setAttribute(MODELS.id(),
				new ArrayList<String>(modelURIs));

		// release
		saveRelease(configuration, sourceReleaseCheck, sourceReleaseCombo,
				SOURCE_RELEASE);
		saveRelease(configuration, targetReleaseCheck, targetReleaseCombo,
				TARGET_RELEASE);

		// validation
		ValidationLevel level = SelectionUtils
				.getSelectedElement(validationCombo.getSelection());
		configuration.setAttribute(VALIDATION_LEVEL.id(), level.toString());

		// backup
		boolean backup = backupCheck.getSelection();
		configuration.setAttribute(BACKUP.id(), backup);

		// VM arguments
		configuration.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
				vmArgsText.getText());
	}

	/** Save the value of a release to the launch configuration. */
	private void saveRelease(ILaunchConfigurationWorkingCopy configuration,
			Button check, ComboViewer combo, MigratorCommandLineOption option) {
		if (check.getSelection()) {
			configuration.setAttribute(option.id(), -1);
		} else {
			Release release = SelectionUtils.getSelectedElement(combo
					.getSelection());
			if (release != null) {
				configuration.setAttribute(option.id(), release.getNumber());
			}
		}
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
		for (String modelURI : modelURIs) {
			IFile modelFile = FileUtils.getFile(modelURI);
			if (!modelFile.exists()) {
				setErrorMessage("Model does not exist.");
				return false;
			}
		}

		// release
		if (!isValidRelease(sourceReleaseCheck, sourceReleaseCombo,
				SOURCE_RELEASE)
				|| !isValidRelease(targetReleaseCheck, targetReleaseCombo,
						TARGET_RELEASE)) {
			return false;
		}

		setErrorMessage(null);
		return true;
	}

	/** Check whether a release is set. */
	private boolean isValidRelease(Button check, ComboViewer combo,
			MigratorCommandLineOption option) {
		if (!check.getSelection()
				&& SelectionUtils.getSelectedElement(combo.getSelection()) == null) {
			setErrorMessage(StringUtils.upperCamelCaseToText(option.name())
					+ " must be set");
			return false;
		}
		return true;
	}

	/** Refresh the combo viewer to select the source release. */
	private void refreshSourceReleaseCombo() {
		refreshReleaseCombo(sourceReleaseCheck, sourceReleaseCombo);
	}

	/** Refresh the combo viewer to select the target release. */
	private void refreshTargetReleaseCombo() {
		refreshReleaseCombo(targetReleaseCheck, targetReleaseCombo);
	}

	/**
	 * Refresh a release combo viewer, depending on whether it is enabled or
	 * not.
	 */
	private void refreshReleaseCombo(Button check, ComboViewer combo) {
		if (check.getSelection()) {
			combo.setSelection(StructuredSelection.EMPTY);
		} else {
			combo.setInput(new Object());
		}
	}

	/** Listener that updates the dialog after text fields were modified. */
	private class TextModifyListener implements ModifyListener {
		/** {@inheritDoc} */
		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * Listener that updates the dialog after the text field showing the history
	 * is modified.
	 */
	private class HistoryTextModifyListener extends TextModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			migrator = null;
			refreshSourceReleaseCombo();
			refreshTargetReleaseCombo();
			super.modifyText(e);
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
			dialog.setInitialPattern(("*." + HistoryUtils.HISTORY_FILE_EXTENSION));

			if (dialog.open() == Window.OK) {
				Object result[] = dialog.getResult();
				if (result.length == 1) {
					IFile resource = (IFile) result[0];
					IPath path = resource.getFullPath();
					historyText.setText(path.toString());
					migrator = null;
					refreshSourceReleaseCombo();
					refreshTargetReleaseCombo();
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
					if (modelURIs.size() == 1) {
						refreshSourceReleaseCombo();
					}
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
			String modelURI = modelURIs.get(0);
			modelURIs.removeAll(elements);
			modelViewer.refresh();
			if (elements.contains(modelURI)) {
				refreshSourceReleaseCombo();
			}
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * This listener is attached to the auto check box and toggles automatic
	 * detection of the release.
	 */
	private class SourceReleaseCheckListener extends SelectionAdapter {

		/** {@inheritDoc} */
		@Override
		public void widgetSelected(SelectionEvent e) {
			sourceReleaseCombo.getCombo().setEnabled(
					!sourceReleaseCheck.getSelection());
			refreshSourceReleaseCombo();
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * This listener is attached to the auto check box and toggles automatic
	 * detection of the release.
	 */
	private class TargetReleaseCheckListener extends SelectionAdapter {

		/** {@inheritDoc} */
		@Override
		public void widgetSelected(SelectionEvent e) {
			targetReleaseCombo.getCombo().setEnabled(
					!targetReleaseCheck.getSelection());
			refreshTargetReleaseCombo();
			updateLaunchConfigurationDialog();
		}
	}

	/**
	 * This listener is attached to the combo box and toggles an update of the
	 * configuration dialog.
	 */
	private class ComboListener implements ISelectionChangedListener {
		/** {@inheritDoc} */
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			updateLaunchConfigurationDialog();
		}
	}
}