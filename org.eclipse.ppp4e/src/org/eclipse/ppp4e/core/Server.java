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
package org.eclipse.ppp4e.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import org.eclipse.ppp4e.ProvisioningPlugin;
import org.eclipse.ppp4j.messages.Initialize;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;
import org.eclipse.ppp4j.messages.ValidationResult;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Server {
	private String baseMethod = "projectProvisioning/";
	StreamConnectionProvider streamConnectionProvider;
	Map<Integer, Semaphore> messageResponseSemaphores = new HashMap<>();
	Map<Integer, Object> messageResponses = new HashMap<>();
	private Integer nextMessageId = 0;
	private Gson gson = new Gson();
	private PrintWriter writer;
	private BufferedReader reader;

	public Server(StreamConnectionProvider streamConnectionProvider, String serverName) {
		this.streamConnectionProvider = new LoggingStreamConnectionProviderProxy(streamConnectionProvider, serverName);
	}

	public boolean openConnection() {
		if (!streamConnectionProvider.start()) {
			return false;
		}
		writer = new PrintWriter(streamConnectionProvider.getOutputStream());
		listenForMessages();
		return true;
	}

	public void closeConnection() {
		streamConnectionProvider.stop();
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				ProvisioningPlugin.logError(e);
			}
		}
		if (writer != null) {
			writer.close();
		}
	}

	public CompletableFuture<InitializeResult> Initalize() {
		return sendMessage("initalize", new Initialize(true, true)).thenApply(object -> {
			return gson.fromJson(gson.toJson(object), InitializeResult.class);
		});
	}

	public CompletableFuture<ValidationResult> Validation(ProvisioningParameters parameters) {
		return sendMessage("validation", parameters).thenApply(object -> {
			return gson.fromJson(gson.toJson(object), ValidationResult.class);
		});
	}

	public CompletableFuture<PreviewResult> Preview(ProvisioningParameters parameters) {
		return sendMessage("preview", parameters).thenApply(object -> {
			return gson.fromJson(gson.toJson(object), PreviewResult.class);
		});
	}

	public CompletableFuture<ProvisionResult> Provision(ProvisioningParameters parameters) {
		return sendMessage("provision", parameters).thenApply(object -> {
			return gson.fromJson(gson.toJson(object), ProvisionResult.class);
		});
	}

	private CompletableFuture<Object> sendMessage(String method, Object params) {
		CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
			final int id = nextMessageId;
			nextMessageId++;
			RpcRequest request = new RpcRequest(String.valueOf(id), baseMethod + method, params);
			writer.println(gson.toJson(request));
			writer.flush();
			System.out.println("client2server: " + gson.toJson(request));

			Semaphore responseSemaphore = new Semaphore(0);
			messageResponseSemaphores.put(id, responseSemaphore);
			try {
				responseSemaphore.acquire();
				Object result = messageResponses.get(id);
				messageResponses.remove(id);
				return result;
			} catch (InterruptedException e) {
				ProvisioningPlugin.logError(e);
				return null;
			}
		});
		return completableFuture;
	}

	private void listenForMessages() {
		CompletableFuture.runAsync(() -> {
			try {
				reader = new BufferedReader(new InputStreamReader(streamConnectionProvider.getInputStream()));
				while (true) {
					String input = reader.readLine();
					if (input == null) {
						break;
					}
					RpcResponse response;
					try {
						response = gson.fromJson(input, RpcResponse.class);
					} catch (JsonSyntaxException e) {
						System.out.println("Unknown message format: " + input);
						continue;
					}
					Integer id = Integer.parseInt(response.id);
					messageResponses.put(id, response.result);
					messageResponseSemaphores.get(id).release();
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
