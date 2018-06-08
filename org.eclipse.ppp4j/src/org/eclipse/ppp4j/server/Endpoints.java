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
