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
package org.eclipse.rustprovisioningserver;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.ppp4j.messages.ComponentVersion;
import org.eclipse.ppp4j.messages.ComponentVersionSelection;
import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.Initialize;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.ParameterType;
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
	boolean supportMarkdown;
	boolean allowFileCreation;

	@Override
	public InitializeResult initialize(Initialize initialize) {
		this.supportMarkdown = initialize.supportMarkdown;
		this.allowFileCreation = initialize.allowFileCreation;
		// TODO: better storage and generation of templates
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

		InitializeResult result = new InitializeResult(false, false, true, templates, componentVersions,
				new ProvisioningParameters("new_rust_project", "/tmp/new_rust_project", "0.0.1-beta", selection,
						new ComponentVersionSelection[0]));
		return result;
	}

	@Override
	public PreviewResult preview(ProvisioningParameters parameters) {
		ValidationResult validation = validation(parameters);
		if (validation.errorMessage != null || validation.erroneousParameters.length > 0) {
			return new PreviewResult(validation.errorMessage, validation.erroneousParameters, null);
		}
		String preview;
		if (supportMarkdown) {
			preview = "# Preview \n > steps to make proccess";
		} else {
			preview = "Preview \nsteps to make proccess (plain text)";
		}
		return new PreviewResult(null, new ErroneousParameter[0], preview);
	}

	@Override
	public ValidationResult validation(ProvisioningParameters parameters) {
		java.util.List<ErroneousParameter> erroneousParameter = new ArrayList<>();
		String errorMessage = null;

		if (parameters.name == null || parameters.name.isEmpty()) {
			errorMessage = "Name field cannot be empty";
			erroneousParameter.add(new ErroneousParameter(ParameterType.Name, "Name field cannot be empty", null));
		}

		String locationError = null;
		if (parameters.location == null || parameters.location.isEmpty()) {
			locationError = "Location field cannot be empty";
		} else {
			File directory = new File(parameters.location);
			if (directory.isFile()) {
				locationError = "Given location is an existing file";
			} else if (directory.getParentFile() == null
					|| (!directory.exists() && !directory.getParentFile().canWrite())) {
				locationError = "Unable to create project in given location";
			} else if (directory.exists() && !directory.canWrite()) {
				locationError = "Cannot write in given location";
			}
		}
		if (locationError != null) {
			if (errorMessage == null) {
				errorMessage = "Location field cannot be empty";
			} else {
				errorMessage = "Multiple errors";
			}
			erroneousParameter
			.add(new ErroneousParameter(ParameterType.Location, "Location field cannot be empty", null));
		}

		if (parameters.templateSelection == null || parameters.templateSelection.id == null) {
			if (errorMessage == null) {
				errorMessage = "Select a template";
			} else {
				errorMessage = "Multiple errors";
			}
			erroneousParameter.add(new ErroneousParameter(ParameterType.Template, "Select a template", null));
		}

		return new ValidationResult(errorMessage, erroneousParameter.toArray(new ErroneousParameter[0]));
	}

	@Override
	public ProvisionResult provision(ProvisioningParameters parameters) {
		ProvisionResult result = new ProvisionResult(null, new ErroneousParameter[0], "/tmp/rust_project",
				new String[] { "openFile.rs", "sub/openFile.rs" });
		return result;
	}

	@Override
	public ProvisionInstructionsResult provisionInstructions(ProvisioningParameters parameters) {
		return null;
	}

	@Override
	public RpcResponse generateResponseFromCustomEndpoint(RpcRequest request) {
		return null;
	}

}
