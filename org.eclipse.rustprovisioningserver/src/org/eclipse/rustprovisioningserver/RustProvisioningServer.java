package org.eclipse.rustprovisioningserver;

class RustProvisioningServer {

	public static void main(String[] args) {
		Server server = new Server();
		server.beginListening();
	}
}
