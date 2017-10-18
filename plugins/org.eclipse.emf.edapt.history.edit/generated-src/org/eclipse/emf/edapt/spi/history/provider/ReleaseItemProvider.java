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
package org.eclipse.emf.edapt.spi.history.provider;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.history.provider.util.RestrictingDragAndDropCommand;
import org.eclipse.emf.edapt.spi.history.Change;
import org.eclipse.emf.edapt.spi.history.CompositeChange;
import org.eclipse.emf.edapt.spi.history.Create;
import org.eclipse.emf.edapt.spi.history.HistoryPackage;
import org.eclipse.emf.edapt.spi.history.OperationChange;
import org.eclipse.emf.edapt.spi.history.Release;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.edapt.spi.history.Release} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ReleaseItemProvider
	extends ItemProviderAdapter
	implements
	IEditingDomainItemProvider,
	IStructuredItemContentProvider,
	ITreeItemContentProvider,
	IItemLabelProvider,
	IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ReleaseItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addDatePropertyDescriptor(object);
			addLabelPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors
			.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_Release_date_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Release_date_feature", "_UI_Release_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				HistoryPackage.Literals.RELEASE__DATE,
				true,
				false,
				false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null,
				null));
	}

	/**
	 * This adds a property descriptor for the Label feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addLabelPropertyDescriptor(Object object) {
		itemPropertyDescriptors
			.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_Release_label_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Release_label_feature", "_UI_Release_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				HistoryPackage.Literals.RELEASE__LABEL,
				true,
				false,
				false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null,
				null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(HistoryPackage.Literals.RELEASE__CHANGES);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	private boolean shouldBeReleased(Release release) {
		if (release == null) {
			return false;
		}
		if (release.getNumber() == 0) {
			/* initial release */
			return false;
		}
		final Date date = release.getDate();
		if (date != null) {
			return false;
		}
		return checkForPotentialBreakingChanges(release.getChanges());
	}

	private boolean checkForPotentialBreakingChanges(List<Change> changes) {
		for (final Change change : changes) {
			if (OperationChange.class.isInstance(change)) {
				if (OperationChange.class.cast(change).getOperation().getOperation().isBreaking()) {
					return true;
				}
			} else if (CompositeChange.class.isInstance(change)) {
				if (checkForPotentialBreakingChanges(
					new ArrayList<Change>(CompositeChange.class.cast(change).getChanges()))) {
					return true;
				}
			} else if (Create.class.isInstance(change)) {
				continue;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * This returns Release.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		if (shouldBeReleased((Release) object)) {
			return overlayImage(object, getResourceLocator().getImage("full/obj16/ReleaseBreaking")); //$NON-NLS-1$
		}
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Release")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		final Release element = (Release) object;
		final DateFormat formatter = DateFormat.getDateTimeInstance();
		final Date date = element.getDate();
		final String dateSegment = " (" //$NON-NLS-1$
			+ (date != null ? formatter.format(date) : "not yet released") //$NON-NLS-1$
			+ (shouldBeReleased(element) ? " and potentially contains breaking changes" : "") //$NON-NLS-1$//$NON-NLS-2$
			+ ")"; //$NON-NLS-1$
		final String labelSegment = element.getLabel() != null ? " " + element //$NON-NLS-1$
			.getLabel() : ""; //$NON-NLS-1$
		return HistoryEditPlugin.INSTANCE.getString("_UI_Release_type") //$NON-NLS-1$
			+ labelSegment + dateSegment;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Release.class)) {
		case HistoryPackage.RELEASE__DATE:
		case HistoryPackage.RELEASE__LABEL:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case HistoryPackage.RELEASE__CHANGES:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return HistoryEditPlugin.INSTANCE;
	}

	@Override
	protected Command createDragAndDropCommand(EditingDomain domain,
		Object owner, float location, int operations, int operation,
		Collection<?> collection) {
		return new RestrictingDragAndDropCommand(domain, owner, location,
			operations, operation, collection) {
			@Override
			protected boolean prepareDropCopyOn() {
				return false;
			}

			@Override
			protected boolean prepareDropCopyInsert(Object parent,
				Collection<?> children, int index) {
				return false;
			}
		};
	}
}
