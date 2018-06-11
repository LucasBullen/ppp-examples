/*********************************************************************
 * Copyright (c) 2018 Red Hat Inc. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Lucas Bullen (Red Hat Inc.) - Initial implementation
 *******************************************************************************/
package org.eclipse.rustprovisioningclient;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class RustPreferenceInitializer extends AbstractPreferenceInitializer {
	private static final IPreferenceStore STORE = RustProvisioningPlugin.getDefault().getPreferenceStore();

	public static String rppsPathPreference = "rppc.rpps_path"; //$NON-NLS-1$

	@Override
	public void initializeDefaultPreferences() {
		STORE.setDefault(rppsPathPreference, getRPPSPathBestGuess());
	}

	public static String getRPPSPathBestGuess() {
		return "";
	}
}
