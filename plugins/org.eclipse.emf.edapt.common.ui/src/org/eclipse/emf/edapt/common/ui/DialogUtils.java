package org.eclipse.emf.edapt.common.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Helper methods to show simple dialogs.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class DialogUtils {
	
	/** Constructor. */
	private DialogUtils() {
		// this class is not supposed to be instantiated.
	}

	/** Show an error dialog with a title and message in the UI thread. */
	public static void openErrorDialogAsync(final String title,
			final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(),
						title, message);
			}
		});
	}
}
