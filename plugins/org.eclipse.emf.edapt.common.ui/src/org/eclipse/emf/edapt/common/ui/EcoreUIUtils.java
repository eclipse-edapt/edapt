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
package org.eclipse.emf.edapt.common.ui;

import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.ui.IFileEditorInput;


/**
 * Helper methods to deal with the Ecore user interface
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class EcoreUIUtils {
	
	/**
	 * Decide whether an Ecore editor edits a metamodel or a dynamic instance of a metamodel (reflective editor)
	 * 
	 * @param editor Ecore editor
	 * @return true if Ecore editor edits a metamodel, false otherwise
	 */
	public static boolean isMetamodelEditor(EcoreEditor editor) {
		if(editor.getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) editor.getEditorInput();
			if(ResourceUtils.ECORE_FILE_EXTENSION.equals(fileEditorInput.getFile().getFileExtension())) {
				return true;
			}
		}
		
		return false;
	}

}
