/*******************************************************************************
 * Copyright (c) 2019 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christian W. Damus - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.edapt.history.preferences.ui;

import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * An enumeration of preference values for a prompting preference.
 */
public enum PromptKind {
	PROMPT(MessageDialogWithToggle.PROMPT), NEVER(MessageDialogWithToggle.NEVER), ALWAYS(
		MessageDialogWithToggle.ALWAYS);

	private final String value;

	private PromptKind(String value) {
		this.value = value;
	}

	/**
	 * Get the value of a preference {@code key} from the given {@code store}.
	 *
	 * @param store the preference store
	 * @param key the preference to get
	 * @return the preference value
	 */
	public static PromptKind get(IPreferenceStore store, String key) {
		store.setDefault(key, PROMPT.value);

		return getByValue(store.getString(key));
	}

	/**
	 * Get a prompt kind by string value.
	 *
	 * @param value the value
	 * @return the corresponding prompt kind, or {@link #PROMPT} if the {@code value}
	 *         is invalid
	 */
	public static PromptKind getByValue(String value) {
		PromptKind result = PROMPT;

		for (final PromptKind next : values()) {
			if (next.value.equals(value)) {
				result = next;
				break;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return value;
	}

}
