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
package org.eclipse.webprovisioningserver;

import org.eclipse.ppp4j.messages.Initialize;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisionInstructionsResult;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;
import org.eclipse.ppp4j.messages.ValidationResult;
import org.eclipse.ppp4j.server.ProvisioningServer;

public class Server extends ProvisioningServer {
	boolean supportMarkdown;
	boolean allowFileCreation;

	@Override
	public InitializeResult initialize(Initialize initialize) {
		this.supportMarkdown = initialize.supportMarkdown;
		this.allowFileCreation = initialize.allowFileCreation;
		return new Initializer().initialize();
	}

	@Override
	public PreviewResult preview(ProvisioningParameters parameters) {
		return new Previewer().preview(parameters, supportMarkdown);
	}

	@Override
	public ValidationResult validation(ProvisioningParameters parameters) {
		return new Validator().validation(parameters);
	}

	@Override
	public ProvisionResult provision(ProvisioningParameters parameters) {
		return new Provisionner().provision(parameters);
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
