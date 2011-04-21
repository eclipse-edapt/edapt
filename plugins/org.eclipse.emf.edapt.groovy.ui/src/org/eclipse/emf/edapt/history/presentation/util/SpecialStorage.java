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
package org.eclipse.emf.edapt.history.presentation.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.edapt.history.MigrationChange;


/**
 * Special storage for {@link MigrationChange}
 * 
 * @author herrmama
 *
 */
public class SpecialStorage implements IStorage {
	
	/**
	 * Contents
	 */
	private String contents;

	/**
	 * Constructor
	 * 
	 * @param contents
	 */
	public SpecialStorage(String contents) {
		this.contents = contents;
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public InputStream getContents() {
		return new ByteArrayInputStream(contents.getBytes());
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public IPath getFullPath() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public String getName() {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public boolean isReadOnly() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}
}
