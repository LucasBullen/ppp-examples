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
