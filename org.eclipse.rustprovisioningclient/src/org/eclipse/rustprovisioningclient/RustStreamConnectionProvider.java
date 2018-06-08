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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ppp4e.core.StreamConnectionProvider;

public class RustStreamConnectionProvider implements StreamConnectionProvider {
	private Process process;
	private final String rpps = "/home/lbullen/Documents/rust_server/rust_pps.jar";
	@Override
	public void start() throws IOException {
		if (this.process != null && this.process.isAlive()) {
			return;
		}
		String[] command = new String[] { "/bin/bash", "-c", "java -jar " + rpps };
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			command = new String[] { "cmd", "/c", "java -jar " + rpps };
		}
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		this.process = builder.start();
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
