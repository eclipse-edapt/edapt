package org.eclipse.emf.edapt.history.presentation.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.presentation.HistoryEditorPlugin;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.ide.IDE;

/**
 * Helper methods to deal with the Java UI.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class JavaUIUtils {

	/** Constructor. */
	private JavaUIUtils() {
		// not supposed to be instantiated
	}

	/** Create a class that implements a custom migration. */
	public static IJavaElement createCustomMigration(EObject element) {
		OpenNewClassWizardAction action = new OpenNewClassWizardAction();
		IProject project = getProject(element);
		NewClassWizardPage page = new NewClassWizardPage();
		page.init(new StructuredSelection(project));
		page.setSuperClass(CustomMigration.class.getName(), true);
		action.setConfiguredWizardPage(page);
		action.run();
		IJavaElement javaElement = action.getCreatedElement();
		return javaElement;
	}

	/** Get the project in which a certain model element is stored. */
	private static IProject getProject(EObject element) {
		IFile file = URIUtils.getFile(element.eResource().getURI());
		IProject project = file.getProject();
		return project;
	}

	/** Show the custom migration to which a migration change refers. */
	public static void showCustomMigration(MigrationChange change) {
		IProject project = getProject(change);
		IJavaProject javaProject = JavaCore.create(project);

		try {
			IType type = javaProject.findType(change.getMigration());
			openResource((IFile) type.getResource());
		} catch (JavaModelException e) {
			LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
		}
	}

	/** Select the custom migration to which a migration change refers. */
	public static IJavaElement selectCustomMigration(MigrationChange change) {
		IProject project = getProject(change);
		try {
			IJavaSearchScope searchScope = null;
			IJavaProject javaProject = JavaCore.create(project);
			IType superType = javaProject.findType(CustomMigration.class
					.getName());
			if (superType != null) {
				searchScope = SearchEngine.createHierarchyScope(superType);
			}

			SelectionDialog dialog = JavaUI.createTypeDialog(Display
					.getDefault().getActiveShell(), PlatformUI.getWorkbench()
					.getProgressService(), searchScope,
					IJavaElementSearchConstants.CONSIDER_CLASSES, false, change
							.getMigration());
			dialog.setTitle("Select Custom Migration");
			if (dialog.open() == Window.OK) {
				IType type = (IType) dialog.getResult()[0];
				return type;
			}
		} catch (JavaModelException e) {
			LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
		}
		return null;

	}

	/** Open a resource with the appropriate editor. */
	private static void openResource(final IFile resource) {
		final IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (activePage != null) {
			try {
				IDE.openEditor(activePage, resource, true);
			} catch (PartInitException e) {
				LoggingUtils.logError(HistoryEditorPlugin.getPlugin(), e);
			}
		}
	}

}
