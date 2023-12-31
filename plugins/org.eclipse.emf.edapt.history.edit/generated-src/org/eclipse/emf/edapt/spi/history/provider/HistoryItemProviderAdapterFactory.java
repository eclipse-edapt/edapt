/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * BMW Car IT - Initial API and implementation
 * Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.spi.history.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edapt.spi.history.util.HistoryAdapterFactory;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged
 * fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class HistoryItemProviderAdapterFactory extends HistoryAdapterFactory implements ComposeableAdapterFactory,
	IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public HistoryItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.History} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected HistoryItemProvider historyItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.History}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createHistoryAdapter() {
		if (historyItemProvider == null) {
			historyItemProvider = new HistoryItemProvider(this);
		}

		return historyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.Release} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ReleaseItemProvider releaseItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.Release}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createReleaseAdapter() {
		if (releaseItemProvider == null) {
			releaseItemProvider = new ReleaseItemProvider(this);
		}

		return releaseItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.NoChange} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected NoChangeItemProvider noChangeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.NoChange}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createNoChangeAdapter() {
		if (noChangeItemProvider == null) {
			noChangeItemProvider = new NoChangeItemProvider(this);
		}

		return noChangeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.Create} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected CreateItemProvider createItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.Create}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createCreateAdapter() {
		if (createItemProvider == null) {
			createItemProvider = new CreateItemProvider(this);
		}

		return createItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.Move} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MoveItemProvider moveItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.Move}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createMoveAdapter() {
		if (moveItemProvider == null) {
			moveItemProvider = new MoveItemProvider(this);
		}

		return moveItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.Delete} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeleteItemProvider deleteItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.Delete}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createDeleteAdapter() {
		if (deleteItemProvider == null) {
			deleteItemProvider = new DeleteItemProvider(this);
		}

		return deleteItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.Set} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SetItemProvider setItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.Set}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createSetAdapter() {
		if (setItemProvider == null) {
			setItemProvider = new SetItemProvider(this);
		}

		return setItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.Add} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AddItemProvider addItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.Add}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createAddAdapter() {
		if (addItemProvider == null) {
			addItemProvider = new AddItemProvider(this);
		}

		return addItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.Remove} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected RemoveItemProvider removeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.Remove}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createRemoveAdapter() {
		if (removeItemProvider == null) {
			removeItemProvider = new RemoveItemProvider(this);
		}

		return removeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.CompositeChange}
	 * instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected CompositeChangeItemProvider compositeChangeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.CompositeChange}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createCompositeChangeAdapter() {
		if (compositeChangeItemProvider == null) {
			compositeChangeItemProvider = new CompositeChangeItemProvider(this);
		}

		return compositeChangeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.OperationChange}
	 * instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OperationChangeItemProvider operationChangeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.OperationChange}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createOperationChangeAdapter() {
		if (operationChangeItemProvider == null) {
			operationChangeItemProvider = new OperationChangeItemProvider(this);
		}

		return operationChangeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.OperationInstance}
	 * instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OperationInstanceItemProvider operationInstanceItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.OperationInstance}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createOperationInstanceAdapter() {
		if (operationInstanceItemProvider == null) {
			operationInstanceItemProvider = new OperationInstanceItemProvider(this);
		}

		return operationInstanceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.ParameterInstance}
	 * instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ParameterInstanceItemProvider parameterInstanceItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.ParameterInstance}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createParameterInstanceAdapter() {
		if (parameterInstanceItemProvider == null) {
			parameterInstanceItemProvider = new ParameterInstanceItemProvider(this);
		}

		return parameterInstanceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.ModelReference}
	 * instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ModelReferenceItemProvider modelReferenceItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.ModelReference}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createModelReferenceAdapter() {
		if (modelReferenceItemProvider == null) {
			modelReferenceItemProvider = new ModelReferenceItemProvider(this);
		}

		return modelReferenceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.edapt.spi.history.MigrationChange}
	 * instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MigrationChangeItemProvider migrationChangeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.edapt.spi.history.MigrationChange}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createMigrationChangeAdapter() {
		if (migrationChangeItemProvider == null) {
			migrationChangeItemProvider = new MigrationChangeItemProvider(this);
		}

		return migrationChangeItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			final Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || ((Class<?>) type).isInstance(adapter)) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void dispose() {
		if (historyItemProvider != null) {
			historyItemProvider.dispose();
		}
		if (releaseItemProvider != null) {
			releaseItemProvider.dispose();
		}
		if (noChangeItemProvider != null) {
			noChangeItemProvider.dispose();
		}
		if (createItemProvider != null) {
			createItemProvider.dispose();
		}
		if (moveItemProvider != null) {
			moveItemProvider.dispose();
		}
		if (deleteItemProvider != null) {
			deleteItemProvider.dispose();
		}
		if (setItemProvider != null) {
			setItemProvider.dispose();
		}
		if (addItemProvider != null) {
			addItemProvider.dispose();
		}
		if (removeItemProvider != null) {
			removeItemProvider.dispose();
		}
		if (compositeChangeItemProvider != null) {
			compositeChangeItemProvider.dispose();
		}
		if (operationChangeItemProvider != null) {
			operationChangeItemProvider.dispose();
		}
		if (operationInstanceItemProvider != null) {
			operationInstanceItemProvider.dispose();
		}
		if (parameterInstanceItemProvider != null) {
			parameterInstanceItemProvider.dispose();
		}
		if (modelReferenceItemProvider != null) {
			modelReferenceItemProvider.dispose();
		}
		if (migrationChangeItemProvider != null) {
			migrationChangeItemProvider.dispose();
		}
	}

}
