package org.eclipse.rustprovisioningclient;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ppp4e.core.StreamConnectionProvider;

public class RustStreamConnectionProvider implements StreamConnectionProvider {
	private Process process;
	private final String rpps = "rpps";
	@Override
	public void start() throws IOException {
		String[] command = new String[] { "/bin/bash", "-c", rpps };
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			command = new String[] { "cmd", "/c", rpps };
		}
		this.process = Runtime.getRuntime().exec(command);
	}

	@Override
	public InputStream getInputStream() {
		return new FilterInputStream(process.getInputStream()) {
			@Override
			public int read() throws IOException {
				int res = super.read();
				System.err.print((char) res);
				return res;
			}

			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				int bytes = super.read(b, off, len);
				byte[] payload = new byte[bytes];
				System.arraycopy(b, off, payload, 0, bytes);
				System.err.print(new String(payload));
				return bytes;
			}

			@Override
			public int read(byte[] b) throws IOException {
				int bytes = super.read(b);
				byte[] payload = new byte[bytes];
				System.arraycopy(b, 0, payload, 0, bytes);
				System.err.print(new String(payload));
				return bytes;
			}
		};
	}

	@Override
	public OutputStream getOutputStream() {
		return new FilterOutputStream(process.getOutputStream()) {
			@Override
			public void write(int b) throws IOException {
				System.err.print((char) b);
				super.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				System.err.print(new String(b));
				super.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				byte[] actual = new byte[len];
				System.arraycopy(b, off, actual, 0, len);
				System.err.print(new String(actual));
				super.write(b, off, len);
			}
		};
	}

	@Override
	public InputStream getErrorStream() {
		return new FilterInputStream(process.getErrorStream()) {
			@Override
			public int read() throws IOException {
				int res = super.read();
				System.err.print((char) res);
				return res;
			}

			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				int bytes = super.read(b, off, len);
				byte[] payload = new byte[bytes];
				System.arraycopy(b, off, payload, 0, bytes);
				System.err.print(new String(payload));
				return bytes;
			}

			@Override
			public int read(byte[] b) throws IOException {
				int bytes = super.read(b);
				byte[] payload = new byte[bytes];
				System.arraycopy(b, 0, payload, 0, bytes);
				System.err.print(new String(payload));
				return bytes;
			}
		};
	}

	@Override
	public void stop() {
		process.destroy();
	}

}
