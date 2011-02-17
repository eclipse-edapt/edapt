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
package org.eclipse.emf.edapt.history.reconstruction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.edapt.common.LoggingUtils;
import org.eclipse.emf.edapt.common.URIUtils;
import org.eclipse.emf.edapt.history.History;
import org.eclipse.emf.edapt.history.provider.HistoryEditPlugin;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;
import org.eclipse.emf.edapt.history.reconstruction.MigratorCodeGenerator;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;


/**
 * Helper class to generate a migrator project
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class MigratorProjectGenerator {

	/**
	 * Folder with migrator
	 */
	private static final String MIGRATOR_FOLDER = "migrator";

	/**
	 * History from which the migrator should be generated
	 */
	private final History history;
	
	/**
	 * Target project
	 */
	private final IProject project;

	/**
	 * Constructor
	 */
	public MigratorProjectGenerator(History history, IProject project) {
		this.history = history;
		this.project = project;
	}
	
	/**
	 * Generate the migrator project
	 */
	public void generate(IProgressMonitor monitor) throws CoreException, IOException {
		// create migrator project
		
		int totalWork = 6 + history.getReleases().size()-2;
		monitor.beginTask("Generate Migration", totalWork);
		
		createPluginProject(monitor);					
		emptyMigratorFolder(monitor);
		createManifest(monitor);
		createPluginXML(monitor);
		createBuildProperties(monitor);
		createMigratorFolder(monitor);

		generateMigrator(monitor);
	}

	/**
	 * Delete the contents of the migrator folder if it exists
	 */
	private void emptyMigratorFolder(IProgressMonitor monitor) {
		IFolder folder = project.getFolder(MIGRATOR_FOLDER);
		if(folder.exists()) {
			try {
				for(IResource resource : folder.members()) {
					resource.delete(true, new NullProgressMonitor());
				}
				folder.refreshLocal(IResource.DEPTH_INFINITE,
						new NullProgressMonitor());
			} catch (CoreException e) {
				LoggingUtils.logError(HistoryEditPlugin.getPlugin(), e);
			}
		}
		
		monitor.worked(1);
	}

	/**
	 * Create the migrator folder
	 */
	private void createMigratorFolder(IProgressMonitor monitor) throws CoreException {
		IFolder migratorFolder = project.getFolder(MIGRATOR_FOLDER);
		if(!migratorFolder.exists()) {
			migratorFolder.create(false, true, new NullProgressMonitor());
		}
		
		monitor.worked(1);
	}

	/**
	 * Generate the migrator
	 * @param monitor 
	 */
	private void generateMigrator(IProgressMonitor monitor) {
		IFolder folder = project.getFolder(MIGRATOR_FOLDER);
		
		// generate migration in parallel with metamodel reconstruction
		EcoreForwardReconstructor reconstructor = new EcoreForwardReconstructor(URIUtils.getURI(folder));
		reconstructor.addReconstructor(createMigratorGenerator(folder));
		reconstructor.reconstruct(history.getLastRelease(), false);
		
		monitor.worked(history.getReleases().size()-2);
	}

	/**
	 * Create the migrator code generator
	 */
	protected MigratorCodeGenerator createMigratorGenerator(IFolder folder) {
		return new MigratorCodeGenerator(URIUtils.getURI(folder));
	}

	/**
	 * Create a plugin project
	 * @param monitor 
	 */
	public void createPluginProject(IProgressMonitor monitor) throws CoreException {
		if(!project.exists()) {
			// create project
			project.create(new NullProgressMonitor());
			project.open(new NullProgressMonitor());
			
			// set project description
			IProjectDescription description = project.getDescription();
			String[] natureIds = new String[]{JavaCore.NATURE_ID, "org.eclipse.pde.PluginNature"};
			description.setNatureIds(natureIds);
			project.setDescription(description, new NullProgressMonitor());
			
			// create java project
			IJavaProject javaProject = JavaCore.create(project);	
			List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();
			classpathEntries.add(JavaCore.newContainerEntry(new Path(
					"org.eclipse.jdt.launching.JRE_CONTAINER")));
			classpathEntries.add(JavaCore.newContainerEntry(new Path(
					"org.eclipse.pde.core.requiredPlugins")));
	
			javaProject.setRawClasspath(classpathEntries
					.toArray(new IClasspathEntry[classpathEntries.size()]),
					new NullProgressMonitor());
		}
		
		monitor.worked(1);
	}
	
	/**
	 * Create file for plugin specification
	 */
	public void createPluginXML(IProgressMonitor monitor) throws CoreException, IOException {
		
		IFile file = getFile(project, "plugin.xml");
		if(!file.exists()) {
			StringBuilder content = new StringBuilder();
			content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			content.append("<?eclipse version=\"3.2\"?>\n");
			content.append("<plugin>\n");
			content.append("<extension point=\"org.eclipse.emf.edapt.cope.migrators\">\n");
			content.append("<migrator path=\"" + MIGRATOR_FOLDER + "/\"/>");
			content.append("</extension>\n");
			content.append("</plugin>");

			createFile(file, content.toString());
		}
		
		monitor.worked(1);
	}

	/**
	 * Create file for build properties
	 */
	public void createBuildProperties(IProgressMonitor monitor) throws CoreException, IOException {
		
		IFile file = getFile(project, "build.properties");
		if(!file.exists()) {
			StringBuilder content = new StringBuilder();
			content.append("bin.includes = META-INF/,\\\n");
			content.append("               .,\\\n");
			content.append("               plugin.xml,\\\n");
			content.append("               " + MIGRATOR_FOLDER + "/\n");
		
			createFile(file, content.toString());
		}
		
		monitor.worked(1);
	}
	
	/**
	 * Create file with project manifest
	 */
	public void createManifest(IProgressMonitor monitor) throws CoreException, IOException {
		
		IFolder folder = project.getFolder("META-INF");
		if(!folder.exists()) {
			folder.create(false, true, new NullProgressMonitor());
		}
		
		IFile file = getFile(folder, "MANIFEST.MF");
		if(!file.exists()) {
			String projectName = project.getName();
			
			StringBuilder content = new StringBuilder("Manifest-Version: 1.0\n");
			content.append("Bundle-ManifestVersion: 2\n");
			content.append("Bundle-Name: " + projectName + "\n");
			content.append("Bundle-SymbolicName: " + projectName + "; singleton:=true\n");
			content.append("Bundle-Version: 1.0.0\n");
			content.append("Require-Bundle: org.eclipse.emf.edapt.cope.migration");
			content.append("\n");

			createFile(file, content.toString());
		}
		
		monitor.worked(1);
	}
	
	/**
	 * Create a file
	 */
	public IFile createFile(IFile file, String content) throws CoreException, IOException {
		
		InputStream stream = new ByteArrayInputStream(content.getBytes(file.getCharset()));
		file.create(stream, true, new NullProgressMonitor());
		stream.close();

		return file;
	}

	/**
	 * Get the file with a certain name in a container
	 */
	private IFile getFile(IContainer container, String name) {
		return container.getFile(new Path(name));
	}
}
