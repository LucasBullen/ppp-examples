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

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ppp4e.core.StreamConnectionProvider;
import org.eclipse.ppp4e.ui.wizard.NewProjectWizard;

public class NewCargoProjectWizard extends NewProjectWizard {
	private RustStreamConnectionProvider connectionProvider;

	@Override
	protected StreamConnectionProvider getStreamConnectionProvider() {
		if (connectionProvider == null) {
			connectionProvider = new RustStreamConnectionProvider();
		}
		return connectionProvider;
	}

	@Override
	protected String getWizardName() {
		return "New Cargo Project";
	}

	@Override
	protected File newFolderLocation() {
		IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		int appendedNumber = 0;
		File newFile = workspacePath.append("new_rust_project").toFile();
		while (newFile.isDirectory()) {
			appendedNumber++;
			newFile = workspacePath.append("new_rust_project_" + appendedNumber).toFile();
		}
		return newFile;
	}

}
