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
package org.eclipse.emf.edapt.history.instantiation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.common.FileUtils;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.history.recorder.EditingDomainListener;


/**
 * Helper class to import a migrator as history
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigratorImporter {
	
	/**
	 * Change recorder
	 */
	private final EditingDomainListener listener;
	
	/**
	 * Constructor
	 */
	public MigratorImporter(EditingDomainListener listener) {
		this.listener = listener;
	}

	/**
	 * Import a migrator from a URL
	 */
	public void importMigrator(URI migratorURI) throws MalformedURLException, IOException {
		try {
			for(int release = 0; true; release++) {
				URI releaseURI = migratorURI.appendSegment("release" + (release+1));
				URI metamodelURI = releaseURI.appendSegment("release" + release + "." + ResourceUtils.ECORE_FILE_EXTENSION);
				URIUtils.getURL(metamodelURI).openStream().close();
				
				System.out.println("Release " + (release+1));
				try {
					for(int step = 1; true; step++) {
						URI stepURI = releaseURI.appendSegment("step" + step + ".groovy");
						String script = FileUtils.getContents(URIUtils.getURL(stepURI).openStream());
						
						ScriptCommand command = new ScriptCommand(script,
								listener.getExtent());
						listener.getEditingDomain().getCommandStack().execute(
								command);
						System.out.println("Step " + step);
					}
				}
				catch(FileNotFoundException e) {
					// release finished
				}
				
				listener.release();
			}
		}
		catch(FileNotFoundException e) {
			// migrator finished
		}
	}
}
