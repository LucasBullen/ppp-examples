package org.eclipse.rustprovisioningserver;

import org.eclipse.ppp4j.messages.ComponentVersion;
import org.eclipse.ppp4j.messages.ComponentVersionSelection;
import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisionInstructionsResult;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;
import org.eclipse.ppp4j.messages.Template;
import org.eclipse.ppp4j.messages.TemplateSelection;
import org.eclipse.ppp4j.messages.ValidationResult;
import org.eclipse.ppp4j.messages.Version;
import org.eclipse.ppp4j.server.ProvisioningServer;

public class Server extends ProvisioningServer {

	@Override
	public InitializeResult initialize(RpcRequest request) {
		ComponentVersion[] cargoTemplateComponentVersions = new ComponentVersion[] {
				new ComponentVersion("cargo_verison", "Cargo Version", null,
						new Version[] { new Version("0.0.1", "0.0.1", null), new Version("0.2.0", "0.2.0", null) }) };
		ComponentVersion[] componentVersions = new ComponentVersion[] {
				new ComponentVersion("rust_version", "Rust Version", null,
						new Version[] { new Version("1.0.0", "1.0.0", null), new Version("2.0.0", "2.0.0", null) }) };
		Template[] templates = new Template[] {
				new Template("hello_world", "Hello World", "basic project outputting 'hello world' to the console",
						new ComponentVersion[0]),
				new Template("crate_example", "Cargo Crate Example",
						"Basic cargo based Rust project that imports an external crate",
						cargoTemplateComponentVersions) };

		TemplateSelection selection = new TemplateSelection("hello_world", new ComponentVersionSelection[0]);

		InitializeResult result = new InitializeResult(true, false, true, templates, componentVersions,
				new ProvisioningParameters("new_rust_project", "/tmp/new_rust_project", "0.0.1-beta", selection,
						new ComponentVersionSelection[0]));
		return result;
	}

	@Override
	public PreviewResult preview(RpcRequest request) {
		return null;
	}

	@Override
	public ValidationResult validation(RpcRequest request) {
		return null;
	}

	@Override
	public ProvisionResult provision(RpcRequest request) {
		ProvisionResult result = new ProvisionResult(null, new ErroneousParameter[0], "/tmp/rust_project",
				new String[] { "openFile.rs", "sub/openFile.rs" });
		return result;
	}

	@Override
	public ProvisionInstructionsResult provisionInstructions(RpcRequest request) {
		return null;
	}

	@Override
	public RpcResponse generateResponseFromCustomEndpoint(RpcRequest request) {
		return null;
	}

}
