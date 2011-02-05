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
package org.eclipse.emf.edapt.cope.declaration.parser.ui;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.cope.common.LoggingUtils;
import org.eclipse.emf.edapt.cope.common.ResourceUtils;
import org.eclipse.emf.edapt.cope.common.URIUtils;
import org.eclipse.emf.edapt.cope.common.ui.SelectionUtils;
import org.eclipse.emf.edapt.cope.declaration.DeclarationPlugin;
import org.eclipse.emf.edapt.cope.declaration.Library;
import org.eclipse.emf.edapt.cope.declaration.parser.OperationParser;
import org.eclipse.emf.edapt.cope.declaration.presentation.DeclarationEditor;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * Command to parse a declaration from Groovy files. As parameter, either a
 * single file can be provided or a folder in which groovy files are contained.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class ExtractDeclarationHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IResource resource = SelectionUtils.getSelectedElement(selection);

		if (resource instanceof IFile) {
			extractFromSingleFile((IFile) resource);
		} else if (resource instanceof IFolder) {
			extractFromFilesInFolder((IFolder) resource);
		}

		return null;
	}

	/** Extract the declaration of operations from a single file. */
	private void extractFromSingleFile(IFile file) {
		try {
			URI declarationURI = extractDeclaration(file);
			showDeclaration(declarationURI);
		} catch (Exception e) {
			LoggingUtils.logError(DeclarationPlugin.getPlugin(), e);
		}
	}

	/** Extract the declaration of operations from a Groovy file. */
	protected URI extractDeclaration(IFile groovyFile)
			throws RecognitionException, TokenStreamException, CoreException,
			IOException {
		URI groovyURI = URIUtils.getURI(groovyFile);
		URI declarationURI = groovyURI.trimFileExtension().appendFileExtension(
				"declaration");

		OperationParser parser = new OperationParser();
		Library library = parser.parse(groovyFile.getContents());
		library.setImplementation(groovyURI.deresolve(declarationURI)
				.toFileString());

		ResourceUtils.saveElement(declarationURI, library);
		return declarationURI;
	}

	/** Open an editor to display the declaration of operations. */
	protected void showDeclaration(URI declarationURI) throws PartInitException {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		DeclarationEditor editor = (DeclarationEditor) activePage.openEditor(
				new URIEditorInput(declarationURI),
				"org.eclipse.emf.edapt.cope.declaration.presentation.DeclarationEditorID");
		if (editor.getViewer() instanceof TreeViewer) {
			((TreeViewer) editor.getViewer()).expandToLevel(3);
		}
		ValidateAction validate = new ValidateAction();
		validate.updateSelection(new StructuredSelection(editor
				.getEditingDomain().getResourceSet().getResources().get(0)
				.getContents()));
		validate.setActiveWorkbenchPart(editor);
	}

	/**
	 * Extract declaration of operations from all groovy files contained in a
	 * folder.
	 */
	private void extractFromFilesInFolder(final IFolder folder) {
		Job job = new Job("Extract Declaration") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					GroovyFileCounter counter = new GroovyFileCounter();
					folder.accept(counter);
					monitor.beginTask("Extract Declaration", counter.getSize());
					GroovyFileExtractor extractor = new GroovyFileExtractor(
							monitor);
					folder.accept(extractor);
					return Status.OK_STATUS;
				} catch (CoreException e) {
					return LoggingUtils.createStatus(DeclarationPlugin
							.getPlugin(), IStatus.ERROR, IStatus.OK,
							"Error while extracting declarations", e);
				} finally {
					monitor.done();
				}
			}
		};
		job.schedule();
	}

	/** Resource visitor that counts Groovy files. */
	private class GroovyFileCounter extends GroovyFileVisitor {

		/** Number of found Groovy files. */
		private int size = 0;

		/** {@inheritDoc} */
		@Override
		protected void visit(IFile groovyFile) {
			size++;
		}

		/** Return the number of found Groovy files. */
		int getSize() {
			return size;
		}
	}

	/** Resource visitor that extracts Groovy files. */
	private class GroovyFileExtractor extends GroovyFileVisitor {

		/** Monitor. */
		private final IProgressMonitor monitor;

		/** Constructor. */
		public GroovyFileExtractor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		/** {@inheritDoc} */
		@Override
		protected void visit(IFile groovyFile) {
			try {
				extractDeclaration(groovyFile);
				monitor.worked(1);
			} catch (Exception e) {
				LoggingUtils.logError(DeclarationPlugin.getPlugin(), e);
			}
		}
	}

	/** Base class for visitors looking for Groovy files. */
	private abstract class GroovyFileVisitor implements IResourceVisitor {

		/** {@inheritDoc} */
		public boolean visit(IResource resource) {
			if (resource.getType() == IResource.FILE) {
				IFile file = (IFile) resource;
				if ("groovy".equals(file.getFileExtension())) {
					visit(file);
				}
			}
			return true;
		}

		/** Visit a Groovy file. */
		protected abstract void visit(IFile groovyFile);
	}
}
