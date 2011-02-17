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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;

/**
 * An extended selection dialog for multi-valued features
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ExtendedFeatureEditorDialog extends AbstractElementListSelectionDialog
{
	/**
	 * Table view
	 */
	private TableViewer featureTableViewer;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void handleDefaultSelected() {
		addSelectedElements();
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param labelProvider
	 * @param object
	 * @param eClassifier
	 * @param currentValues
	 * @param displayName
	 * @param choiceOfValues
	 */
    @SuppressWarnings("unchecked")
	public ExtendedFeatureEditorDialog(Shell parent, ILabelProvider labelProvider, Object object, EClassifier eClassifier, List currentValues, String displayName, Collection choiceOfValues)
    {
        super(parent, labelProvider);
        listValues = new ArrayList();
        setShellStyle(getShellStyle() | 0x10 | 0x400);
        this.labelProvider = labelProvider;
        this.object = object;
        this.eClassifier = eClassifier;
        this.displayName = displayName;
        this.choiceOfValues = choiceOfValues;
        org.eclipse.emf.common.notify.AdapterFactory adapterFactory = new ComposedAdapterFactory(Collections.EMPTY_LIST);
        values = new ItemProvider(adapterFactory, currentValues);
        contentProvider = new AdapterFactoryContentProvider(adapterFactory);
    }

    /**
     * Constructor
     * 
     * @param parent
     * @param labelProvider
     * @param eObject
     * @param eStructuralFeature
     * @param displayName
     * @param choiceOfValues
     */
    @SuppressWarnings("unchecked")
	public ExtendedFeatureEditorDialog(Shell parent, ILabelProvider labelProvider, EObject eObject, EStructuralFeature eStructuralFeature, String displayName, Collection choiceOfValues)
    {
        this(parent, labelProvider, eObject, eStructuralFeature.getEType(), (List)eObject.eGet(eStructuralFeature), displayName, choiceOfValues);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void computeResult()
    {
        setResult(values.getChildren());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        shell.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_FeatureEditorDialog_title", new Object[] {
            displayName, labelProvider.getText(object)
        }));
        shell.setImage(labelProvider.getImage(object));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite contents = (Composite)super.createDialogArea(parent);
        GridLayout contentsGridLayout = (GridLayout)contents.getLayout();
        contentsGridLayout.numColumns = 3;
        GridData contentsGridData = (GridData)contents.getLayoutData();
        contentsGridData.horizontalAlignment = 4;
        contentsGridData.verticalAlignment = 4;
        Composite choiceComposite = new Composite(contents, 0);
        GridData data = new GridData(4, 4, true, true);
        data.horizontalAlignment = 0x1000008;
        choiceComposite.setLayoutData(data);
        GridLayout layout = new GridLayout();
        data.horizontalAlignment = 4;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 1;
        choiceComposite.setLayout(layout);
        Label choiceLabel = createMessageArea(choiceComposite);
        choiceLabel.setText("Select the features to add (? = any character, * = any String)");
        createFilterText(choiceComposite);
        createFilteredList(choiceComposite);
        fFilteredList.setElements(choiceOfValues.toArray());
        Composite controlButtons = new Composite(contents, 0);
        GridData controlButtonsGridData = new GridData();
        controlButtonsGridData.verticalAlignment = 4;
        controlButtonsGridData.horizontalAlignment = 4;
        controlButtons.setLayoutData(controlButtonsGridData);
        GridLayout controlsButtonGridLayout = new GridLayout();
        controlButtons.setLayout(controlsButtonGridLayout);
        new Label(controlButtons, 0);
        Button addButton = new Button(controlButtons, 8);
        addButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Add_label"));
        GridData addButtonGridData = new GridData();
        addButtonGridData.verticalAlignment = 4;
        addButtonGridData.horizontalAlignment = 4;
        addButton.setLayoutData(addButtonGridData);
        Button removeButton = new Button(controlButtons, 8);
        removeButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Remove_label"));
        GridData removeButtonGridData = new GridData();
        removeButtonGridData.verticalAlignment = 4;
        removeButtonGridData.horizontalAlignment = 4;
        removeButton.setLayoutData(removeButtonGridData);
        Label spaceLabel = new Label(controlButtons, 0);
        GridData spaceLabelGridData = new GridData();
        spaceLabelGridData.verticalSpan = 2;
        spaceLabel.setLayoutData(spaceLabelGridData);
        Button upButton = new Button(controlButtons, 8);
        upButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Up_label"));
        GridData upButtonGridData = new GridData();
        upButtonGridData.verticalAlignment = 4;
        upButtonGridData.horizontalAlignment = 4;
        upButton.setLayoutData(upButtonGridData);
        Button downButton = new Button(controlButtons, 8);
        downButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Down_label"));
        GridData downButtonGridData = new GridData();
        downButtonGridData.verticalAlignment = 4;
        downButtonGridData.horizontalAlignment = 4;
        downButton.setLayoutData(downButtonGridData);
        Composite featureComposite = new Composite(contents, 0);
        data = new GridData(4, 4, true, true);
        data.horizontalAlignment = 0x1000008;
        featureComposite.setLayoutData(data);
        layout = new GridLayout();
        data.horizontalAlignment = 4;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 1;
        featureComposite.setLayout(layout);
        Label featureLabel = new Label(featureComposite, 0);
        featureLabel.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Feature_label"));
        GridData featureLabelGridData = new GridData();
        featureLabelGridData.horizontalSpan = 2;
        featureLabelGridData.horizontalAlignment = 4;
        featureLabelGridData.verticalAlignment = 4;
        featureLabel.setLayoutData(featureLabelGridData);
        Table featureTable = new Table(featureComposite, 2050);
        GridData featureTableGridData = new GridData();
        featureTableGridData.widthHint = Display.getCurrent().getBounds().width / 5;
        featureTableGridData.heightHint = Display.getCurrent().getBounds().height / 3;
        featureTableGridData.verticalAlignment = 4;
        featureTableGridData.horizontalAlignment = 4;
        featureTableGridData.grabExcessHorizontalSpace = true;
        featureTableGridData.grabExcessVerticalSpace = true;
        featureTable.setLayoutData(featureTableGridData);
        featureTableViewer = new TableViewer(featureTable);
        featureTableViewer.setContentProvider(contentProvider);
        featureTableViewer.setLabelProvider(labelProvider);
        featureTableViewer.setInput(values);
        if(!values.getChildren().isEmpty())
            featureTableViewer.setSelection(new StructuredSelection(values.getChildren().get(0)));
        upButton.addSelectionListener(new SelectionAdapter(){
            @Override
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event)
            {
                IStructuredSelection selection = (IStructuredSelection) featureTableViewer.getSelection();
                int minIndex = 0;
                Object value;
                int index;
                for(Iterator i = selection.iterator(); i.hasNext(); values.getChildren().move(Math.max(index - 1, minIndex++), value))
                {
                    value = i.next();
                    index = values.getChildren().indexOf(value);
                }

            }
        });
        downButton.addSelectionListener(new SelectionAdapter(){
            @Override
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event)
            {
                IStructuredSelection selection = (IStructuredSelection) featureTableViewer.getSelection();
                int maxIndex = values.getChildren().size() - selection.size();
                Object value;
                int index;
                for(Iterator i = selection.iterator(); i.hasNext(); values.getChildren().move(Math.min(index + 1, maxIndex++), value))
                {
                    value = i.next();
                    index = values.getChildren().indexOf(value);
                }

            }
        });
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent event)
            {
                addSelectedElements();
            }
        });
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent event)
            {
                removeSelectedElements();
            }

        });
        
        featureTable.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				removeSelectedElements();
			}
        });
        
        return contents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean close()
    {
        contentProvider.dispose();
        return super.close();
    }

    /**
     * Add the selected elements
     */
    private void addSelectedElements() {
		if(getSelectedElements() != null)
		{
		    Object selection[] = getSelectedElements();
		    Object aobj[] = selection;
		    int i = 0;
		    for(int j = aobj.length; i < j; i++)
		    {
		        Object item = aobj[i];
		        if(!values.getChildren().contains(item))
		            values.getChildren().add(item);
		    }

		}
		if(getOkButton() != null)
		    getOkButton().setEnabled(true);
	}

    /**
     * Remove the selected elements
     */
	@SuppressWarnings("unchecked")
	private void removeSelectedElements() {
		IStructuredSelection selection = (IStructuredSelection) featureTableViewer.getSelection();
		Object firstValue = null;
		Object value;
		for(Iterator i = selection.iterator(); i.hasNext(); values.getChildren().remove(value))
		{
		    value = i.next();
		    if(firstValue == null)
		        firstValue = value;
		}

		if(!values.getChildren().isEmpty())
		    featureTableViewer.setSelection(new StructuredSelection(values.getChildren().get(0)));
		if(getOkButton() != null)
		    getOkButton().setEnabled(true);
	}

	/**
	 * Values of the list
	 */
	@SuppressWarnings("unchecked")
	protected List listValues;
	
	/**
	 * Label provider
	 */
    protected ILabelProvider labelProvider;
    
    /**
     * Content provider
     */
    protected IContentProvider contentProvider;
    
    /**
     *
     */
    protected Object object;
    
    /**
     *
     */
    protected EClassifier eClassifier;
    
    /**
     *
     */
    protected String displayName;
    
    /**
     *
     */
    protected ItemProvider values;
    
    /**
     *
     */
    @SuppressWarnings("unchecked")
	protected Collection choiceOfValues;

}
