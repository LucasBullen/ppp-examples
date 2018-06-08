package org.eclipse.rustprovisioningserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.ppp4j.messages.ComponentVersion;
import org.eclipse.ppp4j.messages.ComponentVersionSelection;
import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;
import org.eclipse.ppp4j.messages.Template;
import org.eclipse.ppp4j.messages.TemplateSelection;
import org.eclipse.ppp4j.messages.Version;

import com.google.gson.Gson;

class RustProvisioningServer {
	private static String methodPrefix = "projectProvisioning/";
	private static Gson gson = new Gson();

	public static void main(String[] args) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String input = br.readLine();
				if (input == null) {
					break;
				}
				RpcRequest request;
				try {
					request = gson.fromJson(input, RpcRequest.class);
				} catch (Exception e) {
					continue;
				}
				sendMessage(generateResponse(request));
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void sendMessage(RpcResponse response) {
		if (response == null) {
			return;
		}
		String string = gson.toJson(response);
		if (string == null || string.isEmpty() || string.equals("null")) {
			return;
		}
		System.out.println(string);
	}

	private static RpcResponse generateResponse(RpcRequest request) {
		if (request == null || request.method == null) {
			return null;
		}
		String method = request.method;
		if (method.startsWith(methodPrefix)) {
			method = method.substring(methodPrefix.length());
		} else {
			return null;
		}
		switch (method) {
		case "initalize":
			return initialize(request);
		case "validation":
			return validation(request);
		case "preview":
			return preview(request);
		case "provision":
			return provision(request);
		case "provisionInstructions":
			return provisionInstructions(request);
		default:
			return null;
		}
	}

	private static RpcResponse provisionInstructions(RpcRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private static RpcResponse provision(RpcRequest request) {
		ProvisionResult result = new ProvisionResult(null, new ErroneousParameter[0], new String[] { "test" },
				new String[] { "test" });
		return generateResponseMessage(request, result);
	}

	private static RpcResponse preview(RpcRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private static RpcResponse validation(RpcRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private static RpcResponse initialize(RpcRequest request) {
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

		TemplateSelection selection = new TemplateSelection("hello_world", new ComponentVersion[0]);

		InitializeResult result = new InitializeResult(true, false, true, templates, componentVersions,
				new ProvisioningParameters("new_rust_project", "/tmp/new_rust_project", "0.0.1-beta", selection,
						new ComponentVersionSelection[0]));
		return generateResponseMessage(request, result);
	}

	private static RpcResponse generateResponseMessage(RpcRequest request, Object message) {
		return new RpcResponse(request.id, message);
	}
}
