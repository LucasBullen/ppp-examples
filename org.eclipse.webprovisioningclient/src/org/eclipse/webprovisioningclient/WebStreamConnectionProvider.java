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
package org.eclipse.webprovisioningclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ppp4e.core.StreamConnectionProvider;
import org.eclipse.ui.PlatformUI;

public class WebStreamConnectionProvider implements StreamConnectionProvider {
	private Process process;
	@Override
	public boolean start() {
		if (this.process != null && this.process.isAlive()) {
			return false;
		}
		String serverCommand = WebProvisioningPlugin.getDefault().getPreferenceStore()
				.getString(WebPreferenceInitializer.wppsPathPreference);
		if (serverCommand.isEmpty()) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "No Server",
					"Please specify a valid provisioning server path in the Web preferences");
			return false;
		}
		String[] command = new String[] { "/bin/bash", "-c", serverCommand };
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			command = new String[] { "cmd", "/c", serverCommand };
		}
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		try {
			this.process = builder.start();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public InputStream getInputStream() {
		return process.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() {
		return process.getOutputStream();
	}

	@Override
	public InputStream getErrorStream() {
		return process.getErrorStream();
	}

	@Override
	public void stop() {
		process.destroy();
	}

}
