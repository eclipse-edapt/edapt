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
package org.eclipse.emf.edapt.history.recorder;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Stack;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.internal.common.LoggingUtils;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.internal.common.MetamodelUtils;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.History;
import org.eclipse.emf.edapt.spi.history.provider.HistoryEditPlugin;

/**
 * Listens to command stack of an Ecore editor and collects changes to current
 * metamodel release.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class CommandStackListener implements
	org.eclipse.emf.common.command.CommandStackListener {

	/** Command stack of Ecore editor. */
	private final CommandStack commandStack;

	/**
	 * EMF change recorder to record metamodel changes during the execution of a
	 * command.
	 */
	private ChangeRecorder metamodelRecorder;

	/**
	 * EMF change recorder to record history changes during the execution of a
	 * command (to be immediately undone so that the history is not affected by
	 * a delete command).
	 */
	private HistoryChangeRecorder historyRecorder;

	/** Flag to indicate whether listener is active or not. */
	private boolean listening;

	/**
	 * Stack saving the number of changes which need to be removed in case of
	 * undoing a command.
	 */
	private final Stack<Integer> numberChanges;

	/** Metamodel extent. */
	private MetamodelExtent extent;

	/** The history resource. */
	private final Resource historyResource;

	/** Constructor. */
	public CommandStackListener(CommandStack commandStack,
		Resource historyResource) {
		this.commandStack = commandStack;
		numberChanges = new Stack<Integer>();
		listening = false;
		this.historyResource = historyResource;
	}

	/** Activate listener. */
	public void beginListening() {
		if (!isListening()) {
			commandStack.addCommandStackListener(this);
			extent = new MetamodelExtent(MetamodelUtils
				.getAllRootPackages(historyResource.getResourceSet()));

			metamodelRecorder = new ChangeRecorder(getHistoryRootPackages());
			historyRecorder = new HistoryChangeRecorder(getHistory());

			listening = true;
		} else {
			throw new IllegalStateException("Listener already activated"); //$NON-NLS-1$
		}
	}

	/** Deactivate listener. */
	public void endListening() {
		if (isListening()) {
			commandStack.removeCommandStackListener(this);
			commandStack.flush();
			numberChanges.clear();
			extent.dispose();

			metamodelRecorder.endRecording();
			historyRecorder.endRecording();

			listening = false;
		} else {
			throw new IllegalStateException("Listener already deactivated"); //$NON-NLS-1$
		}
	}

	/** Returns listening. */
	public boolean isListening() {
		return listening;
	}

	/** Reset the recorder if it no longer works on the real metamodel. */
	public void resetRecorder() {
		if (!checkRecorder()) {
			metamodelRecorder.endRecording();
			historyRecorder.endRecording();

			metamodelRecorder = new ChangeRecorder(getHistoryRootPackages());
			EcoreUtil.resolveAll(historyResource);
			historyRecorder = new HistoryChangeRecorder(getHistory());

			extent.setRootPackages(MetamodelUtils
				.getAllRootPackages(historyResource.getResourceSet()));

			LoggingUtils.logInfo(HistoryEditPlugin.getPlugin(),
				"Recorder got out of sync and was safely restarted."); //$NON-NLS-1$
		}
	}

	/**
	 * Check whether the recorder still operates on the real metamodel, and is
	 * not detached due to the save mechanism which reloads the metamodel.
	 */
	private boolean checkRecorder() {
		if (metamodelRecorder == null || metamodelRecorder.getElements().isEmpty()) {
			return false;
		}
		return !metamodelRecorder.getElements().get(0).eIsProxy();
	}

	/** {@inheritDoc} */
	@Override
	public void commandStackChanged(EventObject event) {
		final Command command = commandStack.getMostRecentCommand();
		process(command);
		extent.clearExtentMap();
	}

	/** Process a command. */
	@SuppressWarnings("unchecked")
	private void process(Command command) {

		metamodelRecorder.endRecording();
		historyRecorder.endRecording();

		if (!checkRecorder()) {
			LoggingUtils.logError(HistoryEditPlugin.getPlugin(),
				"Recorder no longer working"); //$NON-NLS-1$
		}

		if (command != null) {
			// command is undone
			if (isUndo(command)) {
				pop();
			} else {
				final CompositeChange changeContainer = metamodelRecorder
					.getChanges();
				// command able to provide a representation of the change
				if (command instanceof IChangeProvider) {
					final IChangeProvider changeProvider = (IChangeProvider) command;
					final List<Change> changes = changeProvider
						.getChanges(changeContainer.getChanges());
					push(changes);
				}
				// command not able to provide a representation of the
				// change
				else {
					push((List) changeContainer.getChanges());
				}
			}
		}

		metamodelRecorder = new ChangeRecorder(getHistoryRootPackages());
		historyRecorder = new HistoryChangeRecorder(getHistory());
	}

	/** Check whether a change of the command stack reflects an undone command. */
	private boolean isUndo(Command command) {
		return command == commandStack.getRedoCommand();
	}

	/** Remove last sequence of changes in order to cope with an undone command. */
	private void pop() {
		final int number = numberChanges.pop();
		if (number == 0) {
			return;
		}
		final List<Change> changes = getHistory().getLastRelease().getChanges();
		final List<Change> changesToRemove = new ArrayList<Change>();
		final int size = changes.size();
		for (int i = size - number; i < size; i++) {
			changesToRemove.add(changes.get(i));
		}
		changes.removeAll(changesToRemove);
	}

	/** Append changes grouped by metamodels to the current releases. */
	private void push(List<Change> changes) {
		numberChanges.push(changes.size());
		getHistory().getLastRelease().getChanges().addAll(changes);
	}

	/** Get the history that is listened to. */
	public History getHistory() {
		final History history = (History) historyResource.getContents().get(0);
		return history;
	}

	/** Get all root packages which are listened to. */
	public List<EPackage> getHistoryRootPackages() {
		return getHistory().getRootPackages();
	}

	/** Returns extent. */
	MetamodelExtent getExtent() {
		return extent;
	}
}
