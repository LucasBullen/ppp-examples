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
package org.eclipse.ppp4e.core;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.ppp4e.ProvisioningPlugin;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoggingStreamConnectionProviderProxy implements StreamConnectionProvider {
	private static final boolean FORMATTING = Boolean.parseBoolean(System.getProperty("ppp.logging.formatting")); //$NON-NLS-1$
	private Gson gson;

	private StreamConnectionProvider provider;
	private String serverName;
	private InputStream inputStream;
	private OutputStream outputStream;
	private InputStream errorStream;

	public LoggingStreamConnectionProviderProxy(StreamConnectionProvider provider, String serverName) {
		this.provider = provider;
		this.serverName = serverName;
		if (FORMATTING) {
			gson = new GsonBuilder().setPrettyPrinting().create();
		}
	}

	@Override
	public boolean start() {
		return provider.start();
	}

	@Override
	public InputStream getInputStream() {
		if (inputStream != null) {
			return inputStream;
		}
		if (provider.getInputStream() != null) {
			inputStream = new FilterInputStream(provider.getInputStream()) {
				@Override
				public int read(byte[] b, int off, int len) throws IOException {
					int bytes = super.read(b, off, len);
					byte[] payload = new byte[bytes];
					System.arraycopy(b, off, payload, 0, bytes);
					String message = new String(payload);
					if (FORMATTING) {
						if (message != null) {
							logToConsole(serverName + " to PPP4E:"
									+ gson.toJson(gson.fromJson(message, Object.class)));
						}
					} else {
						logToConsole(serverName + " to PPP4E:" + message);
					}
					return bytes;
				}
			};
		}
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		if (outputStream != null) {
			return outputStream;
		}
		if (provider.getOutputStream() != null) {
			outputStream = new FilterOutputStream(provider.getOutputStream()) {
				@Override
				public void write(byte[] b, int off, int len) throws IOException {
					String message = new String(b, off, len);
					if (FORMATTING) {
						if (message != null) {
							logToConsole("PPP4E to " + serverName + ":"
									+ gson.toJson(gson.fromJson(message, Object.class)));
						}
					} else {
						logToConsole("PPP4E to " + serverName + ":" + message);
					}
					super.write(b, off, len);
				}
			};
		}
		return outputStream;
	}

	@Override
	public InputStream getErrorStream() {
		if (errorStream != null) {
			return errorStream;
		}
		if (provider.getErrorStream() != null) {
			errorStream = new FilterInputStream(provider.getErrorStream()) {
				@Override
				public int read(byte[] b, int off, int len) throws IOException {
					int bytes = super.read(b, off, len);
					byte[] payload = new byte[bytes];
					System.arraycopy(b, off, payload, 0, bytes);
					logToConsole("Error from " + serverName + ":" + new String(payload));
					return bytes;
				}
			};
		}
		return errorStream;
	}

	@Override
	public void stop() {
		provider.stop();
		try {
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			if (errorStream != null) {
				errorStream.close();
				errorStream = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void logToConsole(String string) {
		if (consoleStream == null || consoleStream.isClosed()) {
			consoleStream = findConsole().newMessageStream();
		}
		consoleStream.println(string);
	}

	private MessageConsoleStream consoleStream;
	private MessageConsole findConsole() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (IConsole element : existing) {
			if (ProvisioningPlugin.PLUGIN_ID.equals(element.getName())) {
				return (MessageConsole) element;
			}
		}
		MessageConsole myConsole = new MessageConsole(ProvisioningPlugin.PLUGIN_ID, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

}
