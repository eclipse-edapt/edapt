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
package org.eclipse.emf.edapt.internal.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;

/**
 * Helper methods to deal with files.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public final class FileUtils {

	/** Constructor. */
	private FileUtils() {
		// hidden, since this class only provides static helper methods
	}

	/** Get contents of an Eclipse resource file as string. */
	public static String getContents(IFile file) {
		try {
			final InputStream input = file.getContents();
			final String contents = getContents(input);
			input.close();
			return contents;
		} catch (final CoreException e) {
			LoggingUtils.logError(CommonActivator.getDefault(), e);
			return null;
		} catch (final IOException e) {
			LoggingUtils.logError(CommonActivator.getDefault(), e);
			return null;
		}
	}

	/** Read contents of an input stream to a string. */
	public static String getContents(InputStream in) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append('\n');
		}
		return buffer.toString();
	}

	/** Get contents of a Java file as string. */
	public static String getContents(File file) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append('\n');
		}
		reader.close();
		return buffer.toString();
	}

	/**
	 * Resolve a file name with respect to a container which may be a project or
	 * a folder.
	 */
	public static IFile resolveFile(IContainer container, String name) {
		if (container instanceof IProject) {
			return ((IProject) container).getFile(name);
		} else if (container instanceof IFolder) {
			return ((IFolder) container).getFile(name);
		}
		return null;
	}

	/** Move a file. */
	public static void move(File source, File target) {
		source.renameTo(target);
	}

	/** Move a file. */
	public static void move(URI source, URI target) {
		move(URIUtils.getJavaFile(source), URIUtils.getJavaFile(target));
	}

	/** Delete a file. */
	public static void delete(File target) {
		target.delete();
	}

	/** Delete a file. */
	public static void delete(URI target) {
		delete(URIUtils.getJavaFile(target));
	}

	/** Copy a file. */
	public static void copy(File source, File target) throws IOException {
		final FileInputStream in = new FileInputStream(source);
		final FileOutputStream out = new FileOutputStream(target);

		final byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}

	/** Copy a file. */
	public static void copy(URI source, URI target) throws IOException {
		copy(URIUtils.getJavaFile(source), URIUtils.getJavaFile(target));
	}

	/** Create a file. */
	public static void createFile(String name, String contents)
		throws IOException {
		final FileWriter writer = new FileWriter(name);
		writer.write(contents);
		writer.close();
	}

	/** Create a directory. */
	public static void createDir(String name) {
		new File(name).mkdir();
	}

	/** Get the extension of a file. Returns null if there is not extension. */
	public static String getExtension(File file) {
		if (!file.isDirectory()) {
			final String name = file.getName();
			final int i = name.lastIndexOf('.');
			if (i >= 0) {
				return name.substring(i + 1);
			}
		}
		return null;
	}

	/** Get the name of a file without its extension. */
	public static String getNameWithoutExtension(File file) {
		final String name = file.getName();
		if (!file.isDirectory()) {
			final int i = name.lastIndexOf('.');
			if (i >= 0) {
				return name.substring(0, i);
			}
		}
		return name;
	}

	/** Replace the extension of a file with another one. */
	public static File replaceFileExtension(File file, String newExtension) {
		final String name = getNameWithoutExtension(file);
		return new File(file.getParentFile(), name + "." + newExtension); //$NON-NLS-1$
	}

	/**
	 * Delete the contents of a directory (without deleting the directory
	 * itself).
	 */
	public static void deleteContents(File dir) {
		if (!dir.exists()) {
			return;
		}
		for (final File file : dir.listFiles()) {
			if (file.isDirectory()) {
				deleteContents(file);
			}
			file.delete();
		}
	}

	/**
	 * Get a file from the workspace. The path must be workspace-relative. This
	 * method always returns a file. However, this does not necessarily exist.
	 */
	public static IFile getFile(String fullPath) {
		return ResourcesPlugin.getWorkspace().getRoot()
			.getFile(new Path(fullPath));
	}

}
