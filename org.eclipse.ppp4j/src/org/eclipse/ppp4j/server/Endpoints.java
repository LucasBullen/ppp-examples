package org.eclipse.ppp4j.server;

import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisionInstructionsResult;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;
import org.eclipse.ppp4j.messages.ValidationResult;

public interface Endpoints {

	RpcResponse generateResponseFromCustomEndpoint(RpcRequest request);

	ProvisionInstructionsResult provisionInstructions(RpcRequest request);

	ProvisionResult provision(RpcRequest request);

	PreviewResult preview(RpcRequest request);

	ValidationResult validation(RpcRequest request);

	InitializeResult initialize(RpcRequest request);
}
