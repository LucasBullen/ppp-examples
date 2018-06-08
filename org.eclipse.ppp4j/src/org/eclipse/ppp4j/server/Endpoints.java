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

import org.eclipse.ppp4j.messages.Initialize;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisionInstructionsResult;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;
import org.eclipse.ppp4j.messages.ValidationResult;

public interface Endpoints {

	RpcResponse generateResponseFromCustomEndpoint(RpcRequest request);

	InitializeResult initialize(Initialize initialize);

	ValidationResult validation(ProvisioningParameters parameters);

	PreviewResult preview(ProvisioningParameters parameters);

	ProvisionResult provision(ProvisioningParameters parameters);

	ProvisionInstructionsResult provisionInstructions(ProvisioningParameters parameters);
}
