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
package org.eclipse.emf.edapt.declaration.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edapt.declaration.DeclarationPackage;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.edapt.declaration.Parameter} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ParameterItemProvider
	extends IdentifiedElementItemProvider
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
	public ParameterItemProvider(AdapterFactory adapterFactory) {
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

			addRequiredPropertyDescriptor(object);
			addMainPropertyDescriptor(object);
			addManyPropertyDescriptor(object);
			addClassifierPropertyDescriptor(object);
			addClassifierNamePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Required feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addRequiredPropertyDescriptor(Object object) {
		itemPropertyDescriptors
			.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_Parameter_required_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Parameter_required_feature", "_UI_Parameter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DeclarationPackage.Literals.PARAMETER__REQUIRED,
				true,
				false,
				false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null,
				null));
	}

	/**
	 * This adds a property descriptor for the Main feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addMainPropertyDescriptor(Object object) {
		itemPropertyDescriptors
			.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_Parameter_main_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Parameter_main_feature", "_UI_Parameter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DeclarationPackage.Literals.PARAMETER__MAIN,
				true,
				false,
				false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null,
				null));
	}

	/**
	 * This adds a property descriptor for the Many feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addManyPropertyDescriptor(Object object) {
		itemPropertyDescriptors
			.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_Parameter_many_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Parameter_many_feature", "_UI_Parameter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DeclarationPackage.Literals.PARAMETER__MANY,
				true,
				false,
				false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null,
				null));
	}

	/**
	 * This adds a property descriptor for the Classifier feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addClassifierPropertyDescriptor(Object object) {
		itemPropertyDescriptors
			.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_Parameter_classifier_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Parameter_classifier_feature", //$NON-NLS-1$ //$NON-NLS-2$
					"_UI_Parameter_type"), //$NON-NLS-1$
				DeclarationPackage.Literals.PARAMETER__CLASSIFIER,
				true,
				false,
				true,
				null,
				null,
				null));
	}

	/**
	 * This adds a property descriptor for the Classifier Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addClassifierNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors
			.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_Parameter_classifierName_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Parameter_classifierName_feature", //$NON-NLS-1$ //$NON-NLS-2$
					"_UI_Parameter_type"), //$NON-NLS-1$
				DeclarationPackage.Literals.PARAMETER__CLASSIFIER_NAME,
				true,
				false,
				false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null,
				null));
	}

	/**
	 * This returns Parameter.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Parameter")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		final String label = ((Parameter) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_Parameter_type") : //$NON-NLS-1$
			getString("_UI_Parameter_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Parameter.class)) {
		case DeclarationPackage.PARAMETER__REQUIRED:
		case DeclarationPackage.PARAMETER__MAIN:
		case DeclarationPackage.PARAMETER__MANY:
		case DeclarationPackage.PARAMETER__CLASSIFIER_NAME:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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

}
