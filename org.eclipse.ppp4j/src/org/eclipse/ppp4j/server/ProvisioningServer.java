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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.ppp4j.messages.Initialize;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;

import com.google.gson.Gson;

public abstract class ProvisioningServer implements Endpoints {
	private static String methodPrefix = "projectProvisioning/";
	private static Gson gson = new Gson();

	public void beginListening() {
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

	private void sendMessage(RpcResponse response) {
		if (response == null) {
			return;
		}
		String string = gson.toJson(response);
		if (string == null || string.isEmpty() || string.equals("null")) {
			return;
		}
		System.out.println(string);
	}

	private RpcResponse generateResponse(RpcRequest request) {
		if (request == null || request.method == null) {
			return null;
		}
		String method = request.method;
		if (method.startsWith(methodPrefix)) {
			method = method.substring(methodPrefix.length());
		} else {
			return null;
		}
		Object result;
		switch (method) {
		case "initalize":
			result = initialize(gson.fromJson(gson.toJson(request.params), Initialize.class));
			break;
		case "validation":
			result = validation(gson.fromJson(gson.toJson(request.params), ProvisioningParameters.class));
			break;
		case "preview":
			result = preview(gson.fromJson(gson.toJson(request.params), ProvisioningParameters.class));
			break;
		case "provision":
			result = provision(gson.fromJson(gson.toJson(request.params), ProvisioningParameters.class));
			break;
		case "provisionInstructions":
			result = provisionInstructions(gson.fromJson(gson.toJson(request.params), ProvisioningParameters.class));
			break;
		default:
			return generateResponseFromCustomEndpoint(request);
		}
		return new RpcResponse(request.id, result);
	}
}
