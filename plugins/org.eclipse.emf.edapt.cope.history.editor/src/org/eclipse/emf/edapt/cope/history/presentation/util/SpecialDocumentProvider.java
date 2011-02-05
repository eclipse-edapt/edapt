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
package org.eclipse.emf.edapt.cope.history.presentation.util;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.editor.GroovyPartitionScanner;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edapt.cope.history.MigrationChange;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.StorageDocumentProvider;



/**
 * Special document provider for {@link MigrationChange}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class SpecialDocumentProvider extends StorageDocumentProvider {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element,
			IDocument document, boolean overwrite) {
		
		((SpecialEditorInput) element).setMigration(document.get());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setupDocument(Object element, IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			IDocumentPartitioner partitioner= new FastPartitioner(GroovyPlugin.getDefault().getGroovyPartitionScanner(), GroovyPartitionScanner.GROOVY_PARTITION_TYPES);
			extension3.setDocumentPartitioner(GroovyPlugin.GROOVY_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
	}
}
