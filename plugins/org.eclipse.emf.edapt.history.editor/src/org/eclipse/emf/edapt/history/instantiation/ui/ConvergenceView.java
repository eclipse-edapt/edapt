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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;

// CB Migrate
//import org.eclipse.emf.compare.diff.merge.EMFCompareEObjectCopier;
//import org.eclipse.emf.compare.diff.merge.service.MergeService;
//import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
//import org.eclipse.emf.compare.diff.metamodel.DiffElement;
//import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
//import org.eclipse.emf.compare.diff.metamodel.DiffModel;
//import org.eclipse.emf.compare.diff.service.DiffService;
//import org.eclipse.emf.compare.match.metamodel.Match2Elements;
//import org.eclipse.emf.compare.match.metamodel.MatchModel;
//import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.ui.ModelSash;
import org.eclipse.emf.edapt.common.ui.PartAdapter;
import org.eclipse.emf.edapt.common.ui.StructureTreeViewer;
import org.eclipse.emf.edapt.history.instantiation.BreakingSwitch;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.history.reconstruction.DiffModelFilterUtils;
import org.eclipse.emf.edapt.history.reconstruction.DiffModelOrderFilter;
import org.eclipse.emf.edapt.history.reconstruction.DiffModelResourceFilter;
import org.eclipse.emf.edapt.history.reconstruction.IDiffModelFilter;
import org.eclipse.emf.edapt.history.reconstruction.Mapping;
import org.eclipse.emf.edapt.history.reconstruction.ModelAssert;
import org.eclipse.emf.edapt.history.reconstruction.ui.DiffSelectionAdapter;
import org.eclipse.emf.edapt.history.recorder.ui.EcoreEditorDetector;
import org.eclipse.emf.edapt.spi.history.HistoryFactory;
import org.eclipse.emf.edapt.spi.history.NoChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorDecorator;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


/**
 * View to support the convergence of a source metamodel to a target metamodel
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ConvergenceView extends ViewPart implements CommandStackListener,
		ISelectionChangedListener {

	/**
	 * Identifier of the view for convenience
	 */
	public static final String ID = ConvergenceView.class.getName();
	
	/**
	 * Viewer for the difference model
	 */
	private ModelSash differenceSash;

	/**
	 * Viewer for the target metamodel
	 */
	private ModelSash targetSash;

	/**
	 * Editor with the source metamodel
	 */
	private EcoreEditor editor;

	/**
	 * Resource containing the source metamodel
	 */
	private Resource sourceResource;

	/**
	 * Resource containing the target metamodel
	 */
	private Resource targetResource;

	/**
	 * Adapter to listen to the difference viewer that automatically updates
	 * source and target metamodel viewer
	 */
	private DiffSelectionAdapter selectionAdapter;
	
	/**
	 * Mapping between source and target metamodel
	 */
	private Mapping mapping;
	
	/**
	 * Flag whether selection is currently going on (in order to avoid {@link StackOverflowError})
	 */
	private boolean selecting = false;

	/**
	 * Set of breaking changes
	 */
	
	// CB Migrate. 
//	private Set<DiffElement> breakingChanges;

	/**
	 * Whether synchronization is turned on or off
	 */
	private boolean synchronization = true;

	/**
	 * Whether the view is currently refreshing
	 */
	private boolean refresh;

	/** Check whether the editor is closed. */
	private final IPartListener partListener = new PartAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void partClosed(IWorkbenchPart part) {
			if (editor == part) {
				differenceSash.getStructureViewer().setInput(null);
				targetSash.getStructureViewer().setInput(null);

				detachOldEditor();
			}
		}

	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPartControl(Composite parent) {
		SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		
		differenceSash = new ModelSash(sash, SWT.None);
		StructureTreeViewer differenceViewer = differenceSash.getStructureViewer();
		differenceViewer.setLabelProvider(new DecoratingLabelProvider((ILabelProvider) differenceViewer.getLabelProvider(), new Decorator()));
		
		targetSash = new ModelSash(sash, SWT.None);
		targetSash.getStructureViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if(selecting) return;
				if(event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					if(selection.getFirstElement() instanceof EObject) {
						EObject element = (EObject) selection.getFirstElement();
						EObject source = mapping.getSource(element);
						if(source != null) {
							selecting = true;
							editor.setSelectionToViewer(Collections.singleton(source));
							selecting = false;
						}
					}
				}
			}
			
		});
		
		differenceViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if(event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					
					System.out.println("DifferenViewer doubleclick, selection" + selection);
					// CB Migrate
					// Execute a change command on the editor editing domain. 
					
//					if(selection.getFirstElement() instanceof DiffElement) {
//						final DiffElement element = (DiffElement) selection.getFirstElement();
//						
//						editor.getEditingDomain().getCommandStack().execute(new ChangeCommand(editor.getEditingDomain().getResourceSet()) {
//
//							@Override
//							protected void doExecute() {
//										fixCopier();
//										MergeService.merge(element, true);
//							}
//							
//						});
//					}
				}
			}
		});
		
		getSite().getPage().addPartListener(partListener);
		
		getViewSite().getActionBars().getToolBarManager().add(
				new Action("Sync", IAction.AS_CHECK_BOX) {

					{
						setChecked(true);
						setImageDescriptor(PlatformUI.getWorkbench()
								.getSharedImages().getImageDescriptor(
										ISharedImages.IMG_ELCL_SYNCED));
						setDisabledImageDescriptor(PlatformUI.getWorkbench()
								.getSharedImages().getImageDescriptor(
										ISharedImages.IMG_ELCL_SYNCED_DISABLED));
					}

					@Override
					public void run() {
						setSynchronization(isChecked());
					}
				});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		if(!differenceSash.isDisposed()) {
			differenceSash.setFocus();
		}
	}

	/**
	 * Initialize the contents of the view
	 */
	public void init(URI targetURI, EcoreEditor editor) {
		detachOldEditor();		
		attachNewEditor(targetURI, editor);
	}

	/**
	 * Detach the view from the old editor
	 */
	private void detachOldEditor() {
		if(this.editor != null) {
			this.editor.getEditingDomain().getCommandStack().removeCommandStackListener(this);
			this.editor.removeSelectionChangedListener(this);
			if(!differenceSash.getStructureViewer().getTree().isDisposed()) {
				differenceSash.getStructureViewer().getTree().removeSelectionListener(selectionAdapter);
			}
			this.editor = null;
			
			// CB Migrate
//			breakingChanges = null;
		}
	}

	/**
	 * Attach the view to the new editor
	 */
	private void attachNewEditor(URI targetURI, EcoreEditor editor) {
		try {
			sourceResource = editor.getEditingDomain().getResourceSet().getResources().get(0);
			
			ResourceSet resourceSet = ResourceUtils.loadResourceSet(targetURI);
			targetResource = resourceSet.getResources().get(0);
			
			this.editor = editor;
			this.editor.getEditingDomain().getCommandStack().addCommandStackListener(this);
			selectionAdapter = new DiffSelectionAdapter(editor.getViewer(), null, targetSash.getStructureViewer(), targetSash.getPropertiesViewer()) {
				@Override
				public void widgetSelected(SelectionEvent e) {
					selecting = true;
					super.widgetSelected(e);
					selecting = false;
				}
			};
			differenceSash.getStructureViewer().getTree().addSelectionListener(selectionAdapter);
			this.editor.addSelectionChangedListener(this);
			
			refresh();
			targetSash.getStructureViewer().setInput(targetResource);
			targetSash.getStructureViewer().expandToLevel(2);
			
		} catch (IOException e) {
			LoggingUtils
					.logError(HistoryEditorPlugin.getPlugin(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void commandStackChanged(EventObject event) {
		if(!refresh && synchronization) {
			refresh();
		}
	}

	/**
	 * Refresh viewer contents
	 */
	private void refresh() {
//		try {
			refresh = true;
				
			
			// CB Migrate, we need a comparison snapshot here. 
			
			
//			ComparisonResourceSnapshot snapshot = compare();

//			if (ModelAssert.numberOfChanges(snapshot.getDiff()) == 0) {
//				if(targetResource != null) {
//					URI targetURI = targetResource.getURI();
//					Integer number = extractNumber(targetURI);
//					if(number != null) {
//						addMarker(number+1);
//						String name = targetURI.lastSegment().replace(number.toString(), "" + (number+1));
//						try {
//							targetURI = targetURI.trimSegments(1).appendSegment(name);
//							
//							ResourceSet resourceSet;
//							resourceSet = ResourceUtils.loadResourceSet(targetURI);
//							targetResource = resourceSet.getResources().get(0);
//							
//							targetSash.getStructureViewer().setInput(targetResource);
//							targetSash.getStructureViewer().expandToLevel(2);
//
//							snapshot = compare();
//						} catch (IOException e) {
//							// ignore
//						}
//					}
//				}
//			}

//			initMapping(snapshot.getMatch());
//			calculateBreaking(snapshot.getDiff());

//			differenceSash.getStructureViewer().setInput(snapshot.getDiff());
			
			
			
			differenceSash.getStructureViewer().expandToLevel(3);
			
//		} catch (InterruptedException e) {
//			LoggingUtils
//					.logError(HistoryEditorPlugin.getPlugin(), e);
//		} finally {
//			refresh = false;
//		}
	}
	
	
	/** Compare source and target resource. */
// CB Migrate
// Returns a comparison snapshot. 
	
	
//	private ComparisonResourceSnapshot compare() throws InterruptedException {
//		ComparisonResourceSnapshot snapshot = DiffFactory.eINSTANCE
//				.createComparisonResourceSnapshot();
//
//		MatchModel match = MatchService.doResourceMatch(targetResource,
//				sourceResource, null);
//		DiffModel diff = DiffService.doDiff(match);
//		IDiffModelFilter filter = DiffModelFilterUtils
//				.and(DiffModelOrderFilter.INSTANCE,
//						DiffModelResourceFilter.INSTANCE);
//		DiffModelFilterUtils.filter(diff, filter);
//
//		snapshot.setMatch(match);
//		snapshot.setDiff(diff);
//
//		return snapshot;
//
//	}

	/**
	 * Add a marker with the current revision number to the history
	 */
	private void addMarker(final Integer number) {
		final Release release = EcoreEditorDetector.getInstance().getListener(
				editor).getHistory().getLastRelease();
		Command command = new ChangeCommand(release) {
			@Override
			protected void doExecute() {
				NoChange marker = HistoryFactory.eINSTANCE.createNoChange();
				marker.setDescription(number.toString());
				release.getChanges().add(marker);
			}
		};
		editor.getEditingDomain().getCommandStack().execute(command);
	}

	/**
	 * Initialize the mapping
	 */
	
	
	// CB Migrate
//	private void initMapping(MatchModel match) {
//		mapping = new Mapping();
//		for(Iterator<EObject> i = match.eAllContents(); i.hasNext(); ) {
//			EObject element = i.next();
//			if(element instanceof Match2Elements) {
//				Match2Elements match2Elements = (Match2Elements) element;
//				mapping.map(match2Elements.getRightElement(), match2Elements
//						.getLeftElement());
//			}
//		}
//		selectionAdapter.setMapping(mapping);
//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		getSite().getPage().removePartListener(partListener);
		detachOldEditor();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		if(selecting) return;
		if(event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			if(selection.getFirstElement() instanceof EObject) {
				EObject element = (EObject) selection.getFirstElement();
				EObject target = mapping.getTarget(element);
				if(target != null) {
					selecting = true;
					targetSash.getStructureViewer().setSelection(new StructuredSelection(target), true);
					selecting = false;
				}
			}
		}
	}

	/**
	 * Calculate the breaking changes
	 */
	
	// CB Migrate
//	public void calculateBreaking(DiffModel model) {
//
//		breakingChanges = new HashSet<DiffElement>();
//		BreakingSwitch s = new BreakingSwitch();
//		
//		for(Iterator<EObject> i = model.eAllContents(); i.hasNext(); ) {
//			EObject eObject = i.next();
//			if(eObject instanceof DiffElement) {
//				DiffElement element = (DiffElement) eObject;
//				boolean breaking = s.doSwitch(element);
//				if(breaking) {
//					breakingChanges.add(element);
//				}
//			}
//		}
//		
//		for(DiffElement change : new HashSet<DiffElement>(breakingChanges)) {
//			while(change.eContainer() != null && change.eContainer() instanceof DiffElement) {
//				change = (DiffElement) change.eContainer();
//				breakingChanges.add(change);
//			}
//		}
//	}
	
	/**
	 * Extract the revision number from the URI
	 * 
	 * @param targetURI
	 * @return Revision number
	 */
	private Integer extractNumber(URI targetURI) {
		String name = targetURI.trimFileExtension().lastSegment();
		int index = name.indexOf('_');
		if(index >= 0) {
			name = name.substring(index+1);
			index = name.indexOf('.');
			if(index >= 0) {
				name = name.substring(index+1);
				try {
					return Integer.parseInt(name);
				}
				catch(NumberFormatException e) {
					return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * Enable or disable synchronization
	 * 
	 * @param synchronization
	 */
	private void setSynchronization(boolean synchronization) {
		this.synchronization = synchronization;
		if(synchronization) {
			refresh();
		}
	}
	
	/**
	 * @return Widget for difference representation
	 */
	public ModelSash getDifferenceSash() {
		return differenceSash;
	}

	/**
	 * Fix the copier of EMF Compare
	 */
	
	// CB Migrate
//	private void fixCopier() {
//		try {
//			DiffModel diff = (DiffModel) differenceSash.getStructureViewer()
//					.getInput();
//			Field field = MergeService.class
//					.getDeclaredField("copier");
//			field.setAccessible(true);
//				field.set(null, new EMFCompareEObjectCopier(diff) {
//					@Override
//					public EObject get(Object key) {
//					EObject value = super.get(key);
//					if (value == null) {
//						if (key instanceof EDataType) {
//							EDataType type = (EDataType) key;
//							EPackage ePackage = type.getEPackage();
//							if (ePackage == EcorePackage.eINSTANCE) {
//								value = type;
//							}
//						} else if (key instanceof EObject) {
//							value = mapping.getSource((EObject) key);
//							}
//					}
//						return value;
//									}
//				});
//		} catch (SecurityException e) {
//			// ignore
//		} catch (NoSuchFieldException e) {
//			// ignore
//		} catch (IllegalArgumentException e) {
//			// ignore
//		} catch (IllegalAccessException e) {
//			// ignore
//		}
//	}

	/**
	 * Decorator for breaking changes
	 * 
	 * @author herrmama
	 * @author $Author$
	 * @version $Rev$
	 * @levd.rating RED Rev:
	 */
	public class Decorator implements ILabelDecorator, IColorDecorator {
		
		/**
		 * Color for breaking changes
		 */
		private final Color red = new Color(Display.getDefault(), 255, 0, 0);
		
		/**
		 * {@inheritDoc}
		 */
		public Color decorateBackground(Object element) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Color decorateForeground(Object element) {
			
// CB Migrate			
//			if(breakingChanges.contains(element)) {
//				return red;
//			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Image decorateImage(Image image, Object element) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String decorateText(String text, Object element) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public void addListener(ILabelProviderListener listener) {
			// not required
		}

		/**
		 * {@inheritDoc}
		 */
		public void dispose() {
			red.dispose();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public void removeListener(ILabelProviderListener listener) {
			// not required
		}
	}
}
