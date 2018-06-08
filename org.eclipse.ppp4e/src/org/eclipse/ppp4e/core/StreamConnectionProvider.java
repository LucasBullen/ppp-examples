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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamConnectionProvider {
	public void start() throws IOException;

	public InputStream getInputStream();

	public OutputStream getOutputStream();

	public InputStream getErrorStream();

	public void stop();
}
