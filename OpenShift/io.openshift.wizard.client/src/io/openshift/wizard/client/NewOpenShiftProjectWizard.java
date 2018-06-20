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
package io.openshift.wizard.client;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ppp4e.core.StreamConnectionProvider;
import org.eclipse.ppp4e.ui.wizard.NewProjectWizard;

public class NewOpenShiftProjectWizard extends NewProjectWizard {
	private OpenShiftStreamConnectionProvider connectionProvider;

	@Override
	protected StreamConnectionProvider getStreamConnectionProvider() {
		if (connectionProvider == null) {
			connectionProvider = new OpenShiftStreamConnectionProvider();
		}
		return connectionProvider;
	}

	@Override
	protected String getWizardName() {
		return "New OpenShift Project";
	}

	@Override
	protected File newFolderLocation() {
		IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		int appendedNumber = 0;
		File newFile = workspacePath.append("newProject").toFile();
		while (newFile.isDirectory()) {
			appendedNumber++;
			newFile = workspacePath.append("newProject-" + appendedNumber).toFile();
		}
		return newFile;
	}

}
