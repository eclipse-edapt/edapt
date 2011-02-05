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
package org.eclipse.emf.edapt.cope.history.instantiation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.cope.common.MetamodelExtent;
import org.eclipse.emf.edapt.cope.history.Add;
import org.eclipse.emf.edapt.cope.history.Change;
import org.eclipse.emf.edapt.cope.history.CompositeChange;
import org.eclipse.emf.edapt.cope.history.Create;
import org.eclipse.emf.edapt.cope.history.Delete;
import org.eclipse.emf.edapt.cope.history.MigrateableChange;
import org.eclipse.emf.edapt.cope.history.MigrationChange;
import org.eclipse.emf.edapt.cope.history.Move;
import org.eclipse.emf.edapt.cope.history.PrimitiveChange;
import org.eclipse.emf.edapt.cope.history.Remove;
import org.eclipse.emf.edapt.cope.history.Set;
import org.eclipse.emf.edapt.cope.history.ValueChange;
import org.eclipse.emf.edapt.cope.history.reconstruction.EcoreReconstructorSwitchBase;
import org.eclipse.emf.edapt.cope.history.recorder.IChangeProvider;
import org.eclipse.emf.edit.command.ChangeCommand;


/**
 * Command to undo a number of changes
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class UndoChangesCommand extends ChangeCommand implements
		IChangeProvider {

	/**
	 * The changes to be undone
	 */
	private final List<Change> changes;
	
	/**
	 * The switch to undo changes
	 */
	private final UndoSwitch undoSwitch;

	/**
	 * Constructor
	 */
	public UndoChangesCommand(List<Change> changes, MetamodelExtent extent) {
		super(getNotifiers(changes, extent));
		
		this.changes = changes;
		undoSwitch = new UndoSwitch();
	}

	/**
	 * Get all the elements whose content is about to be changed
	 */
	private static Collection<Notifier> getNotifiers(List<Change> changes, MetamodelExtent extent) {
		Collection<Notifier> notifiers = new HashSet<Notifier>();
		notifiers.addAll(extent.getRootPackages());
		for(Change change : changes) {
			notifiers.add(change.getRelease().getHistory());
		}
		return notifiers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		Collections.reverse(changes);
		for(Change change : changes) {
			undoSwitch.doSwitch(change);
			EcoreUtil.delete(change);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Change> getChanges(List<PrimitiveChange> changes) {
		return Collections.emptyList();
	}
	
	/**
	 * Switch to undo changes
	 */
	private class UndoSwitch extends EcoreReconstructorSwitchBase<Object> {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseSet(Set set) {
			set(set.getElement(), set.getFeature(), set.getOldValue());
			return set;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseAdd(Add add) {
			remove(add.getElement(), add.getFeature(), add.getValue());
			return add;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseRemove(Remove remove) {
			add(remove.getElement(), remove.getFeature(), remove.getValue());
			return remove;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseCreate(Create create) {
			delete(create.getElement());
			return create;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseDelete(Delete delete) {
			add(delete.getTarget(), delete.getReference(), delete.getElement());
			for(ValueChange change : delete.getChanges()) {
				doSwitch(change);
			}
			return delete;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseMove(Move move) {
			add(move.getSource(), move.getReference(), move.getElement());
			return move;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseCompositeChange(CompositeChange compositeChange) {
			List<PrimitiveChange> changes = compositeChange.getChanges();
			for(int i = changes.size()-1; i >= 0; i--) {
				doSwitch(changes.get(i));
			}
			return compositeChange;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object caseMigrationChange(MigrationChange migrationChange) {
			List<MigrateableChange> changes = migrationChange.getChanges();
			for(int i = changes.size()-1; i >= 0; i--) {
				doSwitch(changes.get(i));
			}
			return migrationChange;
		}
	}
}
